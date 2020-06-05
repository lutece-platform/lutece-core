<?xml version='1.0'?>
<xsl:stylesheet version='1.0' xmlns:xsl='http://www.w3.org/1999/XSL/Transform'>
    <xsl:output method='html' />

    <xsl:param name='locale' />
    <xsl:param name='default_locale' />


    <xsl:template match='node() | @*'>
        <xsl:copy>

            <xsl:apply-templates select='node()' />

        </xsl:copy>
    </xsl:template>

    <xsl:template match='section'>

        <xsl:param name='ancretmptmp' select='translate(@name,"&apos;","")' />
        <xsl:param name='ancretmp' select='translate($ancretmptmp," ","_")' />
        <xsl:param name='ancre' select='translate($ancretmp,"/","")' />
        <xsl:choose>
            <xsl:when
                test='starts-with($ancre,"1") or starts-with($ancre,"2") or starts-with($ancre,"3") or starts-with($ancre,"4") or starts-with($ancre,"5") or starts-with($ancre,"6") or starts-with($ancre,"7") or starts-with($ancre,"8") or starts-with($ancre,"9")'>
                <a name='a{$ancre}'></a>
                <h2>
                    <xsl:value-of select='@name' />
                </h2>
            </xsl:when>
            <xsl:otherwise>
                <a name='{$ancre}'></a>
                <h2>
                    <xsl:value-of select='@name' />
                </h2>
            </xsl:otherwise>
        </xsl:choose>

        <xsl:copy>

            <xsl:apply-templates select='@* | node()' />

        </xsl:copy>
    </xsl:template>

    <xsl:template match='subsection'>

        <xsl:param name='ancretmptmp' select='translate(@name,"&apos;","")' />
        <xsl:param name='ancretmp' select='translate($ancretmptmp," ","_")' />
        <xsl:param name='ancre' select='translate($ancretmp,"/","")' />

        <xsl:choose>
            <xsl:when
                test='starts-with($ancre,"1") or starts-with($ancre,"2") or starts-with($ancre,"3") or starts-with($ancre,"4") or starts-with($ancre,"5") or starts-with($ancre,"6") or starts-with($ancre,"7") or starts-with($ancre,"8") or starts-with($ancre,"9")'>
                <a name='a{$ancre}'></a>
                <h3>
                    <xsl:value-of select='@name' />
                </h3>
            </xsl:when>
            <xsl:otherwise>
                <a name='{$ancre}'></a>
                <h3>
                    <xsl:value-of select='@name' />
                </h3>
            </xsl:otherwise>
        </xsl:choose>


        <xsl:copy>

            <xsl:apply-templates select='@* | node()' />

        </xsl:copy>
    </xsl:template>

    <!-- modify the image url -->
    <xsl:template match='img'>

        <xsl:choose>
            <xsl:when test='$locale = $default_locale'>
                <img src='doc/xml/resources/images/{@src}' alt='{@alt}' title='{@title}' />
            </xsl:when>
            <xsl:when test='starts-with(@src,"../../") '>
                <img src='doc/xml/resources/images/images/{@src}' alt='{@alt}' title='{@title}' />
            </xsl:when>
            <xsl:otherwise>
                <img src='doc/xml/{$locale}/resources/images/{@src}' alt='{@alt}' title='{@title}' />
            </xsl:otherwise>
        </xsl:choose>

    </xsl:template>


    <!-- modify the link url -->
    <xsl:template match='a[@href]'>

        <xsl:choose>

            <xsl:when test='not(starts-with(@href,"http")) and not(contains(@href, "#"))'>
                <a href='jsp/admin/documentation/AdminDocumentation.jsp?doc={substring-before(@href,".html")}'>
                    <xsl:value-of select='.' />
                </a>

            </xsl:when>

            <xsl:when test='not(starts-with(@href,"http"))  and contains(@href, "#")'>
                <a
                    href='jsp/admin/documentation/AdminDocumentation.jsp?doc={substring-before(@href,".html")}{substring-after(@href,".html")}'>
                    <xsl:value-of select='.' />
                </a>
            </xsl:when>

            <xsl:otherwise>
                <xsl:copy>
                    <xsl:apply-templates select='@* | node()' />
                </xsl:copy>
            </xsl:otherwise>

        </xsl:choose>

    </xsl:template>

</xsl:stylesheet>