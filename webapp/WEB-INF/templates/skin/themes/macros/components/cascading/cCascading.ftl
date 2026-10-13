<#--
Macro: cCascading

Description: Generates a collapsible details/summary element that reveals or hides content on click.

Parameters:
- title (string, required): The label displayed in the summary toggle.
- id (string, optional): The unique identifier for the cascading element. Default: ''.
- class (string, optional): Additional CSS class(es) for the cascading container. Default: ''.
- params (string, optional): Additional HTML attributes for the cascading element. Default: ''.
- attrs (hash, optional): additional HTML attributes as name/value pairs, e.g. {'aria-hidden': 'true'}. Prefer it to params: the macro writes the attribute and its value is escaped like any value of the page.
- state (boolean, optional): If true, the element is open on page load. Default: false.

Showcase:
- desc: Cascading - @cCascading
- newFeature: false
- updatedFeature: true
- deprecated: false

Snippet:

    Basic collapsible section:

    <@cCascading title='More information'>
        <p>Here is additional content that can be expanded or collapsed.</p>
    </@cCascading>

    Open by default:

    <@cCascading title='Frequently asked questions' state=true>
        <p>Find answers to common questions below.</p>
    </@cCascading>

-->
<#macro cCascading title id='' class='' params='' state=false attrs={} >
<details<#if id?has_content> id='${id}'</#if> class="cascading ${class!}" <#if state> open</#if> aria-expanded="<#if state>true<#else>false</#if>"<@commonAttrs params=params attrs=attrs />>
  <summary><span class="cascading-label">${title}</span></summary>
  <div class="cascading-content"><#nested></div>
</details>
</#macro>