<?xml version="1.0"?>

<xsl:transform xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:variable name="id_portlet" select="portlet/portlet-id" />

<xsl:template match="portlet">
  <p>
    <xsl:apply-templates select="links-portlet" />
	<br/>
  </p>
</xsl:template>

<xsl:template match="links-portlet">
       <br/>
	   <xsl:apply-templates select="link" />
       <br/>
</xsl:template>

<xsl:template match="link">
    <small><xsl:value-of select="link-name" /> : <xsl:value-of select="link-url"/></small>
    <br/>
</xsl:template>

</xsl:transform>
