#!/usr/bin/env node
/*
 * LUT-33387 — end-to-end guard for the refactoring of the FreeMarker macros.
 *
 * Drives a real browser against a running Lutece instance, visits the front office and every
 * read-only back-office screen it can reach, and checks that each page is rendered correctly:
 *
 *   1. the page loads and is not a Lutece / FreeMarker error page;
 *   2. no FreeMarker construct and no markup reached the browser as text;
 *   3. the attributes the macros build (id, class, params, attrs) are real DOM attributes.
 *
 * Each page is also written as a normalised snapshot of the RAW SERVER RESPONSE under
 * snapshots/<LABEL>/, so that compare-snapshots.py can diff two runs — typically develop
 * ("before") against the refactored macros ("after"). An empty diff proves there is no
 * visual regression.
 *
 * Usage:
 *   NODE_PATH=<dir containing playwright-core> LABEL=before node pages-e2e.js
 * Environment:
 *   BASE_URL        default http://localhost:9080/lutece-core
 *   ADMIN_USER      default admin
 *   ADMIN_PASSWORD  default adminadmin
 *   LABEL           names the snapshot directory (default "current")
 *   OUT_DIR         default ./snapshots
 *   MAX_PAGES       upper bound of back-office pages visited (default 250)
 *   CHROMIUM        browser executable; by default the one bundled with playwright-core, or else
 *                   the newest headless shell found in ~/.cache/ms-playwright
 */
'use strict';

const fs = require( 'fs' );
const path = require( 'path' );
const { chromium } = require( 'playwright-core' );

const BASE_URL = process.env.BASE_URL || 'http://localhost:9080/lutece-core';
const ADMIN_USER = process.env.ADMIN_USER || 'admin';
const ADMIN_PASSWORD = process.env.ADMIN_PASSWORD || 'adminadmin';
const LABEL = process.env.LABEL || 'current';
const OUT_DIR = path.join( process.env.OUT_DIR || path.join( __dirname, 'snapshots' ), LABEL );
const MAX_PAGES = parseInt( process.env.MAX_PAGES || '250', 10 );

function chromiumExecutable( )
{
    if ( process.env.CHROMIUM )
    {
        return process.env.CHROMIUM;
    }
    if ( fs.existsSync( chromium.executablePath( ) ) )
    {
        return undefined;
    }
    const cache = path.join( require( 'os' ).homedir( ), '.cache', 'ms-playwright' );
    const shells = fs.existsSync( cache ) ? fs.readdirSync( cache ).filter( d => d.startsWith( 'chromium_headless_shell-' ) ).sort( ) : [];
    return shells.length ? path.join( cache, shells.pop( ), 'chrome-headless-shell-linux64', 'chrome-headless-shell' ) : undefined;
}

/* ------------------------------------------------------------------ pages */

const FO_PAGES = [
    // the home page answers 404 when the database references a page template the webapp does not
    // ship: the error page is still rendered by the front-office macros, so it is kept
    { name: 'fo-portal', url: '/jsp/site/Portal.jsp', allowedStatus: [ 404 ] },
    { name: 'fo-map', url: '/jsp/site/Portal.jsp?page=map' },
    { name: 'fo-search', url: '/jsp/site/Portal.jsp?page=search' },
    { name: 'fo-search-query', url: '/jsp/site/Portal.jsp?page=search&query=lutece' },
];

const BO_EXTRA = [
    { name: 'bo-site-preview', url: '/jsp/admin/site/AdminSite.jsp' },
    // visited after authenticating: fetching it first would consume the CSRF token of the form
    { name: 'bo-login', url: '/jsp/admin/AdminLogin.jsp' },
];

// Never follow these: they change state, end the session or stream a file.
const BO_EXCLUDED = /\/Do[A-Z]|action_|Logout|Remove|Delete|Confirm|Export|Download|\.pdf|Indexing|Reinit|Reset|Clear|Flush|Activate|Disable|Enable|Install|Uninstall|Refresh|Move|Up\.jsp|Down\.jsp|Unlock|Lock|Anonymize|Send|Import|javascript:|#/;

/* ------------------------------------------------------------- assertions */

const LEAKED_MARKUP = /&lt;\s*\/?\s*(div|span|a\s|p\s|p>|ul|li|button|img|table|tr|td|form|input|i\s|i>|strong|small|h[1-6]|script|svg|nav|section|header|footer|iframe|br|hr)\b/i;
const RAW_DIRECTIVE = /<#(if|list|assign|local|macro|outputformat|noautoesc|else|attempt)\b|<\/#|<@[a-zA-Z]|<\/@|\$\{[a-zA-Z_]/;
const ERROR_MARKERS = [
    'FreeMarker template error', 'freemarker.core.', 'freemarker.template.', 'NonMarkupOutputException',
    'ParseException', "Can't compare values of these types", 'java.lang.NullPointerException',
    'Warning : wrong or deprecated argument(s)',
];

