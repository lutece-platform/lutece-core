/*
 * Copyright (c) 2002-2014, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.portal.web.documentation;

import fr.paris.lutece.portal.business.right.FeatureGroup;
import fr.paris.lutece.portal.business.right.FeatureGroupHome;
import fr.paris.lutece.portal.business.right.Right;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.html.XmlTransformerService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.io.File;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import javax.xml.transform.stream.StreamSource;


/**
 *
 * Classe for display the admin features documentation
 *
 */
public class AdminDocumentationJspBean
{
    //xsl
    private static final String XSL_PATH = "admin_documentation.xsl";

    //xsl paramaters
    private static final String PARAMS_LOCAL = "locale";
    private static final String PARAMS_DEFAULT_LOCAL = "default_locale";

    //parameters
    private static final String PARAMETER_FEATURE_DOC = "doc";

    //jsp
    private static final String JSP_CLOSE = "javascript:window.close()";

    //templates
    private static final String TEMPLATE_ADMIN_SUMMARY_DOCUMENTATION = "admin/documentation/admin_summary_documentation.html";

    //bookmark
    private static final String BOOKMARK_FEATURE_GROUP_LIST = "feature_group_list";
    private static final String BOOKMARK_HELP_ICON = "help_icon";

    //images
    private static final String IMAGE_HELP_PATH = "images/admin/skin/features/admin_help.png";

    //properties
    private static final String PROPERTY_XSL_BASE_PATH = "lutece.documentation.xsl.path";
    private static final String ADMIN_DOCUMENTATION_XSL_UNIQUE_PREFIX = "adminDocumentation-";

    //messages
    private static final String MESSAGE_ERROR = "portal.features.documentation.message.error";

    //utils
    private static final String LOCAL_DEFAULT = "en";
    private static final String XML_BASE_PATH = "/doc/xml/";
    private static final String XML_USER_PATH = "/xdoc/user/";
    private static final String FEATURES_GROUP_SYSTEM = "SYSTEM";

    /**
    * Returns the view of features documentation
    *
    * @param request The request
     * @return The HTML documentation
     * @throws AccessDeniedException If the access is refused to the user
    */
    public String getDocumentation( HttpServletRequest request )
        throws AccessDeniedException
    {
        String strFeature = request.getParameter( PARAMETER_FEATURE_DOC );

        AdminUser user = AdminUserService.getAdminUser( request );
        Locale locale = user.getLocale(  );

        //get the xsl file
        String strXslPath = AppPathService.getPath( PROPERTY_XSL_BASE_PATH, XSL_PATH );
        File fileXsl = new File( strXslPath );
        StreamSource sourceStyleSheet = new StreamSource( fileXsl );

        //get the xml documentation file
        String strXmlPath;
        StreamSource sourceXml;
        String strLocal = locale.toString(  );

        if ( ( locale == null ) || strLocal.equals( LOCAL_DEFAULT ) )
        {
            strXmlPath = AppPathService.getWebAppPath(  ) + XML_BASE_PATH + XML_USER_PATH + strFeature + ".xml";
        }
        else
        {
            strXmlPath = AppPathService.getWebAppPath(  ) + XML_BASE_PATH + locale.toString(  ) + XML_USER_PATH +
                strFeature + ".xml";
        }

        sourceXml = new StreamSource( new File( strXmlPath ) );

        String strHtmlDoc = null;

        Map<String, String> params = new HashMap<String, String>(  );
        params.put( PARAMS_LOCAL, locale.toString(  ) );
        params.put( PARAMS_DEFAULT_LOCAL, LOCAL_DEFAULT );

        XmlTransformerService xmlTransformerService = new XmlTransformerService(  );
        String strUniqueId = ADMIN_DOCUMENTATION_XSL_UNIQUE_PREFIX + strXmlPath;

        try
        {
            strHtmlDoc = xmlTransformerService.transformBySourceWithXslCache( sourceXml, sourceStyleSheet, strUniqueId,
                    params, null );
        }
        catch ( Exception e )
        {
            AppLogService.error( "Can't parse XML: " + e.getMessage(  ), e );

            return null;
        }

        return strHtmlDoc;
    }

