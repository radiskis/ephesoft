/*Batch Class Inserts*/
INSERT INTO batch_class(id, creation_date, last_modified, batch_class_description, batch_class_name, priority, process_name, unc_folder, batch_class_version, curr_user, last_modified_by) VALUES (2, now(), now(), 'Tesseract Mail Room', 'TesseractMailRoom', 1, 'invoice', 'C:\\ephesoft-data\\another-monitored-folder', '1.0.0.0', null, null);

/*Batch Class Modules*/

INSERT INTO batch_class_module(id,creation_date,last_modified,order_number,workflow_name,batch_class_id,module_id) VALUES (9,now(),now(),11,'Tesseract_Page_Process_Module',2,1);
INSERT INTO batch_class_module(id,creation_date,last_modified,order_number,workflow_name,batch_class_id,module_id) VALUES (10,now(),now(),1,'Folder_Import_Module',2,2);
INSERT INTO batch_class_module(id,creation_date,last_modified,order_number,workflow_name,batch_class_id,module_id) VALUES (11,now(),now(),21,'Document_Assembler_Module',2,3);
INSERT INTO batch_class_module(id,creation_date,last_modified,order_number,workflow_name,batch_class_id,module_id) VALUES (12,now(),now(),41,'Tesseract_Extraction_Module',2,4);
INSERT INTO batch_class_module(id,creation_date,last_modified,order_number,workflow_name,batch_class_id,module_id) VALUES (13,now(),now(),31,'Review_Document_Module',2,5);
INSERT INTO batch_class_module(id,creation_date,last_modified,order_number,workflow_name,batch_class_id,module_id) VALUES (14,now(),now(),61,'Validate_Document_Module',2,6);
INSERT INTO batch_class_module(id,creation_date,last_modified,order_number,workflow_name,batch_class_id,module_id) VALUES (15,now(),now(),71,'Export_Module',2,8);
INSERT INTO batch_class_module(id,creation_date,last_modified,order_number,workflow_name,batch_class_id,module_id) VALUES (21,now(),now(),51,'Automated_Validation_Module',2,9);

/*Batch Class Plugins*/

INSERT INTO batch_class_plugin(id,creation_date,last_modified,order_number,batch_class_module_id,plugin_id) VALUES (12,now(),now(),71,9,2);
INSERT INTO batch_class_plugin(id,creation_date,last_modified,order_number,batch_class_module_id,plugin_id) VALUES (14,now(),now(),11,11,4);
INSERT INTO batch_class_plugin(id,creation_date,last_modified,order_number,batch_class_module_id,plugin_id) VALUES (19,now(),now(),81,9,9);
INSERT INTO batch_class_plugin(id,creation_date,last_modified,order_number,batch_class_module_id,plugin_id) VALUES (20,now(),now(),51,9,16);
INSERT INTO batch_class_plugin(id,creation_date,last_modified,order_number,batch_class_module_id,plugin_id) VALUES (23,now(),now(),21,12,17);
INSERT INTO batch_class_plugin(id,creation_date,last_modified,order_number,batch_class_module_id,plugin_id) VALUES (25,now(),now(),41,15,18);
INSERT INTO batch_class_plugin(id,creation_date,last_modified,order_number,batch_class_module_id,plugin_id) VALUES (34,now(),now(),51,15,20);
INSERT INTO batch_class_plugin(id,creation_date,last_modified,order_number,batch_class_module_id,plugin_id) VALUES (53,now(),now(),11,9,28);
INSERT INTO batch_class_plugin(id,creation_date,last_modified,order_number,batch_class_module_id,plugin_id) VALUES (54,now(),now(),21,9,27);
INSERT INTO batch_class_plugin(id,creation_date,last_modified,order_number,batch_class_module_id,plugin_id) VALUES (55,now(),now(),31,9,29);
INSERT INTO batch_class_plugin(id,creation_date,last_modified,order_number,batch_class_module_id,plugin_id) VALUES (56,now(),now(),41,9,26);
INSERT INTO batch_class_plugin(id,creation_date,last_modified,order_number,batch_class_module_id,plugin_id) VALUES (57,now(),now(),61,9,25);
INSERT INTO batch_class_plugin(id,creation_date,last_modified,order_number,batch_class_module_id,plugin_id) VALUES (58,now(),now(),91,9,19);
INSERT INTO batch_class_plugin(id,creation_date,last_modified,order_number,batch_class_module_id,plugin_id) VALUES (59,now(),now(),11,10,33);
INSERT INTO batch_class_plugin(id,creation_date,last_modified,order_number,batch_class_module_id,plugin_id) VALUES (60,now(),now(),21,10,31);
INSERT INTO batch_class_plugin(id,creation_date,last_modified,order_number,batch_class_module_id,plugin_id) VALUES (61,now(),now(),11,12,13);
INSERT INTO batch_class_plugin(id,creation_date,last_modified,order_number,batch_class_module_id,plugin_id) VALUES (62,now(),now(),31,12,6);
INSERT INTO batch_class_plugin(id,creation_date,last_modified,order_number,batch_class_module_id,plugin_id) VALUES (63,now(),now(),41,12,36);
INSERT INTO batch_class_plugin(id,creation_date,last_modified,order_number,batch_class_module_id,plugin_id) VALUES (64,now(),now(),11,13,34);
INSERT INTO batch_class_plugin(id,creation_date,last_modified,order_number,batch_class_module_id,plugin_id) VALUES (65,now(),now(),11,14,37);
INSERT INTO batch_class_plugin(id,creation_date,last_modified,order_number,batch_class_module_id,plugin_id) VALUES (66,now(),now(),11,15,30);
INSERT INTO batch_class_plugin(id,creation_date,last_modified,order_number,batch_class_module_id,plugin_id) VALUES (67,now(),now(),21,15,23);
INSERT INTO batch_class_plugin(id,creation_date,last_modified,order_number,batch_class_module_id,plugin_id) VALUES (68,now(),now(),31,15,24);
INSERT INTO batch_class_plugin(id,creation_date,last_modified,order_number,batch_class_module_id,plugin_id) VALUES (69,now(),now(),61,15,3);
INSERT INTO batch_class_plugin(id,creation_date,last_modified,order_number,batch_class_module_id,plugin_id) VALUES (95,now(),now(),101,9,6);
INSERT INTO batch_class_plugin(id,creation_date,last_modified,order_number,batch_class_module_id,plugin_id) VALUES (99,now(),now(),21,11,6);
INSERT INTO batch_class_plugin(id,creation_date,last_modified,order_number,batch_class_module_id,plugin_id) VALUES (103,now(),now(),5,15,6);
INSERT INTO batch_class_plugin(id,creation_date,last_modified,order_number,batch_class_module_id,plugin_id) VALUES (76,now(),now(),11,21,22);
INSERT INTO batch_class_plugin(id,creation_date,last_modified,order_number,batch_class_module_id,plugin_id) VALUES (77,now(),now(),21,21,6);
INSERT INTO batch_class_plugin(id,creation_date,last_modified,order_number,batch_class_module_id,plugin_id) VALUES (91,now(),now(),5,12,39);

