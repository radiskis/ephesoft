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
