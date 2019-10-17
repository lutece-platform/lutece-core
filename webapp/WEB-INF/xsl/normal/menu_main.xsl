<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:param name="site-path" select="site-path" />

    <xsl:template match="menu-list">
        <xsl:apply-templates select="menu" />
    </xsl:template>

    <xsl:template match="menu">
        <li>
            <a href="{$site-path}?page_id={page-id}" class="first-level" target="_top">
                <xsl:value-of select="page-name" />
            </a>
        </li>
    </xsl:template>

</xsl:stylesheet>

