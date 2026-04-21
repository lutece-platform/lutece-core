<#--
Macro: cBtnGroup

Description: Generates a group of buttons, either from a list of button objects or from nested content.

Parameters:
- label (string, required): The aria-label for the button group.
- buttonList (object, optional): List of button items, each with 'label', 'class', and optional 'disabled' attributes. Default: {}.
- class (string, optional): Additional CSS class(es) for the button group. Default: ''.
- id (string, optional): The unique identifier for the button group. Default: ''.
- type (string, optional): Layout type for the group ('vertical' for stacked). Default: ''.
- params (string, optional): Additional HTML attributes for the button group. Default: ''.

Showcase:
- desc: Groupe de boutons - @cBtnGroup
- bs: components/button-group
- newFeature: false

Snippet:

    Button group with nested buttons:

    <@cBtnGroup label='Actions'>
        <@cBtn label='Edit' class='secondary' />
        <@cBtn label='Delete' class='danger' />
    </@cBtnGroup>

    Button group from a list:

    <@cBtnGroup label='Options' buttonList=[
        {'label': 'Option A', 'class': 'primary', 'disabled': false},
        {'label': 'Option B', 'class': 'secondary', 'disabled': false}
    ] />

-->
<#macro cBtnGroup label buttonList={} class='' id='' type='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<#local btnClass>btn-group<#if type='vertical'> btn-group-vertical</#if><#if class!=''> ${class}</#if></#local>
<@cSection type='div' class=btnClass id=id params='${params} role="group" aria-label="${label}"'>
	<#if buttonList?has_content>
		<#if type == 'vertical'>
		    <@chList class="list-unstyled d-flex flex-column m-0">
		         <#list buttonList as button>
		                <@chItem class='m-0 d-flex' >
		                    <@cBtn label='${button.label}' class='${button.class} w-100' disabled=button.disabled />
		                </@chItem>
		         </#list>
		    </@chList>
		<#else>
		    <@chList class="list-unstyled d-flex flex-wrap m-0">
		         <#list buttonList as button>
		                <@chItem class='m-0 d-flex' >
		                    <@cBtn label='${button.label}' class='${button.class}' disabled=button.disabled />
		                </@chItem>
		         </#list>
		    </@chList>
		</#if>
	<#else>
		<#nested>
	</#if>
</@cSection>
</#macro>