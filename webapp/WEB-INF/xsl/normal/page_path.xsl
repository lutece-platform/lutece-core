<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:param name="site-path" select="site-path" />


    <xsl:template match="page">
        <xsl:if test="position()!=last()-1">
            <a href="{$site-path}?page_id={page-id}" target="_top">
                <xsl:value-of select="page-name" />
            </a>
            >
        </xsl:if>
        <xsl:if test="position()=last()-1">
            <xsl:value-of select="page-name" />
        </xsl:if>
    </xsl:template>


    <xsl:template match="page_link">
        <xsl:if test="position()!=last()-1">
            <a href="{$site-path}?{page-url}" target="_top">
                <xsl:value-of select="page-name" />
            </a>
            >
        </xsl:if>
        <xsl:if test="position()=last()-1">
            <xsl:value-of select="page-name" />
        </xsl:if>
    </xsl:template>


</xsl:stylesheet>