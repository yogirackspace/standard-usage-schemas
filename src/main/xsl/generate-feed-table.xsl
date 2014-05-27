<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns="http://docbook.org/ns/docbook" 
    xmlns:wadl="http://wadl.dev.java.net/2009/02"
    exclude-result-prefixes="xs"
    version="2.0" 
    xpath-default-namespace="http://docbook.org/ns/docbook">
    
    <xsl:output indent="yes"/>
    
    <xsl:template match="/">
        <table rules="all"
            xmlns:xi="http://www.w3.org/2001/XInclude"
            xmlns:xlink="http://www.w3.org/1999/xlink" 
            version="5.0">
            <caption>Feed Catalog</caption>
            <col width="29%"/>
            <col width="71%"/>
            <thead>
                <tr>
                    <th>Feed Name</th>
                    <th>URI</th>
                </tr>
            </thead>
            <tbody>
               <xsl:apply-templates select=".//section"/>
            </tbody>
        </table>
    </xsl:template>

    <xsl:template match="section">
        <xsl:variable name="wadl" select="substring-before(.//wadl:resource[1]/@href,'#')"/>
        <xsl:variable name="id" select="substring-after(.//wadl:resource[1]/@href,'#')"/>
        <tr>
        <td>
            <para><xsl:value-of select="title"/></para>
        </td>
        <td>
            <para>
                https://<replaceable>endpoint</replaceable>/<xsl:value-of select="doc(resolve-uri($wadl,base-uri(.)))//wadl:resource[@id = $id]/@path"/>
            </para>
        </td>
        </tr>
    </xsl:template>

</xsl:stylesheet>