/*Batch Class Plugin Configs*/
INSERT INTO batch_class_plugin_config(id,creation_date,last_modified,qualifier,plugin_config_value,batch_class_plugin_id,plugin_config_id) VALUES (37,now(),now(),null,'tif;gif',12,1);
INSERT INTO batch_class_plugin_config(id,creation_date,last_modified,qualifier,plugin_config_value,batch_class_plugin_id,plugin_config_id) VALUES (38,now(),now(),null,'100',12,2);
INSERT INTO batch_class_plugin_config(id,creation_date,last_modified,qualifier,plugin_config_value,batch_class_plugin_id,plugin_config_id) VALUES (39,now(),now(),null,'0',12,3);
INSERT INTO batch_class_plugin_config(id,creation_date,last_modified,qualifier,plugin_config_value,batch_class_plugin_id,plugin_config_id) VALUES (40,now(),now(),null,'CODE39;QR;DATAMATRIX',12,4);
INSERT INTO batch_class_plugin_config(id,creation_date,last_modified,qualifier,plugin_config_value,batch_class_plugin_id,plugin_config_id) VALUES (138,now(),now(),null,'ON',12,121);
INSERT INTO batch_class_plugin_config(id,creation_date,last_modified,qualifier,plugin_config_value,batch_class_plugin_id,plugin_config_id) VALUES (41,now(),now(),null,'100',14,5);
INSERT INTO batch_class_plugin_config(id,creation_date,last_modified,qualifier,plugin_config_value,batch_class_plugin_id,plugin_config_id) VALUES (42,now(),now(),null,'100',14,6);
INSERT INTO batch_class_plugin_config(id,creation_date,last_modified,qualifier,plugin_config_value,batch_class_plugin_id,plugin_config_id) VALUES (43,now(),now(),null,'50',14,7);
INSERT INTO batch_class_plugin_config(id,creation_date,last_modified,qualifier,plugin_config_value,batch_class_plugin_id,plugin_config_id) VALUES (44,now(),now(),null,'25',14,8);
INSERT INTO batch_class_plugin_config(id,creation_date,last_modified,qualifier,plugin_config_value,batch_class_plugin_id,plugin_config_id) VALUES (45,now(),now(),null,'50',14,9);
INSERT INTO batch_class_plugin_config(id,creation_date,last_modified,qualifier,plugin_config_value,batch_class_plugin_id,plugin_config_id) VALUES (46,now(),now(),null,'75',14,10);
INSERT INTO batch_class_plugin_config(id,creation_date,last_modified,qualifier,plugin_config_value,batch_class_plugin_id,plugin_config_id) VALUES (47,now(),now(),null,'50',14,11);
INSERT INTO batch_class_plugin_config(id,creation_date,last_modified,qualifier,plugin_config_value,batch_class_plugin_id,plugin_config_id) VALUES (48,now(),now(),null,'50',14,12);
INSERT INTO batch_class_plugin_config(id,creation_date,last_modified,qualifier,plugin_config_value,batch_class_plugin_id,plugin_config_id) VALUES (49,now(),now(),null,'SearchClassification',14,13);
INSERT INTO batch_class_plugin_config(id,creation_date,last_modified,qualifier,plugin_config_value,batch_class_plugin_id,plugin_config_id) VALUES (59,now(),now(),null,'html',19,23);
INSERT INTO batch_class_plugin_config(id,creation_date,last_modified,qualifier,plugin_config_value,batch_class_plugin_id,plugin_config_id) VALUES (60,now(),now(),null,'1',19,24);
INSERT INTO batch_class_plugin_config(id,creation_date,last_modified,qualifier,plugin_config_value,batch_class_plugin_id,plugin_config_id) VALUES (61,now(),now(),null,'1',19,25);
INSERT INTO batch_class_plugin_config(id,creation_date,last_modified,qualifier,plugin_config_value,batch_class_plugin_id,plugin_config_id) VALUES (62,now(),now(),null,'2',19,26);
INSERT INTO batch_class_plugin_config(id,creation_date,last_modified,qualifier,plugin_config_value,batch_class_plugin_id,plugin_config_id) VALUES (63,now(),now(),null,'50',19,27);
INSERT INTO batch_class_plugin_config(id,creation_date,last_modified,qualifier,plugin_config_value,batch_class_plugin_id,plugin_config_id) VALUES (64,now(),now(),null,'summary',19,28);
INSERT INTO batch_class_plugin_config(id,creation_date,last_modified,qualifier,plugin_config_value,batch_class_plugin_id,plugin_config_id) VALUES (65,now(),now(),null,'50',19,29);
INSERT INTO batch_class_plugin_config(id,creation_date,last_modified,qualifier,plugin_config_value,batch_class_plugin_id,plugin_config_id) VALUES (66,now(),now(),null,'summary',19,30);
INSERT INTO batch_class_plugin_config(id,creation_date,last_modified,qualifier,plugin_config_value,batch_class_plugin_id,plugin_config_id) VALUES (67,now(),now(),null,'name;title',19,31);
INSERT INTO batch_class_plugin_config(id,creation_date,last_modified,qualifier,plugin_config_value,batch_class_plugin_id,plugin_config_id) VALUES (129,now(),now(),null,'ON',19,117);
INSERT INTO batch_class_plugin_config(id,creation_date,last_modified,qualifier,plugin_config_value,batch_class_plugin_id,plugin_config_id) VALUES (130,now(),now(),null,'10',19,118);
INSERT INTO batch_class_plugin_config(id,creation_date,last_modified,qualifier,plugin_config_value,batch_class_plugin_id,plugin_config_id) VALUES (70,now(),now(),null,'tif;gif',20,70);
INSERT INTO batch_class_plugin_config(id,creation_date,last_modified,qualifier,plugin_config_value,batch_class_plugin_id,plugin_config_id) VALUES (110,now(),now(),null,'eng',20,110);
INSERT INTO batch_class_plugin_config(id,creation_date,last_modified,qualifier,plugin_config_value,batch_class_plugin_id,plugin_config_id) VALUES (262,now(),now(),null,' ;:,/\\',23,152);
INSERT INTO batch_class_plugin_config(id,creation_date,last_modified,qualifier,plugin_config_value,batch_class_plugin_id,plugin_config_id) VALUES (84,now(),now(),null,'MM/dd/yyyy',23,80);
INSERT INTO batch_class_plugin_config(id,creation_date,last_modified,qualifier,plugin_config_value,batch_class_plugin_id,plugin_config_id) VALUES (85,now(),now(),null,'5',23,79);
INSERT INTO batch_class_plugin_config(id,creation_date,last_modified,qualifier,plugin_config_value,batch_class_plugin_id,plugin_config_id) VALUES (86,now(),now(),null,'jdbc:mysql://localhost:3306/dcma',23,78);
INSERT INTO batch_class_plugin_config(id,creation_date,last_modified,qualifier,plugin_config_value,batch_class_plugin_id,plugin_config_id) VALUES (87,now(),now(),null,'com.mysql.jdbc.Driver',23,77);
INSERT INTO batch_class_plugin_config(id,creation_date,last_modified,qualifier,plugin_config_value,batch_class_plugin_id,plugin_config_id) VALUES (88,now(),now(),null,'root',23,76);
INSERT INTO batch_class_plugin_config(id,creation_date,last_modified,qualifier,plugin_config_value,batch_class_plugin_id,plugin_config_id) VALUES (89,now(),now(),null,'dcma',23,75);
INSERT INTO batch_class_plugin_config(id,creation_date,last_modified,qualifier,plugin_config_value,batch_class_plugin_id,plugin_config_id) VALUES (90,now(),now(),null,'1',23,73);
INSERT INTO batch_class_plugin_config(id,creation_date,last_modified,qualifier,plugin_config_value,batch_class_plugin_id,plugin_config_id) VALUES (91,now(),now(),null,'1',23,74);
INSERT INTO batch_class_plugin_config(id,creation_date,last_modified,qualifier,plugin_config_value,batch_class_plugin_id,plugin_config_id) VALUES (92,now(),now(),null,'2',23,72);
INSERT INTO batch_class_plugin_config(id,creation_date,last_modified,qualifier,plugin_config_value,batch_class_plugin_id,plugin_config_id) VALUES (93,now(),now(),null,'500',23,81);
INSERT INTO batch_class_plugin_config(id,creation_date,last_modified,qualifier,plugin_config_value,batch_class_plugin_id,plugin_config_id) VALUES (94,now(),now(),null,'50',23,82);
INSERT INTO batch_class_plugin_config(id,creation_date,last_modified,qualifier,plugin_config_value,batch_class_plugin_id,plugin_config_id) VALUES (95,now(),now(),null,'name;title',23,83);
INSERT INTO batch_class_plugin_config(id,creation_date,last_modified,qualifier,plugin_config_value,batch_class_plugin_id,plugin_config_id) VALUES (97,now(),now(),null,'ALLPAGES',23,96);
INSERT INTO batch_class_plugin_config(id,creation_date,last_modified,qualifier,plugin_config_value,batch_class_plugin_id,plugin_config_id) VALUES (8,now(),now(),null,'25',3,8);

