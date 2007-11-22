<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:param name="site-path" select="site-path" />

<xsl:template match="menu-list">
	<div id="menu-main">
         <ul id="menu">
             <xsl:apply-templates select="menu" />
         </ul>
    </div>
</xsl:template>


<xsl:template match="menu">
    <xsl:variable name="index">
    	<xsl:number level="single" />
    </xsl:variable>

    <xsl:if test="$index &lt; 5">

     <xsl:choose>
       <xsl:when test="position()=1" >
            <li class="first">
                <a href="{$site-path}?page_id={page-id}" target="_top" >
                   <xsl:value-of select="page-name" />
                </a>
	            <!-- <xsl:apply-templates select="sublevel-menu-list" /> -->
             </li>
       </xsl:when>
       <xsl:when test="position()=last() or $index=4" >
                   <li class="last">
                      <a href="{$site-path}?page_id={page-id}" target="_top" >
                              <xsl:value-of select="page-name" />
                       </a>
                      <!-- <xsl:apply-templates select="sublevel-menu-list" /> -->
                    </li>
       </xsl:when>
       <xsl:otherwise>
                   <li>
                      <a href="{$site-path}?page_id={page-id}" target="_top" >
                              <xsl:value-of select="page-name" />
                       </a>
                       <!-- <xsl:apply-templates select="sublevel-menu-list" /> -->
                    </li>
       </xsl:otherwise>
        </xsl:choose>
   </xsl:if>

</xsl:template>

<xsl:template match="sublevel-menu-list" >
	<xsl:if test="sublevel-menu!=''">
    	<ul>
 	            <xsl:apply-templates select="sublevel-menu" />
        </ul>
	</xsl:if>
</xsl:template>

<xsl:template match="sublevel-menu">
   <xsl:variable name="index_sous_menu">
         <xsl:number level="single" />
   </xsl:variable>

        <xsl:choose>
           <xsl:when test="position()=1" >
             <li class="first">
                <a href="{$site-path}?page_id={page-id}" target="_top" >
	                    <span><xsl:value-of select="page-name" /></span>
                </a>
              </li>
           </xsl:when>
           <xsl:when test="position()=last()" >
             <li class="last">
                <a href="{$site-path}?page_id={page-id}" target="_top" >
	                    <span><xsl:value-of select="page-name" /></span>
                </a>
              </li>
           </xsl:when>
           <xsl:otherwise>
               <li>
                <a href="{$site-path}?page_id={page-id}" target="_top" >
	                    <span><xsl:value-of select="page-name" /></span>
                </a>
              </li>
           </xsl:otherwise>
        </xsl:choose>

</xsl:template>

</xsl:stylesheet>
