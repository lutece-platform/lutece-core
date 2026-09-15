---
description: "Lutece 8 JSP constraints: admin feature JSP boilerplate, bean naming, errorPage"
paths:
  - "**/*.jsp"
---

# JSP Admin Pages — Lutece 8

## Boilerplate (MANDATORY)

Each admin-feature MUST have a JSP in `webapp/jsp/admin/plugins/{name}/`:

```jsp
<%@ page errorPage="../../ErrorPage.jsp" %>

${ pageContext.setAttribute( 'strContent', entityJspBean.processController( pageContext.request , pageContext.response ) ) }

<jsp:include page="../../AdminHeader.jsp" />

${ pageContext.getAttribute( 'strContent' ) }

<%@ include file="../../AdminFooter.jsp" %>
```

## Bean Naming

The bean name is `{entity}JspBean` in **camelCase**: `taskJspBean`, `projectJspBean`, `formResponseJspBean`.

This name MUST match the `@Named` value (or default CDI name) of the corresponding JSPBean class (`@SessionScoped` if stateful, `@RequestScoped` if stateless).