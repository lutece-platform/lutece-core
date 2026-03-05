<#-- 
Macro: formField

Description: Generates a form row with the specified CSS class.

Parameters:
- class (string, optional): additional classes to add to the form row.

Snippet:

    Basic form row with two columns:

    <@formField>
        <div class="col-6">
            <@input type='text' name='firstName' id='firstName' placeHolder='First name' />
        </div>
        <div class="col-6">
            <@input type='text' name='lastName' id='lastName' placeHolder='Last name' />
        </div>
    </@formField>

-->
<#macro formField class='' deprecated...>
<@deprecatedWarning args=deprecated />
<div class="form-row mb-3<#if class!=''> ${class}</#if>">
<#nested>
</div>
</#macro>