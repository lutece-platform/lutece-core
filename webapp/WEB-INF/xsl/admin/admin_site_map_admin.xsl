<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    
    <xsl:param name="site-path" select="site-path" />
    <xsl:variable name="current-page-id">
        <xsl:value-of select="current-page-id" />
    </xsl:variable>
    
    
    <xsl:template match="page[page-level=0]">
        <script type="text/javascript">
            window.onfocus=function(){
            initializeMenu('menu1', 'actuator1');
            <xsl:value-of select="css-id" />
            openMenu("actuator<xsl:value-of select="current-page-id" />");
            }
            
            function openMenu(actuator) {
            
            var act = document.getElementById(actuator);
            
            var nIdMenu = act.parentNode.parentNode.getAttribute('id');
            if ( nIdMenu != '') {
            var menu = document.getElementById(nIdMenu);
            
            if (menu == null ) return;
            
            menu.parentNode.style.backgroundImage ="url(images/admin/skin/minus.gif)";
            menu.style.display = "block";
            
            var nIdAct = 'actuator' + nIdMenu.substr( 4, nIdMenu.length - 4  );
            openMenu( nIdAct );
            }
            
            return true;
            }
        </script>
        
        <table cellpadding="0" cellspacing="0" width="100%" border="0">
            <tr>
                <td width="100%" align="left">
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
                </td>
            </tr>
        </table>
        
        <ul>
            <li class="tree-menu-menubar">
                <a href="#" id="actuator1" class="tree-menu-actuator">
                    <xsl:value-of select="page-name" />
                </a>
                <xsl:if test="not(string(page-role)='none')"><br />
                    <span class="colored">
                        <xsl:text disable-output-escaping="yes">
                            #i18n{portal.site.admin_page.tabAdminMapRoleReserved}
                        </xsl:text>
                    </span>
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
                
                <ul id="menu{page-id}" class="tree-menu-menu">
                    <xsl:apply-templates select="child-pages-list" />
                </ul>
            </li>
        </ul>
    </xsl:template>
    
    <xsl:template match="page[page-level>0]" >
        <li>
            <a href="#" id="actuator{page-id}" class="tree-menu-actuator"> </a>
            <a href="{$site-path}?page_id={page-id}">
                <xsl:value-of select="page-name" />
            </a>
            <xsl:if test="not(string(page-role)='none')"><br />
                <span class="colored">
                    <xsl:text disable-output-escaping="yes">
                        #i18n{portal.site.admin_page.tabAdminMapRoleReserved}
                    </xsl:text>
                </span> 
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
                    <ul id="menu{page-id}" class="tree-menu-menu">
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
