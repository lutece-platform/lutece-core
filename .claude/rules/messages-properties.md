---
description: "Lutece 8 i18n constraints: no prefix in .properties, prefix in Java/templates, key naming"
---

# i18n — messages.properties Rules

## The Golden Rule

**The prefix is NEVER in messages.properties, ALWAYS in Java/templates.**

```properties
# CORRECT — in myplugin_messages.properties
adminFeature.manageEntities.name=Manage Entities
manage_entity.pageTitle=Manage Entities
message.confirmRemoveEntity=Are you sure?

# WRONG — prefix included
# myplugin.adminFeature.manageEntities.name=Manage Entities
```

## Prefix by Project Type

| Type | Prefix in Java/templates | Properties location |
|------|--------------------------|---------------------|
| **Plugin** | `pluginName.` | `resources/pluginName_messages.properties` |
| **Module** | `module.pluginName.moduleName.` | `resources/moduleName_messages.properties` |
| **Core** | `portal.` | `resources/portal_messages.properties` |

## Key Naming

- `plugin.description` / `plugin.provider` — plugin metadata
- `adminFeature.xxx.name` / `.description` — admin menu
- `manage_xxx.pageTitle` / `.buttonAdd` / `.noData` — list pages
- `create_xxx.pageTitle` / `modify_xxx.pageTitle` — form pages
- `model.entity.xxx.attribute.yyy` — entity field labels
- `message.confirmRemoveXxx` — delete confirmation
- `message.error.*` / `message.success.*` — user messages
- `permission.resourceType.xxx.*` — RBAC labels

## Common Error

| Symptom | Cause | Fix |
|---------|-------|-----|
| i18n key displayed as-is (e.g. `myplugin.label.name`) | Prefix included in `.properties` file | Remove prefix — keep only `label.name=Value` |
