package com.parkwoodrx.fastrx.constants;

public class FastRxQueryConstants {

	/* Queries for Pharmacy management */
	public final static String CREATE_USER = "INSERT INTO users(role_id,first_name,last_name,username,password,phone_num,active,pharmacist_license_num,created_by,state_license_num,email) values(?,?,?,?,?,?,?,?,?,?,?)";
	public final static String ADD_PHARMACY_CORPORATION = "INSERT INTO pharmacy_corporation(corporation_name,address,city,state,zip_code,primary_contact_person_firstname,primary_contact_person_lastname,email_addr_for_invoice,phone_num,fax_number,created_by,active) values(?,?,?,?,?,?,?,?,?,?,?,?)";
	// public final static String ADD_BILLING_DETAILS = "INSERT INTO
	// billing_setup(corporation_id,res_charge,req_charge,efax_req_charge,created_by,tax_per_transaction)
	// values(?,?,?,?,?,?)";
	public final static String ADD_BILLING_DETAILS = "INSERT INTO billing_setup(corporation_id,tax_per_month,tax_per_day,transaction_type,transaction_rate_per_day,transaction_rate_per_month,created_by) values(?,?,?,?,?,?,?)";
	public final static String ADD_DEFAULT_BILLING_DETAILS = "INSERT INTO billing_setup(corporation_id,created_by,transaction_type) values(?,?,?)";
	public final static String ADD_PAYMENT_PROFILE = "INSERT INTO creditcard_setup(corporation_id,cust_profile_id,cust_payment_acct_id,active_profile,created_by,card_last_4digit,card_holder_name) values(?,?,?,?,?,?,?)";
	public final static String VALIDATE_CORPORATION = "SELECT COUNT(*) FROM pharmacy_corporation WHERE  corporation_name=?";
	public final static String GET_CORPORATION_LIST = "SELECT id,corporation_name,address,city,state,zip_code,primary_contact_person_firstname,primary_contact_person_lastname,email_addr_for_invoice,payment_type,phone_num,fax_number,created_on,created_by,updated_on,updated_by,active FROM pharmacy_corporation ORDER BY id DESC";
	public final static String GET_CORPORATION_BY_ID = "SELECT id,corporation_name,address,city,state,zip_code,primary_contact_person_firstname,primary_contact_person_lastname,email_addr_for_invoice,payment_type,phone_num,fax_number,created_on,created_by,updated_on,updated_by,active FROM pharmacy_corporation where id=? ";
	public final static String SEARCH_CORPORATION_LIKE_NAME = "SELECT id,corporation_name,address,city,state,zip_code,primary_contact_person_firstname,primary_contact_person_lastname,email_addr_for_invoice,payment_type,phone_num,fax_number,created_on,created_by,updated_on,updated_by,active FROM pharmacy_corporation where corporation_name like ?";
	public final static String SEARCH_CORPORATION_LIKE_PHONE = "SELECT id,corporation_name,address,city,state,zip_code,primary_contact_person_firstname,primary_contact_person_lastname,email_addr_for_invoice,payment_type,phone_num,fax_number,created_on,created_by,updated_on,updated_by,active FROM pharmacy_corporation where phone_num like ?";
	public final static String SEARCH_CORPORATION_LIKE_NAME_AND_PHONE = "SELECT id,corporation_name,address,city,state,zip_code,primary_contact_person_firstname,primary_contact_person_lastname,email_addr_for_invoice,payment_type,phone_num,fax_number,created_on,created_by,updated_on,updated_by,active FROM pharmacy_corporation where corporation_name like ? and phone_num like ?";
	public final static String GET_USER_AND_CORPORATION_BY_ID = "SELECT u.username,pc.id,pc.corporation_name,pc.address,pc.city,pc.state,pc.zip_code,pc.primary_contact_person_firstname,pc.primary_contact_person_lastname,pc.email_addr_for_invoice,pc.payment_type,pc.phone_num,pc.fax_number,pc.active FROM users u JOIN corporation_users cu ON cu.user_id=u.id JOIN pharmacy_corporation pc ON pc.id=cu.corporation_id AND pc.id=? WHERE u.role_id=2 ";
	public final static String GET_BILLING_BY_CORPORATION_ID = "SELECT bs.transaction_type_change_log, bs.start_Day,bs.tax_per_day,bs.transaction_rate_per_day,bs.tax_per_month,bs.transaction_rate_per_month,bs.transaction_type,"
			+ "bs.start_txnType FROM billing_setup bs WHERE bs.corporation_id=?";
	public final static String GET_PAYMENT_DETALS_BY_CORPORATION_ID = "SELECT cc.card_last_4digit,cc.card_holder_name FROM creditcard_setup cc WHERE cc.corporation_id=?";
	public final static String GET_STATE_LIST = "select id,state_code,state_name from state";
	public final static String GET_PAYMENT_TYPE_LIST = "select id,payment_type from payment_type";

