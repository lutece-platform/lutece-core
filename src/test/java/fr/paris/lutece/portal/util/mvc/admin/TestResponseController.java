/*
 * Copyright (c) 2002-2022, City of Paris
 * All rights reserved.
 * License 1.0
 */
package fr.paris.lutece.portal.util.mvc.admin;

import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.ResponseBody;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;

/**
 * A controller used by {@link MvcResponseWrittenTest} to exercise the outcomes of
 * {@link MVCAdminJspBean#processController}: a plain view, a view returning {@code null}, a {@code @ResponseBody}
 * view, a redirect and a download.
 */
@RequestScoped
@Named
@Controller( controllerJsp = TestResponseController.CONTROLLER_JSP, controllerPath = TestResponseController.CONTROLLER_PATH, right = TestResponseController.RIGHT, securityTokenEnabled = false )
public class TestResponseController extends MVCAdminJspBean
{
    private static final long serialVersionUID = 1L;

    public static final String CONTROLLER_JSP = "TestMvcResponse.jsp";
    public static final String CONTROLLER_PATH = "jsp/admin/";
    /** An admin right declared by the core init scripts, so {@code init} finds it. */
    public static final String RIGHT = "CORE_PORTLET_TEMPLATE_MANAGEMENT";

    public static final String VIEW_NORMAL = "normal";
    public static final String VIEW_NULL = "nullview";
    public static final String VIEW_JSON = "json";
    public static final String VIEW_JSON_ACCENTS = "jsonaccents";
    public static final String VIEW_REDIRECT = "redir";
    public static final String VIEW_DOWNLOAD = "dl";

    public static final String PAGE_CONTENT = "PAGE-CONTENT";
    public static final String JSON_BODY = "JSON-BODY";
    /** A body with accents: each one takes two bytes in UTF-8. */
    public static final String JSON_ACCENTS_BODY = "{\"name\":\"contenu \u00e9ditorial \u00e9t\u00e9\"}";
    public static final String DOWNLOAD_DATA = "DOWNLOAD-DATA";

    @View( value = VIEW_NORMAL, defaultView = true )
    public String normal( )
    {
        return PAGE_CONTENT;
    }

    @View( VIEW_NULL )
    public String nullView( )
    {
        return null;
    }

    @View( VIEW_JSON )
    @ResponseBody
    public String json( )
    {
        return JSON_BODY;
    }

    @View( VIEW_JSON_ACCENTS )
    @ResponseBody
    public String jsonAccents( )
    {
        return JSON_ACCENTS_BODY;
    }

    @View( VIEW_REDIRECT )
    public String redir( HttpServletRequest request )
    {
        return redirect( request, "View.jsp?view=" + VIEW_NORMAL );
    }

    @View( VIEW_DOWNLOAD )
    public String downloadView( )
    {
        download( DOWNLOAD_DATA, "file.txt", "text/plain" );
        return null;
    }
}
