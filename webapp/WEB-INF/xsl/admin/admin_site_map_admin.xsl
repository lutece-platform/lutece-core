<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:param name="site-path" select="site-path" />
    <xsl:variable name="current-page-id" select="current-page-id" />
    <xsl:template match="page[page-level=0]">
        <ul>
            <li class="lutece-tree-node" data-tree-icon="home">
                <xsl:value-of select="page-name" />
                <xsl:if test="not(string(page-role)='none')">
                    <strong>
                        <xsl:text disable-output-escaping="yes">- #i18n{portal.site.admin_page.tabAdminMapRoleReserved}</xsl:text>
                        <xsl:value-of select="page-role" />
                    </strong>
                </xsl:if>
                <a href="{$site-path}?page_id={page-id}" title="{page-description}"><span class="ti ti-link"></span></a>
                <ul>
                    <xsl:apply-templates select="child-pages-list" />
                </ul>
            </li>
        </ul>
    </xsl:template>
    <xsl:template match="page[page-level>0]">
        <xsl:variable name="index" select="page-id" />
        <xsl:variable name="description" select="page-description" />
        <xsl:choose>
            <xsl:when test="count(child-pages-list/*)>0">
            <li id="node-{$index}" class="lutece-tree-node" data-tree-icon="folder">
                <xsl:value-of select="page-name" />
                <xsl:if test="not(string(page-role)='none')">
                    <strong>
                        <xsl:text disable-output-escaping="yes">#i18n{portal.site.admin_page.tabAdminMapRoleReserved}</xsl:text>
                        <xsl:value-of select="page-role" />
                    </strong>
                </xsl:if>
                <a href="{$site-path}?page_id={page-id}" title="{$description}"><span class="ti ti-link"></span></a>
                <ul>
                    <xsl:apply-templates select="child-pages-list" />
                </ul>
                </li>   
            </xsl:when>
            <xsl:otherwise>
            <li id="node-{$index}" class="lutece-tree-item" data-tree-icon="file">
                <xsl:value-of select="page-name" />
                <xsl:if test="not(string(page-role)='none')">
                    <strong>
                        <xsl:text disable-output-escaping="yes">#i18n{portal.site.admin_page.tabAdminMapRoleReserved}</xsl:text>
                        <xsl:value-of select="page-role" />
                    </strong>
                </xsl:if>
                <a href="{$site-path}?page_id={page-id}" title="{$description}"><span class="ti ti-link"></span></a>
                <xsl:apply-templates select="child-pages-list" />
            </li>    
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    <xsl:template match="child-pages-list">
        <xsl:apply-templates select="page" />
    </xsl:template>
</xsl:stylesheet>