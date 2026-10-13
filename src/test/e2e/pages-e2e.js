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
 *   XSS_MARKERS     markers.json written by xss/seed-core.py: names the field each payload came from
 *   EXTRA_FO_PAGES  comma-separated front-office URLs to visit as well (a plugin's XPages)
 *   XSS_REFLECTED   "1" replays every visited URL once per query parameter, the value replaced by
 *                   an XSS payload (reflected XSS)
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
].concat( ( process.env.EXTRA_FO_PAGES || '' ).split( ',' ).filter( Boolean ).map( u => ( { name: 'fo-' + u.replace( /^\/jsp\/site\//, '' ).replace( /[^\w=]+/g, '_' ), url: u } ) ) );

const BO_EXTRA = [
    { name: 'bo-site-preview', url: '/jsp/admin/site/AdminSite.jsp' },
    // visited after authenticating: fetching it first would consume the CSRF token of the form
    { name: 'bo-login', url: '/jsp/admin/AdminLogin.jsp' },
];

// Never follow these: they change state, end the session or stream a file.
const BO_EXCLUDED = /\/Do[A-Z]|action_|Logout|Remove|Delete|Confirm|Export|Download|\.pdf|Indexing|Reinit|Reset|Clear|Flush|Activate|Disable|Enable|Install|Uninstall|Refresh|Move|Up\.jsp|Down\.jsp|Unlock|Lock|Anonymize|Send|Import|javascript:|#/;

/* ------------------------------------------------------------- assertions */

const LEAKED_MARKUP = /&lt;\s*\/?\s*(div|span|a\s|p\s|p>|ul|li|button|img|table|tr|td|form|input|i\s|i>|strong|small|h[1-6]|script|svg|nav|section|header|footer|iframe|br|hr)\b/i;
// an attribute bundle written through ${...} under auto-escaping: <a title=&quot;x&quot;>
const ESCAPED_ATTRIBUTE = /<[a-zA-Z][^<>]*\s[\w-]+=&quot;/;
// a value escaped twice: &amp;lt; &amp;#39;
const DOUBLE_ESCAPED = /&amp;(lt|gt|quot|amp|apos|nbsp|#\d+|#x[0-9a-f]+);/i;
const RAW_DIRECTIVE = /<#(if|list|assign|local|macro|outputformat|noautoesc|else|attempt)\b|<\/#|<@[a-zA-Z]|<\/@|\$\{[a-zA-Z_]/;
const ERROR_MARKERS = [
    '<h3 class="alert-heading">Internal error', 'Veuillez contacter immédiatement l\'administrateur',
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

// XSS: a payload x'"><svg/onload=alert(N)> or <script>alert(N)</script> written by xss/seed-core.py
// (stored) or put in a query parameter (reflected). N names its origin.
const XSS_MARKERS = process.env.XSS_MARKERS ? JSON.parse( fs.readFileSync( process.env.XSS_MARKERS, 'utf8' ) ) : {};
const XSS_RAW = /<svg\/onload=alert\((\d+)\)>|<script>alert\((\d+)\)<\/script>/g;
const xssHits = [];
let currentPage = '';

function xssHit( page, marker, how )
{
    const origin = XSS_MARKERS[ marker ] || reflected.get( String( marker ) ) || `payload ${marker}`;
    xssHits.push( `${page} : ${how} — ${origin}` );
}

const reflected = new Map( );
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
    currentPage = entry.name;
    let response;
    // one retry: the first request after a template change recompiles every template it includes
    for ( let attempt = 1; !response; attempt++ )
    {
        try
        {
            response = await page.goto( BASE_URL + entry.url, { waitUntil: 'load', timeout: 30000 } );
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

    // inside a <script>, a payload JavaScript-escaped (?js_string: the closing </script> becomes <\/script>)
    // is data in a string literal: only its execution, detected by the dialog, would be a finding
    for ( const m of html.replace( /<script\b[\s\S]*?<\/script>/gi, s => s.replace( /<svg\/onload/g, '<svg_in_script' ) ).matchAll( XSS_RAW ) )
    {
        xssHit( entry.name, m[ 1 ] || m[ 2 ], 'payload written unescaped in the response' );
    }
    const injected = await page.evaluate( ( ) => document.querySelectorAll( 'svg[onload]' ).length );
    if ( injected )
    {
        xssHit( entry.name, '?', `${injected} injected <svg onload> element(s) in the DOM` );
    }

    // an XSS payload shown as text is the expected, harmless outcome: it is not leaked markup
    const markup = withoutScripts( html ).replace( /&lt;svg\/onload=alert\(\d+\)&gt;|&lt;\/?script&gt;(alert\(\d+\))?(&lt;\/script&gt;)?/g, '' );
    for ( const marker of ERROR_MARKERS )
    {
        check( entry.name, !html.includes( marker ), `error page — contains "${marker}"` );
    }
    const leaked = markup.match( LEAKED_MARKUP );
    check( entry.name, !leaked, `markup leaked as text — "${leaked && leaked[ 0 ]}"` );
    const escapedAttribute = withoutScripts( html.replace( /="[^"]*"/g, '=""' ) ).match( ESCAPED_ATTRIBUTE );
    check( entry.name, !escapedAttribute, `attribute bundle escaped — "${escapedAttribute && escapedAttribute[ 0 ].slice( -60 )}"` );
    const doubled = html.replace( /<script\b[\s\S]*?<\/script>/gi, '' ).match( DOUBLE_ESCAPED );
    check( entry.name, !doubled, `double escaping — "${doubled && doubled[ 0 ]}"` );
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

/**
 * Reflected XSS: loads a URL whose query parameter was replaced by a payload, and records whether
 * the payload executed or came back unescaped. Nothing else is checked on these pages.
 */
/**
 * Confirmation pages: Remove*.jsp (never Do*.jsp) and view=confirm* only display a message asking for
 * confirmation; nothing is clicked. The message (an i18n text holding HTML, its arguments data) is
 * checked on the page the browser lands on.
 */
const CONFIRM = /\/Remove[A-Z]\w*\.jsp\?|[?&]view=confirm\w+/;

async function confirmPage( page, url, name )
{
    currentPage = name;
    try
    {
        await page.goto( BASE_URL + url, { waitUntil: 'load', timeout: 30000 } );
        const html = await page.content( );
        const markup = withoutScripts( html ).replace( /&lt;svg\/onload=alert\(\d+\)&gt;|&lt;\/?script&gt;(alert\(\d+\))?(&lt;\/script&gt;)?/g, '' );
        for ( const marker of ERROR_MARKERS )
        {
            check( name, !html.includes( marker ), `error page — contains "${marker}"` );
        }
        const leaked = markup.match( LEAKED_MARKUP );
        check( name, !leaked, `markup leaked as text — "${leaked && leaked[ 0 ]}"` );
        for ( const m of html.matchAll( XSS_RAW ) )
        {
            xssHit( name, m[ 1 ] || m[ 2 ], 'payload in the DOM as markup' );
        }
        notes.push( `${name} : confirmation page checked (${page.url( ).replace( BASE_URL, '' ).split( '?' )[ 0 ]})` );
    }
    catch( e )
    {
        notes.push( `${name} : confirmation page failed ${e.message.split( '\n' )[ 0 ]}` );
    }
}

async function probe( page, url, name )
{
    currentPage = name;
    try
    {
        await page.goto( BASE_URL + url, { waitUntil: 'load', timeout: 30000 } );
        const fetched = await page.context( ).request.get( BASE_URL + url, { maxRedirects: 0, failOnStatusCode: false } );
        const html = ( await fetched.text( ) ).replace( /<script\b[\s\S]*?<\/script>/gi, s => s.replace( /<svg\/onload/g, '<svg_in_script' ) );
        for ( const m of html.matchAll( XSS_RAW ) )
        {
            xssHit( name, m[ 1 ] || m[ 2 ], 'reflected payload written unescaped in the response' );
        }
    }
    catch( e )
    {
        notes.push( `${name} : probe failed ${e.message.split( '\n' )[ 0 ]}` );
    }
}

function reflectedVariants( url, start )
{
    const [ path, query ] = url.split( '?' );
    const params = new URLSearchParams( query || '' );
    const variants = [];
    let n = start;
    for ( const key of [ ...params.keys( ) ] )
    {
        n++;
        const copy = new URLSearchParams( params );
        copy.set( key, `x'"><svg/onload=alert(${n})><script>alert(${n})</script>` );
        reflected.set( String( n ), `reflected ${path} parameter ${key}` );
        variants.push( { url: `${path}?${copy.toString( )}`, name: `reflected-${n}` } );
    }
    return variants;
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
    page.on( 'dialog', async d => {
        xssHit( currentPage, d.message( ), `payload EXECUTED (${d.type( )} dialog)` );
        await d.dismiss( ).catch( ( ) => null );
    } );
    const confirmUrls = new Set( );
    const visitedUrls = FO_PAGES.map( e => e.url ).concat( [ '/jsp/site/Portal.jsp?page_id=1', '/jsp/site/Portal.jsp?page=map&lang=fr' ] );

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
                visitedUrls.push( url );
                visited++;
                for ( const h of hrefs || [] )
                {
                    if ( h && CONFIRM.test( h ) && !/\/Do[A-Z]/.test( h ) )
                    {
                        confirmUrls.add( '/' + h.replace( /^\/?(lutece-core\/)?/, '' ).replace( /&amp;/g, '&' ) );
                    }
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

        let nConfirm = 0;
        for ( const url of [ ...confirmUrls ].sort( ) )
        {
            await confirmPage( page, url, 'confirm-' + nameOf( url ) );
            nConfirm++;
        }
        console.log( `  ${nConfirm} confirmation page(s) checked` );

        if ( process.env.XSS_REFLECTED === '1' )
        {
            let n = 900;
            const seenPaths = new Set( );
            for ( const url of visitedUrls )
            {
                const signature = url.split( '?' )[ 0 ] + '?' + [ ...new URLSearchParams( url.split( '?' )[ 1 ] || '' ).keys( ) ].sort( ).join( '&' );
                if ( !url.includes( '?' ) || seenPaths.has( signature ) )
                {
                    continue;
                }
                seenPaths.add( signature );
                for ( const v of reflectedVariants( url, n ) )
                {
                    await probe( page, v.url, v.name );
                    n++;
                }
            }
            console.log( `  ${n - 900} reflected XSS probe(s)` );
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

    if ( xssHits.length )
    {
        const unique = [ ...new Set( xssHits ) ];
        console.error( `\nXSS — ${unique.length} finding(s):` );
        unique.forEach( h => console.error( '  ' + h ) );
        failures.push( `${unique.length} XSS finding(s)` );
    }

    if ( failures.length )
    {
        console.error( `\nFAIL — ${failures.length} problem(s):` );
        failures.forEach( f => console.error( '  ' + f ) );
        process.exit( 1 );
    }
    console.log( `\nPASS — ${LABEL}\n` );
} )( );
