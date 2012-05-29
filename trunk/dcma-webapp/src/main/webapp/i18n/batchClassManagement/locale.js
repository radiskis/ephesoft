/**
 * ******************************* Default locale
 * **************************************************
 */
var batchClassManagementConstants = {

	delete_document_title : "Delete Document Type",

	delete_field_title : "Delete Field Type",

	delete_function_key : "Delete Function Key",
	
	edit_function_key : "Edit Function Key",

	delete_page_title : "Delete Page Type",

	delete_kv_title : "Delete Key Value Pair",

	delete_regex_title : "Delete Regex",

	name : "Name",

	key_name : "Key",

	method_name : "Method Name",

	description : "Description",

	minimum_confidence_threshold : "Minimum Confidence Threshold",

	recostar_project_file : "Form Processing Project File",

	priority : "Priority",

	data_type : "Data Type",

	pattern : "Pattern",

	field_order : "Field Order",

	key_pattern : "Key Pattern",

	value_pattern : "Value Pattern",

	location : "Location",

	unc_folder : "UNC Folder",

	version : "Version",

	priority_label : "Priority Label",

	sequence_number : "Sequence Number",

	type : "Type",

	document_name : "Document Name",

	mapped_to : "Mapped To",

	select_option : "Select",

	username : "Username",

	password : "Password",

	server_name : "Server Name",

	server_type : "Server Type",

	folder_name : "Folder Name",

	delete_email_configuration_title : "Delete email configuration",

	value : "Value",

	confidence : "Confidence Score",

	coordinates : "Coordinates",

	page_name : "Page Name",

	sample_value : "Sample Value",

	field_option_value_list : "Field Option Value List",

	role : "Role",

	no_of_words : "No of words",

	start_pattern : "Start Pattern",

	end_pattern : "End Pattern",

	between_left : "Between Left",

	between_right : "Between Right",

	column_name : "Column Name",

	column_pattern : "Column Pattern",

	delete_table_title : "Delete Table Info",

	delete_table_column_info_title : "Delete Table Column Info",

	barcode_type : "Barcode Type",

	delete_batch_class_title : "Delete batch class",

	fetch_value : "Fetch Value",

	length_label : "Length",

	width_label : "Width",

	xoffset_label : "X-Offset",

	yoffset_label : "Y-Offset",

	multiplier_label : "Multiplier",

	capture_key_button : "Capture Key",

	capture_value_button : "Capture Value",

	clear_button : "Clear",

	continue_conformation : "Continue",

	name : "Name",

	description : "Description",

	type : "Type",

	field_order : "Field Order",

	sample_value : "Sample Value",

	validation_pattern : "Validation Pattern",

	option_value_list : "Field Option Value List",

	edit_document_title : "Edit Document Type",

	copy_document_title : "Copy Document Type",

	edit_module_title : "Edit Module Type",

	edit_email_configuration_title : "Edit Email Configuration",

	edit_batch_class_field_title : "Edit Batch Class Field",

	add_batch_class_field_title : "Add Batch Class Field",

	delete_batch_class_field_title : "Delete Batch Class Field",

	kv_page_level_field_name : "Field Name",

	is_ssl : "Is SSL",

	port_number : "Port Number",

	add_email_configuration : "Add Email Configuration",

	is_required : "Required",
		
	is_hidden : "Hidden",
	
	is_multiline :"MultiLine",
	
	key_warning : "Press key F1 to F11 (except F5)",
	
	delete_regex_validation : "Delete Regex Validation",
		
	add_regex_validation : "Add Regex Validation",
	
	remote_url : "Remote URL",
	
	test_adv_kv : "Test Adv KV",
	
	remote_bc_identifier : "Remote Batch Class Identifier",
	
	kv_page_value : "Page Value",
	
	warning_title : "Warning"

};
var batchClassManagementMessages = {

	delete_document_type_conformation : "Are you sure you want to delete this document type?",

	delete_field_type_conformation : "Are you sure you want to delete this field type?",

	delete_function_key_conformation : "Are you sure you want to delete this function key?",

	delete_page_type_conformation : "Are you sure you want to delete this page type?",

	delete_kv_type_conformation : "Are you sure you want to delete this key value pair?",

	delete_regex_type_conformation : "Are you sure you want to delete this Regular Expression?",

	blank_error : "Mandatory fields cannot be blank.",

	integer_error : "Integer expected at",

	float_error : "Float Expected at",

	number_error : "Number Expected at",

	field_order_duplicate_error : "Field order {0} already exists.",

	name_common_error : "Name cannot be same.",

	add_field_type : "Add Field Type First.",

	add_document_type : "Add Document Type First.",

	no_record_found : "No Record Found.",

	none_selected_warning : "Select a row first.",

	not_editable_warning : "Row is not editable.",

	not_mapped : "Not Mapped yet.",

	no_record_to_delete : "No record to delete.",

	no_record_to_edit : "No record to edit.",
	
	no_table_to_test : "No table to test.",

	delete_email_configuration_conformation : "Are you sure you want to delete this email configuration?",

	already_exists_error : "Entered value already exists.",

	apply_successful : "Changes applied successfully.",

	save_sucessful : "Changes saved successfully.",

	success : "Successful",

	delete_table_info_conformation : "Are you sure you want to delete this table info configuration?",

	add_table_info : "Add table Info First.",

	delete_table_column_info_conformation : "Are you sure you want to delete this table column info configuration?",

	delete_batch_class_conformation : "Are you sure you want to delete this batch class?",

	delete_success_title : "Delete Sucessful",

	delete_success_message : "Batch Class deleted Successfully",

	delete_success_message : "Batch Class deleted Sucessfully",

	error_retrieving_path : "Error occured while retrieving path. Check settings and try again",

	mouse_not_click_error : "Please click again to finalize the area. Then only will it be captured.",

	key_not_final_error : "Key not finalized. Finalize key first.",

	multiplier_error : "Multiplier should be between 0 and 1",

	data_loss : "Proceeding may lead in data loss. Are you sure you want to proceed?",

	advanced_kv_error : "Please select an image and capture the key and value areas. Click cancel to exit.",

	delete_batch_class_field_conformation : "Are you sure you want to delete this batch class field?",

	no_record_to_copy : "No record to copy.",
	
	no_record_to_export : "No record to export.",

	error_upload_image : "Error uploading image.Please try again.",
	
	delete_regex_validation_confirmation : "Making field type hidden or multiline will delete Regex Validation for document level field. Are you sure you want to proceed?",
		
	add_regex_failure : "Regex Validation cannot be added to hidden or multiline document level field.",
	
    mandatory_fields_cannot_be_blank : "Mandatory fields cannot be blank",

	remote_batch_class_identifier_cannot_be_empty : "Remote batch class identifier cannot be empty",

	error_while_retaining_the_batch_list_priority : "Error while retaining the batch list priority",

	remote_url_cannot_be_empty : "Remote urls cannot be empty",

	all_details_are_necessary : "All details are necessary",

	incomplete_details : "Incomplete details",

	key_is_not_allowed : "Key is not allowed",

	unable_to_determine_location : "Unable to determine location",

	value_overlaps_with_key : "Value overlaps with key",
		
	invalid_regex_pattern: "Invalid regular expression",
	
	cant_delete_own_role: "You can’t unmap the role: '{0}' assigned to you by the admin.",
	
	no_unc_folder_exists: "No Batch Classes found associated with the logged in user.",
	
	unsaved_data_will_lost: "All unsaved changes will be lost.Do you want to continue?"

};
/** ************************** Turkish locale (suffix: _tk)****************** */
var batchClassManagementConstants_tk = {

	delete_document_title : "Belge sil",

	delete_field_title : "alan silme",

	delete_function_key : "İşlev Tuşu sil",
	
	edit_function_key : "İşlev Tuşu Düzenle",

	delete_page_title : "Sayfayı Sil",

	delete_kv_title : "Anahtar Değer sil",

	delete_regex_title : "Sil Regex",

	name : "isim",

	key_name : "anahtar",

	method_name : "Metod Adı",

	description : "tanım",

	minimum_confidence_threshold : "Minimum Güven Eşik",

	recostar_project_file : "Form İşleme Proje Dosyası",

	priority : "öncelik",

	data_type : "Veri Türü",

	pattern : "kalıp",

	key_pattern : "Anahtar Desen",

	value_pattern : "Değer Desen",

	location : "yer",

	unc_folder : "UNC Klasör",

	version : "versiyon",

	priority_label : "Öncelik Etiket",

	sequence_number : "Sıra Numarası",

	type : "tip",

	document_name : "Doküman Adı",

	mapped_to : "için eşlenen",

	select_option : "seçmek",

	username : "Kullanıcı Adı",

	password : "parola",

	server_name : "sunucu Adı",

	server_type : "sunucu Türü",

	folder_name : "Klasör Adı",

	delete_email_configuration_title : "E-posta yapılandırma sil",

	value : "değer",

	confidence : "Güven Puan",

	coordinates : "Koordinatlar",

	page_name : "Sayfa Adı",

	sample_value : "Örnek Değer",

	field_option_value_list : "Alan Seçenek Değer Listesi",

	role : "rol",

	no_of_words : "Kelime",

	barcode_type : "Barkod Tipi",

	start_pattern : "Yıldız Desen",

	end_pattern : "Sona Desen",

	between_left : "Sol arasında",

	between_right : "sağ Arasında",

	column_name : "Sütun Adı",

	column_pattern : "Sütun deseni",

	delete_table_title : "Masa Bilgisi Sil",

	delete_table_column_info_title : "Tablo Sütunu Bilgisi Sil",

	delete_batch_class_title : "Toplu sınıf sil",

	fetch_value : "Değer getir",

	length_label : "uzunluk",

	width_label : "genişlik",

	xoffset_label : "X-Ofset",

	yoffset_label : "Y-Ofset",

	multiplier_label : "çarpan",

	capture_key_button : "Anahtar yakalayın",

	capture_value_button : "Düğme Yakalama",

	clear_button : "açık",

	continue_conformation : "devam etmek",

	name : "isim",

	description : "tanım",

	type : "tip",

	field_order : "Alan sırası",

	sample_value : "Örnek Değer",

	validation_pattern : "Doğrulama Desen",

	option_value_list : "Alan Seçenek Değer Listesi",

	edit_document_title : "Belge Türü Düzenle",

	copy_document_title : "Kopyalama Belge Türü",

	edit_module_title : "Modül Tipi Düzenle",

	edit_email_configuration_title : "E-posta Konfigürasyon Düzenle",

	edit_batch_class_field_title : "Düzen Toplu Sınıf Alan",

	add_batch_class_field_title : "Toplu Sınıf Alan Ekleyin",

	delete_batch_class_field_title : "Toplu Sınıf Alan Sil",

	kv_page_level_field_name : "Alan Adı",

	is_ssl : "SSL",

	port_number : "port Numarası",

	add_email_configuration : "E-posta Konfigürasyon ekle",

	is_required : "gereken",
	
	is_hidden : "gizli",
	
	is_multiline :"Çok satırlı iskonto",
	
	key_warning : "F11 (F5 dışında F1 tuşuna basın.)",
	
	delete_regex_validation : "Regex Doğrulama Sil",
		
	add_regex_validation : "Regex Doğrulama ekle",
	
	remote_url : "uzaktan URL",
		
	remote_bc_identifier : "Uzaktan Toplu Sınıfı Tanımlayıcı",
	
	test_adv_kv : "Testi İlan KV",
	
	kv_page_value : "Sayfa Değeri",
	
	warning_title : "Uyarı"

};

