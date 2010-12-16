<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    
    <xsl:param name="site-path" select="site-path" />
    <xsl:variable name="current-page-id">
        <xsl:value-of select="current-page-id" />
    </xsl:variable>
    
    
    <xsl:template match="page[page-level=0]"> 
        <img src="images/admin/skin/folder_page.png" />
        <div id="tree" class="jstree-default">
            <a href="{$site-path}?page_id={page-id}" title="{page-description}" >
                    <xsl:value-of select="page-name" />
					<xsl:if test="not(string(page-role)='none')"> 
					<strong><xsl:text disable-output-escaping="yes">- #i18n{portal.site.admin_page.tabAdminMapRoleReserved}</xsl:text>
					<xsl:value-of select="page-role" /></strong>
				</xsl:if>            
			</a>
       <ul>
            <xsl:apply-templates select="child-pages-list" />
       </ul>
	</div>
</xsl:template>
    
<xsl:template match="page[page-level>0]" >
<xsl:variable name="index">
    <xsl:value-of select="page-id" />
</xsl:variable>
<xsl:variable name="description">
    <xsl:value-of select="page-description" />
</xsl:variable>

<li id="node-{$index}">
<a href="{$site-path}?page_id={page-id}" title="{$description}">
	<img hspace="5" alt="" src="images/admin/skin/page_world.png" />
    <xsl:value-of select="page-name" />
    <xsl:if test="not(string(page-role)='none')">
    <br />
    <strong>
      <xsl:text disable-output-escaping="yes">#i18n{portal.site.admin_page.tabAdminMapRoleReserved}</xsl:text><xsl:value-of select="page-role" />
    </strong>
     </xsl:if>
     </a>
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