	public final static String UPDATE_CORPORATION_DETAILS = "UPDATE pharmacy_corporation SET address=?,city=?,state=?,zip_code=?,primary_contact_person_firstname=?,primary_contact_person_lastname=?,email_addr_for_invoice=?,phone_num=?,fax_number=?,updated_by=?,active=?,updated_on=? where id=? ";
	// public final static String UPDATE_BILLING_DETAILS = "UPDATE billing_setup
	// SET
	// res_charge=?,req_charge=?,efax_req_charge=?,updated_by=?,tax_per_transaction=?,updated_on=?
	// WHERE corporation_id=?";
	public final static String UPDATE_PER_DAY_BILLING_DETAILS = "UPDATE billing_setup SET tax_per_day=?,transaction_type=?,updated_by=?,transaction_rate_per_day=?,updated_on=?,transaction_type_change_log=?,start_Day=?,start_txnType=?  WHERE corporation_id=?";
	public final static String UPDATE_PER_MONTH_BILLING_DETAILS = "UPDATE billing_setup SET tax_per_month=?,transaction_type=?,updated_by=?,transaction_rate_per_month=?,updated_on=?,transaction_type_change_log=?,start_Day=?,start_txnType=?  WHERE corporation_id=?";
	public final static String GET_transaction_type_BY_CORPORATION_ID = "select transaction_type from billing_setup WHERE corporation_id=?";
	public final static String GET_CREDITCARD_COUNT_BY_CORPORATION_ID = "SELECT COUNT(id) from creditcard_setup where corporation_id=?";
	public final static String GET_BILLING_COUNT_BY_CORPORATION_ID = "SELECT COUNT(id) from billing_setup where corporation_id=?";
	public final static String UPDATE_PAYMENT_TYPE = "UPDATE pharmacy_corporation SET payment_type=?,updated_by=?,updated_on=? where id=? ";
	public final static String GET_PAYMENT_PROFILE_BY_CORPORATION_ID = "SELECT cust_payment_acct_id,cust_profile_id from creditcard_setup where corporation_id=?";
	public final static String UPDATE_PAYMENT_PROFILE = "UPDATE creditcard_setup SET card_last_4digit=?,card_holder_name=?,updated_by=?,updated_on=? WHERE corporation_id=?";
	public final static String GET_EFAX_SENT_CHARGE_BY_PRESID = "SELECT res_charge,req_charge,efax_req_charge,tax_per_transaction FROM billing_setup WHERE corporation_id=(SELECT corporation_id FROM pharmacy_locations WHERE id= ( SELECT DISTINCT res_pharmacy_id FROM prescriptions WHERE req_token=?))";
	public final static String UPDATET_EFAX_SENT_CHARGE_BY_PRESID = "UPDATE txn_detail SET efax_charge=?,res_tax_charge=?,updated_on=?,updated_by=? WHERE prescription_id=?";
	public final static String UPDATE_EFAX_STATUS_BY_TOKEN = "UPDATE prescriptions SET efax_sent=?,updated_on=?,updated_by=? WHERE req_token=? AND STATUS=? AND efax_sent=?";
	public final static String GET_PRESCRIPTION_ID_BY_TOKEN = "SELECT id FROM prescriptions WHERE req_token=? AND STATUS=? AND efax_sent=? LIMIT 1";
	public final static String ADD_INVOICE_DETAILS = "INSERT INTO invoice_detail(pharmacy_corp_id,billing_amount,billing_period,txn_ref_id,STATUS,reason,created_by) VALUES(?,?,?,?,?,?,?)";
	/* Queries for User management */
	public final static String GET_USER_BY_USERNAME_PWD = "SELECT id, role_id, first_name, last_name, username,password, phone_num,email,pharmacist_license_num,state_license_num,active, created_on,created_by,first_login,"
			+ "updated_on,updated_by,first_login  FROM users WHERE username=? and password=? and active='Y'";
	public final static String GET_ADMIN_USER_ROLE_BY_ID = "SELECT id, role_name FROM user_role WHERE role_name!='Pharmacist' AND id=?";
	public final static String GET_USER_ROLE_BY_ID = "SELECT id, role_name FROM user_role WHERE id=?";
	public final static String GET_PHARMACY_LOCATION_BY_LOCATION_PIN = "SELECT id, corporation_id, pharmacy_name, state_license_num, ncpdp_num, npi_num, dea_num, location_pin, address,city, "
			+ "state, zip_code, phone_num, fax_num, created_on, created_by,	updated_on, updated_by, active,pharmacy_store_num FROM pharmacy_locations WHERE location_pin=?";
	public final static String GET_PHARMACY_USER_MAPPING = "SELECT corporation_id FROM corporation_users WHERE user_id=?";
	public final static String GET_LOCATION_USER_MAPPING = "SELECT location_id FROM location_users WHERE user_id=?";
	public final static String UPDATE_PWD_FOR_USER = "UPDATE users SET PASSWORD=? WHERE id=?";
	public final static String UPDATE_PWD_FOR_USERNAME = "UPDATE users SET password=?,updated_by=?,updated_on=? WHERE username=?";
	public final static String GET_USER_BY_ID_PWD = "SELECT id, role_id, first_name, last_name, username,password, phone_num,email,pharmacist_license_num,state_license_num,active, created_on,created_by,"
			+ "updated_on,updated_by,first_login  FROM users WHERE id=? and password=? and active='Y'";
	public final static String INSERT_LOCATION_USER_MAPPING = "INSERT INTO location_users (location_id,user_id) VALUES(?,?)";
	public final static String INSERT_CORPORATION_USER_MAPPING = "INSERT INTO corporation_users (corporation_id,user_id) VALUES(?,?)";
	public final static String GET_ALL_USER_ROLE = "SELECT id,role_name,created_on,created_by,updated_on,updated_by FROM user_role";
	public final static String GET_ALL_USER_ROLE_FOR_SUPER_ADMIN = "SELECT id,role_name,created_on,created_by,updated_on,updated_by FROM user_role where role_name NOT IN ('SUPER ADMIN','Pharmacy Corporation Admin')";
	public final static String GET_LOCATION_LIST_FOR_CORPORATION_ID = "SELECT id, corporation_id, pharmacy_name, state_license_num, ncpdp_num, npi_num, dea_num, location_pin, address,city, "
			+ "state, zip_code, phone_num, fax_num, created_on, created_by,	updated_on, updated_by, active,pharmacy_store_num,address_same_as_corp FROM pharmacy_locations WHERE corporation_id=? ORDER BY id DESC";

	public final static String GET_USER_BY_USERNAME = "SELECT id, role_id, first_name, last_name, username,password, phone_num,email,pharmacist_license_num,state_license_num,active, created_on,created_by,"
			+ "updated_on,updated_by,first_login  FROM users WHERE username=?";
	public final static String COUNT_USER = "SELECT COUNT(*) FROM USERS WHERE  USERNAME=?";
	public final static String UPDATE_USER = "UPDATE users SET first_name=?, last_name=?, state_license_num=?, phone_num=?,email=?, updated_by=?, updated_on=? WHERE username=?";
	public final static String ADD_PRESCRIPTION = "INSERT INTO prescriptions (req_corporation_id, res_corporation_id,req_pharmacy_id,res_pharmacy_id,patient_firstname,patient_lastname,patient_dob,"
			+ "patient_address,presc_number,presc_drug_name, req_pharmacy_comments,STATUS,created_by,req_token,iscompound,respTime,resendFlag) "
			+ "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	public final static String GET_ALL_PHARMACISTS_BY_CORPORATIONID = "SELECT u.id,u.role_id,u.first_name,u.last_name,u.username,u.PASSWORD,u.phone_num,u.active,u.pharmacist_license_num,u.created_on,u.created_by,u.updated_on,u.updated_by,u.state_license_num,pc.corporation_name FROM users u JOIN corporation_users cu ON cu.user_id=u.id JOIN pharmacy_corporation pc ON pc.id=cu.corporation_id WHERE u.role_id=4 AND corporation_id=? ORDER BY  u.id DESC";
	public final static String GET_ALL_LOCATION_ADMINS_BY_CORPORATIONID = "SELECT u.id,u.role_id,u.first_name,u.last_name,u.username,u.PASSWORD,u.phone_num,u.active,u.pharmacist_license_num,u.created_on,u.created_by,u.updated_on,u.updated_by,u.state_license_num,pc.corporation_name FROM users u JOIN corporation_users cu ON cu.user_id=u.id JOIN pharmacy_corporation pc ON pc.id=cu.corporation_id WHERE u.role_id=3 AND corporation_id=? ORDER BY  u.id DESC";
	public final static String GET_ALL_PHARMACISTS = "SELECT u.id,u.role_id,u.first_name,u.last_name,u.username,u.PASSWORD,u.phone_num,u.active,u.pharmacist_license_num,u.created_on,u.created_by,u.updated_on,u.updated_by,u.state_license_num,pc.corporation_name FROM users u JOIN corporation_users cu ON cu.user_id=u.id JOIN pharmacy_corporation pc ON pc.id=cu.corporation_id WHERE u.role_id=4 ORDER BY  u.id DESC";
	public final static String GET_ALL_LOCATION_ADMINS = "SELECT u.id,u.role_id,u.first_name,u.last_name,u.username,u.PASSWORD,u.phone_num,u.active,u.pharmacist_license_num,u.created_on,u.created_by,u.updated_on,u.updated_by,u.state_license_num,pc.corporation_name FROM users u JOIN corporation_users cu ON cu.user_id=u.id JOIN pharmacy_corporation pc ON pc.id=cu.corporation_id WHERE u.role_id=3 ORDER BY  u.id DESC";
	public final static String GET_ALL_CORPORATION_ADMINS = "SELECT u.id,u.role_id,u.first_name,u.last_name,u.username,u.PASSWORD,u.phone_num,u.active,u.pharmacist_license_num,u.created_on,u.created_by,u.updated_on,u.updated_by,u.state_license_num,pc.corporation_name FROM users u JOIN corporation_users cu ON cu.user_id=u.id JOIN pharmacy_corporation pc ON pc.id=cu.corporation_id WHERE u.role_id=2 ORDER BY  u.id DESC";

