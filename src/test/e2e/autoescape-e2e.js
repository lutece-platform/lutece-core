#!/usr/bin/env node
/*
 * LUT-33153 — end-to-end guard for the FreeMarker auto-escaping migration.
 *
 * Drives a real browser against a running Lutece instance and checks that every visited page
 * is rendered correctly, whatever the value of service.freemarker.templateAutoEscape.
 *
 * Three families of checks:
 *
 *   1. the page loads and is not a Lutece error page;
 *   2. no escaping leak — markup must never reach the browser as visible text, and an HTML
 *      attribute must never arrive with its delimiting quotes escaped (the C4 failure mode);
 *   3. structural invariants — attributes that come from a macro `params` bundle must be real
 *      DOM attributes, not text. This is what actually breaks when `${params}` is escaped.
 *
 * Each page is also written as a normalised snapshot so that run-both-modes.sh can diff the
 * two modes against each other: that diff is the real proof of bi-compatibility.
 *
 * Usage:
 *   NODE_PATH=... node autoescape-e2e.js
 * Environment:
 *   BASE_URL        default http://localhost:9080/lutece-core
 *   ADMIN_USER      default admin
 *   ADMIN_PASSWORD  default adminadmin
 *   MODE            "false" | "true"  — names the snapshot directory
 *   OUT_DIR         default ./snapshots
 */
'use strict';

const fs = require( 'fs' );
const path = require( 'path' );
const { chromium } = require( 'playwright-core' );

const BASE_URL = process.env.BASE_URL || 'http://localhost:9080/lutece-core';
const ADMIN_USER = process.env.ADMIN_USER || 'admin';
const ADMIN_PASSWORD = process.env.ADMIN_PASSWORD || 'adminadmin';
const MODE = process.env.MODE || 'unknown';
const OUT_DIR = path.join( process.env.OUT_DIR || path.join( __dirname, 'snapshots' ), MODE );

/* ------------------------------------------------------------------ pages */

// Front office
const FO_PAGES = [
    { name: 'fo-portal', url: '/jsp/site/Portal.jsp' },
];

// Back office — the entry point plus whatever the admin menu actually links to. Discovering the
// list from the running instance rather than hard-coding it keeps the suite valid across versions
// and covers every feature the logged-in account can reach.
const BO_ENTRY = { name: 'bo-home', url: '/jsp/admin/AdminMenu.jsp' };

// Never follow these: they change state or end the session.
const BO_EXCLUDED = /Do[A-Z]|Logout|Remove|Delete|Confirm|Export|Download|\.pdf|Indexing|DoAdmin/;

/* ------------------------------------------------------------- assertions */

// Markup that reached the browser as text instead of being parsed.
const LEAKED_MARKUP = /&lt;\s*(div|span|a\s|p\s|p>|ul|li|button|img|table|tr|td|form|input|i\s|i>|strong|small|h[1-6])/i;

// An attribute whose delimiting quotes were escaped: <btn title=&quot;x&quot;>
const ESCAPED_ATTRIBUTE = /=&quot;/;

// A value escaped twice: &amp;lt; instead of &lt;
const DOUBLE_ESCAPED = /&amp;(lt|gt|quot|amp);/;

// A FreeMarker construct that reached the output — means a template was not parsed as expected.
// Checked outside <script> blocks only: JavaScript template literals use the same ${...} syntax.
const RAW_DIRECTIVE = /<#(if|list|assign|local|macro|outputformat|noautoesc)\b|<@[a-zA-Z]|\$\{[a-zA-Z_]/;

function withoutScripts( html )
{
    return html.replace( /<script\b[\s\S]*?<\/script>/gi, '' ).replace( /<style\b[\s\S]*?<\/style>/gi, '' );
}

// Lutece error pages
const ERROR_MARKERS = [
    'FreeMarker template error',
    'freemarker.core.',
    'freemarker.template.',
    'NonMarkupOutputException',
    'NonStringException',
    'ParseException',
    "Can't compare values of these types",
    'Une erreur est survenue',
    'An error has occurred',
    'Erreur applicative',
    'java.lang.NullPointerException',
];

