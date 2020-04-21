<#include "admindashboard_utils.ftl">

<@adminDashboardPanel title='#i18n{portal.users.adminDashboard.pageTitle}' icon='users' parentId='technical_settings' childId='users_advanced_parameters'>

<@row>
	<@columns lg=3>
		<@tabs>
			<@tabList type='pills' vertical=true>
			<@tabLink active=true href='#defaultUserParameterValues' title='#i18n{portal.users.manage_advanced_parameters.defaultUserParameterValues}' tabIcon='user' />
			<@tabLink href='#ModifyEmailPattern' title='#i18n{portal.users.manage_advanced_parameters.labelModifyEmailPattern}' tabIcon='at' />
			<@tabLink href='#advancedSecurityParameters' title='#i18n{portal.users.manage_advanced_parameters.securityParameters}' tabIcon='shield-alt' />
			<@tabLink href='#lifeTimeEmails' title='#i18n{portal.users.accountLifeTime.labelLifeTimeNotifications}' tabIcon='envelope' />
			<@tabLink href='#userAttributes' title='#i18n{portal.users.manage_attributes.pageTitle}' tabIcon='id-card' />
			<@tabLink href='#anonymizeUsers' title='#i18n{portal.users.anonymize_user.titleAnonymizeUser}' tabIcon='user-secret' />
			<@tabLink href='#userRightsLevels' title='#i18n{portal.users.adminFeature.level_right_management.name}' tabIcon='users' />
			</@tabList>
		</@tabs>
	</@columns>

	<@columns lg=9>
		<@tabContent>
			<#include "user_admindashboard_advanced.ftl" />
			<#include "user_admindashboard_security.ftl" />
			<#include "user_admindashboard_attributes.ftl" />
			<#include "user_admindashboard_anonymization.ftl" />
			<#include "user_admindashboard_right_levels.ftl" />
		</@tabContent>
	</@columns>
</@row>
</@adminDashboardPanel>

