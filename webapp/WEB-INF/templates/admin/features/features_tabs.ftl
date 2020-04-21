<#macro tabsFeatures tab='features'>
        <li <#if tab='features'>class="${gClassActive}" </#if>>
            <@link href='jsp/admin/features/DispatchFeatures.jsp'>#i18n{portal.features.features_tabs.featureDispatch.label}</@link>
        </li>
        <li <#if tab='groups'>class="${gClassActive}" </#if> >
            <@link href='jsp/admin/features/ManageGroups.jsp'>#i18n{portal.features.features_tabs.groupManagement.label}</@link>
        </li>
        <li <#if tab='external'>class="${gClassActive}" </#if>>
            <@link href='jsp/admin/features/ManageExternalFeatures.jsp'>#i18n{portal.features.features_tabs.externalFeaturesManagement.label}</@link>
        </li>
</#macro>