function withoutScripts( html )
{
    return html.replace( /<script\b[\s\S]*?<\/script>/gi, '' ).replace( /<style\b[\s\S]*?<\/style>/gi, '' )
        .replace( /<textarea\b[\s\S]*?<\/textarea>/gi, '' ).replace( /<!--[\s\S]*?-->/g, '' )
        // an escaped tag inside an attribute value is data, not leaked markup
        .replace( /="[^"]*"/g, '=""' );
}

// Defects present on develop before LUT-33387, outside the macros: reported, not failed.
const KNOWN_FAILURES = [
    /^fo-map : malformed attribute\(s\) in the DOM: <span ms-2">$/, // skin/site/site_map.html:15
    /^bo-login : malformed attribute\(s\) in the DOM: <main ">$/, // admin/themes/tabler/pages/login/adminLoginPage.ftl:58
    // a description containing double quotes, written into a title attribute through a params bundle
    /^bo-system-ManageSecurityHeaders.* : malformed attribute\(s\) in the DOM: <p "default-src>/, // admin/system/manage_security_headers.html
    // <@select empty=false ...> : select has no "empty" parameter
    /^bo-style-ManagePortletTemplates.* : error page — contains "Warning : wrong or deprecated argument\(s\)"$/, // admin/style/manage_portlet_templates.html:16
];

const failures = [];
const notes = [];

function check( page, condition, message )
{
    if ( !condition )
    {
        const failure = `${page} : ${message}`;
        ( KNOWN_FAILURES.some( k => k.test( failure ) ) ? notes : failures ).push( failure );
    }
}

/* -------------------------------------------------------------- snapshots */