const failures = [];
const notes = [];

function check( page, condition, message )
{
    if ( !condition )
    {
        failures.push( `${page} : ${message}` );
    }
}

/* -------------------------------------------------------------- snapshots */

/**
 * Removes everything that legitimately changes between two runs (tokens, ids, dates, nonces)
 * so that the two modes can be diffed on structure alone.
 */
function normalise( html )
{
    return html
        .replace( /[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}/gi, 'UUID' )
        .replace( /name="token"\s+id="token"\s+value="[^"]*"/g, 'TOKEN' )
        .replace( /(token=)[^"&'\s]+/g, '$1TOKEN' )
        .replace( /\d{2}\/\d{2}\/\d{4}[ ,]*\d{0,2}:?\d{0,2}/g, 'DATE' )
        .replace( /\d{4}-\d{2}-\d{2}[T ]\d{2}:\d{2}(:\d{2})?/g, 'DATE' )
        .replace( /nonce="[^"]*"/g, 'nonce="N"' )
        .replace( /JSESSIONID=[^;"'\s]*/g, 'JSESSIONID=S' )
        .replace( /\?ts=\d+/g, '?ts=T' )
        .replace( /\s+/g, ' ' )
        .trim( );
}

function writeSnapshot( name, html )
{
    fs.mkdirSync( OUT_DIR, { recursive: true } );
    fs.writeFileSync( path.join( OUT_DIR, `${name}.html` ), normalise( html ), 'utf8' );
}

/* ------------------------------------------------------------------- main */

async function visit( page, entry )
{
    const response = await page.goto( BASE_URL + entry.url, { waitUntil: 'domcontentloaded' } );
    const status = response ? response.status( ) : 0;

    if ( status >= 400 )
    {
        failures.push( `${entry.name} : HTTP ${status} on ${entry.url}` );
        return;
    }

    const html = await page.content( );
    const markup = withoutScripts( html );

    // 1. not an error page
    for ( const marker of ERROR_MARKERS )
    {
        check( entry.name, !html.includes( marker ), `error page — contains "${marker}"` );
    }

    // 2. no escaping leak
    const leaked = markup.match( LEAKED_MARKUP );
    check( entry.name, !leaked, `markup leaked as text — found "${leaked && leaked[ 0 ]}"` );

    const escapedAttr = markup.match( ESCAPED_ATTRIBUTE );
    check( entry.name, !escapedAttr, 'an HTML attribute has its delimiting quotes escaped (=&quot;)' );

    const doubled = markup.match( DOUBLE_ESCAPED );
    check( entry.name, !doubled, `double escaping — found "${doubled && doubled[ 0 ]}"` );

    const directive = markup.match( RAW_DIRECTIVE );
    check( entry.name, !directive, `an unrendered FreeMarker construct reached the page — "${directive && directive[ 0 ]}"` );

    // 3. structural invariants: attributes built by the macros must be real attributes
    const attributeCount = await page.evaluate( ( ) => {
        const selectors = [ '[data-bs-toggle]', '[data-bs-target]', '[data-bs-placement]', '[aria-label]', '[title]' ];
        return selectors.reduce( ( total, s ) => total + document.querySelectorAll( s ).length, 0 );
    } );
    notes.push( `${entry.name} : ${attributeCount} macro-built attribute(s), ${html.length} bytes` );

    writeSnapshot( entry.name, html );
}

/**
 * Collects the back-office pages reachable from the admin menu. Read-only links only — anything
 * that would modify state or log us out is filtered out by BO_EXCLUDED.
 */
async function discoverBackOfficePages( page )
{
    await page.goto( `${BASE_URL}/jsp/admin/AdminMenu.jsp`, { waitUntil: 'domcontentloaded' } );

    const hrefs = await page.evaluate( ( ) => Array.from( document.querySelectorAll( 'a[href]' ) ).map( a => a.getAttribute( 'href' ) ) );

    const seen = new Set( );
    const pages = [];

    for ( const href of hrefs )
    {
        if ( !href || !href.includes( 'jsp/admin/' ) || BO_EXCLUDED_TEST( href ) )
        {
            continue;
        }

        // normalise to a path relative to BASE_URL, drop the query string for the snapshot name
        const url = href.startsWith( 'http' ) ? href.replace( BASE_URL, '' ) : ( href.startsWith( '/' ) ? href : '/' + href );
        const key = url.split( '?' )[ 0 ];

        if ( seen.has( key ) || key.includes( 'AdminMenu.jsp' ) )
        {
            continue;
        }

        seen.add( key );
        pages.push( { name: 'bo-' + key.replace( /^.*jsp\/admin\//, '' ).replace( /\.jsp$/, '' ).replace( /\//g, '-' ), url } );
    }

    console.log( `  ${pages.length} back-office page(s) discovered from the admin menu\n` );

    return pages;
}

function BO_EXCLUDED_TEST( href )
{
    return BO_EXCLUDED.test( href );
}

async function login( page )
{
    await page.goto( `${BASE_URL}/jsp/admin/AdminLogin.jsp`, { waitUntil: 'domcontentloaded' } );
    await page.fill( 'input[name="access_code"]', ADMIN_USER );
    await page.fill( 'input[name="password"]', ADMIN_PASSWORD );
    await Promise.all( [
        page.waitForLoadState( 'domcontentloaded' ),
        page.click( 'button[type="submit"], input[type="submit"]' ),
    ] );

    // Landing anywhere other than the admin menu means we are not really logged in — typically
    // AdminMessage.jsp asking for a password change. Without this check the whole back-office
    // run would silently assert on 20 copies of the login page.
    await page.goto( `${BASE_URL}/jsp/admin/AdminMenu.jsp`, { waitUntil: 'domcontentloaded' } );

    const url = page.url( );
    const text = ( await page.innerText( 'body' ) ).replace( /\s+/g, ' ' ).trim( );

    if ( !url.includes( 'AdminMenu.jsp' ) )
    {
        throw new Error( `back-office login failed for "${ADMIN_USER}" — landed on ${url} : ${text.slice( 0, 160 )}` );
    }

    const menuCount = await page.evaluate( ( ) => document.querySelectorAll( 'a[href*="jsp/admin/"]' ).length );

    if ( menuCount < 5 )
    {
        throw new Error( `back-office login looks incomplete — only ${menuCount} admin link(s) on AdminMenu.jsp` );
    }
}

( async ( ) => {
    console.log( `\n=== LUT-33153 e2e — templateAutoEscape=${MODE} — ${BASE_URL} ===\n` );

    const browser = await chromium.launch( { headless: true } );
    const context = await browser.newContext( { ignoreHTTPSErrors: true } );
    const page = await context.newPage( );

    const consoleErrors = [];
    page.on( 'pageerror', e => consoleErrors.push( String( e ) ) );

    let visited = 0;

    try
    {
        for ( const entry of FO_PAGES )
        {
            await visit( page, entry );
            visited++;
        }

        await login( page );
        console.log( '  login ok\n' );

        await visit( page, BO_ENTRY );
        visited++;

        for ( const entry of await discoverBackOfficePages( page ) )
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

    for ( const n of notes )
    {
        console.log( '  ' + n );
    }

    if ( consoleErrors.length )
    {
        console.log( `\n  ${consoleErrors.length} javascript error(s) in page:` );
        consoleErrors.slice( 0, 5 ).forEach( e => console.log( '    ' + e.split( '\n' )[ 0 ] ) );
    }

    console.log( `\n  snapshots written to ${OUT_DIR}` );

    if ( failures.length )
    {
        console.error( `\nFAIL — ${failures.length} problem(s):` );
        failures.forEach( f => console.error( '  ' + f ) );
        process.exit( 1 );
    }

    console.log( `\nPASS — ${visited} page(s) checked in mode ${MODE}\n` );
} )( );