INSERT INTO batch_class_plugin_config(id,creation_date,last_modified,qualifier,plugin_config_value,batch_class_plugin_id,plugin_config_id) VALUES (104,now(),now(),null,'EphesoftFinalDropFolder',25,98);
INSERT INTO batch_class_plugin_config(id,creation_date,last_modified,qualifier,plugin_config_value,batch_class_plugin_id,plugin_config_id) VALUES (105,now(),now(),null,'pdf',25,99);
INSERT INTO batch_class_plugin_config(id,creation_date,last_modified,qualifier,plugin_config_value,batch_class_plugin_id,plugin_config_id) VALUES (106,now(),now(),null,'http://cmis.alfresco.com/service/cmis',25,100);
INSERT INTO batch_class_plugin_config(id,creation_date,last_modified,qualifier,plugin_config_value,batch_class_plugin_id,plugin_config_id) VALUES (107,now(),now(),null,'admin',25,101);
INSERT INTO batch_class_plugin_config(id,creation_date,last_modified,qualifier,plugin_config_value,batch_class_plugin_id,plugin_config_id) VALUES (108,now(),now(),null,'admin',25,102);
INSERT INTO batch_class_plugin_config(id,creation_date,last_modified,qualifier,plugin_config_value,batch_class_plugin_id,plugin_config_id) VALUES (109,now(),now(),null,'84ccfe80-b325-4d79-ab4d-080a4bdd045b',25,103);

INSERT INTO batch_class_plugin_config(id,creation_date,last_modified,qualifier,plugin_config_value,batch_class_plugin_id,plugin_config_id) VALUES (112,now(),now(),null,'OFF',25,111);

