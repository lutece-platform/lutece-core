<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="html" indent="yes" />

    <xsl:template match="plug-in">
		<!DOCTYPE html>
        <html>
            <head>
                <title>
                    Fiche du plugin
                    <xsl:value-of select="name" />
                </title>
                <link rel="stylesheet" type="text/css" href="../../css/portal_admin.css" title="lutece_admin" />
            </head>


            <center>
                <table cellpadding="0" cellspacing="0" width="80%" border="0">
                    <tr>
                        <td>
                            <table cellpadding="1" cellspacing="1" border="0" width="100%">
                                <tr>
                                    <th class="admin-title" height="30" colspan="3">
                                        <img src="../../{icon-url}" vspace="10" hspace="10" align="middle" />
                                        Plugin :
                                        <xsl:value-of select="name" />
                                    </th>
                                </tr>
                                <tr>
                                    <td>
                                        <blockquote>
                                            <b>Version : </b>
                                            <xsl:value-of select="version" />
                                            <br />
                                            <b>Description : </b>
                                            <xsl:value-of select="description" />
                                            <br />
                                            <b>Auteur : </b>
                                            <xsl:value-of select="provider" />
                                            <br />
                                            <xsl:if test="count(admin-features) != 0">
                                                <h3>
                                                    <xsl:text disable-output-escaping="yes">
                                                        Liste des fonctionnalités d'administration du plugin
                                                    </xsl:text>
                                                </h3>
                                                <xsl:apply-templates select="admin-features" />
                                            </xsl:if>
                                            <xsl:if test="count(portlets) != 0">
                                                <h3>
                                                    <xsl:text disable-output-escaping="yes">
                                                        Liste des portlets du plugin
                                                    </xsl:text>
                                                </h3>
                                                <xsl:apply-templates select="portlets" />
                                            </xsl:if>
                                            <xsl:if test="count(content-service) != 0">
                                                <h3>
                                                    <xsl:text disable-output-escaping="yes">
                                                        Service de contenu
                                                    </xsl:text>
                                                </h3>
                                                <xsl:apply-templates select="content-service" />
                                            </xsl:if>
                                            <xsl:if test="count(application) != 0">
                                                <h3>
                                                    <xsl:text disable-output-escaping="yes">
                                                        Application XPage
                                                    </xsl:text>
                                                </h3>
                                                <xsl:apply-templates select="application" />
                                            </xsl:if>
                                            <xsl:if test="count(link-service) != 0">
                                                <h3>
                                                    <xsl:text disable-output-escaping="yes">
                                                        Service d'insertion de lien via l'éditeur Html
                                                    </xsl:text>
                                                </h3>
                                                <xsl:apply-templates select="link-service" />
                                            </xsl:if>
                                            <xsl:if test="count(html-service) != 0">
                                                <h3>
                                                    <xsl:text disable-output-escaping="yes">
                                                        Service d'insertion de code html via l'éditeur Html
                                                    </xsl:text>
                                                </h3>
                                                <xsl:apply-templates select="html-service" />
                                            </xsl:if>
                                            <xsl:if test="count(params) != 0">
                                                <h3>
                                                    <xsl:text disable-output-escaping="yes">
                                                        Parametres
                                                    </xsl:text>
                                                </h3>
                                                <xsl:apply-templates select="params" />
                                            </xsl:if>

                                            <xsl:if test="not(string(documentation)='')">
                                                <b>
                                                    <xsl:text disable-output-escaping="yes">
                                                        Documentation :
                                                    </xsl:text>
                                                </b>
                                                <xsl:value-of select="documentation" />
                                                <br />
                                                <br />
                                            </xsl:if>
                                            <xsl:if test="not(string(installation)='')">
                                                <b>
                                                    <xsl:text disable-output-escaping="yes">
                                                        Installation :
                                                    </xsl:text>
                                                </b>
                                                <xsl:value-of select="installation" />
                                                <br />
                                                <br />
                                            </xsl:if>
                                            <xsl:if test="not(string(changes)='')">
                                                <b>
                                                    <xsl:text disable-output-escaping="yes">
                                                        Modifications :
                                                    </xsl:text>
                                                </b>
                                                <xsl:value-of select="changes" />
                                                <br />
                                                <br />
                                            </xsl:if>
                                            <xsl:if test="not(string(user-guide)='')">
                                                <b>
                                                    <xsl:text disable-output-escaping="yes">
                                                        Utilisation :
                                                    </xsl:text>
                                                </b>
                                                <xsl:value-of select="user-guide" />
                                                <br />
                                                <br />
                                            </xsl:if>
                                        </blockquote>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </center>
        </html>
    </xsl:template>

    <xsl:template match="admin-features">
        <xsl:apply-templates select="admin-feature" />
    </xsl:template>

    <xsl:template match="admin-feature">
        <ul>
            <li>
                <xsl:value-of select="feature-title" />
                <br />
                <i>
                    <xsl:value-of select="feature-description" />
                </i>
            </li>
        </ul>
    </xsl:template>

    <xsl:template match="portlets">
        <xsl:apply-templates select="portlet" />
    </xsl:template>

    <xsl:template match="portlet">
        <ul>
            <li>
                <xsl:value-of select="portlet-type-name" />
            </li>
        </ul>
    </xsl:template>

    <xsl:template match="content-service">
        <ul>
            <li>
                Ce plugin contient un service de Contenu basé sur la classe :
                <br />
                <i>
                    <xsl:value-of select="content-service-class" />
                </i>
            </li>
        </ul>
    </xsl:template>

    <xsl:template match="link-service">
        <ul>
            <li>
                Ce plugin contient un service de Contenu basé sur la classe :
                <br />
                <i>
                    <xsl:value-of select="link-service-class" />
                </i>
            </li>
        </ul>
    </xsl:template>

    <xsl:template match="html-service">
        <ul>
            <li>
                Ce plugin contient un service de Contenu basé sur la classe :
                <br />
                <i>
                    <xsl:value-of select="html-service-class" />
                </i>
            </li>
        </ul>
    </xsl:template>

    <xsl:template match="params">
        <xsl:apply-templates select="param" />
    </xsl:template>

    <xsl:template match="param">
        <ul>
            <li>
                <b>
                    <xsl:value-of select="param-name" />
                </b>
                :
                <xsl:value-of select="param-value" />
                <br />
                <i>
                    <xsl:value-of select="param-description" />
                </i>
            </li>
        </ul>
    </xsl:template>

</xsl:stylesheet>