	/* Queries for Location */

	public final static String INSERT_PHARMACY_LOCATION = "INSERT INTO pharmacy_locations(corporation_id,pharmacy_name,state_license_num,ncpdp_num,npi_num,dea_num,location_pin,address,city,state,zip_code,phone_num,fax_num,created_by,active,pharmacy_store_num,address_same_as_corp) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	public final static String UPDATE_PHARMACY_LOCATION = "UPDATE pharmacy_locations SET pharmacy_name=?,state_license_num=?,ncpdp_num=?,npi_num=?,dea_num=?,address=?,city=?,state=?,zip_code=?,fax_num=?,updated_by=?,pharmacy_store_num=?,updated_on=?,address_same_as_corp=? where id = ?";
	public final static String UPDATE_PHARMACY_LOCATION_STATUS = "UPDATE pharmacy_locations SET active=?,updated_on=?,updated_by=? where id = ?";

	public final static String GET_PHARMACY_LOCATION_LIST_BY_CORPORATION_ID = "SELECT id,corporation_id,pharmacy_name,state_license_num,ncpdp_num,npi_num,dea_num,location_pin,address,city,state,zip_code,phone_num,fax_num,active,pharmacy_store_num from pharmacy_locations where corporation_id = ? ORDER BY id DESC";
	public final static String GET_PHARMACY_LOCATION__BY_DEA_NUMBER = "SELECT id,corporation_id,pharmacy_name,state_license_num,ncpdp_num,npi_num,dea_num,created_on,created_by,updated_on,updated_by,location_pin,address,city,state,zip_code,phone_num,fax_num,active,pharmacy_store_num,address_same_as_corp from pharmacy_locations where dea_num =?";
	public final static String GET_PHARMACY_LOCATION_DETAIL = "SELECT id,corporation_id,pharmacy_name,state_license_num,ncpdp_num,npi_num,dea_num,location_pin,address,city,state,zip_code,phone_num,fax_num,created_on,created_by,updated_on,updated_by,active,pharmacy_store_num,address_same_as_corp from pharmacy_locations where id = ?";

	public final static String SEARCH_PHARMACY_LOCATION_BY_NAME_PHONE_CORPORATION = "SELECT pl.id, pl.corporation_id,pc.corporation_name,pl.pharmacy_name, pl.state_license_num, pl.ncpdp_num, pl.npi_num, pl.dea_num, pl.location_pin, pl.address,pl.city,pl.state, pl.zip_code, pl.phone_num, pl.fax_num, pl.created_on, pl.created_by,pl.updated_on, pl.updated_by, pl.active,pl.pharmacy_store_num FROM pharmacy_locations pl INNER JOIN pharmacy_corporation pc ON pl.corporation_id=pc.id  AND pl.pharmacy_name LIKE ? AND pl.phone_num LIKE ? AND pl.corporation_id=?";
	public final static String SEARCH_PHARMACY_LOCATION_BY_NAME_CORPORATION = "SELECT pl.id, pl.corporation_id,pc.corporation_name,pl.pharmacy_name, pl.state_license_num, pl.ncpdp_num, pl.npi_num, pl.dea_num, pl.location_pin, pl.address,pl.city,pl.state, pl.zip_code, pl.phone_num, pl.fax_num, pl.created_on, pl.created_by,pl.updated_on, pl.updated_by, pl.active,pl.pharmacy_store_num FROM pharmacy_locations pl INNER JOIN pharmacy_corporation pc ON pl.corporation_id=pc.id  AND pl.pharmacy_name LIKE ? AND pl.corporation_id=?";
	public final static String SEARCH_PHARMACY_LOCATION_BY_PHONE_CORPORATION = "SELECT pl.id, pl.corporation_id,pc.corporation_name,pl.pharmacy_name, pl.state_license_num, pl.ncpdp_num, pl.npi_num, pl.dea_num, pl.location_pin, pl.address,pl.city,pl.state, pl.zip_code, pl.phone_num, pl.fax_num, pl.created_on, pl.created_by,pl.updated_on, pl.updated_by, pl.active,pl.pharmacy_store_num FROM pharmacy_locations pl INNER JOIN pharmacy_corporation pc ON pl.corporation_id=pc.id  AND pl.phone_num LIKE ? AND pl.corporation_id=? ";
	public final static String CREATE_LOCATION_ADMIN = "INSERT INTO users(role_id,first_name,last_name,username,password,active,created_by,phone_num) values(?,?,?,?,?,?,?,?)";

	public final static String SEARCH_PHARMACY_LOCATION = "SELECT pl.id, pl.corporation_id,pc.corporation_name,pl.pharmacy_name, pl.state_license_num, pl.ncpdp_num, pl.npi_num, pl.dea_num, pl.location_pin, pl.address,pl.city,pl.state, pl.zip_code, pl.phone_num, pl.fax_num, pl.created_on, pl.created_by,pl.updated_on, pl.updated_by, pl.active,pl.pharmacy_store_num FROM pharmacy_locations pl INNER JOIN pharmacy_corporation pc ON pl.corporation_id=pc.id  WHERE pl.pharmacy_name LIKE ? and pl.phone_num LIKE ? and pl.active='Y' and pc.active='Y'";
	public final static String SEARCH_PHARMACY_LOCATION_BY_NAME = "SELECT pl.id, pl.corporation_id,pc.corporation_name,pl.pharmacy_name, pl.state_license_num, pl.ncpdp_num, pl.npi_num, pl.dea_num, pl.location_pin, pl.address,pl.city,pl.state, pl.zip_code, pl.phone_num, pl.fax_num, pl.created_on, pl.created_by,pl.updated_on, pl.updated_by, pl.active,pl.pharmacy_store_num FROM pharmacy_locations pl INNER JOIN pharmacy_corporation pc ON pl.corporation_id=pc.id  AND pl.pharmacy_name LIKE ? and pl.active='Y' and pc.active='Y'";
	public final static String SEARCH_PHARMACY_LOCATION_BY_PHONE = "SELECT pl.id, pl.corporation_id,pc.corporation_name,pl.pharmacy_name, pl.state_license_num, pl.ncpdp_num, pl.npi_num, pl.dea_num, pl.location_pin, pl.address,pl.city,pl.state, pl.zip_code, pl.phone_num, pl.fax_num, pl.created_on, pl.created_by,pl.updated_on, pl.updated_by, pl.active,pl.pharmacy_store_num FROM pharmacy_locations pl INNER JOIN pharmacy_corporation pc ON pl.corporation_id=pc.id  AND pl.phone_num LIKE ? and pl.active='Y' and pc.active='Y'";
	public final static String GET_PHARMACY_LOCATION_BY_ID_CORPID = "SELECT id,corporation_id,pharmacy_name,state_license_num,ncpdp_num,npi_num,dea_num,location_pin,address,city,state,zip_code,phone_num,fax_num,created_on,created_by,updated_on,updated_by,active,pharmacy_store_num,address_same_as_corp from pharmacy_locations where id=? AND corporation_id=?";
	public final static String GET_PHARMACY_LOCATION_BY_ID = "SELECT id,corporation_id,pharmacy_name,state_license_num,ncpdp_num,npi_num,dea_num,location_pin,address,city,state,zip_code,phone_num,fax_num,created_on,created_by,updated_on,updated_by,active,pharmacy_store_num,address_same_as_corp from pharmacy_locations where id=?";
	
