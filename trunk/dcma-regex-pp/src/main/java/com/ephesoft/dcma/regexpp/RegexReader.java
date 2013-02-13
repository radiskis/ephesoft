/********************************************************************************* 
* Ephesoft is a Intelligent Document Capture and Mailroom Automation program 
* developed by Ephesoft, Inc. Copyright (C) 2010-2012 Ephesoft Inc. 
* 
* This program is free software; you can redistribute it and/or modify it under 
* the terms of the GNU Affero General Public License version 3 as published by the 
* Free Software Foundation with the addition of the following permission added 
* to Section 15 as permitted in Section 7(a): FOR ANY PART OF THE COVERED WORK 
* IN WHICH THE COPYRIGHT IS OWNED BY EPHESOFT, EPHESOFT DISCLAIMS THE WARRANTY 
* OF NON INFRINGEMENT OF THIRD PARTY RIGHTS. 
* 
* This program is distributed in the hope that it will be useful, but WITHOUT 
* ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS 
* FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more 
* details. 
* 
* You should have received a copy of the GNU Affero General Public License along with 
* this program; if not, see http://www.gnu.org/licenses or write to the Free 
* Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 
* 02110-1301 USA. 
* 
* You can contact Ephesoft, Inc. headquarters at 111 Academy Way, 
* Irvine, CA 92617, USA. or at email address info@ephesoft.com. 
* 
* The interactive user interfaces in modified source and object code versions 
* of this program must display Appropriate Legal Notices, as required under 
* Section 5 of the GNU Affero General Public License version 3. 
* 
* In accordance with Section 7(b) of the GNU Affero General Public License version 3, 
* these Appropriate Legal Notices must retain the display of the "Ephesoft" logo. 
* If the display of the logo is not reasonably feasible for 
* technical reasons, the Appropriate Legal Notices must display the words 
* "Powered by Ephesoft". 
********************************************************************************/ 

package com.ephesoft.dcma.regexpp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.ephesoft.dcma.batch.dao.impl.BatchPluginPropertyContainer.BatchPluginConfiguration;
import com.ephesoft.dcma.batch.schema.Batch;
import com.ephesoft.dcma.batch.schema.Coordinates;
import com.ephesoft.dcma.batch.schema.DocField;
import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.batch.schema.Field;
import com.ephesoft.dcma.batch.schema.HocrPages;
import com.ephesoft.dcma.batch.schema.Page;
import com.ephesoft.dcma.batch.schema.DocField.AlternateValues;
import com.ephesoft.dcma.batch.schema.Document.Pages;
import com.ephesoft.dcma.batch.schema.Field.CoordinatesList;
import com.ephesoft.dcma.batch.schema.HocrPages.HocrPage;
import com.ephesoft.dcma.batch.schema.Page.PageLevelFields;
import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.batch.service.PluginPropertiesService;
import com.ephesoft.dcma.core.DCMAException;
import com.ephesoft.dcma.core.component.ICommonConstants;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.da.domain.KVPageProcess;
import com.ephesoft.dcma.da.id.BatchInstanceID;
import com.ephesoft.dcma.kvfinder.data.InputDataCarrier;
import com.ephesoft.dcma.kvfinder.data.OutputDataCarrier;
import com.ephesoft.dcma.kvfinder.service.KVFinderService;
import com.ephesoft.dcma.regexpp.constant.RegexPPConstant;

/**
 * This class reads the key value on each image file fetched from batch.xml, processes images with existing key value pattern
 * information for each image and writes the extracted information to batch xml to be used by subsequent plugins.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.core.component.ICommonConstants
 * @see com.ephesoft.dcma.batch.service.BatchSchemaService
 * 
 */
@Component
public class RegexReader implements ICommonConstants {

	/**
	 * MAX_VAL_CONST int.
	 */
	private static final int MAX_VAL_CONST = 10;

	/**
	 * KV_PAGE_PROCESS_PLUGIN String.
	 */
	private static final String KV_PAGE_PROCESS_PLUGIN = "KV_PAGE_PROCESS";

	/**
	 * ON_SWTICH String.
	 */
	private static final String ON_SWTICH = "ON";