function normalise( html )
{
    return html
        .replace( /[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}/gi, 'UUID' )
        .replace( /(name="token"[^>]*value=")[^"]*"/g, '$1TOKEN"' )
        .replace( /(token=)[^"&'\s]+/g, '$1TOKEN' )
        .replace( /nonce="[^"]*"/g, 'nonce="N"' )
        .replace( /JSESSIONID=[^;"'\s]*/g, 'JSESSIONID=S' )
        .replace( /\?ts=\d+/g, '?ts=T' )
        .replace( /\s+/g, ' ' )
        .replace( />\s*</g, '>\n<' )
        .trim( );
}

function writeSnapshot( name, html )
{
    fs.mkdirSync( OUT_DIR, { recursive: true } );
    fs.writeFileSync( path.join( OUT_DIR, `${name}.html` ), normalise( html ), 'utf8' );
}

function nameOf( url )
{
    const [ p, q ] = url.split( '?' );
    let name = 'bo-' + p.replace( /^.*jsp\/admin\//, '' ).replace( /\.jsp$/, '' ).replace( /\//g, '-' );
    if ( q )
    {
        const params = new URLSearchParams( q );
        params.delete( 'token' );
        const keep = [ ...params.entries( ) ].map( ( [ k, v ] ) => `${k}=${v}` ).join( '_' );
        if ( keep )
        {
            name += '__' + keep.replace( /[^A-Za-z0-9_=.-]/g, '' ).slice( 0, 90 );
        }
    }
    return name;
}

/* ------------------------------------------------------------------- main */

async function visit( page, entry )
{
    let response;
    // one retry: the first request after a template change recompiles every template it includes
    for ( let attempt = 1; !response; attempt++ )
    {
        try
        {
            response = await page.goto( BASE_URL + entry.url, { waitUntil: 'domcontentloaded', timeout: 30000 } );
        }
        catch( e )
        {
            if ( attempt === 2 )
            {
                failures.push( `${entry.name} : navigation failed ${e.message.split( '\n' )[ 0 ]}` );
                return [];
            }
        }
    }
    const status = response ? response.status( ) : 0;
    if ( status >= 400 && !( entry.allowedStatus || [] ).includes( status ) )
    {
        failures.push( `${entry.name} : HTTP ${status} on ${entry.url}` );
        return [];
    }
    const landed = page.url( ).replace( BASE_URL, '' ).split( '?' )[ 0 ];
    if ( landed !== entry.url.split( '?' )[ 0 ] )
    {
        notes.push( `${entry.name} : redirected to ${landed} — not snapshotted` );
        return [];
    }

    let html;
    try
    {
        const fetched = await page.context( ).request.get( BASE_URL + entry.url, { maxRedirects: 0, failOnStatusCode: false } );
        html = await fetched.text( );
    }
    catch( e )
    {
        notes.push( `${entry.name} : response body unavailable — not snapshotted` );
        return [];
    }

    const markup = withoutScripts( html );
    for ( const marker of ERROR_MARKERS )
    {
        check( entry.name, !html.includes( marker ), `error page — contains "${marker}"` );
    }
    const leaked = markup.match( LEAKED_MARKUP );
    check( entry.name, !leaked, `markup leaked as text — "${leaked && leaked[ 0 ]}"` );
    const directive = markup.match( RAW_DIRECTIVE );
    check( entry.name, !directive, `unrendered FreeMarker construct — "${directive && directive[ 0 ]}"` );

    // attributes written by the macros must be parsed as attributes by the browser: an attribute
    // name containing a quote or an equal sign means a value was cut or a bundle was mangled
    const broken = await page.evaluate( ( ) => {
        const bad = [];
        for ( const el of document.querySelectorAll( '*' ) )
        {
            for ( const a of el.attributes )
            {
                if ( /["'=<>&]/.test( a.name ) )
                {
                    bad.push( `<${el.tagName.toLowerCase( )} ${a.name}>` );
                }
            }
        }
        return bad.slice( 0, 3 );
    } );
    check( entry.name, broken.length === 0, `malformed attribute(s) in the DOM: ${broken.join( ' ' )}` );

    writeSnapshot( entry.name, html );

    // links to follow: read-only back-office screens only
    return page.evaluate( ( ) => Array.from( document.querySelectorAll( 'a[href]' ) ).map( a => a.getAttribute( 'href' ) ) );
}

function toAdminUrl( href )
{
    if ( !href || !href.includes( 'jsp/admin/' ) || BO_EXCLUDED.test( href ) )
    {
        return null;
    }
    let url = href.startsWith( 'http' ) ? href.replace( BASE_URL, '' ) : href;
    if ( url.startsWith( 'http' ) )
    {
        return null; // another host
    }
    url = '/' + url.replace( /^\/?(lutece-core\/)?/, '' ).replace( /^\.\.\//, '' );
    url = url.replace( /([?&])token=[^&]*&?/, '$1' ).replace( /[?&]$/, '' ).replace( /#.*$/, '' );
    return url;
}

async function login( page )
{
    await page.goto( `${BASE_URL}/jsp/admin/AdminLogin.jsp`, { waitUntil: 'domcontentloaded' } );
    await page.fill( 'input[name="access_code"]', ADMIN_USER );
    await page.fill( 'input[name="password"]', ADMIN_PASSWORD );
    await Promise.all( [
        page.waitForURL( u => !String( u ).includes( 'AdminLogin.jsp' ), { timeout: 30000 } ).catch( ( ) => null ),
        page.click( 'button[type="submit"], input[type="submit"]', { noWaitAfter: true } ),
    ] );
    await page.goto( `${BASE_URL}/jsp/admin/AdminMenu.jsp`, { waitUntil: 'domcontentloaded' } );
    if ( !page.url( ).includes( 'AdminMenu.jsp' ) )
    {
        const text = ( await page.innerText( 'body' ) ).replace( /\s+/g, ' ' ).trim( ).slice( 0, 200 );
        throw new Error( `back-office login failed for "${ADMIN_USER}" — landed on ${page.url( )} : ${text}` );
    }
}

( async ( ) => {
    console.log( `\n=== LUT-33387 e2e — ${LABEL} — ${BASE_URL} ===\n` );

    const browser = await chromium.launch( { headless: true, executablePath: chromiumExecutable( ) } );
    const context = await browser.newContext( { ignoreHTTPSErrors: true, locale: 'fr-FR' } );
    const page = await context.newPage( );
    const jsErrors = [];
    page.on( 'pageerror', e => jsErrors.push( `${page.url( ).replace( BASE_URL, '' )} : ${String( e ).split( '\n' )[ 0 ]}` ) );

    let visited = 0;
    try
    {
        for ( const entry of FO_PAGES )
        {
            await visit( page, entry );
            visited++;
        }

        await login( page );

        // breadth-first crawl, two levels below the admin menu
        const seen = new Set( );
        let level = [ '/jsp/admin/AdminMenu.jsp' ];
        for ( let depth = 0; depth < 3 && level.length; depth++ )
        {
            const next = [];
            for ( const url of level )
            {
                const key = nameOf( url );
                if ( seen.has( key ) || seen.size >= MAX_PAGES )
                {
                    continue;
                }
                seen.add( key );
                const hrefs = await visit( page, { name: key, url } );
                visited++;
                for ( const h of hrefs || [] )
                {
                    const u = toAdminUrl( h );
                    if ( u && !seen.has( nameOf( u ) ) )
                    {
                        next.push( u );
                    }
                }
            }
            level = [ ...new Set( next ) ].sort( );
        }

        for ( const entry of BO_EXTRA )
        {
            await visit( page, entry );
            visited++;
        }
    }
    catch( e )
    {
        failures.push( `fatal : ${e.message}` );
    }
    finally
    {
        await browser.close( );
    }

    notes.forEach( n => console.log( '  ' + n ) );
    if ( jsErrors.length )
    {
        console.log( `\n  ${jsErrors.length} javascript error(s) in pages (informative):` );
        [ ...new Set( jsErrors ) ].slice( 0, 10 ).forEach( e => console.log( '    ' + e ) );
    }
    console.log( `\n  ${visited} page(s) visited, snapshots in ${OUT_DIR}` );

    if ( failures.length )
    {
        console.error( `\nFAIL — ${failures.length} problem(s):` );
        failures.forEach( f => console.error( '  ' + f ) );
        process.exit( 1 );
    }
    console.log( `\nPASS — ${LABEL}\n` );
} )( );
