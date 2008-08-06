<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    
    <xsl:param name="site-path" select="site-path" />
    <xsl:variable name="current-page-id">
        <xsl:value-of select="current-page-id" />
    </xsl:variable>
    
    
    <xsl:template match="page[page-level=0]">
   
      <div id="admin-site-menu">
                        <ul>
                            <li>
                                <a href="{$site-path}?page_id={current-page-id}">#i18n{portal.site.admin_page.tabPageManagement} <xsl:value-of select="current-page-id" /></a>
                            </li>
                            <li id="current">
                                <a href="{$site-path}?page_id={current-page-id}">#i18n{portal.site.admin_page.tabAdminMapSite}</a>
                            </li>
                        </ul>
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
