delete from plugin where id=35;
delete from plugin where id=38;

update kv_page_process set no_of_words = 0 where no_of_words is null ;
update kv_extraction set no_of_words = 0 where no_of_words is null;

ALTER TABLE module ADD UNIQUE (module_name);

ALTER TABLE plugin_config ADD UNIQUE (config_name);

Update batch_class_plugin bcp set bcp.order_number = 15 where bcp.order_number = 51 and bcp.plugin_id in (Select id from plugin plg where plg.plugin_name = "RECOSTAR_HOCR");

Update batch_class_plugin bcp set bcp.order_number = -10 where bcp.order_number = 11 and bcp.plugin_id in (Select id from plugin plg where plg.plugin_name = "RECOSTAR_HOCR");

update batch_class_plugin_config set plugin_config_value = null where plugin_config_value = 'Not Mapped yet.'

update batch_class_plugin_config bcpc, batch_class_plugin bcp, batch_class_module bcm, plugin_config pc set bcpc.plugin_config_value = 'OFF' where bcpc.batch_class_plugin_id = bcp.id and bcp.batch_class_module_id = bcm.id and bcm.workflow_name = 'Recostar_Page_Process_Module' and pc.id = bcpc.plugin_config_id and pc.config_name = 'tesseract.switch';

update batch_class_plugin_config bcpc, batch_class_plugin bcp, batch_class_module bcm, plugin_config pc set bcpc.plugin_config_value = 'OFF' where bcpc.batch_class_plugin_id = bcp.id and bcp.batch_class_module_id = bcm.id and bcm.workflow_name = 'Page_Process_Module_BC4' and pc.id = bcpc.plugin_config_id and pc.config_name = 'tesseract.switch';

update batch_class_plugin bcp set bcp.order_number = 17 where bcp.order_number = 55 and bcp.plugin_id in (Select id from plugin plg where plg.plugin_name = "TESSERACT_HOCR"); 

update batch_class_plugin set order_number = 12 where plugin_id in( select id from plugin where plugin_name = 'CSV_FILE_CREATION_PLUGIN');

update plugin_config set is_mandatory = 0 where config_name = 'validation.url(Ctrl+4)';

update plugin_config set is_mandatory = 0 where config_name = 'validation.url(Ctrl+7)';

update plugin_config set is_mandatory = 0 where config_name = 'validation.url(Ctrl+8)';

update plugin_config set is_mandatory = 0 where config_name = 'validation.url(Ctrl+9)';

update plugin_config set is_mandatory = 0 where config_name = 'validation.url1_title';

update plugin_config set is_mandatory = 0 where config_name = 'validation.url2_title';

update plugin_config set is_mandatory = 0 where config_name = 'validation.url3_title';

update plugin_config set is_mandatory = 0 where config_name = 'validation.url4_title';

update plugin_config set is_mandatory = 0 where config_name = 'imagemagick.open_input_image_parameters';

update plugin_config set is_mandatory = 0 where config_name = 'imagemagick.save_output_image_parameters';

update plugin_config set is_mandatory = 0 where config_name = 'filebound.separator';

update plugin_config set is_mandatory = 0 where config_name = 'filebound.division';

update batch_class_plugin_config set plugin_config_value='HOCRtoPDF' where batch_class_plugin_id = (select id from batch_class_plugin where batch_class_module_id = (select id from batch_class_module where batch_class_id = (select id from batch_class where batch_class_name='SearchablePDFGenerator') and module_id=(select id from module where module_name = 'Export')) and plugin_id =(select id from plugin where plugin_name='CREATEMULTIPAGE_FILES')) and plugin_config_id = (select id from plugin_config where config_name = 'createMultipageTif.export_process');

update plugin_config set config_datatype = 'INTEGER' where config_name = 'regular.regex.confidence_score';

update plugin_config set config_datatype = 'INTEGER' where config_name = 'validation.x_dimension';

update plugin_config set config_datatype = 'INTEGER' where config_name = 'validation.y_dimension';

update plugin_config set config_datatype = 'INTEGER' where config_name = 'validation.fuzzy_search_pop_up_x_dimension';

update plugin_config set config_datatype = 'INTEGER' where config_name = 'validation.fuzzy_search_pop_up_y_dimension';