	public final static String GET_NON_REGPHARMACY_BY_ID="SELECT id as id, pharmacy_name as pharmacy_name, store_address as address, phy_city as city, phy_state as state, phy_zip as zip_code,phy_phone as phone_num, phy_efax as fax_num, npi as npi_num, dea as dea_num,store_number as store_number FROM non_reg_phar where id=?";
	
	public final static String GET_PHARMACY_LOCATION_LIST = "SELECT pl.id, pl.corporation_id,pc.corporation_name,pl.pharmacy_name, pl.state_license_num, pl.ncpdp_num, pl.npi_num, pl.dea_num, pl.location_pin, pl.address,pl.city,pl.state, pl.zip_code, pl.phone_num, pl.fax_num, pl.created_on, pl.created_by,pl.updated_on, pl.updated_by, pl.active,pl.pharmacy_store_num FROM pharmacy_locations pl INNER JOIN pharmacy_corporation pc ON pl.corporation_id=pc.id ORDER BY  id DESC";
	public final static String GET_PHARMACY_LOCATION_BY_CORPID = "SELECT id,corporation_id,pharmacy_name,state_license_num,ncpdp_num,npi_num,dea_num,location_pin,address,city,state,zip_code,phone_num,fax_num,created_on,created_by,updated_on,updated_by,active,pharmacy_store_num,address_same_as_corp from pharmacy_locations where corporation_id=?";
	public final static String SEARCH_USER_LIKE_USERNAME_AND_PHONE_CORPID = "SELECT u.id,u.role_id,u.first_name,u.last_name,u.username,u.PASSWORD,u.phone_num,u.active,u.pharmacist_license_num,u.created_on,u.created_by,u.updated_on,u.updated_by,u.state_license_num,pc.corporation_name FROM users u JOIN corporation_users cu ON cu.user_id=u.id JOIN pharmacy_corporation pc ON pc.id=cu.corporation_id WHERE u.username like ? and u.phone_num like ?  AND u.role_id=? AND corporation_id=?";
	public final static String SEARCH_USER_LIKE_USERNAME_CORPID = "SELECT u.id,u.role_id,u.first_name,u.last_name,u.username,u.PASSWORD,u.phone_num,u.active,u.pharmacist_license_num,u.created_on,u.created_by,u.updated_on,u.updated_by,u.state_license_num,pc.corporation_name FROM users u JOIN corporation_users cu ON cu.user_id=u.id JOIN pharmacy_corporation pc ON pc.id=cu.corporation_id WHERE u.username like ? AND u.role_id=? AND corporation_id=?";
	public final static String SEARCH_USER_LIKE_PHONE_CORPID = "SELECT u.id,u.role_id,u.first_name,u.last_name,u.username,u.PASSWORD,u.phone_num,u.active,u.pharmacist_license_num,u.created_on,u.created_by,u.updated_on,u.updated_by,u.state_license_num,pc.corporation_name FROM users u JOIN corporation_users cu ON cu.user_id=u.id JOIN pharmacy_corporation pc ON pc.id=cu.corporation_id WHERE u.phone_num like ?  AND u.role_id=? AND corporation_id=?";
	public final static String SEARCH_USER_LIKE_USERNAME_AND_PHONE = "SELECT u.id,u.role_id,u.first_name,u.last_name,u.username,u.PASSWORD,u.phone_num,u.active,u.pharmacist_license_num,u.created_on,u.created_by,u.updated_on,u.updated_by,u.state_license_num,pc.corporation_name FROM users u JOIN corporation_users cu ON cu.user_id=u.id JOIN pharmacy_corporation pc ON pc.id=cu.corporation_id WHERE u.username like ? and u.phone_num like ?  AND u.role_id=?";
	public final static String SEARCH_USER_LIKE_USERNAME = "SELECT u.id,u.role_id,u.first_name,u.last_name,u.username,u.PASSWORD,u.phone_num,u.active,u.pharmacist_license_num,u.created_on,u.created_by,u.updated_on,u.updated_by,u.state_license_num,pc.corporation_name FROM users u JOIN corporation_users cu ON cu.user_id=u.id JOIN pharmacy_corporation pc ON pc.id=cu.corporation_id WHERE u.username like ? AND u.role_id=?";
	public final static String SEARCH_USER_LIKE_PHONE = "SELECT u.id,u.role_id,u.first_name,u.last_name,u.username,u.PASSWORD,u.phone_num,u.active,u.pharmacist_license_num,u.created_on,u.created_by,u.updated_on,u.updated_by,u.state_license_num,pc.corporation_name FROM users u JOIN corporation_users cu ON cu.user_id=u.id JOIN pharmacy_corporation pc ON pc.id=cu.corporation_id WHERE u.phone_num like ?  AND u.role_id=?";
	public final static String UPDATE_LOCATION_ADDRESS = "UPDATE pharmacy_locations SET address=?,city=?,state=?,zip_code=?,updated_by=?,updated_on=? WHERE  pharmacy_locations.corporation_id=? AND address_same_as_corp='Y'";

	public final static String UPDATE_PHARMACY_CORPORATION_STATUS = "UPDATE pharmacy_corporation SET active=?,updated_on=?,updated_by=? where id = ?";
	// public final static String GET_PRISCRIPTIONS_BY_LOCATION = "SELECT id,
	// req_corporation_id, res_corporation_id, req_pharmacy_id, res_pharmacy_id,
	// patient_firstname,patient_lastname, patient_dob, patient_address,
	// presc_number, presc_drug_name, Presc_drug_qty,"
	// + " directions, orig_date_written, date_last_filled, refills_remaining,
	// provider_name, provider_phone_number, provider_npi, provider_dea,
	// req_pharmacy_comments, res_pharmacy_comments, "
	// + " STATUS, created_on, created_by, updated_on, updated_by,
	// efax_sent,req_token FROM prescriptions WHERE (req_pharmacy_id=? OR
	// res_pharmacy_id=?) "
	// + " AND (STATUS='Pending' OR ( CAST(updated_on AS DATE) =
	// CAST(CURRENT_TIMESTAMP AS DATE))) ORDER BY created_on DESC";
	public final static String UPDATE_FAX_STATUS_BY_PRESCRIPTION_ID = "UPDATE prescriptions SET fax_job_id=?,fax_status=?,updated_on=?,updated_by=? WHERE id=?";
	public final static String UPDATE_FAX_STATUS_BY_FAXJOB_ID = "UPDATE prescriptions SET fax_status=?,updated_on=?,updated_by=? WHERE fax_job_id=?";
	public final static String GET_PRISCRIPTIONS_BY_LOCATION = "SELECT id, req_corporation_id, res_corporation_id, req_pharmacy_id, res_pharmacy_id, patient_firstname,patient_lastname, patient_dob,	patient_address, presc_number, presc_drug_name, Presc_drug_qty,"
			+ "	directions, orig_date_written, date_last_filled, refills_remaining, provider_name, provider_phone_number, 	provider_npi, 	provider_dea, req_pharmacy_comments, res_pharmacy_comments, "
			+ " STATUS, created_on, created_by, updated_on, updated_by, efax_sent,req_token,resendFlag,respTime,fax_status,fax_job_id FROM prescriptions WHERE (req_pharmacy_id=? AND created_on > (NOW() - INTERVAL 24 HOUR)) "
			+ " AND (STATUS='Pending' OR ( CAST(updated_on AS DATE) = CAST(CURRENT_TIMESTAMP AS DATE))) ORDER BY created_on DESC";

