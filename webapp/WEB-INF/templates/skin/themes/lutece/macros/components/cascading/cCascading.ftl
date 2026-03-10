<#--
Macro: cCascading

Description: Generates a collapsible details/summary element that reveals or hides content on click.

Parameters:
- title (string, required): The label displayed in the summary toggle.
- id (string, optional): The unique identifier for the cascading element. Default: ''.
- class (string, optional): Additional CSS class(es) for the cascading container. Default: ''.
- params (string, optional): Additional HTML attributes for the cascading element. Default: ''.
- state (boolean, optional): If true, the element is open on page load. Default: false.

Showcase:
- desc: Cascading - @cCascading
- newFeature: false

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
<#macro cCascading title id='' class='' params='' state=false >
<details<#if id !=''> id='${id}'</#if> class="cascading ${class!}" <#if state> open</#if> aria-expanded="<#if state>true<#else>false</#if>" ${params}>
  <summary><span class="cascading-label">${title}</span></summary>
  <div class="cascading-content"><#nested></div>
</details>
</#macro>