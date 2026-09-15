---
description: "Lutece 8 JspBean/XPage constraints: CDI annotations, CRUD lifecycle, security tokens, pagination"
paths:
  - "**/web/**/*.java"
---

# Web Beans — Lutece 8

## JspBean (back-office admin)

- MUST have CDI scope + `@Named` + `@Controller` + extend `MVCAdminJspBean`. Without a scope annotation and `@Named`, the page will be BLANK.
- **Scope rule**: `@SessionScoped` if the bean stores per-user state as instance fields (working objects like `_task`, filters, multi-step context). This is the typical case for CRUD beans. Note: with `@Inject @Pager IPager` pagination state is managed automatically — no manual `_strCurrentPageIndex`/`_nItemsPerPage` fields needed. `@RequestScoped` if the bean is stateless (no session instance fields).
- Right check: `init(request, RIGHT_MANAGE_ENTITY)` in constructor or first call

## XPage (front-office site)

- `@Named("plugin.xpage.name")` + `@Controller` + extend `MVCApplication`
- Declared in plugin.xml `<applications>`

## Models — MANDATORY

- **NEVER use `getModel()`** — it is deprecated.
- **NEVER use `new HashMap<>()`** to build the model manually.
- **ALWAYS inject `Models`** via `@Inject` and use `_models.put(key, value)`. The framework reads from `_models` automatically when `getPage()` is called. Note: `_models.asMap()` returns an **unmodifiable** map — all `put()` calls must go through the `Models` object, never directly on the returned map.

## CRUD Lifecycle — Strict Naming

| Method | Role | Returns |
|---|---|---|
| `getManageEntities()` | List view | HTML template |
| `getCreateEntity()` | Create form | HTML template |
| `doCreateEntity()` | Create action | Redirect URL |
| `getModifyEntity()` | Edit form | HTML template |
| `doModifyEntity()` | Edit action | Redirect URL |
| `getConfirmRemoveEntity()` | Confirm dialog | AdminMessage URL |
| `doRemoveEntity()` | Delete action | Redirect URL |

## Every `do*` Method — Mandatory Order

1. CSRF token validation (see below)
2. Populate: `populate(entity, request)`
3. Validate: `validate(entity)`
4. Business logic: `EntityHome.create(entity)`
5. Redirect: `redirectView(request, VIEW_MANAGE)`

## Security Token — JspBean vs XPage

**JspBeans** (extend `MVCAdminJspBean`): use the inherited `getSecurityTokenService()` method — no `@Inject` needed, the base class (`AdminFeaturesPageJspBean`) already injects `ISecurityTokenService`.

```java
// In get* form:
_models.put(SecurityTokenService.MARK_TOKEN,
    getSecurityTokenService().getToken(request, ACTION));
// In do* action:
getSecurityTokenService().validate(request, ACTION);
```

**XPages** (extend `MVCApplication`): no inherited method — inject directly:

```java
@Inject
private SecurityTokenService _securityTokenService;

// In get* form:
_models.put(SecurityTokenService.MARK_TOKEN,
    _securityTokenService.getToken(request, ACTION));
// In do* action:
_securityTokenService.validate(request, ACTION);
```

## Pagination (list views)

Use `@Inject @Pager IPager` for CDI-managed pagination (recommended). The `@Pager` qualifier configures paginator name, list bookmark, default items per page, and base URL. Call `_pager.withListItem(list).populateModels(request, _models, getLocale())`. For AJAX tables, add a `@ResponseBody` endpoint and use the `paginationAjax` macro. See `/lutece-patterns` skill §5 for full details.