	public final static String UPDATE_USER_STATUS = "UPDATE users SET active=?,updated_on=?,updated_by=? where id = ?";
	
	public final static String UPDATE_NONREGPHAR_DATABASE = "INSERT INTO non_reg_phar(pharmacy_name,store_address,phy_state,phy_city,phy_zip,phy_efax,phy_phone,phy_ext,dea,npi,store_number) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
	/*
	 * public final static String TRANSFER_OUT_PRESCRIPTION =
	 * "SELECT p.id, p.req_corporation_id, p.res_corporation_id, " +
	 * "p.req_pharmacy_id,p.res_pharmacy_id,p.patient_firstname,p.patient_lastname, p.patient_dob, "
	 * + "p.patient_address, p.presc_number, p.presc_drug_name, " +
	 * "p.presc_drug_qty, p.directions, p.orig_date_written, p.date_last_filled, p.refills_remaining, "
	 * +
	 * "p.provider_name, p.provider_phone_number,p.provider_npi, p.provider_dea, p.req_pharmacy_comments, "
	 * +
	 * "p.res_pharmacy_comments,p.status,p.created_on, p.created_by,p.updated_on, p.updated_by, "
	 * + "req.pharmacy_name AS reqP, req.address AS reqA, req.dea_num AS reqD, "
	 * + "res.pharmacy_name AS resP, res.address AS resA, res.dea_num AS resD, "
	 * +
	 * "reqCorp.corporation_name AS reqCorpName, resCorp.corporation_name AS resCorpName, "
	 * +
	 * "reqUser.first_name AS reqUserFname, reqUser.last_name AS reqUserLname "
	 * +
	 * "FROM prescriptions p JOIN pharmacy_locations req ON req.id=p.req_pharmacy_id "
	 * + "JOIN pharmacy_locations res ON res.id=p.res_pharmacy_id " +
	 * "JOIN pharmacy_corporation reqCorp ON reqCorp.id=req.corporation_id " +
	 * "JOIN pharmacy_corporation resCorp ON resCorp.id=res.corporation_id " +
	 * "JOIN users reqUser ON p.created_by=reqUser.username WHERE p.id=?";
	 */
	public final static String TRANSFER_OUT_PRESCRIPTION = "SELECT p.id, p.req_corporation_id,p.original_fill_date,p.fax_status,p.fax_job_id, "
			+ "p.req_pharmacy_id,p.res_pharmacy_id,p.patient_firstname,p.patient_lastname, p.patient_dob, "
			+ "p.patient_address, p.presc_number, p.presc_drug_name, "
			+ "p.presc_drug_qty, p.directions, p.orig_date_written, p.date_last_filled, p.refills_remaining, "
			+ "p.provider_name, p.provider_phone_number,p.provider_npi, p.provider_dea, p.req_pharmacy_comments, "
			+ "p.res_pharmacy_comments,p.status,p.created_on, p.created_by,p.updated_on, p.updated_by,p.respTime, "
			+ "req.pharmacy_name AS reqP, req.address AS reqA, req.city AS reqC, req.state AS reqS,req.zip_code AS reqZ, req.dea_num AS reqD,req.phone_num AS reqPH,req.fax_num AS reqFN, "
			+ "res.pharmacy_name AS resP, res.store_address AS resA, res.phy_city AS resC, res.phy_state AS resS,res.phy_zip AS resZ, res.dea AS resD, res.phy_phone AS resPH,res.phy_efax AS resFN,res.id AS resId, res.store_number AS resSN,  "
			+ "reqCorp.corporation_name AS reqCorpName,"
			+ "reqUser.first_name AS reqUserFname, reqUser.last_name AS reqUserLname "
			+ "FROM prescriptions p JOIN pharmacy_locations req ON req.id=p.req_pharmacy_id "
			+ "JOIN non_reg_phar res ON res.id=p.res_pharmacy_id "
			+ "JOIN pharmacy_corporation reqCorp ON reqCorp.id=req.corporation_id "
			+ "JOIN users reqUser ON p.created_by=reqUser.username WHERE p.id=?";
	public final static String GET_BILLING_FOR_CORPORATION_ID = "SELECT bs.tax_per_day,bs.transaction_rate_per_day,bs.tax_per_month,bs.transaction_rate_per_month,bs.transaction_type,bs.start_Day,bs.transaction_type_change_log,bs.start_txnType FROM billing_setup bs WHERE bs.corporation_id=?";
	public final static String GET_PRESCRIPTIONS_FOR_REQTOKEN = "SELECT id, req_corporation_id, res_corporation_id, req_pharmacy_id, res_pharmacy_id, patient_firstname,patient_lastname, patient_dob,	patient_address, presc_number, presc_drug_name, Presc_drug_qty,"
			+ "	directions, orig_date_written, date_last_filled, refills_remaining, provider_name, provider_phone_number, 	provider_npi, 	provider_dea, req_pharmacy_comments, res_pharmacy_comments, "
			+ "STATUS, created_on, created_by, updated_on, updated_by, efax_sent,req_token,resendFlag,respTime,fax_status,fax_job_id FROM prescriptions WHERE req_token=?";
	// public final static String INSERT_PRESCRIPTION_TRANSACTION = "INSERT INTO
	// txn_detail (req_corporation_id, res_corporation_id,
	// req_pharmacy_id,res_pharmacy_id,req_charge,
	// req_tax_charge,txn_status,created_by,prescription_id ) VALUES
	// (?,?,?,?,?,?,?,?,?)";
	// public final static String INSERT_PRESCRIPTION_TRANSACTION = "INSERT INTO
	// txn_detail (req_corporation_id, res_corporation_id,
	// req_pharmacy_id,res_pharmacy_id,req_charge, req_tax_charge,res_charge,
	// res_tax_charge,txn_status,created_by,prescription_id ) VALUES
	// (?,?,?,?,?,?,?,?,?,?,?)";
	public final static String BILLING_CHARGE = "SELECT SUM(transaction_rate_per_day) AS txnCharge, SUM(tax_per_day) AS taxCharge"
			+ " FROM txn_detail WHERE req_corporation_id = ? AND (created_on  BETWEEN DATE_FORMAT(NOW() - INTERVAL 1 MONTH, '%Y-%m-01 00:00:00') "
			+ " AND DATE_FORMAT(LAST_DAY(NOW() - INTERVAL 1 MONTH), '%Y-%m-%d 23:59:59')) ";
	public final static String Monthly_BILLING_CHARGE = "SELECT SUM(DISTINCT (transaction_rate_per_month)) AS txnCharge, SUM(DISTINCT (tax_per_month)) AS taxCharge"
			+ " FROM txn_detail WHERE req_corporation_id = ? AND (created_on  BETWEEN DATE_FORMAT(NOW() - INTERVAL 1 MONTH, '%Y-%m-01 00:00:00') "
			+ " AND DATE_FORMAT(LAST_DAY(NOW() - INTERVAL 1 MONTH), '%Y-%m-%d 23:59:59')) ";

