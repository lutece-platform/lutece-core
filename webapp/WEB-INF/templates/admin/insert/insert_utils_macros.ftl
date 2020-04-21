<#macro linkProperties input selected_text >
    <@input type='hidden' name='input' value='${input}' />
    <@table>
        <tr>
            <th colspan="2">#i18n{portal.insert.labelLinkProperties}</th>
        </tr>
        <tr>
            <td>#i18n{portal.insert.labelLinkText} *</td>
            <td><@linkTextInput selected_text=selected_text /></td>
        </tr>
        <tr>
            <td>#i18n{portal.insert.labelLinkTitle} *</td>
            <td><@linkTitleInput /></td>
        </tr>
        <tr>
            <td>#i18n{portal.insert.labelLinkTarget}</td>
            <td>
                <@linkTargetCombo />
            </td>
        </tr>
    </@table>
</#macro>

<#macro linkTextInput selected_text >
    <@input type='text' name='text' value='${selected_text}' inputSize='30' maxlength='50' />
</#macro>
    
<#macro linkTitleInput>
    <@input type='text' name='title' value='' inputSize='30' maxlength='50' />
</#macro>

<#macro linkTargetCombo >
    <@select name='target'>
        <option value="_self">#i18n{portal.insert.labelTargetSelf}
        <option value="_blank">#i18n{portal.insert.labelTargetBlank}
    </@select>
</#macro>
