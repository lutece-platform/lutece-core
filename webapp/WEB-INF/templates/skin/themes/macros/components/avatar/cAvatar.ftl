<#--
Macro: cAvatar

Description: Generates an avatar: a round picture of a person, or their initials when there is no picture.

Parameters:
- name (string, optional): Name of the person. Gives the initials (first letter of the first two words) and the alternative text. Default: ''.
- img (string, optional): URL of the picture. Default: '', the initials are displayed.
- initials (string, optional): Initials to display instead of those computed from the name. Default: ''.
- size (string, optional): '', 'sm', 'lg' or 'xl'. Default: ''.
- color (string, optional): Background of the initials: '' (primary), 'secondary' or 'light'. Default: ''.
- status (string, optional): Displays a status dot: 'online' or 'offline'. Default: ''.
- statusLabel (string, optional): Accessible text of the status dot. Default: ''.
- decorative (boolean, optional): true when the name is written next to the avatar, which is then hidden from screen readers. false gives the avatar the role img and the name as label. Default: true.
- listItem (boolean, optional): Wraps the avatar in a li, to use inside cAvatarGroup. Default: false.
- class (string, optional): Additional CSS classes. Default: ''.
- id (string, optional): HTML id attribute. Default: ''.
- params (map, optional): additional HTML attributes as name/value pairs, e.g. {'aria-label': label}. Names out of the allow list of htmlAttributes (event handlers, URLs, styles...) are refused and every value is escaped. Default: {}.

Showcase:
- desc: Avatar - @cAvatar
- newFeature: true
- updatedFeature: false
- deprecated: false

Snippet:

    Initials, next to the name:

    <@cAvatar name='Anne Martin' /> Anne Martin

    Picture, alone:

    <@cAvatar name='Anne Martin' img='images/avatar.jpg' size='lg' decorative=false />

    With a status:

    <@cAvatar name='Jean Dupont' status='online' statusLabel='En ligne' />

-->
<#macro cAvatar name='' img='' initials='' size='' color='' status='' statusLabel='' decorative=true listItem=false class='' id='' params={} deprecated...>
<@deprecatedWarning args=deprecated />
<#local avatarInitials = initials />
<#if avatarInitials == '' && name?trim != ''>
	<#list name?trim?split(r'\s+', 'r') as word><#if word?index lt 2 && word != ''><#local avatarInitials = avatarInitials + word[0]?upper_case /></#if></#list>
</#if>
<#if listItem><li></#if>
<span class="lutece-ds-avatar<#if size!=''> lutece-ds-avatar-${size}</#if><#if color!=''> lutece-ds-avatar-${color}</#if><#if class!=''> ${class}</#if>"<#if decorative> aria-hidden="true"<#elseif name!=''> role="img" aria-label="${name}<#if statusLabel!=''> (${statusLabel})</#if>"</#if><@commonAttrs id=id params=params />>
<#if img!=''><img src="${img}" alt="" loading="lazy"><#else>${avatarInitials}</#if>
<#if status!=''><span class="lutece-ds-avatar-status ${status}"></span></#if>
<#nested>
</span>
<#if listItem></li></#if>
</#macro>