	public final static String INSERT_PRESCRIPTION_TRANSACTION = "INSERT INTO txn_detail (req_corporation_id, res_corporation_id, req_pharmacy_id,res_pharmacy_id,tax_per_month, tax_per_day,transaction_type, transaction_rate_per_day,transaction_rate_per_month,txn_status,created_by,prescription_id ) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
	public final static String UPDATE_PRESCRIPTION = "UPDATE prescriptions SET patient_firstname=?,patient_lastname=?,patient_dob=?,patient_address=?,presc_number=?,presc_drug_name=?,"
			+ "presc_drug_qty=?,directions=?,orig_date_written=?,date_last_filled=?,refills_remaining=?,provider_name=?,provider_phone_number=?,provider_npi=?,"
			+ "provider_dea=?,res_pharmacy_comments=?,status=?,updated_on=?,updated_by=? WHERE id = ?";
	public final static String UPDATE_PRESCRIPTION_TRANSACTION_ON_RESPONSE = "UPDATE txn_detail SET txn_status=?, updated_on=?, updated_by=?  WHERE prescription_id=?";
	public final static String UPDATE_RESEND_PRESCRIPTION_STATUS = "UPDATE prescriptions SET resendFlag='Y' WHERE id=?";
	public final static String GET_PENDING_PRISCRIPTIONS_TOKEN_BY_LOCATION_PIN_AND_STATUS = "SELECT  DISTINCT req_token FROM prescriptions WHERE res_pharmacy_id=(SELECT id FROM pharmacy_locations WHERE location_pin=?) AND STATUS=? AND efax_sent='N' AND created_on < (NOW()-INTERVAL 30 MINUTE) AND created_on > (NOW()-INTERVAL 1440 MINUTE)";
	public final static String GET_FAXNUMBER_BY_TOKEN = "SELECT DISTINCT fax_num FROM pharmacy_locations WHERE id=(SELECT DISTINCT res_pharmacy_id FROM prescriptions WHERE req_token=?)";
	public final static String GET_CORPORATION_LIST_MULTISELECT = "SELECT id,corporation_name as itemName FROM pharmacy_corporation ORDER BY id DESC";
	public final static String GET_LOCATION_LIST_FOR_CORPORATION_ID_MULTISELECT = "SELECT id, pharmacy_name as itemName	FROM pharmacy_locations WHERE corporation_id=? ORDER BY id DESC";
	public final static String GET_LOCATION_LIST_FOR_CORP_IDS_MULTISELECT = "SELECT id, pharmacy_name as itemName	FROM pharmacy_locations WHERE corporation_id IN (:ids) ORDER BY id DESC";
	public final static String GET_NO_RESPONSE_PRESC_FOR_LOC_IDS_FOR_DATE_RANGE = "SELECT p.id, p.req_corporation_id, p.res_corporation_id, p.req_pharmacy_id,p.res_pharmacy_id,p.patient_firstname,p.patient_lastname, p.patient_dob, p.patient_address, p.presc_number, p.presc_drug_name,"
			+ " p.presc_drug_qty, p.directions, p.orig_date_written, p.date_last_filled, p.refills_remaining, p.provider_name, p.provider_phone_number,p.provider_npi, p.provider_dea, p.req_pharmacy_comments,"
			+ " p.res_pharmacy_comments,p.status,p.created_on, p.created_by,p.updated_on, p.updated_by,"
			+ " req.pharmacy_name AS reqP, req.address AS reqA, req.dea_num AS reqD,"
			+ " res.pharmacy_name AS resP, res.address AS resA, res.dea_num AS resD, "
			+ " reqCorp.corporation_name AS reqCorpName, resCorp.corporation_name AS resCorpName, "
			+ " reqUser.first_name AS reqUserFname, reqUser.last_name AS reqUserLname "
			+ " FROM prescriptions p JOIN pharmacy_locations req ON req.id=p.req_pharmacy_id "
			+ " JOIN pharmacy_locations res ON res.id=p.res_pharmacy_id "
			+ " JOIN pharmacy_corporation reqCorp ON reqCorp.id=req.corporation_id "
			+ " JOIN pharmacy_corporation resCorp ON resCorp.id=res.corporation_id "
			+ " JOIN users reqUser ON p.created_by=reqUser.username " + " WHERE p.res_pharmacy_id IN (:ids)"
			+ " AND STATUS='Pending' AND ( CAST(p.created_on AS DATE) BETWEEN CAST(:fromDate AS DATE) AND CAST(:toDate AS DATE))";

	public final static String GET_TX_IN_TRANSACTION_REPORTS_FOR_LOC_IDS_FOR_DATE_RANGE = "SELECT p.patient_dob,p.presc_drug_name,p.presc_drug_qty,p.refills_remaining, tx.id, tx.req_corporation_id, tx.res_corporation_id," 
			+ " tx.req_pharmacy_id, tx.res_pharmacy_id,  tx.req_pharmacy_id, tx.res_pharmacy_id, tx.tax_per_month,tx.tax_per_day,tx.transaction_type,tx.transaction_rate_per_day ,tx.transaction_rate_per_month,"
			+ "  tx.txn_status, tx.created_on, tx.created_by, tx.updated_on, tx.updated_by, tx.prescription_id,"
			+ "  req.pharmacy_name AS reqP,req.city AS reqc,req.state AS reqs,req.zip_code AS reqz,"
			+ "  res.phy_city AS resc,res.phy_state AS ress,res.phy_zip AS resz, res.pharmacy_name AS resP, reqCorp.corporation_name AS reqCorpName,"			 
			+ "  reqCorp.payment_type AS reqPayType"
			+ "  FROM txn_detail tx JOIN pharmacy_locations req ON req.id=tx.req_pharmacy_id" 
			+ "  JOIN non_reg_phar res ON res.id=tx.res_pharmacy_id "+" JOIN"
			+ " pharmacy_corporation reqCorp ON reqCorp.id=req.corporation_id" 			
			+ "  JOIN prescriptions p ON p.id= tx.prescription_id WHERE tx.req_pharmacy_id IN (:ids) "
			+ " AND ( CAST(tx.created_on AS DATE) BETWEEN CAST(:fromDate AS DATE) AND CAST(:toDate AS DATE)) ORDER BY tx.created_on DESC";
	public final static String GET_TX_OUT_TRANSACTION_REPORTS_FOR_LOC_IDS_FOR_DATE_RANGE = "SELECT tx.id, p.patient_dob,p.presc_drug_name,p.presc_drug_qty,p.refills_remaining, tx.req_corporation_id, tx.res_corporation_id, "
			+ " tx.req_pharmacy_id, tx.res_pharmacy_id, tx.res_charge, tx.req_charge, tx.efax_charge, "
			+ " tx.req_tax_charge ,tx.res_tax_charge ,tx.txn_status, tx.created_on, tx.created_by, tx.updated_on, tx.updated_by, tx.prescription_id,"
			+ " req.pharmacy_name AS reqP, res.pharmacy_name AS resP, reqCorp.corporation_name AS reqCorpName, resCorp.corporation_name AS resCorpName,"
			+ " reqCorp.payment_type AS reqPayType, resCorp.payment_type AS resPayType, 'transferOut' AS TxType "
			+ " FROM txn_detail tx " + " JOIN pharmacy_locations req ON req.id=tx.req_pharmacy_id "
			+ " JOIN pharmacy_locations res ON res.id=tx.res_pharmacy_id " + " JOIN"
			+ " pharmacy_corporation reqCorp ON reqCorp.id=req.corporation_id "
			+ " JOIN pharmacy_corporation resCorp ON resCorp.id=res.corporation_id "
			+ " JOIN prescriptions p ON p.id= tx.prescription_id " + " WHERE tx.res_pharmacy_id IN (:ids) "
			+ " AND ( CAST(tx.created_on AS DATE) BETWEEN CAST(:fromDate AS DATE) AND CAST(:toDate AS DATE)) ORDER BY tx.created_on DESC";

