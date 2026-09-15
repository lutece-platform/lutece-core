---
description: "Lutece 8 global Java conventions: Jakarta EE, CDI, forbidden patterns"
paths:
  - "**/*.java"
---

# Java Conventions — Lutece 8

## Jakarta EE (NOT javax)

- ALL imports must use `jakarta.*` — NEVER `javax.servlet`, `javax.inject`, `javax.annotation`, `javax.persistence`
- `javax.xml.*`, `javax.crypto.*`, `javax.sql.*` are fine (part of JDK, not Jakarta)

## CDI (NOT Spring)

- NEVER use `SpringContextService.getBean()` — use `@Inject` or `CDI.current().select()`
- NEVER use Spring annotations (`@Autowired`, `@Component`, `@Service`, `@Repository`, `@Configuration`)
- NEVER import from `org.springframework.*`
- Use `@ApplicationScoped`, `@RequestScoped`, `@SessionScoped` from `jakarta.enterprise.context`
- Use `@Inject` from `jakarta.inject`
- Use `@Named` from `jakarta.inject` for beans referenced by name (JSP EL, producers)

## Forbidden Libraries

- `net.sf.ehcache` — replaced by JCache (`javax.cache`)
- `org.quartz-scheduler` — replaced by `ManagedScheduledExecutorService`
- `net.sf.json-lib` — replaced by Jackson (`com.fasterxml.jackson`)
- `org.apache.commons.fileupload` — replaced by `fr.paris.lutece.portal.web.upload.MultipartItem`
- `org.glassfish.jersey` — use standard JAX-RS (`jakarta.ws.rs`)

## DAOUtil

- ALWAYS use try-with-resources: `try ( DAOUtil daoUtil = new DAOUtil( SQL, plugin ) )`
- NEVER call `daoUtil.free()` manually

## Logging

- Use SLF4J: `Logger` + `LoggerFactory`
- Use parameterized messages: `logger.debug( "Found {} items", count )` — NOT string concatenation
- `isDebugEnabled()` guard is unnecessary with parameterized logging

**NON NEGOCIABLES RULES**
- javadocs on each methods.
- no inline comments in methods.