/* Table Structure Changes */

alter table kv_extraction modify column value_pattern varchar(255) NULL;

alter table kv_extraction modify column multiplier Float;

alter table kv_page_process modify page_level_field_name varchar(100) not null default 'KV_Page_Process';

INSERT INTO plugin(id, creation_date,last_modified,plugin_name,plugin_desc,plugin_version,workflow_name) VALUES (50, now(),now(),'REGULAR_REGEX_EXTRACTION','Regular Regex Extraction Plugin','1.0.0.0','Regular_Regex_Doc_Fields_Extraction_Plugin');

/*ModuleUpdate*/


/*Plugin Update*/

update plugin set plugin_name='SCRIPTING_PLUGIN',plugin_desc='Scripting plugin' where id=6;

update batch_class_plugin set plugin_id=6 where plugin_id=35;
update batch_class_plugin set plugin_id=6 where plugin_id=38;

update plugin set plugin_name='KEY_VALUE_EXTRACTION' where plugin_name='REGEX_EXTRACTION';

/*Plugin Configs Update*/

insert into batch_class_dynamic_plugin_config (id, creation_date, last_modified, config_name, config_desc, plugin_config_value, batch_class_plugin_id, parent_id) select bcpc.id,bcpc.creation_date, bcpc.last_modified, config_name, qualifier, plugin_config_value, batch_class_plugin_id, parent_id from batch_class_plugin_config bcpc, plugin_config pc where bcpc.plugin_config_id=pc.id AND bcpc.qualifier<>'null';

delete from batch_class_plugin_config where qualifier<>'null' and parent_id is not null;
delete from batch_class_plugin_config where qualifier<>'null' and parent_id is null;

delete from plugin_config where config_name in ('document.type', 'field.type', 'row.id');

update plugin_config set config_multivalue=0 where config_name='createMultipageTif.switch';

delete from batch_class_plugin_config where plugin_config_id in (select id from plugin_config where plugin_config.config_name='fuzzydb.query_delimiters');
delete from batch_class_plugin_config where plugin_config_id in (select id from plugin_config where plugin_config.config_name='createMultipageTif.export_process');
delete from batch_class_plugin_config where plugin_config_id in (select id from plugin_config where plugin_config.config_name='createMultipageTif.coloured_pdf');
delete from batch_class_plugin_config where plugin_config_id in (select id from plugin_config where plugin_config.config_name='createMultipageTif.searchable_pdf');
delete from batch_class_plugin_config where plugin_config_id in (select id from plugin_config where plugin_config.config_name='batch.export_to_folder_switch');
delete from batch_class_plugin_config where plugin_config_id in (select id from plugin_config where plugin_config.config_name='createMultipageTif.switch');

delete from batch_class_plugin_config where plugin_config_id in (select id from plugin_config where plugin_config.config_name='barcode.extraction.valid_extensions');
delete from batch_class_plugin_config where plugin_config_id in (select id from plugin_config where plugin_config.config_name='barcode.extraction.reader_types');
delete from batch_class_plugin_config where plugin_config_id in (select id from plugin_config where plugin_config.config_name='barcode.extraction.min_confidence');
delete from batch_class_plugin_config where plugin_config_id in (select id from plugin_config where plugin_config.config_name='barcode.extraction.max_confidence');
delete from batch_class_plugin_config where plugin_config_id in (select id from plugin_config where plugin_config.config_name='barcode.extraction.switch');
delete from batch_class_plugin_config where plugin_config_id in (select id from plugin_config where plugin_config.config_name='validation.validationScriptSwitch');
delete from batch_class_plugin_config where plugin_config_id in (select id from plugin_config where plugin_config.config_name='da.merge_unknown_document_switch');
delete from batch_class_plugin_config where plugin_config_id in (select id from plugin_config where plugin_config.config_name='filebound.switch');
delete from batch_class_plugin_config where plugin_config_id in (select id from plugin_config where plugin_config.config_name='nsi.final_export_folder');
delete from batch_class_plugin_config where plugin_config_id in (select id from plugin_config where plugin_config.config_name='nsi.switch');
delete from batch_class_plugin_config where plugin_config_id in (select id from plugin_config where plugin_config.config_name='nsi.final_xml_name');
delete from batch_class_plugin_config where plugin_config_id in (select id from plugin_config where plugin_config.config_name='NSI.final_export_folder');
delete from batch_class_plugin_config where plugin_config_id in (select id from plugin_config where plugin_config.config_name='NSI.switch');
delete from batch_class_plugin_config where plugin_config_id in (select id from plugin_config where plugin_config.config_name='NSI.final_xml_name');
delete from batch_class_plugin_config where plugin_config_id in (select id from plugin_config where plugin_config.config_name='tesseract.versions');
delete from batch_class_plugin_config where plugin_config_id in (select id from plugin_config where plugin_config.config_name='regular.regex.confidence_score');
delete from batch_class_plugin_config where plugin_config_id in (select id from plugin_config where plugin_config.config_name='regular.regex.extraction_switch');