	public final static String GET_ALL_TRANSACTION_REPORTS_FOR_LOC_IDS_FOR_DATE_RANGE = "SELECT tx.id, p.patient_dob,p.presc_drug_name,p.presc_drug_qty,p.refills_remaining, tx.req_corporation_id, tx.res_corporation_id, "
			+ " tx.req_pharmacy_id, tx.res_pharmacy_id, tx.res_charge, tx.req_charge, tx.efax_charge,"
			+ " tx.req_tax_charge ,tx.res_tax_charge ,tx.txn_status, tx.created_on, tx.created_by, tx.updated_on, tx.updated_by, tx.prescription_id, "
			+ "req.pharmacy_name AS reqP, res.pharmacy_name AS resP, reqCorp.corporation_name AS reqCorpName, resCorp.corporation_name AS resCorpName,"
			+ "  reqCorp.payment_type AS reqPayType, resCorp.payment_type AS resPayType, 'transferIn' AS TxType"
			+ "  FROM txn_detail tx  " + "   JOIN pharmacy_locations req ON req.id=tx.req_pharmacy_id"
			+ "  JOIN pharmacy_locations res ON res.id=tx.res_pharmacy_id" + "  JOIN "
			+ "pharmacy_corporation reqCorp ON reqCorp.id=req.corporation_id"
			+ " JOIN pharmacy_corporation resCorp ON resCorp.id=res.corporation_id"
			+ "  JOIN prescriptions p ON p.id= tx.prescription_id" + " WHERE tx.req_pharmacy_id IN (:ids)"
			+ " AND ( CAST(tx.created_on AS DATE) BETWEEN CAST(:fromDate AS DATE) AND CAST(:toDate AS DATE))"
			+ " UNION SELECT tx.id, p.patient_dob,p.presc_drug_name,p.presc_drug_qty,p.refills_remaining, tx.req_corporation_id, tx.res_corporation_id,"
			+ "  tx.req_pharmacy_id, tx.res_pharmacy_id, tx.res_charge, tx.req_charge, tx.efax_charge,"
			+ "tx.req_tax_charge ,tx.res_tax_charge ,tx.txn_status, tx.created_on, tx.created_by, tx.updated_on, tx.updated_by, tx.prescription_id,"
			+ " req.pharmacy_name AS reqP, res.pharmacy_name AS resP, reqCorp.corporation_name AS reqCorpName, resCorp.corporation_name AS resCorpName,"
			+ "reqCorp.payment_type AS reqPayType, resCorp.payment_type AS resPayType, 'transferOut' AS TxType "
			+ " FROM txn_detail tx  JOIN pharmacy_locations req ON req.id=tx.req_pharmacy_id"
			+ " JOIN pharmacy_locations res ON res.id=tx.res_pharmacy_id" + " JOIN "
			+ " pharmacy_corporation reqCorp ON reqCorp.id=req.corporation_id"
			+ " JOIN pharmacy_corporation resCorp ON resCorp.id=res.corporation_id"
			+ " JOIN prescriptions p ON p.id= tx.prescription_id " + " WHERE tx.res_pharmacy_id IN (:ids)"
			+ " AND ( CAST(tx.created_on AS DATE) BETWEEN CAST(:fromDate AS DATE) AND CAST(:toDate AS DATE)) ";

	/*
	 * public final static String GET_REVENUE_DETAILS_FOR_CORP_FOR_DATE_RANGE =
	 * "SELECT result.corpId AS corpId, result.corpName AS corpName ,SUM(result.txnCharge) AS txnCharge, SUM(result.taxCharge) AS taxCharge "
	 * + "FROM  (" +
	 * "SELECT p.id AS corpId, p.corporation_name AS corpName, SUM(t.req_charge) AS txnCharge, SUM(t.req_tax_charge) AS taxCharge FROM txn_detail t, pharmacy_corporation p"
	 * +
	 * "	WHERE t.req_corporation_id = p.id AND t.req_corporation_id IN (:ids) "
	 * +
	 * "AND ( CAST(t.created_on AS DATE) BETWEEN CAST(:fromDate AS DATE) AND CAST(:toDate AS DATE))	GROUP BY p.corporation_name	"
	 * + "UNION ALL " + "SELECT p.id AS corpId,p.corporation_name AS corpName, "
	 * +
	 * "SUM(IF(t.res_charge IS NOT NULL, t.res_charge,0)+IF(t.efax_charge IS NOT NULL,t.efax_charge,0)) AS txnCharge, "
	 * +
	 * "SUM(IF(t.res_charge IS NOT NULL,t.res_tax_charge,0)+IF(t.efax_charge IS NOT NULL,t.res_tax_charge, 0)) AS taxCharge "
	 * +
	 * "FROM txn_detail t, pharmacy_corporation p WHERE t.res_corporation_id = p.id AND t.res_corporation_id IN (:ids) "
	 * +
	 * "AND ( CAST(t.updated_on AS DATE) BETWEEN CAST(:fromDate AS DATE) AND CAST(:toDate AS DATE)) 	GROUP BY p.corporation_name) "
	 * + "AS result GROUP BY result.corpName";
	 */
	/**
	 * GET_REVENUE_DETAILS_FOR_CORP_FOR_DATE_RANGE for monthly transactions only
	 */
	public final static String GET_REVENUE_DETAILS_FOR_CORP_FOR_DATE_RANGE =

			"SELECT result.corpId AS corpId, result.corpName AS corpName ,SUM(result.txnCharge) AS txnCharge, SUM(result.taxCharge) AS taxCharge "
					+ " FROM  (SELECT p.id AS corpId, p.corporation_name AS corpName, (SUM(DISTINCT(t.transaction_rate_per_month))+ SUM(t.transaction_rate_per_day)) AS txnCharge ,  (SUM(t.tax_per_day)+SUM(DISTINCT(t.tax_per_month))) AS taxCharge"
					+ " FROM txn_detail t, pharmacy_corporation p"
					+ " WHERE t.req_corporation_id = p.id AND t.req_corporation_id IN (:ids) "
					+ " AND ( CAST(t.created_on AS DATE) BETWEEN CAST(:fromDate AS DATE) AND CAST(:toDate AS DATE)) "
					+ " GROUP BY p.corporation_name " + ")" + "AS result GROUP BY result.corpName";

	// "SELECT result.corpId AS corpId, result.corpName AS corpName
	// ,SUM(result.txnCharge) AS txnCharge, SUM(result.taxCharge) AS taxCharge "
	// + "FROM ("
	// + "SELECT p.id AS corpId, p.corporation_name AS corpName,
	// SUM(t.transaction_rate_per_day) AS txnCharge"
	// + "FROM txn_detail t, pharmacy_corporation p"
	// + " WHERE t.req_corporation_id = p.id AND t.req_corporation_id IN (:ids)
	// "
	// + "AND ( CAST(t.created_on AS DATE) BETWEEN CAST(:fromDate AS DATE) AND
	// CAST(:toDate AS DATE)) GROUP BY p.corporation_name "
	// + "UNION ALL " + "SELECT p.id AS corpId,p.corporation_name AS corpName, "
	// + "FROM txn_detail t, pharmacy_corporation p WHERE t.res_corporation_id =
	// p.id AND t.res_corporation_id IN (:ids) "
	// + "AND ( CAST(t.created_on AS DATE) BETWEEN CAST(:fromDate AS DATE) AND
	// CAST(:toDate AS DATE)) GROUP BY p.corporation_name) "
	// + "AS result GROUP BY result.corpName";

