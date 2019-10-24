<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:param name="site-path" select="site-path" />

    <xsl:template match="menu-list">
        <xsl:variable name="menu-list" select="menu" />

        <script type="text/javascript">
            $(document).ready(function(){
            $("#tree").treeview({
                animated: "fast",
                collapsed: false,
                unique: true,
                persist: "cookie"
            });

            });
        </script>

        <!-- Menu Tree -->
        <xsl:if test="not(string(menu)='')">
            <xsl:text disable-output-escaping="yes">            
                <div class="tree4">        
                    <h2>&#160;</h2>
                    <ul id="tree" class="tree4">
                        <xsl:apply-templates select="menu" />        
                    </ul>    
                </div>
                <br />
            </xsl:text>
        </xsl:if>
    </xsl:template>


    <xsl:template match="menu">
        <xsl:variable name="index">
            <xsl:number level="single" value="position()" />
        </xsl:variable>
        <li>
            <!--<xsl:if test="$index &lt; 7"> -->
            <a href="{$site-path}?page_id={page-id}" target="_top">
                <xsl:value-of select="page-name" />
            </a>
            <br />
            <xsl:value-of select="page-description" />
            <!--<xsl:value-of select="page-description" /><br /> -->
            <xsl:apply-templates select="sublevel-menu-list" />

        </li>
        <!--</xsl:if> -->

    </xsl:template>

    <xsl:template match="sublevel-menu-list">

        <xsl:apply-templates select="sublevel-menu" />

    </xsl:template>


    <xsl:template match="sublevel-menu">
        <xsl:variable name="index_sous_menu">
            <xsl:number level="single" value="position()" />
        </xsl:variable>
        <ul>
            <li>
                <!-- <span> -->
                <a href="{$site-path}?page_id={page-id}" target="_top">
                    <xsl:value-of select="page-name" />
                </a>
            </li>
        </ul>
        <!--</span> -->


    </xsl:template>

</xsl:stylesheet>
