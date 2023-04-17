<#-- Macro: icon
Description: Generates an icon element with customizable attributes.
Parameters:
- prefix (string, optional): the prefix of the icon library.
- style (string, optional): the style of the icon.
- class (string, optional): additional CSS classes to add to the icon.
- title (string, optional): the title attribute of the icon.
- id (string, optional): the ID attribute of the icon.
- params (string, optional): additional parameters to add to the icon.
-->
<#macro icon prefix='ti ti-' style='' class='' title='' id='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<#-- Compat from Font Awesome to Tabler Icons -->
<#local cssStyle = ' ' + style?keep_after(' ') />
<#local tiStyle = style?keep_before(' ') />
<#switch tiStyle>
    <#case 'cog'>
        <#local iconStyle = 'settings' />
        <#break>
    <#case 'wrench'>
        <#local iconStyle = 'tool' />
        <#break>
    <#case 'remove'>
    <#case 'times'>
        <#local iconStyle = 'x' />
        <#break>
    <#case 'puzzle-piece'>
        <#local iconStyle = 'puzzle' />
        <#break>
    <#case 'desktop'>
        <#local iconStyle = 'device-desktop' />
        <#break>
    <#case 'tablet'>
        <#local iconStyle = 'device-tablet' />
        <#break>
    <#case 'mobile'>
        <#local iconStyle = 'device-mobile' />
        <#break>
    <#case 'shield-alt'>
        <#local iconStyle = 'shield' />
        <#break>
    <#case 'eye-slash'>
        <#local iconStyle = 'eye-off' />
        <#break>
    <#case 'envelope'>
        <#local iconStyle = 'mail' />
        <#break>
    <#case 'id-card'>
        <#local iconStyle = 'id' />
        <#break>
    <#case 'user-secret'>
        <#local iconStyle = 'user-question' />
        <#break>
    <#case 'id-card'>
        <#local iconStyle = 'id' />
        <#break>
    <#case 'list-alt'>
        <#local iconStyle = 'list' />
        <#break>
    <#case 'arrows-alt'>
        <#local iconStyle = 'arrows-maximize' />
        <#break>
    <#case 'sync'>
    <#case 'redo'>
        <#local iconStyle = 'refresh' />
        <#break>
    <#case 'play'>
        <#local iconStyle = 'player-play-filled' />
        <#break>
    <#case 'stop'>
        <#local iconStyle = 'player-stop-filled' />
        <#break>
    <#case 'sort'>
    <#case 'arrows'>
        <#local iconStyle = 'arrows-sort' />
        <#break>
    <#case 'step-forward'>
        <#local iconStyle = 'switch-horizontal' />
        <#break>
    <#case 'file-pdf-o'>
        <#local iconStyle = 'pdf' />
        <#break>
    <#case 'file-alt'>
        <#local iconStyle = 'forms' />
        <#break>
    <#case 'sticky-note'>
        <#local iconStyle = 'align-box-left-top' />
        <#break>
    <#case 'check-square'>
        <#local iconStyle = 'square-check' />
        <#break>
    <#case 'dot-circle'>
        <#local iconStyle = 'circle-dot' />
        <#break>
    <#case 'hashtag'>
        <#local iconStyle = 'hash' />
        <#break>
    <#case 'image'>
        <#local iconStyle = 'photo' />
        <#break>
    <#case 'comment'>
        <#local iconStyle = 'message' />
        <#break>
    <#case 'futbol'>
        <#local iconStyle = 'gavel' />
        <#break>
    <#case 'map-marked-alt'>
        <#local iconStyle = 'map-pin' />
        <#break>
    <#case 'phone-square'>
        <#local iconStyle = 'phone' />
        <#break>
    <#case 'list-ol'>
        <#local iconStyle = 'list-numbers' />
        <#break>
    <#case 'th-list'>
        <#local iconStyle = 'list-details' />
        <#break>
    <#case 'question'>
        <#local iconStyle = 'question-mark' />
        <#break>
    <#case 'vote-yea'>
        <#local iconStyle = 'thumb-up' />
        <#break>
    <#case 'comments'>
        <#local iconStyle = 'messages' />
        <#break>
    <#default>
        <#local iconStyle = style />
</#switch>
<#if cssStyle?trim !=''><#local iconStyle = iconStyle + cssStyle /></#if>
<i class="${prefix}${iconStyle}<#if class!=''> ${class}</#if>" aria-hidden="true"<#if title!=''> title='${title}'</#if><#if id!=''> id="${id}"</#if><#if params!=''> ${params}</#if>></i>
</#macro>