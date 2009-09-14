<?xml version="1.0"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:param name="site-path" select="site-path" />

	<xsl:template match="menu-list">
		<xsl:apply-templates select="menu" />
	</xsl:template>

	<xsl:template match="menu">
		<xsl:variable name="index">
			<xsl:number level="single" value="position()" />
		</xsl:variable>

		<xsl:if test="$index &lt; 4">
			<div class="footer1-column">
				<a href="{$site-path}?page_id={page-id}" class="first-level" target="_top">
					<xsl:value-of select="page-name" />
				</a>
				<br />
				<xsl:apply-templates select="sublevel-menu-list" />
			</div>
		</xsl:if>

	</xsl:template>

	<xsl:template match="sublevel-menu-list">
		<xsl:apply-templates select="sublevel-menu" />
	</xsl:template>

	<xsl:template match="sublevel-menu">
		<xsl:variable name="index_sous_menu">
			<xsl:number level="single" value="position()" />
		</xsl:variable>
		<a href="{$site-path}?page_id={page-id}" class="last-level" target="_top">
			<xsl:value-of select="page-name" />
		</a>
		<br />
	</xsl:template>

</xsl:stylesheet>

