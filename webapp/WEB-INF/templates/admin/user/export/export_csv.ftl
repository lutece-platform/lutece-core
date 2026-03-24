<#ftl strip_whitespace=true>
<#list users as user>
"${user.accessCode}";"${user.lastName}";"${user.firstName}";"${user.email}";"${user.status}";"${user.locale}";"${user.level}";"${user.passwordReset}";"${user.accessibilityMode}";"${user.passwordMaxValidDate}";"${user.accountMaxValidDate}";"${user.dateLastLogin}"<#--
Rôles --><#if user.roles??><#list user.roles as role>;"role:${role}"</#list></#if><#--
Droits --><#if user.rights??><#list user.rights as right>;"right:${right}"</#list></#if><#--
Workgroups --><#if user.workgroups??><#list user.workgroups as wg>;"workgroup:${wg}"</#list></#if><#--
Attributs --><#if user.attributes??><#list user.attributes as attr>;"${attr.id}:${attr.fieldId}:${attr.value}"</#list></#if>
</#list>