	/**
	 * Instance of BatchSchemaService.
	 */
	@Autowired
	private BatchSchemaService batchSchemaService;

	/**
	 * Instance of PluginPropertiesService.
	 */
	@Autowired
	@Qualifier("batchInstancePluginPropertiesService")
	private PluginPropertiesService pluginPropertiesService;

	/**
	 * kvFinderService KVFinderService.
	 */
	@Autowired
	private KVFinderService kvFinderService;

	/**
	 * Logger instance for logging using slf4j for logging information.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(RegexReader.class);

	/**
	 * To get Batch Schema Service.
	 * @return the batchSchemaService
	 */
	public BatchSchemaService getBatchSchemaService() {
		return batchSchemaService;
	}

	/**
	 * To get Plugin Properties Service.
	 * @return the pluginPropertiesService
	 */
	public PluginPropertiesService getPluginPropertiesService() {
		return pluginPropertiesService;
	}

	/**
	 * This method is used to update the page value If the page successfully update then it return true otherwise return false. If
	 * page level field is not exist in the batch.xml then it will create the new PageLevelField and enters the alternative value and
	 * values for that page level field and update it in page object.
	 * 
	 * @param page
	 * @param outputDataCarriers {@link OutputDataCarrier}
	 * @param pluginName {@link String}
	 * @throws DCMAApplicationException if any error occur in updation of batch.xml
	 */
	public boolean updateBatchXML(final Page page, List<OutputDataCarrier> outputDataCarriers, final BatchInstanceID batchInstanceID,
			final String pluginName) throws DCMAApplicationException {
		Boolean isSuccessful = false;
		String errMsg = null;

		if (page == null) {
			errMsg = "In valid page.";
			LOGGER.error(errMsg);
			throw new DCMAApplicationException(errMsg);
		}

		PageLevelFields pageLevelFields = page.getPageLevelFields();

		if (pageLevelFields == null) {
			pageLevelFields = new PageLevelFields();
		}

		List<DocField> allPageLevelFields = pageLevelFields.getPageLevelField();
		DocField docField = new DocField();
		AlternateValues alternateValues = docField.getAlternateValues();

		if (alternateValues == null) {
			alternateValues = new AlternateValues();
		}

		DocField docFieldType = new DocField();

		if (outputDataCarriers != null && !outputDataCarriers.isEmpty()) {
			OutputDataCarrier maxOutputDataCarrier = getMaxOutputDataCarrier(outputDataCarriers);

			Coordinates coordinates = new Coordinates();
			docFieldType.setName(pluginName);
			docFieldType.setValue(maxOutputDataCarrier.getValue());
			docFieldType.setConfidence(maxOutputDataCarrier.getConfidence());
			coordinates.setX0(maxOutputDataCarrier.getSpan().getCoordinates().getX0());
			coordinates.setY0(maxOutputDataCarrier.getSpan().getCoordinates().getY0());
			coordinates.setX1(maxOutputDataCarrier.getSpan().getCoordinates().getX1());
			coordinates.setY1(maxOutputDataCarrier.getSpan().getCoordinates().getY1());
			CoordinatesList coordinatesList = new CoordinatesList();
			coordinatesList.getCoordinates().add(coordinates);
			docFieldType.setCoordinatesList(coordinatesList);

			for (OutputDataCarrier outputDataCarrier : outputDataCarriers) {
				Field fieldType = new Field();

				Coordinates alternateCoordinates = new Coordinates();
				fieldType.setName(pluginName);
				fieldType.setValue(outputDataCarrier.getValue());
				fieldType.setConfidence(outputDataCarrier.getConfidence());
				alternateCoordinates.setX0(outputDataCarrier.getSpan().getCoordinates().getX0());
				alternateCoordinates.setY0(outputDataCarrier.getSpan().getCoordinates().getY0());
				alternateCoordinates.setX1(outputDataCarrier.getSpan().getCoordinates().getX1());
				alternateCoordinates.setY1(outputDataCarrier.getSpan().getCoordinates().getY1());

				// CoordinatesList coordinateList = new CoordinatesList();
				coordinatesList.getCoordinates().add(alternateCoordinates);
				fieldType.setCoordinatesList(coordinatesList);

				alternateValues.getAlternateValue().add(fieldType);
			}
			isSuccessful = true;
		} else {
			docFieldType.setName(pluginName);
			docFieldType.setValue(RegexPPConstant.EMPTY);
			isSuccessful = true;
		}
		docFieldType.setAlternateValues(alternateValues);
		allPageLevelFields.add(docFieldType);
		page.setPageLevelFields(pageLevelFields);

		return isSuccessful;
	}

