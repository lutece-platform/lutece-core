<#function escape val>
    <#local s = val?string>
    <#if s?contains(";") || s?contains('"') || s?contains("\n")>
        <#return '"' + s?replace('"', '""') + '"'>
    </#if>
    <#return s>
</#function><#--
--><#list users as user><#--
-->${escape(user.accessCode!)}<#--
-->;${escape(user.lastName!)}<#--
-->;${escape(user.firstName!)}<#--
-->;${escape(user.email!)}<#--
-->;${escape(user.status!)}<#--
-->;${escape(user.locale!)}<#--
-->;${escape(user.level!)}<#--
-->;${escape(user.passwordReset!)}<#--
-->;${escape(user.accessibilityMode!)}<#--
-->;${escape(user.passwordMaxValidDate!)}<#--
-->;${escape(user.accountMaxValidDate!)}<#--
-->;${escape(user.dateLastLogin!)}<#--
Rôles --><#if user.roles??><#list user.roles as role>;${escape("role:" + role!)}</#list></#if><#--
Droits --><#if user.rights??><#list user.rights as right>;${escape("right:" + right!)}</#list></#if><#--
Workgroups --><#if user.workgroups??><#list user.workgroups as wg>;${escape("workgroup:" + wg!)}</#list></#if><#--
Attributs --><#if user.attributes??><#list user.attributes as attr>;${escape(attr!)}</#list></#if>
</#list>