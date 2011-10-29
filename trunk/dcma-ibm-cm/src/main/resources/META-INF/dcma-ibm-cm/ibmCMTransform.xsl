
	<!--
		- XSLT is a template based language to transform Xml documents It uses
		XPath to select specific nodes for processing. - A XSLT file is a well
		formed Xml document
	-->

	<!-- every StyleSheet starts with this tag -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:java="http://xml.apache.org/xslt/java" exclude-result-prefixes="java"
	version="2.0">

	<!-- indicates what our output type is going to be -->
	<xsl:output method="html" />
	<xsl:param name="totalDocSize"></xsl:param>
	<xsl:param name="batchCreationDate"></xsl:param>
	<xsl:param name="batchCreationTime"></xsl:param>
	<xsl:param name="datFileName"></xsl:param>
	<xsl:param name="cmodAppGroup"></xsl:param>
	<xsl:param name="cmodApp"></xsl:param>
	<xsl:param name="userName"></xsl:param>
	<xsl:param name="email"></xsl:param>
	<xsl:param name="batchCreationStationID"></xsl:param>
	<xsl:param name="stationID"></xsl:param>
	<xsl:param name="supplyingSystem"></xsl:param>


	<!--
		Main template to kick off processing our Sample.xml From here on we
		use a simple XPath selection query to get to our data.
	-->
	<xsl:template match="/">
		<xsl:element name="Batch">
			<xsl:element name="SupplyingSystem">
				<xsl:value-of select="$supplyingSystem" />
			</xsl:element>
			<xsl:element name="BatchId">
				<xsl:value-of select="Batch/BatchInstanceIdentifier" />
			</xsl:element>
			<xsl:element name="UserID">
			</xsl:element>
			<xsl:element name="CMODAppGroup">
				<xsl:value-of select="$cmodAppGroup" />
			</xsl:element>
			<xsl:element name="CMODApp">
				<xsl:value-of select="$cmodApp" />
			</xsl:element>
			<xsl:element name="TotalDocs">
				<xsl:value-of select="count(Batch/Documents/Document)" />
			</xsl:element>
			<xsl:element name="TotalBytes">
				<xsl:value-of select="$totalDocSize" />
			</xsl:element>
			<xsl:element name="TotalPages">
				0
			</xsl:element>
			<xsl:element name="LoadDate">
				<xsl:value-of select="$batchCreationDate" />
			</xsl:element>
			<xsl:element name="Bundle">
				<xsl:element name="Type">
					FILE
				</xsl:element>
				<xsl:element name="Name">
					<xsl:value-of select="$datFileName" />
				</xsl:element>
			</xsl:element>
			<xsl:element name="SystemInfo">
				<xsl:element name="Field">
					<xsl:element name="FieldName">
						Batch Class Name
					</xsl:element>
					<xsl:element name="FieldValue">
						<xsl:value-of select="Batch/BatchClassIdentifier" />
					</xsl:element>
				</xsl:element>
				<xsl:element name="Field">
					<xsl:element name="FieldName">
						Batch Creation Date
					</xsl:element>
					<xsl:element name="FieldValue">
						<xsl:value-of select="$batchCreationDate" />
					</xsl:element>
				</xsl:element>
				<xsl:element name="Field">
					<xsl:element name="FieldName">
						Batch Creation Time
					</xsl:element>
					<xsl:element name="FieldValue">
						<xsl:value-of select="$batchCreationTime" />
					</xsl:element>
				</xsl:element>
				<xsl:element name="Field">
					<xsl:element name="FieldName">
						Batch Creator's Station ID
					</xsl:element>
					<xsl:element name="FieldValue">
						<xsl:value-of select="$batchCreationStationID" />
					</xsl:element>
				</xsl:element>
				<xsl:element name="Field">
					<xsl:element name="FieldName">
						Batch ID
					</xsl:element>
					<xsl:element name="FieldValue">
						<xsl:value-of select="Batch/BatchInstanceIdentifier" />
					</xsl:element>
				</xsl:element>
				<xsl:element name="Field">
					<xsl:element name="FieldName">
						Batch Name
					</xsl:element>
					<xsl:element name="FieldValue">
						<xsl:value-of select="Batch/BatchName" />
					</xsl:element>
				</xsl:element>
				<xsl:element name="Field">
					<xsl:element name="FieldName">
						Current Date
					</xsl:element>
					<xsl:element name="FieldValue">
						<xsl:value-of
							select="java:format(java:java.text.SimpleDateFormat.new('MM/dd/yyyy'), java:java.util.Date.new())" />
					</xsl:element>
				</xsl:element>
				<xsl:element name="Field">
					<xsl:element name="FieldName">
						Current Time
					</xsl:element>
					<xsl:element name="FieldValue">
						<xsl:value-of
							select="java:format(java:java.text.SimpleDateFormat.new('HH:mm:ss'), java:java.util.Date.new())" />
					</xsl:element>
				</xsl:element>
				<xsl:element name="Field">
					<xsl:element name="FieldName">
						Document Count
					</xsl:element>
					<xsl:element name="FieldValue">
						<xsl:value-of select="count(Batch/Documents/Document)" />
					</xsl:element>
				</xsl:element>
				<xsl:element name="Field">
					<xsl:element name="FieldName">
						Station ID
					</xsl:element>
					<xsl:element name="FieldValue">
						<xsl:value-of select="$stationID" />
					</xsl:element>
				</xsl:element>
				<xsl:element name="Field">
					<xsl:element name="FieldName">
						User Name
					</xsl:element>
					<xsl:element name="FieldValue">
						Ephesoft
					</xsl:element>
				</xsl:element>
			</xsl:element>
			<xsl:element name="Documents">
				<xsl:for-each select="Batch/Documents/Document">
					<xsl:element name="Document">
						<xsl:element name="DocId">
							<xsl:value-of select="Identifier" />
						</xsl:element>
						<xsl:element name="Offset">
							0
						</xsl:element>
						<xsl:element name="Size">
							<xsl:value-of select="Size" />
						</xsl:element>
						<xsl:element name="FileName">
							<xsl:value-of select="$datFileName" />
						</xsl:element>
						<xsl:element name="Fields">
							<xsl:for-each select="DocumentLevelFields/DocumentLevelField">
								<xsl:if test="not(Name='DRCi_Instance')">
									<xsl:element name="Field">
										<xsl:element name="FieldName">
											<xsl:value-of select="Name" />
										</xsl:element>
										<xsl:element name="FieldValue">
											<xsl:value-of select="Value" />
										</xsl:element>
									</xsl:element>
								</xsl:if>
							</xsl:for-each>
						</xsl:element>
					</xsl:element>
				</xsl:for-each>
			</xsl:element>
			<xsl:element name="Notify">
				<xsl:element name="Email">
					<xsl:value-of select="$email" />
				</xsl:element>
			</xsl:element>
		</xsl:element>
	</xsl:template>
</xsl:stylesheet>
