<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    
    <xsl:param name="site-path" select="site-path" />
    <xsl:variable name="current-page-id">
        <xsl:value-of select="current-page-id" />
    </xsl:variable>
    
    
    <xsl:template match="page[page-level=0]">
   



<div id="content-admin-site-page" class="admin-site-page">
    <p>
        <a href="jsp/admin/site/AdminSite.jsp?page_id={current-page-id}"><img src="images/admin/skin/back_adminsite.png"  width="36" height="36" hspace="12" alt="Administration du site" title="Administratrion du site" /></a>
        <a href="jsp/admin/site/AdminSite.jsp?page_id={current-page-id}&amp;param_block=property"><img src="images/admin/skin/property.png"  width="36" height="36" hspace="12" alt="Propriétés de la page" title="Propriétés de la page" /></a>
        <a href="jsp/admin/site/AdminSite.jsp?page_id={current-page-id}&amp;param_block=image"><img src="images/admin/skin/image.png" width="36" height="36" hspace="12" alt="Image de la page" title="Image de la page" /></a>
        <a href="jsp/admin/site/AdminSite.jsp?page_id={current-page-id}&amp;param_block=portlet"><img src="images/admin/skin/portlet.png" width="36" height="36" hspace="12" alt="Ajouter une rubrique" title="Ajouter une rubrique" /></a>
        <a href="jsp/admin/site/AdminSite.jsp?page_id={current-page-id}&amp;param_block=childpage"><img src="images/admin/skin/childpage.png"  width="36" height="36" hspace="12" alt="Ajouter une page fille" title="Ajouter une page fille" /></a>
    </p>
</div>



        
        <h3><img src="js/jquery/plugins/treeview/images/folder.gif" style="margin-bottom:-10px;"/></h3>
        <ul id="tree" class="treeview">
            <li>
                <span>
                  <a href="{$site-path}?page_id={page-id}">
                    <xsl:value-of select="page-name" />
                  </a>
                </span>
                <xsl:if test="not(string(page-role)='none')"><br />
                    <strong>
                        <xsl:text disable-output-escaping="yes">
                            #i18n{portal.site.admin_page.tabAdminMapRoleReserved}
                        </xsl:text>
                    </strong>
                    <xsl:value-of select="page-role" />
                </xsl:if>
                
                <xsl:if test="not(string(page-description)='')">
                    <br />
                    <strong>
                        <xsl:text disable-output-escaping="yes">
                            #i18n{portal.site.admin_page.tabAdminMapDescription}
                        </xsl:text>
                    </strong>
                    <xsl:apply-templates select="page-description" />
                </xsl:if>
                <ul>
                   <xsl:apply-templates select="child-pages-list" />
                </ul>
            </li>
        </ul>
    </xsl:template>
    
    <xsl:template match="page[page-level>0]" >
        <li>
        <span>
            <a href="{$site-path}?page_id={page-id}">
                <xsl:value-of select="page-name" />
            </a>
          </span>  
            <xsl:if test="not(string(page-role)='none')"><br />
                <strong>
                    <xsl:text disable-output-escaping="yes">
                        #i18n{portal.site.admin_page.tabAdminMapRoleReserved}
                    </xsl:text>
                </strong> 
                <xsl:value-of select="page-role" />
            </xsl:if>
            
            <xsl:if test="not(string(page-description)='')">
                <br />
                <strong>
                    <xsl:text disable-output-escaping="yes">
                        #i18n{portal.site.admin_page.tabAdminMapDescription}
                    </xsl:text>
                </strong>
                <xsl:apply-templates select="page-description" />
            </xsl:if>
            
            <xsl:choose>
                <xsl:when test="count(child-pages-list/*)>0">
                    <ul>
                        <xsl:apply-templates select="child-pages-list" />
                    </ul>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:apply-templates select="child-pages-list" />
                </xsl:otherwise>
            </xsl:choose>
        </li>
    </xsl:template>
    
    <xsl:template match="page-description">
        <xsl:value-of select="." />
    </xsl:template>
    
    <xsl:template match="child-pages-list">
        <xsl:apply-templates select="page" />
    </xsl:template>
    
</xsl:stylesheet>
