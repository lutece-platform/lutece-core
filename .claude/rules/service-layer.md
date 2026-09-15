---
description: "Lutece 8 service layer constraints: CDI scopes, injection, events, configuration"
paths:
  - "**/service/**/*.java"
---

# Service Layer — Lutece 8

## CDI Scopes

- Singleton service: `@ApplicationScoped`
- Per-request service: `@RequestScoped`
- NEVER use static `getInstance()` in new code — use `@Inject` or `CDI.current().select()`
- `getInstance()` wrapping `CDI.current().select()` is `@Deprecated(since = "8.0", forRemoval = true)` in lutece-core — remove when all callers are internal, keep temporarily with `@Deprecated` only if external callers depend on it

## Injection

- Prefer `@Inject` field injection for services
- Use `CDI.current().select(IMyService.class).get()` only in static contexts (Home classes)
- Multiple implementations: `CDI.current().select(IProvider.class).stream().filter(...)`

## CDI Eligibility — Decision Matrix

When migrating a class away from `getInstance()`, pick the pattern by class shape, not by habit:

| Class shape | Make it CDI bean? | Access pattern |
|---|---|---|
| Stateless service, no-arg constructor | ✅ `@ApplicationScoped` | `@Inject` (callers must be CDI) or `CDI.current().select(X.class).get()` |
| Stateful per-request object (constructor args, e.g. `executionId`, `nodeId`) | ❌ Impossible (no fixed identity) | `new XxxObject(...)` + dependencies via `private final X _x = CDI.current().select(X.class).get();` cached in field |
| Static utility / facade (all methods `static`) | ❌ No instance | `private static final X _x = CDI.current().select(X.class).get();` (matches Home pattern) |
| Daemon (`extends Daemon`) | ❌ Instantiated by Lutece daemon framework | Inline `CDI.current().select(X.class).get()` or cached field |
| Pipeline node / `@PipelineNodeType` | ❌ Instantiated via reflection by engine | Cached instance field with `CDI.current().select(...)` |
| Reflection / SPI loaded class | ❌ No CDI hook | Cached instance field |
| Tool / object with dynamic per-call args (`new GrepTool(datasets)`) | ❌ State varies per instance | Inline or cached `CDI.current().select(...)` |

**Rule of thumb:** if the class CAN be a CDI bean, it MUST be one. Programmatic lookup (`CDI.current()`) is reserved for classes that genuinely cannot be CDI-managed.

**Caching the lookup:** when calling `CDI.current().select(X.class).get()` more than once per instance, cache it in a `private final` (or `private static final`) field — repeated lookups walk the BeanManager. The cached reference is safe for `@ApplicationScoped` dependencies (singleton anyway).

## CDI Qualifiers (custom)

For custom `@Qualifier` annotations (e.g. `@BlockingIO`, `@Orchestration`), provide a nested `Literal` for programmatic lookup. The canonical CDI pattern triggers a misleading Eclipse warning ("annotation type should not be used as a superinterface for Literal") — it's the spec-mandated form. Suppress it:

```java
@Qualifier
@Retention( RetentionPolicy.RUNTIME )
@Target( { ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.TYPE } )
public @interface MyQualifier
{
    @SuppressWarnings( "all" )
    final class Literal extends AnnotationLiteral<MyQualifier> implements MyQualifier
    {
        private static final long serialVersionUID = 1L;
        public static final Literal INSTANCE = new Literal( );
    }
}
```

Usage: `CDI.current().select( SomeService.class, MyQualifier.Literal.INSTANCE ).get();`

## Events (CDI)

- Fire synchronous: `CDI.current().getBeanManager().getEvent().fire(new MyEvent(...))` — triggers `@Observes` observers
- Fire asynchronous: `CDI.current().getBeanManager().getEvent().fireAsync(new MyEvent(...))` — triggers `@ObservesAsync` observers
- With qualifier: `.select(new TypeQualifier(EventAction.CREATE)).fire(event)` or `.fireAsync(event)`
- Observe sync: `public void onEvent(@Observes MyEvent event) { }`
- Observe async: `public void onEvent(@ObservesAsync MyEvent event) { }`
- NEVER use deprecated `ResourceEventManager` or Spring event patterns

## Configuration

- Static properties: `AppPropertiesService.getProperty("key", "default")`
- Runtime overrides: `DatastoreService.getInstanceDataValue("key", "default")`
- Injected config: `@Inject @ConfigProperty(name = "key", defaultValue = "x")`

## Cache Integration

- Extend `AbstractCacheableService<K, V>` for cacheable services
- Invalidate on mutations: `_cache.remove(key)` or `_cache.removeAll()`
- Fire cache events via CDI on reset