	/**
	 * This method is taking input output data carrier list and used to return the output data carrier object having the maximum confidence
	 * value. If two object having the maximum confidence value then this method return the first object having the maximum confidence
	 * value.
	 * 
	 * @param outputDataCarriers {@link OutputDataCarrier}
	 * @return maxOutputDataCarrier having maximum confidence value
	 */
	private OutputDataCarrier getMaxOutputDataCarrier(List<OutputDataCarrier> outputDataCarriers) {

		OutputDataCarrier maxOutputDataCarrier = outputDataCarriers.get(RegexPPConstant.ZERO);

		for (OutputDataCarrier outputDataCarrier : outputDataCarriers) {
			if (outputDataCarrier.getConfidence() > maxOutputDataCarrier.getConfidence()) {
				maxOutputDataCarrier = outputDataCarrier;
			}
		}
		return maxOutputDataCarrier;
	}

	/**
	 * This method taking batchInstanceID as input and fetching out the batchClassPluginConfigList with the use of batchInstanceID,
	 * plugin, RegexPattern. On iterating over the batchClassPluginConfigList input data carries list is created and each input data
	 * carrier list is stored in the map in accordance of the page level field name.
	 * 
	 * @param batchInstanceID {@link BatchInstanceID}
	 * @param mapCarrier Map<String, List<InputDataCarrier>>
	 * @return inputDataCarriers list of {@link InputDataCarrier}
	 * @throws DCMAApplicationException if any error occur in retrieving list
	 */
	private void retrieveInputCarrierList(final BatchInstanceID batchInstanceID, final Map<String, List<InputDataCarrier>> mapCarrier)
			throws DCMAApplicationException {
		String errMsg = null;
		BatchPluginConfiguration[] batchClassPluginConfigList = pluginPropertiesService.getPluginProperties(batchInstanceID.getID(),
				KV_PAGE_PROCESS_PLUGIN, RegexProperties.KEY_VALUE_PATTERNS);

		if (batchClassPluginConfigList == null) {
			errMsg = "In valid parameters.";
			LOGGER.error(errMsg);
			throw new DCMAApplicationException(errMsg);
		}

		for (BatchPluginConfiguration batchPluginConfiguration : batchClassPluginConfigList) {
			if (null == batchPluginConfiguration) {
				LOGGER.error("BatchPluginConfiguration is null.");
				continue;
			}
			for (KVPageProcess kvPageProcess : batchPluginConfiguration.getKvPageProcesses()) {
				if (null == kvPageProcess) {
					LOGGER.error("KVPageProcess is null.");
					continue;
				}
				createInputData(kvPageProcess, mapCarrier);
			}
		}
	}