INSERT INTO batch_class_plugin_config(id,creation_date,last_modified,qualifier,plugin_config_value,batch_class_plugin_id,plugin_config_id) VALUES (146,now(),now(),null,'OFF',34,124);
INSERT INTO batch_class_plugin_config(id,creation_date,last_modified,qualifier,plugin_config_value,batch_class_plugin_id,plugin_config_id) VALUES (147,now(),now(),null,'https://apwest.filebound.com/v5',34,125);
INSERT INTO batch_class_plugin_config(id,creation_date,last_modified,qualifier,plugin_config_value,batch_class_plugin_id,plugin_config_id) VALUES (148,now(),now(),null,'admin',34,126);
INSERT INTO batch_class_plugin_config(id,creation_date,last_modified,qualifier,plugin_config_value,batch_class_plugin_id,plugin_config_id) VALUES (149,now(),now(),null,'admin',34,127);

INSERT INTO batch_class_plugin_config(id,creation_date,last_modified,qualifier,plugin_config_value,batch_class_plugin_id,plugin_config_id) VALUES (224,now(),now(),null,'Ephesoft',34,144);

INSERT INTO batch_class_plugin_config(id,creation_date,last_modified,qualifier,plugin_config_value,batch_class_plugin_id,plugin_config_id) VALUES (171,now(),now(),null,'.png',53,129);
INSERT INTO batch_class_plugin_config(id,creation_date,last_modified,qualifier,plugin_config_value,batch_class_plugin_id,plugin_config_id) VALUES (172,now(),now(),null,'.tif',53,130);

INSERT INTO batch_class_plugin_config(id,creation_date,last_modified,qualifier,plugin_config_value,batch_class_plugin_id,plugin_config_id) VALUES (167,now(),now(),null,'.png',54,138);
INSERT INTO batch_class_plugin_config(id,creation_date,last_modified,qualifier,plugin_config_value,batch_class_plugin_id,plugin_config_id) VALUES (168,now(),now(),null,'.tif',54,139);

INSERT INTO batch_class_plugin_config(id,creation_date,last_modified,qualifier,plugin_config_value,batch_class_plugin_id,plugin_config_id) VALUES (181,now(),now(),null,'ON',55,131);
INSERT INTO batch_class_plugin_config(id,creation_date,last_modified,qualifier,plugin_config_value,batch_class_plugin_id,plugin_config_id) VALUES (182,now(),now(),null,'.png',55,132);
INSERT INTO batch_class_plugin_config(id,creation_date,last_modified,qualifier,plugin_config_value,batch_class_plugin_id,plugin_config_id) VALUES (183,now(),now(),null,'.tif',55,133);
INSERT INTO batch_class_plugin_config(id,creation_date,last_modified,qualifier,plugin_config_value,batch_class_plugin_id,plugin_config_id) VALUES (184,now(),now(),null,'200',55,134);
INSERT INTO batch_class_plugin_config(id,creation_date,last_modified,qualifier,plugin_config_value,batch_class_plugin_id,plugin_config_id) VALUES (185,now(),now(),null,'150',55,135);
INSERT INTO batch_class_plugin_config(id,creation_date,last_modified,qualifier,plugin_config_value,batch_class_plugin_id,plugin_config_id) VALUES (186,now(),now(),null,'200',55,136);
INSERT INTO batch_class_plugin_config(id,creation_date,last_modified,qualifier,plugin_config_value,batch_class_plugin_id,plugin_config_id) VALUES (187,now(),now(),null,'150',55,137);

INSERT INTO batch_class_plugin_config(id,creation_date,last_modified,qualifier,plugin_config_value,batch_class_plugin_id,plugin_config_id) VALUES (161,now(),now(),null,'ON',56,140);
INSERT INTO batch_class_plugin_config(id,creation_date,last_modified,qualifier,plugin_config_value,batch_class_plugin_id,plugin_config_id) VALUES (162,now(),now(),null,'10',56,141);
INSERT INTO batch_class_plugin_config(id,creation_date,last_modified,qualifier,plugin_config_value,batch_class_plugin_id,plugin_config_id) VALUES (163,now(),now(),null,'RMSE',56,142);
INSERT INTO batch_class_plugin_config(id,creation_date,last_modified,qualifier,plugin_config_value,batch_class_plugin_id,plugin_config_id) VALUES (164,now(),now(),null,'10',56,143);

INSERT INTO batch_class_plugin_config(id,creation_date,last_modified,qualifier,plugin_config_value,batch_class_plugin_id,plugin_config_id) VALUES (116,now(),now(),null,'KV_Page_Process',58,114);

INSERT INTO batch_class_plugin_config(id,creation_date,last_modified,qualifier,plugin_config_value,batch_class_plugin_id,plugin_config_id) VALUES (139,now(),now(),null,'ON',58,122);
INSERT INTO batch_class_plugin_config(id,creation_date,last_modified,qualifier,plugin_config_value,batch_class_plugin_id,plugin_config_id) VALUES (140,now(),now(),null,'10',58,123);

INSERT INTO batch_class_plugin_config(id,creation_date,last_modified,qualifier,plugin_config_value,batch_class_plugin_id,plugin_config_id) VALUES (125,now(),now(),null,null,59,115);
INSERT INTO batch_class_plugin_config(id,creation_date,last_modified,qualifier,plugin_config_value,batch_class_plugin_id,plugin_config_id) VALUES (126,now(),now(),null,'-compress LZW -density 300',59,116);

INSERT INTO batch_class_plugin_config(id,creation_date,last_modified,qualifier,plugin_config_value,batch_class_plugin_id,plugin_config_id) VALUES (152,now(),now(),null,'YES',59,113);

INSERT INTO batch_class_plugin_config(id,creation_date,last_modified,qualifier,plugin_config_value,batch_class_plugin_id,plugin_config_id) VALUES (155,now(),now(),null,'tif;gif',60,22);
INSERT INTO batch_class_plugin_config(id,creation_date,last_modified,qualifier,plugin_config_value,batch_class_plugin_id,plugin_config_id) VALUES (69,now(),now(),null,'100',61,35);

INSERT INTO batch_class_plugin_config(id,creation_date,last_modified,qualifier,plugin_config_value,batch_class_plugin_id,plugin_config_id) VALUES (255,now(),now(),null,'ON',66,150);