	public final static String GET_REVENUE_DETAILS_FOR_CORP = "SELECT result.corpId AS corpId, result.corpName AS corpName ,SUM(result.txnCharge) AS txnCharge, SUM(result.taxCharge) AS taxCharge "
			+ " FROM  (SELECT p.id AS corpId, p.corporation_name AS corpName, (SUM(DISTINCT(t.transaction_rate_per_month))+ SUM(t.transaction_rate_per_day)) AS txnCharge ,(SUM(t.tax_per_day)+SUM(DISTINCT(t.tax_per_month))) AS taxCharge"
			+ " FROM txn_detail t, pharmacy_corporation p"
			+ " WHERE t.req_corporation_id = p.id AND t.req_corporation_id IN (:ids) " + " GROUP BY p.corporation_name "
			+ ")" + "AS result GROUP BY result.corpName";

	// "SELECT result.corpId AS corpId, result.corpName AS corpName
	// ,result.txnCharge As txnCharge "
	// + "FROM ("+ "SELECT p.id As corpId, p.corporation_name AS corpName,
	// (SUM(DISTINCT(t.transaction_rate_per_month))+
	// SUM(t.transaction_rate_per_day)) AS txnCharge,
	// (SUM(t.tax_per_day)+SUM(DISTINCT(t.tax_per_month))) "
	// +" AS taxCharge FROM txn_detail t, " + "pharmacy_corporation p WHERE
	// t.req_corporation_id = p.id AND t.req_corporation_id IN (:ids) GROUP BY
	// p.corporation_name "
	// + "UNION ALL " + "SELECT p.id As corpId, p.corporation_name AS corpName,
	// "
	// + "FROM txn_detail t, pharmacy_corporation p WHERE t.res_corporation_id =
	// p.id AND t.res_corporation_id IN (:ids) "+"GROUP BY p.corporation_name"
	// + ") " + "AS result GROUP BY result.corpName";

	// "SELECT result.corpId AS corpId, result.corpName AS corpName
	// ,SUM(result.txnCharge) AS txnCharge, SUM(result.taxCharge) AS taxCharge "
	// + "FROM ("
	// + "SELECT p.id As corpId, p.corporation_name AS corpName,
	// SUM(t.req_charge) AS txnCharge, SUM(t.req_tax_charge) AS taxCharge FROM
	// txn_detail t, "
	// + "pharmacy_corporation p WHERE t.req_corporation_id = p.id AND
	// t.req_corporation_id IN (:ids) GROUP BY p.corporation_name "
	// + "UNION ALL " + "SELECT p.id As corpId, p.corporation_name AS corpName,
	// "
	// + "SUM(IF(t.res_charge IS NOT NULL, t.res_charge,0)+IF(t.efax_charge IS
	// NOT NULL,t.efax_charge,0)) AS txnCharge, "
	// + "SUM(IF(t.res_charge <> NULL,0,t.res_tax_charge)+IF(t.efax_charge IS
	// NOT NULL,t.res_tax_charge, 0)) AS taxCharge "
	// + "FROM txn_detail t, pharmacy_corporation p WHERE t.res_corporation_id =
	// p.id AND t.res_corporation_id IN (:ids) GROUP BY p.corporation_name"
	// + ") " + "AS result GROUP BY result.corpName";
	/***************************************************************************/
	public final static String TXN_TYPE_FOR_CORP = "SELECT transaction_type FROM  billing_setup WHERE corporation_id= ? ";

	public final static String UPDATE_TXN_STATUS = "UPDATE billing_setup SET transaction_type = start_txnType";

	/* Queries for Security Question */

	public final static String GET_SECURITY_QUESTION_LIST = "SELECT id,question FROM security_questions";
	public final static String SET_SECURITY_QUESTION_FOR_USER = "INSERT INTO user_security_question(user_id,question_id,answer,created_by) VALUES(?,?,?,?)";
	public final static String UPDATE_SECURITY_QUESTION_FOR_USER = "UPDATE user_security_question SET question_id=?,answer=?,updated_by=?,updated_on=? WHERE user_id=?";
	public final static String UPDATE_FIRST_LOGIN = "UPDATE USERS SET first_login =? WHERE id=?";
	public final static String GET_SECURITY_QUESTION_COUNT_BY_USERNAME = "SELECT count(*) FROM security_questions WHERE id=(SELECT question_id FROM user_security_question WHERE user_id=(SELECT id FROM users WHERE username=?))";
	public final static String GET_SECURITY_QUESTION_BY_USERNAME = "SELECT question FROM security_questions WHERE id=(SELECT question_id FROM user_security_question WHERE user_id=(SELECT id FROM users WHERE username=?))";
	public final static String GET_SECURITY_ANSWER_BY_USERNAME = "SELECT answer FROM user_security_question WHERE user_id=(SELECT id FROM users WHERE username=?)";

	/* Queries for Drug Database */

	public final static String UPDATE_DRUG_DATABASE = "INSERT INTO drugs(PRODUCTID,PROPRIETARYNAME,PROPRIETARYNAMESUFFIX,NONPROPRIETARYNAME,DOSAGEFORMNAME,ACTIVE_NUMERATOR_STRENGTH,ACTIVE_INGRED_UNIT,DEASCHEDULE) VALUES(?,?,?,?,?,?,?,?)";
	
	public final static String SEARCH_DRUG_bY_PROP_AND_NONPROP_NAME = "SELECT PRODUCTID,PROPRIETARYNAME,PROPRIETARYNAMESUFFIX,NONPROPRIETARYNAME,DOSAGEFORMNAME,ACTIVE_NUMERATOR_STRENGTH,ACTIVE_INGRED_UNIT,DEASCHEDULE FROM drugs WHERE (PROPRIETARYNAME LIKE ? OR NONPROPRIETARYNAME LIKE ?) AND (NOT DEASCHEDULE ='CII' OR DEASCHEDULE IS NULL)";

	/* Queries for User log */
	public final static String ADD_USER_LOG = "INSERT INTO user_log(username,event,status,remarks) VALUES(?,?,?,?)";

	/* Queries for Prescription log */
	public final static String ADD_PRESCRIPTION_LOG = "INSERT INTO prescription_log(presc_number,patient_firstname,patient_lastname,patient_dob,patient_address,presc_drug_name,event,status,remarks,updated_by) VALUES(?,?,?,?,?,?,?,?,?,?)";
	public final static String GET_PRESCRIPTION_LOG_LIST = "SELECT id,DATE,presc_number,patient_firstname,patient_lastname,patient_dob,patient_address,presc_drug_name,EVENT,STATUS,remarks,updated_by FROM prescription_log ORDER BY DATE DESC";
	public final static String SEARCH_USER_LOG_LIST_BY_USERNAME_AND_DATE = "SELECT id,DATE, username,EVENT,STATUS,remarks FROM user_log WHERE  username LIKE ? AND  DATE BETWEEN  ? AND  ? ORDER BY DATE DESC";
	public final static String SEARCH_PRESCRIPTION_LOG_LIST_BY_USERNAME_AND_DATE = "SELECT id,DATE,presc_number,patient_firstname,patient_lastname,patient_dob,patient_address,presc_drug_name,EVENT,STATUS,remarks,updated_by FROM prescription_log WHERE patient_lastname OR patient_firstname like ? AND date BETWEEN ? AND ? ORDER BY DATE DESC";
}
