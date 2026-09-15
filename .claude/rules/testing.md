---
description: "Lutece 8 build and test commands, JUnit 5 conventions, test base classes"
paths:
  - "**/test/**/*.java"
  - "pom.xml"
---

# Build & Test — Lutece 8

## Build Commands

| Goal | Command |
|------|---------|
| Compile only (skip tests) | `mvn clean install -Dmaven.test.skip=true` |
| Full build with tests | `mvn clean lutece:exploded antrun:run -Dlutece-test-hsql test -q` |

**NEVER use `mvn test` alone** — Lutece requires `lutece:exploded` (webapp explosion) + `antrun:run` (HSQL DB setup via `-Dlutece-test-hsql`) before tests can run.

## JUnit 5

- `@Test` from `org.junit.jupiter.api.Test` (NOT `org.junit.Test`)
- Assertions from `org.junit.jupiter.api.Assertions` — message parameter is LAST: `assertEquals(expected, actual, "msg")`
- `@BeforeEach` / `@AfterEach` (NOT `@Before` / `@After`)
- `@BeforeAll` / `@AfterAll` (NOT `@BeforeClass` / `@AfterClass`)
- `@Disabled` (NOT `@Ignore`)

## POM Dependency

`library-lutece-unit-testing` MUST be present in `pom.xml` — it provides `LuteceTestCase`, mock classes, and Weld-testing support:

```xml
<!-- Required — test base classes, mocks, Weld-testing -->
<dependency>
    <groupId>fr.paris.lutece.plugins</groupId>
    <artifactId>library-lutece-unit-testing</artifactId>
    <type>jar</type>
    <scope>test</scope>
</dependency>

<!-- Optional — add if tests use bean validation, Jakarta EL, or JAXB -->
<dependency>
    <groupId>org.hibernate.validator</groupId>
    <artifactId>hibernate-validator</artifactId>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.glassfish</groupId>
    <artifactId>jakarta.el</artifactId>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.glassfish.jaxb</groupId>
    <artifactId>jaxb-runtime</artifactId>
    <scope>test</scope>
</dependency>
```

Versions are managed by the Lutece global POM — do NOT specify `<version>` for these.

## Test Base Classes

- Business layer tests: extend `LuteceTestCase` (from `library-lutece-unit-testing`)
- Web layer tests: extend `LuteceTestCase`, use `MockHttpServletRequest` from `fr.paris.lutece.test.mocks` (NOT `org.springframework.mock`)

## Mocking

- Use `MockHttpServletRequest` / `MockHttpServletResponse` from `fr.paris.lutece.test.mocks`
- NEVER use `fr.paris.lutece.portal.service.spring.MokeHttpServletRequest` (v7 legacy, removed in v8)
- For CDI beans in tests, use `@Inject` with Weld-testing or `CDI.current().select()`