delete from plugin_config_sample_value where plugin_config_id in (select id from plugin_config where plugin_config.config_name='fuzzydb.query_delimiters');
delete from plugin_config_sample_value where plugin_config_id in (select id from plugin_config where plugin_config.config_name='createMultipageTif.export_process');
delete from plugin_config_sample_value where plugin_config_id in (select id from plugin_config where plugin_config.config_name='createMultipageTif.coloured_pdf');
delete from plugin_config_sample_value where plugin_config_id in (select id from plugin_config where plugin_config.config_name='createMultipageTif.searchable_pdf');
delete from plugin_config_sample_value where plugin_config_id in (select id from plugin_config where plugin_config.config_name='batch.export_to_folder_switch');
delete from plugin_config_sample_value where plugin_config_id in (select id from plugin_config where plugin_config.config_name='createMultipageTif.switch');

delete from plugin_config_sample_value where plugin_config_id in (select id from plugin_config where plugin_config.config_name='barcode.extraction.valid_extensions');
delete from plugin_config_sample_value where plugin_config_id in (select id from plugin_config where plugin_config.config_name='barcode.extraction.reader_types');
delete from plugin_config_sample_value where plugin_config_id in (select id from plugin_config where plugin_config.config_name='barcode.extraction.min_confidence');
delete from plugin_config_sample_value where plugin_config_id in (select id from plugin_config where plugin_config.config_name='barcode.extraction.max_confidence');
delete from plugin_config_sample_value where plugin_config_id in (select id from plugin_config where plugin_config.config_name='barcode.extraction.switch');
delete from plugin_config_sample_value where plugin_config_id in (select id from plugin_config where plugin_config.config_name='validation.validationScriptSwitch');
delete from plugin_config_sample_value where plugin_config_id in (select id from plugin_config where plugin_config.config_name='da.merge_unknown_document_switch');
delete from plugin_config_sample_value where plugin_config_id in (select id from plugin_config where plugin_config.config_name='filebound.switch');
delete from plugin_config_sample_value where plugin_config_id in (select id from plugin_config where plugin_config.config_name='nsi.final_export_folder');
delete from plugin_config_sample_value where plugin_config_id in (select id from plugin_config where plugin_config.config_name='nsi.switch');
delete from plugin_config_sample_value where plugin_config_id in (select id from plugin_config where plugin_config.config_name='nsi.final_xml_name');
delete from plugin_config_sample_value where plugin_config_id in (select id from plugin_config where plugin_config.config_name='NSI.final_export_folder');
delete from plugin_config_sample_value where plugin_config_id in (select id from plugin_config where plugin_config.config_name='NSI.switch');
delete from plugin_config_sample_value where plugin_config_id in (select id from plugin_config where plugin_config.config_name='NSI.final_xml_name');
delete from plugin_config_sample_value where plugin_config_id in (select id from plugin_config where plugin_config.config_name='tesseract.versions');
delete from plugin_config_sample_value where plugin_config_id in (select id from plugin_config where plugin_config.config_name='regular.regex.confidence_score');
delete from plugin_config_sample_value where plugin_config_id in (select id from plugin_config where plugin_config.config_name='regular.regex.extraction_switch');


delete from plugin_config where plugin_config.config_name='fuzzydb.query_delimiters';
delete from plugin_config where plugin_config.config_name='createMultipageTif.export_process';
delete from plugin_config where plugin_config.config_name='createMultipageTif.coloured_pdf';
delete from plugin_config where plugin_config.config_name='createMultipageTif.searchable_pdf';
delete from plugin_config where plugin_config.config_name='batch.export_to_folder_switch';
delete from plugin_config where plugin_config.config_name='createMultipageTif.switch';