INSERT INTO batch_class_plugin_config(id,creation_date,last_modified,qualifier,plugin_config_value,batch_class_plugin_id,plugin_config_id) VALUES (259,now(),now(),null,'ON',67,151);
INSERT INTO batch_class_plugin_config(id,creation_date,last_modified,qualifier,plugin_config_value,batch_class_plugin_id,plugin_config_id) VALUES (71,now(),now(),null,'C:\\ephesoft-data\\final-drop-folder',67,36);

/*Document Types*/

INSERT INTO document_type(id, creation_date, last_modified, document_type_description, min_confidence_threshold, document_type_name, priority, batch_class_id) VALUES (5,now(),now(),'Unknown',0,'Unknown',0,2);
INSERT INTO document_type(id, creation_date, last_modified, document_type_description, min_confidence_threshold, document_type_name, priority, batch_class_id) VALUES (6,now(),now(),'Application-Checklist',35,'Application-Checklist',0,2);
INSERT INTO document_type(id, creation_date, last_modified, document_type_description, min_confidence_threshold, document_type_name, priority, batch_class_id) VALUES (7,now(),now(),'Workers-Comp-02',35,'Workers-Comp-02',0,2);
INSERT INTO document_type(id, creation_date, last_modified, document_type_description, min_confidence_threshold, document_type_name, priority, batch_class_id) VALUES (8,now(),now(),'US-Invoice-Data',35,'US-Invoice-Data',0,2);
INSERT INTO document_type(id, creation_date, last_modified, document_type_description, min_confidence_threshold, document_type_name, priority, batch_class_id) VALUES (10,now(),now(),'Invoice-Table',35,'Invoice-Table',0,2);
INSERT INTO document_type(id, creation_date, last_modified, document_type_description, min_confidence_threshold, document_type_name, priority, batch_class_id) VALUES (12,now(),now(),'US Invoice',35,'US Invoice',0,2);
 
/*Field Types*/
INSERT INTO field_type(id, creation_date, last_modified, field_data_type, field_type_description, field_type_name, field_order_number, pattern, sample_value, document_type_id) VALUES (1,now(),now(),'DATE','Invoice Date','Invoice Date', 1, 'Invoice;Date:;[0-9]{2}/[0-9]{2}/[0-9]{2,4}', 'dd/mm/yyyy', 6);
INSERT INTO field_type(id, creation_date, last_modified, field_data_type, field_type_description, field_type_name, field_order_number, pattern, sample_value, document_type_id) VALUES (2,now(),now(),'LONG','Part Number','Part Number', 2, '998[0-9]{9}', '998xxxxxxxxx', 6);
INSERT INTO field_type(id, creation_date, last_modified, field_data_type, field_type_description, field_type_name, field_order_number, pattern, sample_value, document_type_id) VALUES (3,now(),now(),'DOUBLE','Invoice Total','Invoice Total', 3, '!Sub;Total;[0-9]{1,3},?[0-9]{1,3}\\S[0-9]{2}','wx.yz',6);
INSERT INTO field_type(id, creation_date, last_modified, field_data_type, field_type_description, field_type_name, field_order_number, pattern, sample_value, document_type_id) VALUES (4,now(),now(),'STRING','State','State', 4, 'CA|NY|AZ|NV|NJ','CA|NY|AZ|NV|NJ',6);
INSERT INTO field_type(id, creation_date, last_modified, field_data_type, field_type_description, field_type_name, field_order_number, pattern, sample_value, document_type_id) VALUES (5,now(),now(),'STRING','City','City', 5, 'Irvine|Paris|Syndey','Irvine|Paris|Syndey' ,6);
INSERT INTO field_type(id, creation_date, last_modified, field_data_type, field_type_description, field_type_name, field_order_number, pattern, sample_value, document_type_id) VALUES (6,now(),now(),'DATE','Invoice Date','Invoice Date', 1, 'Invoice;Date:;[0-9]{2}/[0-9]{2}/[0-9]{2,4}','dd/mm/yyyy' ,7);
INSERT INTO field_type(id, creation_date, last_modified, field_data_type, field_type_description, field_type_name, field_order_number, pattern, sample_value, document_type_id) VALUES (7,now(),now(),'LONG','Part Number','Part Number', 2, '998[0-9]{9}','998xxxxxxxxx', 7);
INSERT INTO field_type(id, creation_date, last_modified, field_data_type, field_type_description, field_type_name, field_order_number, pattern, sample_value, document_type_id) VALUES (8,now(),now(),'DOUBLE','Invoice Total','Invoice Total', 3, '!Sub;Total;[0-9]{1,3},?[0-9]{1,3}\\S[0-9]{2}','wx.yz',7);
INSERT INTO field_type(id, creation_date, last_modified, field_data_type, field_type_description, field_type_name, field_order_number, pattern, sample_value, document_type_id) VALUES (9,now(),now(),'STRING','State','State', 4, 'CA|NY|AZ|NV|NJ','CA|NY|AZ|NV|NJ' ,7);
INSERT INTO field_type(id, creation_date, last_modified, field_data_type, field_type_description, field_type_name, field_order_number, pattern, sample_value, document_type_id) VALUES (10,now(),now(),'STRING','City','City', 5, 'Irvine|Paris|Syndey','Irvine|Paris|Syndey',7);
INSERT INTO field_type(id, creation_date, last_modified, field_data_type, field_type_description, field_type_name, field_order_number, pattern, sample_value, document_type_id) VALUES (11,now(),now(),'DATE','Invoice Date','Invoice Date', 1, 'Invoice;Date:;[0-9]{2}/[0-9]{2}/[0-9]{2,4}','dd/mm/yyyy',8);
INSERT INTO field_type(id, creation_date, last_modified, field_data_type, field_type_description, field_type_name, field_order_number, pattern, sample_value, document_type_id) VALUES (12,now(),now(),'LONG','Part Number','Part Number', 2, '998[0-9]{9}','998xxxxxxxxx',8);
INSERT INTO field_type(id, creation_date, last_modified, field_data_type, field_type_description, field_type_name, field_order_number, pattern, sample_value, document_type_id) VALUES (13,now(),now(),'DOUBLE','Invoice Total','Invoice Total', 3, '!Sub;Total;[0-9]{1,3},?[0-9]{1,3}\\S[0-9]{2}','wx.yz',8);
INSERT INTO field_type(id, creation_date, last_modified, field_data_type, field_type_description, field_type_name, field_order_number, pattern, sample_value, document_type_id) VALUES (14,now(),now(),'STRING','State','State', 4, 'CA|NY|AZ|NV|NJ','CA|NY|AZ|NV|NJ',8);
INSERT INTO field_type(id, creation_date, last_modified, field_data_type, field_type_description, field_type_name, field_order_number, pattern, sample_value, document_type_id) VALUES (15,now(),now(),'STRING','City','City', 5, 'Irvine|Paris|Syndey','Irvine|Paris|Syndey' ,8);
INSERT INTO field_type(id, creation_date, last_modified, field_data_type, field_type_description, field_type_name, field_order_number, pattern, sample_value, document_type_id) VALUES (36,now(),now(),'DATE','Invoice Date','Invoice Date', 1, 'Invoice;Date:;[0-9]{2}/[0-9]{2}/[0-9]{2,4}','dd/mm/yyyy',10);
INSERT INTO field_type(id, creation_date, last_modified, field_data_type, field_type_description, field_type_name, field_order_number, pattern, sample_value, document_type_id) VALUES (37,now(),now(),'LONG','Part Number','Part Number', 2, '998[0-9]{9}','998xxxxxxxxx',10);
INSERT INTO field_type(id, creation_date, last_modified, field_data_type, field_type_description, field_type_name, field_order_number, pattern, sample_value, document_type_id) VALUES (38,now(),now(),'DOUBLE','Invoice Total','Invoice Total', 3, '!Sub;Total;[0-9]{1,3},?[0-9]{1,3}\\S[0-9]{2}','wx.yz',10);
INSERT INTO field_type(id, creation_date, last_modified, field_data_type, field_type_description, field_type_name, field_order_number, pattern, sample_value, document_type_id) VALUES (39,now(),now(),'STRING','State','State', 4, 'CA|NY|AZ|NV|NJ','CA|NY|AZ|NV|NJ',10);
INSERT INTO field_type(id, creation_date, last_modified, field_data_type, field_type_description, field_type_name, field_order_number, pattern, sample_value, document_type_id) VALUES (40,now(),now(),'STRING','City','City', 5, 'Irvine|Paris|Syndey','Irvine|Paris|Syndey' ,10);
INSERT INTO field_type(id, creation_date, last_modified, field_data_type, field_type_description, field_type_name, field_order_number, pattern, sample_value, document_type_id) VALUES (46,now(),now(),'DATE','Invoice Date','Invoice Date', 1, 'Invoice;Date:;[0-9]{2}/[0-9]{2}/[0-9]{2,4}','dd/mm/yyyy',12);
INSERT INTO field_type(id, creation_date, last_modified, field_data_type, field_type_description, field_type_name, field_order_number, pattern, sample_value, document_type_id) VALUES (47,now(),now(),'LONG','Part Number','Part Number', 2, '998[0-9]{9}','998xxxxxxxxx',12);
INSERT INTO field_type(id, creation_date, last_modified, field_data_type, field_type_description, field_type_name, field_order_number, pattern, sample_value, document_type_id) VALUES (48,now(),now(),'DOUBLE','Invoice Total','Invoice Total', 3, '!Sub;Total;[0-9]{1,3},?[0-9]{1,3}\\S[0-9]{2}','wx.yz',12);
INSERT INTO field_type(id, creation_date, last_modified, field_data_type, field_type_description, field_type_name, field_order_number, pattern, sample_value, document_type_id) VALUES (49,now(),now(),'STRING','State','State', 4, 'CA|NY|AZ|NV|NJ','CA|NY|AZ|NV|NJ',12);
INSERT INTO field_type(id, creation_date, last_modified, field_data_type, field_type_description, field_type_name, field_order_number, pattern, sample_value, document_type_id) VALUES (50,now(),now(),'STRING','City','City', 5, 'Irvine|Paris|Syndey','Irvine|Paris|Syndey' ,12);
 
