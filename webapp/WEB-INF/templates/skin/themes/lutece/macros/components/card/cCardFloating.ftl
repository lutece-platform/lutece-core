<#--
Macro: cCardFloating

Description: Generates a floating card overlay with an optional close button, suitable for contextual information panels.

Parameters:
- id (string, required): The unique identifier for the floating card.
- title (string, optional): The card title text. Default: ''.
- titleLevel (number, optional): Heading level for the title. Default: 3.
- class (string, optional): Additional CSS class(es) for the floating card. Default: ''.
- dismissible (boolean, optional): If true, displays a close button. Default: true.
- params (string, optional): Additional HTML attributes for the floating card. Default: ''.

Showcase:
- desc: Carte flottante - @cCardFloating
- bs: components/card
- newFeature: false

Snippet:

    Dismissible floating card:

    <@cCardFloating id='welcome-notice' title='Welcome'>
        <p>This is your first visit. Discover our new features!</p>
    </@cCardFloating>

    Non-dismissible floating card:

    <@cCardFloating id='info-panel' title='Important notice' dismissible=false>
        <p>Scheduled maintenance on Saturday from 2am to 6am.</p>
    </@cCardFloating>

-->
<#macro cCardFloating id title='' titleLevel=3 class='' dismissible=true params='' deprecated...>
<@deprecatedWarning args=deprecated />
<div class="card card-floating<#if class!=''> ${class!}</#if>" id="${id!}-card" <#if params!=''> ${params!}</#if>>
    <div class="card-body">
    	<div class='card-title-container'>
    		<@cTitle level=titleLevel class='card-title'>${title!}</@cTitle>
    		<#if dismissible>
              <button type="button" class="btn-close" aria-label="#i18n{portal.theme.labelClose}" data-bs-target="#${id!}-card" data-bs-dismiss="alert" />
              </button>
            </#if>
       	</div>
        <#if title!=''></#if>
        <#nested>
    </div>
</div>
</#macro>