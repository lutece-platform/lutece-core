<#--
Macro: cCol

Description: Generates a Bootstrap grid column with configurable responsive breakpoints and sizing.

Parameters:
- cols (string, optional): Column size specification. The 'col-' prefix is added automatically; use values like 'md-6', 'lg-4', 'sm-12 md-8'. Default: ''.
- default (string, optional): Fallback CSS class when cols is empty. Default: 'col'.
- class (string, optional): Additional CSS class(es) for the column. Default: ''.
- id (string, optional): Unique identifier for the column element. Default: ''.
- params (string, optional): Additional HTML attributes for the column element. Default: ''.

Snippet:

    Basic auto-width column:

    <@cCol>
        <p>Column content</p>
    </@cCol>

    Responsive column with specific sizes:

    <@cCol cols='sm-12 md-6 lg-4' class='mb-3'>
        <p>Responsive column content</p>
    </@cCol>

-->
<#macro cCol cols='' default='col' class='' id='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<#if cols!=''>
    <#local cClass>col-${cols} ${class}</#local> 
<#else>
    <#local cClass>${default!} ${class}</#local>
</#if>
<@cSection type='div' class=cClass id=id params=params >
<#nested>
</@cSection>
</#macro>