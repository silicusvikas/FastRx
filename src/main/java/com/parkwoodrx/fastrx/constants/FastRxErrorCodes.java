package com.parkwoodrx.fastrx.constants;

public class FastRxErrorCodes {

	public final static String SERVER_ERROR = "Server error";
	public final static int CREATE_ERROR_CODE = 1001;
	public final static String ERROR_ADDING_PAYMENT_DETAILS__MSG = "Error in adding payment details.";
	public final static String ERROR_CREATING_USER_MSG = "Error in creating user";
	public final static String ERROR_CREATING_PHARMACY_MSG = "Error in creating pharmacy";
	public final static String ERROR_AADING_BILLING_MSG = "Error in adding billing details.";
	public final static String ERROR_ADDING_PRESCRIPTION_REQUEST = "Error adding prescription request.";
	public final static String ERROR_ADDING_TRANSACTION_REQUEST = "Error adding prescription transaction details.";
	public final static String ERROR_ADDING_SECURITY_QUESTION = "Error in adding security question. Please try again.";
	public final static String ERROR_VALIDATING_SECURITY_ANSWER = "Error in validating security question. Please try again.";
	public final static String ERROR_RESET_PASSWORD = "Error in resetting password. Please try again.";
	public final static String ERROR_LOGOUT = "Error in logout";
	public final static String ERROR_ADDING_AUDIT_LOG = "Error in adding audit log";

	public final static int UPDATE_ERROR_CODE = 1002;
	public final static String TRANSACTION_TYPE_EMPTY_MSG = "Please select transaction type";
	public final static String CORPORATION_UPDATE_ERROR_MSG = "Pharmacy corporation update  failed";
	public final static String BILLING_UPDATE_ERROR_MSG = "Billing update  failed";
	public final static String ERROR_UPDATING_PROFILE_MSG = "Error in updating profile.";
	public final static String UPDATE_PWD_ERROR_MSG = "User update password error";
	public final static String UPDATE_USER_CORPORATION_MAPPING_ERROR_MSG = "Updation of corporation User mapping failed";
	public final static String UPDATE_USER_LOCATION_MAPPING_ERROR_MSG = "Updation of location user mapping failed";
	public final static String USER_UPDATION_ERROR_MSG = "User updation failed";
	public final static String PAYMENT_UPDATE_ERROR_MSG = "Payment details update failed";
	public final static String LOCATION_UPDATE_ERROR_MSG = "Pharmacy location update  failed";
	public final static String PHARMACY_CORPORATION_STATUS_UPDATE_ERROR_MSG = "Pharmacy corporation status update failed";
	public final static String PHARMACY_CORPORATION_ACTIVE_ERROR_MSG = "Billing and payment details required to activate new pharmacy corporation";
	public final static String PHARMACY_LOCATION_STATUS_UPDATE_ERROR_MSG = "Pharmacy location status update failed";
	public final static String USER_STATUS_UPDATE_ERROR_MSG = "User status updation failed";
	public final static String PRESCRIPTION_UPDATION_ERROR_MSG = "Prescription updation failed";
	public final static String PRESCRIPTION_TRANSAC_UPDATION_ERROR_MSG = "Prescription transaction updation failed";
	public final static String PRESCRIPTION_RESEND_ERROR_MSG = "Prescription Resend failed";
	

	public final static int DUPLICATE_CODE = 1003;
	public final static String DUPLICATE_PHARMACY_MSG = "Pharmacy corporation  already  exists with the same name.";
	public final static int DUPLICATE_DEA_CODE = 1001;
	public final static String DUPLICATE_PHARMACY_DEA_MSG = "Pharmacy location already exists with the same DEA Number.";
	public final static String USER_EXISTS_MSG = "User already exists with the same username.";
	public final static String DUPLICATE_RECORD_MSG = "Duplicate record";

	public final static int INVALID_CODE = 1004;
	public final static String INVALID_CREDIT_CARD_MSG = "The credit card number is invalid";
	public final static String INVALID_OLD_PWD_MSG = "Incorrect old password";
	public final static String INVALID_TOKEN_MSG = "Invalid token";
	public final static String INVALID_LOCATION_PIN_MSG = "Invalid location pin";
	public final static String INVALID_CREDENTIALS_MSG = "Invalid credentials";
	public final static String INVALID_AUTH_HEADER_MSG = "Missing or invalid authorization header with bearer type.";
	public final static String USER_ROLE_MAPPING_NOT_FOUND_MSG = "User role mapping not found";
	public final static String INCORRECT_USERNAME = "Incorrect username";
	public final static String INCORRECT_PASSWORD = "Your password is incorrect";
	public final static String INVALID_DEA_NUMBER_MSG = "The DEA number is invalid";

