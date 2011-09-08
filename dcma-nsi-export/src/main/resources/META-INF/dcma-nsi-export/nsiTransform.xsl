
	<!--
		- XSLT is a template based language to transform Xml documents It uses
		XPath to select specific nodes for processing. - A XSLT file is a well
		formed Xml document
	-->

	<!-- every StyleSheet starts with this tag -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	version="2.0">

	<!-- indicates what our output type is going to be -->
	<xsl:output method="html" />
	<xsl:param name="date"></xsl:param>
	<xsl:param name="hours"></xsl:param>
	<xsl:param name="nsiExportImages"></xsl:param>
	<xsl:param name="nsiExportPath"></xsl:param>


	<!--
		Main template to kick off processing our Sample.xml From here on we
		use a simple XPath selection query to get to our data.
	-->
	<xsl:template match="/">
		<xsl:element name="AutoStore">
			<xsl:element name="Header">
				<xsl:attribute name="Version">1</xsl:attribute>
				<xsl:attribute name="DateCreated"><xsl:value-of select="$date" /></xsl:attribute>
				<xsl:attribute name="TimeCreated"><xsl:value-of select="$hours" /></xsl:attribute>
				<xsl:attribute name="AUTHOR"></xsl:attribute>
				<xsl:attribute name="COMMENTS"></xsl:attribute>
			</xsl:element>
			<xsl:element name="KnowledgeObjects">
				<xsl:attribute name="Count"><xsl:value-of
					select="count(Batch/Documents/Document)" /></xsl:attribute>
				<xsl:for-each select="Batch/Documents/Document">
					<xsl:element name="KnowledgeObject">
						<xsl:element name="KnowledgeDocuments">
							<xsl:attribute name="Count"><xsl:value-of
								select="count(../Document)" /></xsl:attribute>
							<xsl:element name="KnowledgeDocument">
								<xsl:variable name="batchLocalPath">
									<xsl:value-of select="../../BatchLocalPath" />
								</xsl:variable>
								<xsl:variable name="multiPageTiffExport">
									<xsl:value-of select="$nsiExportImages" />
								</xsl:variable>
								<xsl:variable name="multiPageTiff">
									<xsl:value-of select="MultiPageTiffFile" />
								</xsl:variable>
								<xsl:variable name="multiPageTiffAlt">
									<xsl:value-of
										select="concat(substring-before($multiPageTiff, 'tif'), translate(substring-after($multiPageTiff, '.'), 'tif', 'dat'))" />
								</xsl:variable>
								<xsl:attribute name="DocRef"><xsl:value-of
									select="$nsiExportPath" /><xsl:value-of
									select="concat($multiPageTiffExport, $multiPageTiffAlt)" /></xsl:attribute>
								<xsl:attribute name="Type">tif</xsl:attribute>
								<xsl:attribute name="Name"><xsl:value-of select="MultiPageTiffFile" /></xsl:attribute>
							</xsl:element>
						</xsl:element>
						<xsl:element name="KnowledgeFields">
							<xsl:attribute name="Count"><xsl:value-of
								select="count(DocumentLevelFields/DocumentLevelField)" /></xsl:attribute>
							<xsl:for-each select="DocumentLevelFields/DocumentLevelField">
								<xsl:element name="Field">
									<xsl:attribute name="Name"><xsl:value-of
										select="Name" /></xsl:attribute>
									<xsl:attribute name="Value"><xsl:value-of
										select="Value" /></xsl:attribute>
								</xsl:element>
							</xsl:for-each>
						</xsl:element>
					</xsl:element>
				</xsl:for-each>
			</xsl:element>
		</xsl:element>
	</xsl:template>
</xsl:stylesheet>
