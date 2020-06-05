<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:param name="site-path" select="site-path" />

    <xsl:template match="menu-list">
        <br />
        <br />
        <div id="menu-init">
            <div id="menu-init-content">
                <ul id="menu-verti">
                    <xsl:apply-templates select="menu" />
                </ul>
            </div>
        </div>
    </xsl:template>


    <xsl:template match="menu">
        <xsl:variable name="index">
            <xsl:number level="single" value="position()" />
        </xsl:variable>

        <xsl:if test="$index &gt; 7">
            <li class="first-verti">
                <a href="{$site-path}?page_id={page-id}" target="_top">
                    <xsl:value-of select="page-name" />
                </a>
                <xsl:apply-templates select="sublevel-menu-list" />
            </li>
        </xsl:if>
    </xsl:template>

    <xsl:template match="sublevel-menu-list">
        <ul>
            <li class="last-verti">
                <xsl:apply-templates select="sublevel-menu" />
            </li>
        </ul>
    </xsl:template>

    <xsl:template match="sublevel-menu">
        <xsl:variable name="index_sous_menu">
            <xsl:number level="single" value="position()" />
        </xsl:variable>

        <a href="{$site-path}?page_id={page-id}" target="_top">
            <span>
                <xsl:value-of select="page-name" />
            </span>
        </a>
    </xsl:template>

</xsl:stylesheet>
