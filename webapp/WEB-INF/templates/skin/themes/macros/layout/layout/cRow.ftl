<#--
Macro: cRow

Description: Generates a Bootstrap grid row element to contain columns.

Parameters:
- class (string, optional): Additional CSS class(es) for the row. Default: ''.
- id (string, optional): Unique identifier for the row element. Default: ''.
- params (string, optional): Additional HTML attributes for the row element. Default: ''.
- attrs (hash, optional): additional HTML attributes as name/value pairs, e.g. {'aria-hidden': 'true'}. Prefer it to params: the macro writes the attribute and its value is escaped like any value of the page.

Showcase:
- desc: "Ligne de grille - @cRow"
- bs: layout/grid
- newFeature: false

Snippet:

    Basic row with columns:

    <@cRow>
        <@cCol cols='md-6'>Left column</@cCol>
        <@cCol cols='md-6'>Right column</@cCol>
    </@cRow>

    Row with alignment and spacing:

    <@cRow class='justify-content-center g-3' id='content-row'>
        <@cCol cols='lg-4'>Card 1</@cCol>
        <@cCol cols='lg-4'>Card 2</@cCol>
        <@cCol cols='lg-4'>Card 3</@cCol>
    </@cRow>

-->
<#macro cRow class='' id='' params='' attrs={} deprecated...>
<@deprecatedWarning args=deprecated />
<@cSection type='div' class='row ${class}' id=id params=params attrs=attrs>
<#nested>
</@cSection>
</#macro>