/*Page Type*/
INSERT INTO page_type(id, creation_date, last_modified, page_type_description, page_type_name, document_type_id) VALUES (10,now(),now(),'Application-Checklist_First_Page page','Application-Checklist_First_Page',6);
INSERT INTO page_type(id, creation_date, last_modified, page_type_description, page_type_name, document_type_id) VALUES (11,now(),now(),'Application-Checklist_Middle_Page page','Application-Checklist_Middle_Page',6);
INSERT INTO page_type(id, creation_date, last_modified, page_type_description, page_type_name, document_type_id) VALUES (12,now(),now(),'Application-Checklist_Last_Page page','Application-Checklist_Last_Page',6);
INSERT INTO page_type(id, creation_date, last_modified, page_type_description, page_type_name, document_type_id) VALUES (13,now(),now(),'Workers-Comp-02_First_Page page','Workers-Comp-02_First_Page',7);
INSERT INTO page_type(id, creation_date, last_modified, page_type_description, page_type_name, document_type_id) VALUES (14,now(),now(),'Workers-Comp-02_Middle_Page page','Workers-Comp-02_Middle_Page',7);
INSERT INTO page_type(id, creation_date, last_modified, page_type_description, page_type_name, document_type_id) VALUES (15,now(),now(),'Workers-Comp-02_Last_Page page','Workers-Comp-02_Last_Page',7);
INSERT INTO page_type(id, creation_date, last_modified, page_type_description, page_type_name, document_type_id) VALUES (16,now(),now(),'US-Invoice-Data_First_Page page','US-Invoice-Data_First_Page',8);
INSERT INTO page_type(id, creation_date, last_modified, page_type_description, page_type_name, document_type_id) VALUES (17,now(),now(),'US-Invoice-Data_Middle_Page page','US-Invoice-Data_Middle_Page',8);
INSERT INTO page_type(id, creation_date, last_modified, page_type_description, page_type_name, document_type_id) VALUES (18,now(),now(),'US-Invoice-Data_Last_Page page','US-Invoice-Data_Last_Page',8);
INSERT INTO page_type(id, creation_date, last_modified, page_type_description, page_type_name, document_type_id) VALUES (22,now(),now(),'Invoice-Table_First_Page page','Invoice-Table_First_Page',10);
INSERT INTO page_type(id, creation_date, last_modified, page_type_description, page_type_name, document_type_id) VALUES (23,now(),now(),'Invoice-Table_Middle_Page page','Invoice-Table_Middle_Page',10);
INSERT INTO page_type(id, creation_date, last_modified, page_type_description, page_type_name, document_type_id) VALUES (24,now(),now(),'Invoice-Table_Last_Page page','Invoice-Table_Last_Page',10);
INSERT INTO page_type(id, creation_date, last_modified, page_type_description, page_type_name, document_type_id) VALUES (28,now(),now(),'US Invoice_First_Page page','US Invoice_First_Page',12);
INSERT INTO page_type(id, creation_date, last_modified, page_type_description, page_type_name, document_type_id) VALUES (29,now(),now(),'US Invoice_Middle_Page page','US Invoice_Middle_Page',12);
INSERT INTO page_type(id, creation_date, last_modified, page_type_description, page_type_name, document_type_id) VALUES (30,now(),now(),'US Invoice_Last_Page page','US Invoice_Last_Page',12);