    /**
     * Returns an error message when an error occured
     *
     * @param request The request
     * @return The URL of message
     */
    public String doAdminMessage( HttpServletRequest request )
    {
        return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR, JSP_CLOSE, AdminMessage.TYPE_ERROR );
    }

    /**
     * Returns the view of summary documentation
     *
     * @param request The request
     * @return The HTML documentation
     */
    public String getSummaryDocumentation( HttpServletRequest request )
    {
        AdminUser user = AdminUserService.getAdminUser( request );

        List<FeatureGroup> listFeatureGroups = getFeatureGroupsList( user );
        Map<String, Object> model = new HashMap<String, Object>(  );

        model.put( BOOKMARK_FEATURE_GROUP_LIST, listFeatureGroups );
        model.put( BOOKMARK_HELP_ICON, IMAGE_HELP_PATH );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_ADMIN_SUMMARY_DOCUMENTATION,
                user.getLocale(  ), model );

        return template.getHtml(  );
    }

    /**
     * Returns an array that contains all feature groups corresponding to the user
     *
     * @param user The user
     * @return A list of FeatureGroup objects
     */
    private List<FeatureGroup> getFeatureGroupsList( AdminUser user )
    {
        // structure that will be returned
        ArrayList<FeatureGroup> aOutFeatureGroupList = new ArrayList<FeatureGroup>(  );

        // get the list of user's features
        Map<String, Right> featuresMap = user.getRights(  );
        Collection<Right> features = featuresMap.values(  );

        // for each group, load the features
        for ( FeatureGroup featureGroup : FeatureGroupHome.getFeatureGroupsList(  ) )
        {
            ArrayList<Right> aLeftFeatures = new ArrayList<Right>(  );

            for ( Right right : features )
            {
                right.setLocale( user.getLocale(  ) );

                String strFeatureGroup = right.getFeatureGroup(  );
                String strUrlDocumentation = right.getDocumentationUrl(  );

                if ( featureGroup.getId(  ).equalsIgnoreCase( strFeatureGroup ) && ( strUrlDocumentation != null ) &&
                        !( strUrlDocumentation.equals( "" ) ) )
                {
                    featureGroup.addFeature( right );
                }
                else if ( ( strUrlDocumentation != null ) && !( strUrlDocumentation.equals( "" ) ) )
                {
                    aLeftFeatures.add( right );
                }
            }

            if ( !featureGroup.isEmpty(  ) )
            {
                featureGroup.setLocale( user.getLocale(  ) );
                aOutFeatureGroupList.add( featureGroup );
            }

            features = aLeftFeatures;
        }

        FeatureGroup featureGroupSystem = FeatureGroupHome.findByPrimaryKey( FEATURES_GROUP_SYSTEM );

        if ( ( featureGroupSystem != null ) && !features.isEmpty(  ) )
        {
            boolean bSystemFeaturesGroupExist = false;

            //Check if the features group system exist in list features group
            for ( FeatureGroup featureGroup : aOutFeatureGroupList )
            {
                //if exist
                if ( featureGroup.getId(  ).equalsIgnoreCase( featureGroupSystem.getId(  ) ) )
                {
                    // add the features with no group to the list in features group SYSTEM
                    for ( Right right : features )
                    {
                        featureGroup.addFeature( right );
                    }

                    bSystemFeaturesGroupExist = true;

                    break;
                }
            }

            // if not, add features group SYSTEM to the list with the feautres no group
            if ( !bSystemFeaturesGroupExist )
            {
                for ( Right right : features )
                {
                    featureGroupSystem.addFeature( right );
                }

                featureGroupSystem.setLocale( user.getLocale(  ) );
                aOutFeatureGroupList.add( featureGroupSystem );
            }
        }
        else if ( ( aOutFeatureGroupList.size(  ) > 0 ) && !features.isEmpty(  ) )
        {
            FeatureGroup lastFeatureGroup = aOutFeatureGroupList.get( aOutFeatureGroupList.size(  ) - 1 );

            for ( Right right : features )
            {
                lastFeatureGroup.addFeature( right );
            }
        }

        return aOutFeatureGroupList;
    }
}