	/**
	 * This method is used to update the batch.xml file.
	 * 
	 * @param batchInstanceID {@link BatchInstanceID}
	 * @throws DCMAApplicationException if any error occur in updating batch.xml
	 * @throws DCMAException if any error occur in retrieving data from batchSchemaService
	 */
	public void readRegex(final BatchInstanceID batchInstanceID) throws DCMAApplicationException, DCMAException {
		String regexSwitch = pluginPropertiesService.getPropertyValue(batchInstanceID.getID(), KV_PAGE_PROCESS_PLUGIN,
				RegexProperties.KVPAGEPROCESS_SWITCH);
		if (regexSwitch.equalsIgnoreCase(ON_SWTICH)) {
			String errMsg = null;
			String batchInstanceIdentifier = batchInstanceID.getID();
			LOGGER.info("Initializing......");
		
			String maxValue = pluginPropertiesService.getPropertyValue(batchInstanceID.getID(), KV_PAGE_PROCESS_PLUGIN,
					RegexProperties.KVPAGEPROCESS_MAX_RESULT_COUNT);
			int maxVal = MAX_VAL_CONST;
			try {
				maxVal = Integer.parseInt(maxValue);
			} catch (NumberFormatException e) {
				LOGGER.info("Cannot parse max result count. Using default value 10 for max results.");
			}
			LOGGER.info("Properties Initialized Successfully");

			if (null == batchInstanceIdentifier) {
				errMsg = "Invalid batchInstanceIdentifier.";
				LOGGER.error(errMsg);
				throw new DCMAApplicationException(errMsg);
			}

			final Map<String, List<InputDataCarrier>> mapCarrier = new HashMap<String, List<InputDataCarrier>>();

			retrieveInputCarrierList(batchInstanceID, mapCarrier);

			List<OutputDataCarrier> outputDataCarriers = new ArrayList<OutputDataCarrier>();

			final Batch batch = batchSchemaService.getBatch(batchInstanceIdentifier);

			if (batch == null) {
				errMsg = "Invalid batch.";
				LOGGER.error(errMsg);
				throw new DCMAApplicationException(errMsg);
			}

			List<Document> documentTypeList = batch.getDocuments().getDocument();
			for (Document document : documentTypeList) {
				Pages pages = document.getPages();
				if (null == pages) {
					continue;
				}
				List<Page> pageList = pages.getPage();
				if (null == pageList || pageList.isEmpty()) {
					continue;
				}

				for (Page page : pageList) {

					if (null == page) {
						continue;
					}

					final HocrPages hocrPages = batchSchemaService.getHocrPages(batchInstanceID, page.getIdentifier());

					if (hocrPages == null) {
						errMsg = "Invalid hocrPages.";
						LOGGER.error(errMsg);
						throw new DCMAApplicationException(errMsg);
					}

					List<HocrPage> hocrPageList = hocrPages.getHocrPage();

					if (hocrPageList == null) {
						errMsg = "Invalid hocrPageList.";
						LOGGER.error(errMsg);
						throw new DCMAApplicationException(errMsg);
					}
					HocrPage hocrPage = hocrPageList.get(RegexPPConstant.ZERO);

					for (String string : mapCarrier.keySet()) {
						List<InputDataCarrier> inputDataCarriersForDesc = mapCarrier.get(string);
						outputDataCarriers = kvFinderService.findKeyValue(inputDataCarriersForDesc, hocrPage, null, null, maxVal);
						Boolean isSuccessful = updateBatchXML(page, outputDataCarriers, batchInstanceID, string);
						if (!isSuccessful) {
							errMsg = ("Error in upadting the page in batch.xml" + page);
							LOGGER.error(errMsg);
							throw new DCMAApplicationException(errMsg);
						}
					}
				}
			}

			batchSchemaService.updateBatch(batch);

			LOGGER.info("Update Sucessfully." + batchInstanceIdentifier);
		} else {
			LOGGER.info("Skipping regex classification. Switch set as OFF");
		}
	}

	/**
	 * This method is used to create the input data carriers from the KVPageProcess object and stored it on the map.
	 * 
	 * @param kvPageProcess {@link KVPageProcess}
	 * @param mapCarrier Map<String, List<InputDataCarrier>>
	 * @return inputDataCarrier
	 */
	private void createInputData(final KVPageProcess kvPageProcess, final Map<String, List<InputDataCarrier>> mapCarrier) {
		if (kvPageProcess != null && mapCarrier != null) {
			InputDataCarrier inputDataCarrier = new InputDataCarrier(kvPageProcess.getLocationType(), kvPageProcess.getKeyPattern(),
					kvPageProcess.getValuePattern(), kvPageProcess.getNoOfWords());
			String description = kvPageProcess.getPageLevelFieldName();
			if (null != description && !description.isEmpty()) {
				List<InputDataCarrier> inputDataCarrierList = mapCarrier.get(description);
				if (null == inputDataCarrierList) {
					inputDataCarrierList = new ArrayList<InputDataCarrier>();
				}
				inputDataCarrierList.add(inputDataCarrier);
				mapCarrier.put(description, inputDataCarrierList);
			}
		}
	}

}