/*Key Value Extraction*/
INSERT INTO kv_extraction(id,creation_date,last_modified,key_pattern,location,value_pattern,field_type_id) VALUES (10,now(),now(),'order','BOTTOM_LEFT','must',10);
INSERT INTO kv_extraction(id,creation_date,last_modified,key_pattern,location,value_pattern,field_type_id) VALUES (11,now(),now(),'Date','RIGHT','[0-9]{2}/[0-9]{2}/[0-9]{2,4}',11);
INSERT INTO kv_extraction(id,creation_date,last_modified,key_pattern,location,value_pattern,field_type_id) VALUES (12,now(),now(),'Part','BOTTOM','998[0-9]{9}',12);
INSERT INTO kv_extraction(id,creation_date,last_modified,key_pattern,location,value_pattern,field_type_id) VALUES (13,now(),now(),'Total','RIGHT','[0-9]{1,3},?[0-9]{1,3}\\S[0-9]{2}',13);
INSERT INTO kv_extraction(id,creation_date,last_modified,key_pattern,location,value_pattern,field_type_id) VALUES (14,now(),now(),'Hills','BOTTOM_LEFT','CA|NY|AZ|NV|NJ',14);
INSERT INTO kv_extraction(id,creation_date,last_modified,key_pattern,location,value_pattern,field_type_id) VALUES (15,now(),now(),'Belverly','BOTTOM','Irvine',15);
INSERT INTO kv_extraction(id,creation_date,last_modified,key_pattern,location,value_pattern,field_type_id) VALUES (16,now(),now(),'completed','TOP_LEFT','Please',16);
INSERT INTO kv_extraction(id,creation_date,last_modified,key_pattern,location,value_pattern,field_type_id) VALUES (17,now(),now(),'checklist','TOP_RIGHT','prior',17);
INSERT INTO kv_extraction(id,creation_date,last_modified,key_pattern,location,value_pattern,field_type_id) VALUES (18,now(),now(),'the','TOP','help',18);
INSERT INTO kv_extraction(id,creation_date,last_modified,key_pattern,location,value_pattern,field_type_id) VALUES (22,now(),now(),'association','BOTTOM','set',22);
INSERT INTO kv_extraction(id,creation_date,last_modified,key_pattern,location,value_pattern,field_type_id) VALUES (23,now(),now(),'order','BOTTOM_LEFT','must',23);
INSERT INTO kv_extraction(id,creation_date,last_modified,key_pattern,location,value_pattern,field_type_id) VALUES (24,now(),now(),'association','BOTTOM','set',24);
INSERT INTO kv_extraction(id,creation_date,last_modified,key_pattern,location,value_pattern,field_type_id) VALUES (28,now(),now(),'Total','RIGHT','[0-9]{1,3},?[0-9]{1,3}\\S[0-9]{2}',28);
INSERT INTO kv_extraction(id,creation_date,last_modified,key_pattern,location,value_pattern,field_type_id) VALUES (29,now(),now(),'Hills','BOTTOM_LEFT','CA|NY|AZ|NV|NJ',29);
INSERT INTO kv_extraction(id,creation_date,last_modified,key_pattern,location,value_pattern,field_type_id) VALUES (30,now(),now(),'Belverly','BOTTOM','Irvine',30);

/*Identifiers Update*/
update batch_class set identifier = concat('BC',hex(id));
update document_type set identifier = concat('DT',hex(id));
update page_type set identifier = concat('PT',hex(id));
update field_type set identifier = concat('FT',hex(id));

