<#--
Macro: cAvatarGroup

Description: Generates a group of overlapping avatars (cAvatar), for example the participants of an event.

Parameters:
- label (string, optional): Accessible name of the group, read by screen readers (aria-label). Default: ''.
- class (string, optional): Additional CSS classes. Default: ''.
- id (string, optional): HTML id attribute. Default: ''.
- params (map, optional): additional HTML attributes as name/value pairs, e.g. {'aria-label': label}. Names out of the allow list of htmlAttributes (event handlers, URLs, styles...) are refused and every value is escaped. Default: {}.

Showcase:
- desc: Groupe d'avatars - @cAvatarGroup
- newFeature: true
- updatedFeature: false
- deprecated: false

Snippet:

    Group of avatars:

    <@cAvatarGroup label='Participants'>
        <@cAvatar name='Anne Martin' decorative=false listItem=true />
        <@cAvatar name='Jean Dupont' color='secondary' decorative=false listItem=true />
    </@cAvatarGroup>

-->
<#macro cAvatarGroup label='' class='' id='' params={} deprecated...>
<@deprecatedWarning args=deprecated />
<ul class="lutece-ds-avatar-group<#if class!=''> ${class}</#if>"<#if label!=''> aria-label="${label}"</#if><@commonAttrs id=id params=params />>
<#nested>
</ul>
</#macro>
