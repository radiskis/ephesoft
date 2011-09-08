<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="/">
		<xsl:element name="BULK_TRANSACTION">
			<xsl:element name="CUSTOMER">Sample Value</xsl:element>
			<xsl:element name="PRODUCER">
				<xsl:element name="PRODUCER_EMAIL">Sample Value</xsl:element>
				<xsl:element name="PRODUCER_PHONE">Sample Value</xsl:element>
			</xsl:element>
			<xsl:element name="BATCH">
				<xsl:element name="BATCH_ID"><xsl:value-of select="Batch/BatchInstanceIdentifier" /></xsl:element>
				<xsl:element name="LOAD">
					<xsl:for-each select="Batch/Documents/Document">
						<xsl:element name="DOCUMENT">
							<xsl:element name="DOC_ID"><xsl:value-of select="../../BatchInstanceIdentifier" />_<xsl:value-of select="Identifier" /></xsl:element>
								<xsl:element name="IMAGE">
									<xsl:element name="IMAGE_PATH"><xsl:value-of select="MultiPagePdfFile" /></xsl:element>
								</xsl:element>
								<xsl:element name="DCTM_LOCATION">
									<xsl:element name="FOLDER"><xsl:value-of select="../../BatchClassIdentifier" />/<xsl:value-of select="../../BatchInstanceIdentifier" />/<xsl:value-of select="Identifier" /></xsl:element>
								</xsl:element>
								<xsl:element name="LOGICAL_DOC_TYPE">
									<xsl:element name="LOGICAL_DOC_TYPE_ID"><xsl:value-of select="Type" /></xsl:element>
								</xsl:element>
								<xsl:element name="Document">
									<xsl:element name="ATTRIBUTES">
										<xsl:for-each select="DocumentLevelFields/DocumentLevelField">
											<xsl:variable name="fieldName" select="translate(Name,' ','')"></xsl:variable>
											<xsl:element name="{$fieldName}"><xsl:value-of select="Value"/></xsl:element>
										</xsl:for-each>
									</xsl:element>
								</xsl:element>
						</xsl:element>
					</xsl:for-each>
				</xsl:element>
			</xsl:element>
		</xsl:element>
	</xsl:template>

</xsl:stylesheet>