/*Regular Expression inserts*/
INSERT INTO regex_validation(id, creation_date, last_modified, pattern, field_type_id) VALUES (1, now(), now(),'[0-9]{2}/[0-9]{2}/[0-9]{2,4}', 1);
INSERT INTO regex_validation(id, creation_date, last_modified, pattern, field_type_id) VALUES (2, now(), now(),'998[0-9]{9}', 2);
INSERT INTO regex_validation(id, creation_date, last_modified, pattern, field_type_id) VALUES (3, now(), now(),'[0-9]{1,3},?[0-9]{1,3}\\S[0-9]{2}', 3);
INSERT INTO regex_validation(id, creation_date, last_modified, pattern, field_type_id) VALUES (4, now(), now(),'CA|NY|AZ|NV|NJ', 4);
INSERT INTO regex_validation(id, creation_date, last_modified, pattern, field_type_id) VALUES (5, now(), now(),'Irvine|Paris|Syndey', 5);
INSERT INTO regex_validation(id, creation_date, last_modified, pattern, field_type_id) VALUES (6, now(), now(),'[0-9]{2}/[0-9]{2}/[0-9]{2,4}', 6);
INSERT INTO regex_validation(id, creation_date, last_modified, pattern, field_type_id) VALUES (7, now(), now(),'998[0-9]{9}', 7);
INSERT INTO regex_validation(id, creation_date, last_modified, pattern, field_type_id) VALUES (8, now(), now(),'[0-9]{1,3},?[0-9]{1,3}\\S[0-9]{2}', 8);
INSERT INTO regex_validation(id, creation_date, last_modified, pattern, field_type_id) VALUES (9, now(), now(),'CA|NY|AZ|NV|NJ', 9);
INSERT INTO regex_validation(id, creation_date, last_modified, pattern, field_type_id) VALUES (10, now(), now(),'Irvine|Paris|Syndey', 10);
INSERT INTO regex_validation(id, creation_date, last_modified, pattern, field_type_id) VALUES (11, now(), now(),'[0-9]{2}/[0-9]{2}/[0-9]{2,4}', 11);
INSERT INTO regex_validation(id, creation_date, last_modified, pattern, field_type_id) VALUES (12, now(), now(),'998[0-9]{9}', 12);
INSERT INTO regex_validation(id, creation_date, last_modified, pattern, field_type_id) VALUES (13, now(), now(),'[0-9]{1,3},?[0-9]{1,3}\\S[0-9]{2}', 13);
INSERT INTO regex_validation(id, creation_date, last_modified, pattern, field_type_id) VALUES (14, now(), now(),'CA|NY|AZ|NV|NJ', 14);
INSERT INTO regex_validation(id, creation_date, last_modified, pattern, field_type_id) VALUES (15, now(), now(),'Irvine|Paris|Syndey', 15);
INSERT INTO regex_validation(id, creation_date, last_modified, pattern, field_type_id) VALUES (36, now(), now(),'[0-9]{2}/[0-9]{2}/[0-9]{2,4}', 36);
INSERT INTO regex_validation(id, creation_date, last_modified, pattern, field_type_id) VALUES (37, now(), now(),'998[0-9]{9}', 37);
INSERT INTO regex_validation(id, creation_date, last_modified, pattern, field_type_id) VALUES (38, now(), now(),'[0-9]{1,3},?[0-9]{1,3}\\S[0-9]{2}', 38);
INSERT INTO regex_validation(id, creation_date, last_modified, pattern, field_type_id) VALUES (39, now(), now(),'CA|NY|AZ|NV|NJ', 39);
INSERT INTO regex_validation(id, creation_date, last_modified, pattern, field_type_id) VALUES (40, now(), now(),'Irvine|Paris|Syndey', 40);
INSERT INTO regex_validation(id, creation_date, last_modified, pattern, field_type_id) VALUES (46, now(), now(),'[0-9]{2}/[0-9]{2}/[0-9]{2,4}', 46);
INSERT INTO regex_validation(id, creation_date, last_modified, pattern, field_type_id) VALUES (47, now(), now(),'998[0-9]{9}', 47);
INSERT INTO regex_validation(id, creation_date, last_modified, pattern, field_type_id) VALUES (48, now(), now(),'[0-9]{1,3},?[0-9]{1,3}\\S[0-9]{2}', 48);
INSERT INTO regex_validation(id, creation_date, last_modified, pattern, field_type_id) VALUES (49, now(), now(),'CA|NY|AZ|NV|NJ', 49);
INSERT INTO regex_validation(id, creation_date, last_modified, pattern, field_type_id) VALUES (50, now(), now(),'Irvine|Paris|Syndey', 50);


/*Sample tables/inserts for Fuzzy DB*/
CREATE TABLE us_invoice (id bigint(20) NOT NULL AUTO_INCREMENT, invoice_date datetime DEFAULT NULL,part_number bigint(20) DEFAULT NULL,invoice_total double(20,2) DEFAULT NULL,state varchar(255) DEFAULT NULL,city varchar(255) DEFAULT NULL, PRIMARY KEY (id));
INSERT INTO us_invoice(id, invoice_date, part_number, invoice_total, state, city) VALUES (1,now(),11,100.20,'CA','Irvine');
INSERT INTO us_invoice(id, invoice_date, part_number, invoice_total, state, city) VALUES (2,now(),12,200.20,'CALIFORNIA','APPLICATION');
INSERT INTO us_invoice(id, invoice_date, part_number, invoice_total, state, city) VALUES (3,now(),13,300.20,'expropriation','gurgaon');
 
CREATE TABLE us_invoice_data (id bigint(20) NOT NULL AUTO_INCREMENT, invoice_date datetime DEFAULT NULL, part_number bigint(20) DEFAULT NULL, invoice_total double(20,2) DEFAULT NULL,state varchar(255) DEFAULT NULL, city varchar(255) DEFAULT NULL, PRIMARY KEY (id));
INSERT INTO us_invoice_data(id, invoice_date, part_number, invoice_total, state, city) VALUES (1,now(),11,100.20,'CA','Irvine');
INSERT INTO us_invoice_data(id, invoice_date, part_number, invoice_total, state, city) VALUES (2,now(),12,200.20,'CA','gurgaon');
 
CREATE TABLE application_checklist (id bigint(20) NOT NULL AUTO_INCREMENT,invoice_date datetime DEFAULT NULL, part_number bigint(20) DEFAULT NULL,invoice_total double(20,2) DEFAULT NULL,state varchar(255) DEFAULT NULL, city varchar(255) DEFAULT NULL,PRIMARY KEY (id));
INSERT INTO application_checklist(id, invoice_date, part_number, invoice_total, state, city) VALUES (1,now(),11,100.20,'CALIFORNIA','abc');
INSERT INTO application_checklist(id, invoice_date, part_number, invoice_total, state, city) VALUES (2,now(),12,200.20,'CALIFORNIA','APPLICATION');

/*Sample for vFBValidation*/
CREATE TABLE vFBValidation(file_id bigint(20) NOT NULL AUTO_INCREMENT,loan_number_value VARCHAR(255) NOT NULL,borrow_fullname VARCHAR(255) NOT NULL,cborrow_fullname VARCHAR(255) NOT NULL,PRIMARY KEY (file_id));
INSERT INTO vFBValidation(file_id, loan_number_value, borrow_fullname, cborrow_fullname) VALUES (1,'abc', 'abc,xyz', 'xyz,zbc');

update kv_page_process set no_of_words = 0 where no_of_words is null ;
update kv_extraction set no_of_words = 0 where no_of_words is null;