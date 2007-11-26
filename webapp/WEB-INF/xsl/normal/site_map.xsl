<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:param name="site-path" select="site-path" />


<xsl:template match="page[page-level=0]">
<div id="content">
<div id="one-zone-first">
		<div id="one-zone-first-content">			
			<xsl:apply-templates select="child-pages-list" />
		</div>
	</div>
</div>
<div id="sidebar">
	<img src="document?id=7&amp;id_attribute=52" alt="banner" title="banner"/>
	&#160;
</div>
	
</xsl:template>


<xsl:template match="page[page-level=1]" >
<ul class="site-map-level-one">
	<li>
		<a href="{$site-path}?page_id={page-id}" target="_top">
			<xsl:value-of select="page-name" />
		</a>
		<xsl:apply-templates select="page-description" />
		<xsl:apply-templates select="page-image" />
		<xsl:apply-templates select="child-pages-list" />
	    <xsl:text disable-output-escaping="yes">
		    <![CDATA[<div class="clear">&#160;</div>]]>
	    </xsl:text>
	</li>
</ul>
</xsl:template>


<xsl:template match="page[page-level=2]" >
<ul class="site-map-level-two">
	<li>
		<a href="{$site-path}?page_id={page-id}" target="_top">
			<xsl:value-of select="page-name" />
		</a>
		<xsl:apply-templates select="page-description" />
		<xsl:apply-templates select="child-pages-list" />
	</li>
</ul>
</xsl:template>


<xsl:template match="page[page-level>2]" >
<ul class="site-map-level-highest">
	<li>
		<a href="{$site-path}?page_id={page-id}" target="_top">
			<xsl:value-of select="page-name" />
		</a>
		<xsl:apply-templates select="page-description" />
		<xsl:apply-templates select="child-pages-list" />
	</li>
</ul>
</xsl:template>


<xsl:template match="page-description">
	<br /><xsl:value-of select="." />
</xsl:template>


<xsl:template match="child-pages-list[page-level=0]">
	<xsl:if test="count(page)>0" >
		<xsl:apply-templates select="page" />
    </xsl:if>
</xsl:template>


<xsl:template match="child-pages-list[page-level=1]">
	<xsl:if test="count(page)>0" >
		<xsl:apply-templates select="page" />
    </xsl:if>
</xsl:template>


<xsl:template match="child-pages-list[page-level=2]">
	<xsl:if test="count(page)>0" >
		<xsl:apply-templates select="page" />
    </xsl:if>
</xsl:template>

<xsl:template match="child-pages-list[page-level>2]">
	<xsl:if test="count(page)>0" >
		<xsl:apply-templates select="page" />
    </xsl:if>
</xsl:template>


<xsl:template match="page-image">
	<div class="level-one-image">
    	<div class="polaroid">
		<img  border="0" width="80" height="80" src="images/local/data/pages/{.}" alt="" />
         </div>
	</div >
</xsl:template>


</xsl:stylesheet>