	public final static int NOT_FOUND_CODE = 1005;
	public final static String NO_STATE_FOUND_MSG = "No state found";
	public final static String NO_DIRECTIONS_FOUND_MSG = "No state found";
	public final static String NO_LOCATION_FOUND_MSG = "No locations found for corporation id";
	public final static String NO_ROLES_FOUND_MSG = "No user roles found";
	public final static String NO_CORPORATION_FOUND_MSG = "No pharmacy corporation found";
	public final static String USER_CORPORATION_RELATION_NOT_FOUND_MSG = "User corporation relation not found";
	public final static String USER_DOESNT_EXISTS_MSG = "User doesn't exists";
	public final static String USER_LIST_NOT_FOUND = "Users not found";
	public final static String USER_NOT_LINKED_WITH_CORPORATION_OF_PROVIDED_LOCATION_PIN = "User not linked to the corporation of provided location pin";
	public final static String NO_PHARMACY_FOUND_MSG = "No pharmacy location found";
	public final static String NO_DRUGS_FOUND_MSG = "No drugs found";
	public final static String PRESCRIPTION_LIST_NOT_FOUND = "Prescription list not found";
	public final static String PRESCRIPTION_NOT_FOUND = "No prescription found";
	public final static String BILLING_DETAILS_NOT_FOUND = "Billing details not found";
	public final static String QUESTION_LIST_NOT_FOUND = "Security question not found";
	public final static String SECURITY_QUESTION_NOT_SET = "You have not set any security question.";
	public final static String NO_LOGS_FOUND = "No logs found";

	public final static int EMPTY_FIELD_CODE = 1006;
	public final static String EMPTY_SEARCH_FIELDS_MSG = "Search fields can not be empty.";
	public final static String EMPTY_CORPORATION_ID_MSG = "Pharmacy corporation Id can not be zero or empty";
	public final static String EMPTY_FIELDS_MSG = "Payment Details can not be empty";

	public final static int DATABASE_ERROR_CODE = 1007;
	public final static String DATABASE_ERROR_MSG = "Database error";

	public final static int USER_NOT_ADMIN_CODE = 1008;
	public final static String USER_NOT_ADMIN_MSG = "User is not admin";

	public final static int PWD_ENCRYPTION_ERROR_CODE = 1009;
	public final static String PWD_ENCRYPTION_ERROR_MSG = "Password encryption error";

	public final static int EMAIL_SENDING_ERROR_CODE = 1010;
	public final static String EMAIL_SENDING_ERROR_MSG = "Email sending failed";
	public final static String EFAX_SENDING_ERROR_MSG = "Error in sending Efax";

	public final static String ERROR_CREATING_PHARMACY_LOCATION_MSG = "Error in creating pharmacy location";
	public final static String ERROR_UPDATING_PHARMACY_LOCATION_MSG = "Error in updating pharmacy location";
	public final static String DUPLICATE_PHARMACY_LOCATION_MSG = "Pharmacy location with this phone number already exists ";
	public final static int ERROR_FETCHING_RECORDS_CODE = 1018;
	public final static String ERROR_FETCHING_RECORDS_MSG = "Error in fetching records from DB.";
	public final static String ERROR_SEARCHING_RECORDS_MSG = "Error in searching records from DB.";

	public final static int INACTIVE_CODE = 1011;
	public final static String LOCATION_INACTIVE_MSG = "Location is inactive.Please contact administrator";
	public final static String CORPORATION_INACTIVE_MSG = "Corporation is inactive.Please contact administrator.  support@fastrxtransfer.com (1-800-605-3906)";
	public final static String CORPORATION_INACTIVE_lOG_MSG = "Corporation is inactive.";

	public final static int FILE_UPLOAD_READ_CODE = 1012;
	public final static String FILE_UPLOAD_READ_MSG = "Error while reading file.";
	public final static String INVALID_DATA_FORMAT = "Data is not in proper format";
	public final static String INVALID_FILE_FORMAT = "Invalid file format (only .xlsx accepted)";

	public final static int FILE_UPLOAD_CODE = 1013;
	public final static String FILE_UPLOAD_MSG = "Error while registering pharmacy location - ";

	public final static int FETCHING_BILLING_DETAILS_CODE = 1014;
	public final static String FETCHING_BILLING_DETAILS_MSG = "Error while fetching billing details from txn_details for corpId - ";

	public final static int BUILDING_BILLING_CODE = 1015;
	public final static String BUILDING_BILLING_MSG = "Error while building billing report object";

	public final static int CALCULATING_TOTAL_CODE = 1016;
	public final static String CALCULATING_TOTAL_MSG = "Error while calculating total billing amount";

	public final static int GENERATING_BILLING_REPORT_EMAIL_CODE = 1017;
	public final static String GENERATING_BILLING_REPORT_EMAIL_MSG = "Error while generating billing report and sending email";

	public final static int FETCHING_PAYMENT_DETAILS_FOR_DEBIT_CODE = 1018;
	public final static String FETCHING_PAYMENT_DETAILS_FOR_DEBIT_MSG = "Error while fetching paymnet details for bill generation";

	public final static int DEBITING_PAYMENT_FROM_CARD_CODE = 1019;
	public final static String DEBITING_PAYMENT_FROM_CARD_MSG = "Error while debiting paymnet from credit card";

	public final static int CALCULATING_PERIOD_CODE = 1020;
	public final static String CALCULATING_PERIOD_MSG = "Error while calculating billing period";

	public final static int CREDIT_CARD_EMAIL_CODE = 1021;
	public final static String CREDIT_CARD_EMAIL_MSG = "Error while sending email for credit card paymnet status";

	public final static int BLANK_FILE_UPLOAD_CODE = 1022;
	public final static String BLANK_FILE_UPLOAD_MSG = "No data available in file";

	public final static int FILE_UPLOAD_CORP_CODE = 1023;
	public final static String FILE_UPLOAD_CORP_MSG = "Pharmacy corporation with name - ";

}