delete from plugin_config where plugin_config.config_name='barcode.extraction.valid_extensions';
delete from plugin_config where plugin_config.config_name='barcode.extraction.reader_types';
delete from plugin_config where plugin_config.config_name='barcode.extraction.min_confidence';
delete from plugin_config where plugin_config.config_name='barcode.extraction.max_confidence';
delete from plugin_config where plugin_config.config_name='barcode.extraction.switch';
delete from plugin_config where plugin_config.config_name='validation.validationScriptSwitch';
delete from plugin_config where plugin_config.config_name='da.merge_unknown_document_switch';
delete from plugin_config where plugin_config.config_name='filebound.switch';
delete from plugin_config where plugin_config.config_name='nsi.final_export_folder';
delete from plugin_config where plugin_config.config_name='nsi.switch';
delete from plugin_config where plugin_config.config_name='nsi.final_xml_name';
delete from plugin_config where plugin_config.config_name='NSI.final_export_folder';
delete from plugin_config where plugin_config.config_name='NSI.switch';
delete from plugin_config where plugin_config.config_name='NSI.final_xml_name';
delete from plugin_config where plugin_config.config_name='tesseract.versions';
delete from plugin_config where plugin_config.config_name='regular.regex.confidence_score';
delete from plugin_config where plugin_config.config_name='regular.regex.extraction_switch';

update kv_page_process set page_level_field_name=(select plugin_config_value from batch_class_plugin_config where batch_class_plugin_config.id=kv_page_process.batch_class_plugin_config_id);



INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING','Barcode Extraction Switch',0,'barcode.extraction.switch',null);
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'INTEGER','Barcode Extraction Max Confidence',0,'barcode.extraction.max_confidence',null);
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'INTEGER','Barcode Extraction Min Confidence',0,'barcode.extraction.min_confidence',null);
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING','Barcode Extraction Reader Types',1,'barcode.extraction.reader_types',null);
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING','Barcode Extraction Valid Extensions',1,'barcode.extraction.valid_extensions',null);
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING','Create Multipage Tiff Switch',0,'createMultipageTif.switch',null);
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING','Export To Folder Switch',0,'batch.export_to_folder_switch',null);
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING','Query Delimiters',0,'fuzzydb.query_delimiters',null);

INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING','Multipage File Export Process',0,'createMultipageTif.export_process',null);
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING','Colored Output PDF',0,'createMultipageTif.coloured_pdf',null);
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING','Searchable Output PDF',0,'createMultipageTif.searchable_pdf',null);
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING','Validation Script Switch',0,'validation.validationScriptSwitch',null);

INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING','DA Merge Unknown Document Switch',0,'da.merge_unknown_document_switch',null);

INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING','Regular Regex Extraction Switch',0,'regular.regex.extraction_switch',null);
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING','Regular Regex Confidence Score',0,'regular.regex.confidence_score',null);
INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING','FileBound Switch',0,'filebound.switch',null);

/*Plugin Config Sample Values*/

INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'tif',(select id from plugin_config where config_name='barcode.extraction.valid_extensions'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'gif',(select id from plugin_config where config_name='barcode.extraction.valid_extensions'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'CODE39',(select id from plugin_config where config_name='barcode.extraction.reader_types'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'CODE93',(select id from plugin_config where config_name='barcode.extraction.reader_types'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'CODE128',(select id from plugin_config where config_name='barcode.extraction.reader_types'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'ITF',(select id from plugin_config where config_name='barcode.extraction.reader_types'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'PDF417',(select id from plugin_config where config_name='barcode.extraction.reader_types'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'QR',(select id from plugin_config where config_name='barcode.extraction.reader_types'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'DATAMATRIX',(select id from plugin_config where config_name='barcode.extraction.reader_types'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'CODABAR',(select id from plugin_config where config_name='barcode.extraction.reader_types'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) values (now(),now(),'EAN13',(select id from plugin_config where config_name='barcode.extraction.reader_types'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'ON',(select id from plugin_config where config_name='barcode.extraction.switch'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'OFF',(select id from plugin_config where config_name='barcode.extraction.switch'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'ON',(select id from plugin_config where config_name='createMultipageTif.switch'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'OFF',(select id from plugin_config where config_name='createMultipageTif.switch'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'ON',(select id from plugin_config where config_name='batch.export_to_folder_switch'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'OFF',(select id from plugin_config where config_name='batch.export_to_folder_switch'));

INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'IMAGE_MAGIC',(select id from plugin_config where config_name='createMultipageTif.export_process'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'HOCRtoPDF',(select id from plugin_config where config_name='createMultipageTif.export_process'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'TRUE',(select id from plugin_config where config_name='createMultipageTif.coloured_pdf'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'FALSE',(select id from plugin_config where config_name='createMultipageTif.coloured_pdf'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'TRUE',(select id from plugin_config where config_name='createMultipageTif.searchable_pdf'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'FALSE',(select id from plugin_config where config_name='createMultipageTif.searchable_pdf'));

INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'ON',(select id from plugin_config where config_name='validation.validationScriptSwitch'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'OFF',(select id from plugin_config where config_name='validation.validationScriptSwitch'));

INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'AutomaticClassification',(select id from plugin_config where config_name='da.factory_classification'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'OFF',(select id from plugin_config where config_name='da.merge_unknown_document_switch'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'ON',(select id from plugin_config where config_name='da.merge_unknown_document_switch'));

INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'ON',(select id from plugin_config where config_name='regular.regex.extraction_switch'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'OFF',(select id from plugin_config where config_name='regular.regex.extraction_switch'));

INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'OFF',(select id from plugin_config where config_name='filebound.switch'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'ON',(select id from plugin_config where config_name='filebound.switch'));

INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING','Tesseract Version',0,'tesseract.versions',null);

INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'tesseract_version_3',(select id from plugin_config where config_name='tesseract.versions'));

update batch_class_module, batch_class_plugin, plugin set batch_class_plugin.order_number = 55 where 
batch_class_module.id = batch_class_plugin.batch_class_module_id and plugin.id = batch_class_plugin.plugin_id and 
batch_class_module.workflow_name like '%Extraction%' and plugin.workflow_name = 'Scripting_Plugin';

INSERT INTO plugin (id, creation_date, last_modified, plugin_desc, plugin_name, plugin_version, workflow_name) VALUES (51, now(), now(), 'NSI Export Plugin', 'NSI_EXPORT', '1.0.0.0', 'NSI_Export_Plugin');

INSERT INTO plugin_config (`creation_date`, `last_modified`, `config_datatype`, `config_desc`, `config_multivalue`, `config_name`, `plugin_id`) VALUES (now(), now(), 'STRING', 'NSI Export Folder', '', 'nsi.final_export_folder', NULL);
INSERT INTO plugin_config (`creation_date`, `last_modified`, `config_datatype`, `config_desc`, `config_multivalue`, `config_name`, `plugin_id`) VALUES ( now(), now(), 'STRING', 'NSI State Switch', '', 'nsi.switch', NULL);
INSERT INTO plugin_config (`creation_date`, `last_modified`, `config_datatype`, `config_desc`, `config_multivalue`, `config_name`, `plugin_id`) VALUES (now(), now(), 'STRING', 'Final NSI XML Name', '', 'nsi.final_xml_name', NULL);

INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'ON',(select id from plugin_config where config_name='nsi.switch'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'OFF',(select id from plugin_config where config_name='nsi.switch'));

INSERT INTO module_config (`id`, `creation_date`, `last_modified`, `child_display_name`, `child_key`, `is_mandatory`, `module_id`) VALUES (1, now(), now(), NULL, NULL, 0, NULL);
INSERT INTO module_config (`id`, `creation_date`, `last_modified`, `child_display_name`, `child_key`, `is_mandatory`, `module_id`) VALUES (2, now(), now(), 'Plugin Configuration', 'PluginConfiguration', 0, NULL);
INSERT INTO module_config (`id`, `creation_date`, `last_modified`, `child_display_name`, `child_key`, `is_mandatory`, `module_id`) VALUES (3, now(), now(), 'Email Accounts', 'EmailAccounts', 0, NULL);
INSERT INTO module_config (`id`, `creation_date`, `last_modified`, `child_display_name`, `child_key`, `is_mandatory`, `module_id`) VALUES (4, now(), now(), NULL, NULL, 1, NULL);
INSERT INTO module_config (`id`, `creation_date`, `last_modified`, `child_display_name`, `child_key`, `is_mandatory`, `module_id`) VALUES (5, now(), now(), 'Script Page Processing', 'ScriptPageProcessing', 0, NULL);
INSERT INTO module_config (`id`, `creation_date`, `last_modified`, `child_display_name`, `child_key`, `is_mandatory`, `module_id`) VALUES (6, now(), now(), 'Learn Index', 'LearnIndex', '', NULL);
INSERT INTO module_config (`id`, `creation_date`, `last_modified`, `child_display_name`, `child_key`, `is_mandatory`, `module_id`) VALUES (7, now(), now(), 'Lucene Sample', 'LuceneSample', 1, NULL);
INSERT INTO module_config (`id`, `creation_date`, `last_modified`, `child_display_name`, `child_key`, `is_mandatory`, `module_id`) VALUES (8, now(), now(), 'Image Sample', 'ImageSample', 1, NULL);
INSERT INTO module_config (`id`, `creation_date`, `last_modified`, `child_display_name`, `child_key`, `is_mandatory`, `module_id`) VALUES (9, now(), now(), 'Script Document Assembler', 'ScriptDocumentAssembler', 0, NULL);
INSERT INTO module_config (`id`, `creation_date`, `last_modified`, `child_display_name`, `child_key`, `is_mandatory`, `module_id`) VALUES (10, now(), now(), 'Script Review', 'ScriptReview', 0, NULL);
INSERT INTO module_config (`id`, `creation_date`, `last_modified`, `child_display_name`, `child_key`, `is_mandatory`, `module_id`) VALUES (11, now(), now(), 'Script Extraction', 'ScriptExtraction', 0, NULL);
INSERT INTO module_config (`id`, `creation_date`, `last_modified`, `child_display_name`, `child_key`, `is_mandatory`, `module_id`) VALUES (12, now(), now(), 'Fuzzy-DB Index', 'Fuzzy-DBIndex', 0, NULL);
INSERT INTO module_config (`id`, `creation_date`, `last_modified`, `child_display_name`, `child_key`, `is_mandatory`, `module_id`) VALUES (13, now(), now(), 'Recostar Extraction', 'RecostarExtraction', 0, NULL);
INSERT INTO module_config (`id`, `creation_date`, `last_modified`, `child_display_name`, `child_key`, `is_mandatory`, `module_id`) VALUES (14, now(), now(), 'Script Automatic Validation', 'ScriptAutomaticValidation', 0, NULL);
INSERT INTO module_config (`id`, `creation_date`, `last_modified`, `child_display_name`, `child_key`, `is_mandatory`, `module_id`) VALUES (15, now(), now(), 'Script Validation', 'ScriptValidation', 0, NULL);
INSERT INTO module_config (`id`, `creation_date`, `last_modified`, `child_display_name`, `child_key`, `is_mandatory`, `module_id`) VALUES (16, now(), now(), 'Script Add New Table', 'ScriptAddNewTable', 0, NULL);
INSERT INTO module_config (`id`, `creation_date`, `last_modified`, `child_display_name`, `child_key`, `is_mandatory`, `module_id`) VALUES (17, now(), now(), 'Script FunctionKey', 'ScriptFunctionKey', 0, NULL);
INSERT INTO module_config (`id`, `creation_date`, `last_modified`, `child_display_name`, `child_key`, `is_mandatory`, `module_id`) VALUES (18, now(), now(), 'Script Export', 'ScriptExport', 0, NULL);
INSERT INTO module_config (`id`, `creation_date`, `last_modified`, `child_display_name`, `child_key`, `is_mandatory`, `module_id`) VALUES (19, now(), now(), 'Cmis Mapping', 'CmisMapping', 0, NULL);

INSERT INTO plugin (creation_date, last_modified, plugin_desc, plugin_name, plugin_version, workflow_name) VALUES (now(), now(), 'Barcode Extraction Plugin', 'BARCODE_EXTRACTION', '1.0.0.0', 'BarCode_Extraction_Plugin');

INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING','Recostar color switch',0,'recostar.color_switch',null);

INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'ON',(select id from plugin_config where config_name='recostar.color_switch'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'OFF',(select id from plugin_config where config_name='recostar.color_switch'));

INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING','Tesseract color switch',0,'tesseract.color_switch',null);

INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'ON',(select id from plugin_config where config_name='tesseract.color_switch'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'OFF',(select id from plugin_config where config_name='tesseract.color_switch'));

INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING','Recostar Extarction color switch',0,'recostar_extraction.color_switch',null);

INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'ON',(select id from plugin_config where config_name='recostar_extraction.color_switch'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'OFF',(select id from plugin_config where config_name='recostar_extraction.color_switch'));

INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING','Recostar Auto Rotate switch',0,'recostar.auto_rotate_switch',null);

INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'ON',(select id from plugin_config where config_name='recostar.auto_rotate_switch'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'OFF',(select id from plugin_config where config_name='recostar.auto_rotate_switch'));

INSERT INTO plugin_config(creation_date,last_modified,config_datatype,config_desc,config_multivalue,config_name,plugin_id) VALUES (now(),now(),'STRING','Recostar Auto Rotate switch',0,'recostar_extraction.auto_rotate_switch',null);

INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'ON',(select id from plugin_config where config_name='recostar_extraction.auto_rotate_switch'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'OFF',(select id from plugin_config where config_name='recostar_extraction.auto_rotate_switch'));

delete from plugin_config_sample_value where sample_value = 'gif' and plugin_config_id = (select id from plugin_config where config_name='folderimporter.valid_extensions');

INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'png',(select id from plugin_config where config_name='recostar.valid_extensions'));

INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'png',(select id from plugin_config where config_name='tesseract.valid_extensions'));

INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'com.microsoft.jdbc.sqlserver.SQLServerDriver',(select id from plugin_config where config_name='fuzzydb.database.driver'));

INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'net.sourceforge.jtds.jdbc.Driver',(select id from plugin_config where config_name='fuzzydb.database.driver'));

INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'Fpr_MultiLanguage.rsp',(select id from plugin_config where config_name='recostar.project_file'));

delete from batch_class_plugin_config where plugin_config_id in (select id from plugin_config where plugin_config.config_name='folderimporter.valid_extensions');

delete from batch_class_plugin_config where plugin_config_id in (select id from plugin_config where plugin_config.config_name='tesseract.valid_extensions');

delete from batch_class_plugin_config where plugin_config_id in (select id from plugin_config where plugin_config.config_name='recostar.valid_extensions');

delete from batch_class_plugin_config where plugin_config_id in (select id from plugin_config where plugin_config.config_name='barcode.extraction.switch');

delete from batch_class_plugin_config where plugin_config_id in (select id from plugin_config where plugin_config.config_name='barcode.switch');

delete from batch_class_plugin_config where plugin_config_id in (select id from plugin_config where plugin_config.config_name='imagemagick.save_output_image_parameters');

delete from batch_class_plugin_config where plugin_config_id in (select id from plugin_config where plugin_config.config_name='fuzzydb.stop_words');

delete from plugin_config_sample_value where plugin_config_id in (select id from plugin_config where plugin_config.config_name='fuzzydb.stop_words');

INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'name',(select id from plugin_config where config_name='fuzzydb.stop_words'));
INSERT INTO plugin_config_sample_value(creation_date,last_modified,sample_value,plugin_config_id) VALUES (now(),now(),'title',(select id from plugin_config where config_name='fuzzydb.stop_words'));

drop table batch_class_module_config;

CREATE TABLE batch_class_module_config (
	id BIGINT(20) NOT NULL AUTO_INCREMENT,
	creation_date DATETIME NOT NULL,
	last_modified DATETIME NULL DEFAULT NULL,
	batch_class_module_id BIGINT(20) NULL DEFAULT NULL,
	module_config_id BIGINT(20) NULL DEFAULT NULL,
	PRIMARY KEY (id),
	INDEX `FKE4C5156947189631` (batch_class_module_id),
	INDEX `FKE4C51569E0DA1978` (module_config_id),
	CONSTRAINT `FKE4C5156947189631` FOREIGN KEY (batch_class_module_id) REFERENCES batch_class_module (id),
	CONSTRAINT `FKE4C51569E0DA1978` FOREIGN KEY (module_config_id) REFERENCES module_config (id)
);