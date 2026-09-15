---
description: "Lutece 8 Freemarker constraints: layout macros, form components, JSP paths, i18n"
paths:
  - "**/templates/admin/**/*.html"
---

# Freemarker Templates — Lutece 8

## Reference Sources — MANDATORY

Before writing or modifying a template, ALWAYS consult the macros definition sources in `~/.lutece-references/lutece-core/webapp/WEB-INF/templates/admin/themes/tabler/`
and templates examples here : ~/.lutece-references/lutece-core/webapp/WEB-INF/templates/admin/themes/tabler/

## Bootstrap 5 & Tabler Icons — Already Loaded

BS5 (CSS + JS) and Tabler Icons are globally loaded by the admin theme. Do NOT add `<link>` or `<script>` imports for these resources. Use BS5 classes and Tabler icons (`ti ti-*`) **only as a last resort** — when no Freemarker macro exists for the need AND no existing admin template in `~/.lutece-references/lutece-core/` already demonstrates the desired layout. Always prefer macros over raw HTML/BS5.

## Layout Structure (MANDATORY)

Every admin template MUST use: `@pageContainer` > `@pageColumn` > `@pageHeader`. Do NOT use `@row` / `@columns` / `@pageColumn` together. @row and @columns can be used only inside @pageColumn.

## i18n Format
`#i18n{prefix.key.subkey}` — always reuse existing portal i18n utility keys instead of creating new ones whenever possible. For example: `#i18n{portal.util.labelActions}`, `#i18n{portal.util.labelModify}`, `#i18n{portal.util.labelDelete}`, `#i18n{portal.util.labelBack}`, `#i18n{portal.util.labelValidate}`, `#i18n{portal.util.labelCancel}`.

## Null Safety

Always use `${value!}` (with `!`) to handle null values in Freemarker expressions.

**Model variables `errors`, `infos`, `warnings`** are NOT pre-initialized — they only exist after `addError()`/`addInfo()`/`addWarning()` is called. Always use null-safe access:
- `<#if (errors!)?size gt 0>` NOT `<#if errors?size gt 0>`
- `<#list (errors![]) as error>` NOT `<#list errors as error>`
- Same for `infos` and `warnings`

# Examples :

## List Page Pattern

```html
<@pageContainer>
    <@pageColumn>
        <@pageHeader title='#i18n{myplugin.manage_tasks.pageTitle}'>
            <@aButton href='jsp/admin/plugins/myplugin/ManageTasks.jsp?view=createTask' buttonIcon='plus' title='#i18n{myplugin.manage_tasks.buttonAdd}' color='primary' />
        </@pageHeader>
        <#if task_list?size gt 0>
            <@table>
                <tr>
                    <th>#i18n{myplugin.model.entity.task.attribute.title}</th>
                    <th>#i18n{portal.util.labelActions}</th>
                </tr>
                <#list task_list as task>
                <tr>
                    <td>${task.title!}</td>
                    <td>
                        <@aButton href='jsp/admin/plugins/myplugin/ManageTasks.jsp?view=modifyTask&id=${task.idTask}' buttonIcon='edit' title='#i18n{portal.util.labelModify}' />
                        <@aButton href='jsp/admin/plugins/myplugin/ManageTasks.jsp?action=confirmRemoveTask&id=${task.idTask}' buttonIcon='trash' color='danger' title='#i18n{portal.util.labelDelete}' />
                    </td>
                </tr>
                </#list>
            </@table>
        <#else>
            <@alert color='info'>#i18n{myplugin.manage_tasks.noData}</@alert>
        </#if>
    </@pageColumn>
</@pageContainer>
```

## Form Page Pattern

```html
<@pageContainer>
    <@pageColumn>
        <@pageHeader title='#i18n{myplugin.create_task.pageTitle}'>
            <@aButton href='jsp/admin/plugins/myplugin/ManageTasks.jsp?view=manageTasks' buttonIcon='arrow-left' title='#i18n{portal.util.labelBack}' />
        </@pageHeader>
        <@tform method='post' name='create_task' action='jsp/admin/plugins/myplugin/ManageTasks.jsp' boxed=true>
            <@input type='hidden' name='action' value='createTask' />
            <@formGroup labelFor='title' labelKey='#i18n{myplugin.model.entity.task.attribute.title}' mandatory=true rows=2>
                <@input type='text' name='title' id='title' value='${task.title!}' />
            </@formGroup>
            <@formGroup labelFor='description' labelKey='#i18n{myplugin.model.entity.task.attribute.description}' rows=2>
                <@input type='textarea' name='description' id='description'>${task.description!}</@input>
            </@formGroup>
            <@formGroup labelFor='completed' labelKey='#i18n{myplugin.model.entity.task.attribute.completed}' rows=2>
                <@checkBox orientation='switch' labelKey='#i18n{myplugin.model.entity.task.attribute.completed}' name='completed' id='completed' value='true' checked=task.completed!false />
            </@formGroup>
            <@formGroup rows=2>
                <@button type='submit' buttonIcon='check' title='#i18n{portal.util.labelValidate}' color='primary' />
                <@aButton href='jsp/admin/plugins/myplugin/ManageTasks.jsp?view=manageTasks' buttonIcon='times' title='#i18n{portal.util.labelCancel}' />
            </@formGroup>
        </@tform>
    </@pageColumn>
</@pageContainer>
```
## JavaScript — Vanilla Only, No jQuery

- **NEVER** use jQuery (`$`, `jQuery`, `$.ajax`, `.click()`, `.on()`, etc.)
- Use native DOM APIs: `document.querySelector`, `addEventListener`, `fetch`, `classList`, `dataset`
- Use ES6+: `const`/`let`, arrow functions, template literals, destructuring, `async`/`await`

## Third-Party Libraries — No CDN

- **NEVER** use CDN links. All third-party JS/CSS must be downloaded and placed locally.
- JS files go in `webapp/js/{pluginName}/`
- CSS files go in `webapp/css/{pluginName}/`