var batchClassManagementMessages_tk = {

	delete_document_type_conformation : "Eğer bu belge türünü silmek istediğiniz emin misiniz?",

	delete_field_type_conformation : "Eğer bu alan türü silmek istediğiniz emin misiniz?",

	delete_function_key_conformation : "Bu fonksiyon tuşu Silmek istediğiniz emin misiniz?",

	delete_page_type_conformation : "Bu sayfa türünü silmek istediğinizden emin misiniz?",

	delete_kv_type_conformation : "Bu anahtar değer çiftini silmek istediğinizden emin misiniz?",

	delete_regex_type_conformation : "Bu Regex Doğrulama silmek istediğinizden emin mısınız?",

	blank_error : "Zorunlu alanlar boş bırakılamaz.",

	integer_error : "Tamsayı beklenen",

	float_error : "Beklenen Float",

	number_error : "Number Beklenen",

	field_order_duplicate_error : "Alan sırası {0} zaten var.",

	name_common_error : "Adı aynı olamaz.",

	add_field_type : "Öncelikle Alan Türü ekleyin.",

	add_document_type : "Öncelikle Belge Türü ekleyin.",

	no_record_found : "Kayıt bulunamadı.",

	none_selected_warning : "Önce bir satır seçin.",

	not_editable_warning : "Satır düzenlenebilir değildir.",

	not_mapped : "Henüz eşlenen değil.",

	no_record_to_delete : "Hiçbir kayıt silmek için.",

	no_record_to_edit : "Hiçbir kayıt düzenlemek için.",
	
	no_table_to_test : "Yok masa testi.",

	delete_email_configuration_conformation : "Bu e-posta yapılandırma silmek istediğinizden emin misiniz?",

	already_exists_error : "Girilen değeri zaten var.",

	apply_successful : "Değişiklikler başarıyla uygulandı.",

	save_sucessful : "Değişiklikler başarıyla kaydedildi.",

	success : "başarılı",

	delete_table_info_conformation : "Bu tablo bilgileri yapılandırmasını silmek istediğinizden emin mısınız?",

	add_table_info : "İlk tablo Ekle.",

	delete_table_column_info_conformation : "Bu tablo sütunu bilgi yapılandırmasını silmek istediğinizden emin mısınız?",

	delete_batch_class_conformation : "Bu toplu iş sınıfı silmek istediğiniz emin misiniz?",

	delete_success_title : "Başarılı Sil",

	delete_success_message : "Toplu sınıfı başarıyla silindi",

	error_retrieving_path : "Yol alınırken hata oluştu. Ayarlarını kontrol edin ve tekrar deneyin",

	mouse_not_click_error : "Alan sonuçlandırmak için tekrar tıklayınız. Sonra sadece Çekilecek.",

	key_not_final_error : "Key sonuçlandırılır. Ilk önemli Finalize.",

	multiplier_error : "Çarpan 0 ve 1 arasında olmalıdır",

	data_loss : "Bildiri veri kaybına yol açabilir. Devam etmek istediğiniz emin misiniz?",

	advanced_kv_error : "Lütfen bir görüntü seçin ve anahtar ve değer alanları yakalamak. Çıkmak için iptal tıklayın.",

	delete_batch_class_field_conformation : "Bu toplu sınıf alan silmek istediğiniz emin misiniz?",

	no_record_to_copy : "hiçbir kayıt kopyalamak için.",
	
	no_record_to_export : "Hayır rekor ihracat.",

	error_upload_image : "Hata yükleme image.Please tekrar deneyin.",
	
	delete_regex_validation_confirmation : "Böylece alan tipi gizli veya birden çok satırda sil (regex doğrulaması için belge seviyesi. Devam etmek istediğinizden emin misiniz?",
	
	add_regex_failure : "Regex Doğrulama için gizli veya birden çok satırda belge seviyesi.",	

	mandatory_fields_cannot_be_blank : "Zorunlu alanlar boş olamaz",

	remote_batch_class_identifier_cannot_be_empty : "Uzaktan toplu sınıf tanımlayıcısı boş olamaz",

	error_while_retaining_the_batch_list_priority : "Hata toplu listesi önceliği korurken",

	remote_url_cannot_be_empty : "Uzaktan url'ler boş olamaz",

	all_details_are_necessary : "Tüm detaylar gerekli",

	incomplete_details : "eksik bilgi",

	key_is_not_allowed : "Anahtar izin verilmiyor",

	unable_to_determine_location : "Konumunu belirlemek için açılamıyor",

	value_overlaps_with_key : "Tuşu ile Değer örtüşmektedir",
		
	invalid_regex_pattern :"Geçersiz olağan ifade",
	
	cant_delete_own_role:"Bu rolü Eşlemesini Geri Al'ı olamaz: {0} yönetici tarafından atanmış.",
	
	no_unc_folder_exists: "Hayır UNC klasörü geçerli kullanıcı için var.",
	
	unsaved_data_will_lost: "Kaydedilmemiş tüm değişiklikler devam etmek istiyor lost.Do olacak?"

};