---
description: "Lutece 8 DAO/Home constraints: DAOUtil lifecycle, SQL constants, Home facade, CDI lookup"
paths:
  - "**/business/**/*.java"
---

# DAO & Home Patterns — Lutece 8

## DAO Class

- `@ApplicationScoped` + `@Named("plugin.entityDAO")` + implements `IEntityDAO`
- SQL constants: `SQL_QUERY_SELECT`, `SQL_QUERY_INSERT`, `SQL_QUERY_UPDATE`, `SQL_QUERY_DELETE`, `SQL_QUERY_SELECTALL`
- DAOUtil ALWAYS with try-with-resources: `try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY, plugin ) )`
- Parameters are **1-indexed**, use `nIndex++` for sequential access
- `executeUpdate()` for INSERT/UPDATE/DELETE, `executeQuery()` for SELECT
- `daoUtil.next()` with `if` for single row, `while` for collections

## Home — Static Facade

- `private static IEntityDAO _dao = CDI.current().select( IEntityDAO.class ).get();`
- `private static Plugin _plugin = PluginService.getPlugin( "pluginname" );`
- Private constructor, all methods static
- Methods: `create`, `update`, `remove`, `findByPrimaryKey`, `findAll`, `findReferenceList`
- NEVER declare a `BEAN_xxx_DAO` / `DAO_BEAN_NAME` string constant — the typesafe `CDI.current().select( IEntityDAO.class )` lookup makes it dead code. The DAO's `@Named("plugin.entityDAO")` is the only place the bean name lives.

## Interface

Every DAO MUST have an `IEntityDAO` interface with: `insert`, `load`, `store`, `delete`, `selectAll`, `selectReferenceList`. All methods take `Plugin plugin` as last parameter.

## Return Types

- `List<Entity>` for standard lists
- `ReferenceList` for dropdowns (`<select>`)
- `Optional<Entity>` for optional single results

## Reference

For full code examples and DAOUtil parameter types: `/lutece-dao` skill.
