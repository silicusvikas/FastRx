package com.parkwoodrx.fastrx.integration;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.parkwoodrx.fastrx.constants.FastRxErrorCodes;
import com.parkwoodrx.fastrx.exception.FastRxException;
import com.parkwoodrx.fastrx.model.CreditcardDetails;
import com.parkwoodrx.fastrx.model.FastRxAudit;
import com.parkwoodrx.fastrx.model.InvoiceDetails;
import com.parkwoodrx.fastrx.model.PaymentProfileDetails;
import com.parkwoodrx.fastrx.repository.PharmacyCorporationDao;
import com.parkwoodrx.fastrx.security.PasswordGenerator;

import net.authorize.Environment;
import net.authorize.api.contract.v1.CreateCustomerProfileRequest;
import net.authorize.api.contract.v1.CreateCustomerProfileResponse;
import net.authorize.api.contract.v1.CreateTransactionRequest;
import net.authorize.api.contract.v1.CreateTransactionResponse;
import net.authorize.api.contract.v1.CreditCardType;
import net.authorize.api.contract.v1.CustomerAddressType;
import net.authorize.api.contract.v1.CustomerPaymentProfileExType;
import net.authorize.api.contract.v1.CustomerPaymentProfileType;
import net.authorize.api.contract.v1.CustomerProfileExType;
import net.authorize.api.contract.v1.CustomerProfilePaymentType;
import net.authorize.api.contract.v1.CustomerProfileType;
import net.authorize.api.contract.v1.CustomerTypeEnum;
import net.authorize.api.contract.v1.DeleteCustomerProfileRequest;
import net.authorize.api.contract.v1.DeleteCustomerProfileResponse;
import net.authorize.api.contract.v1.MerchantAuthenticationType;
import net.authorize.api.contract.v1.MessageTypeEnum;
import net.authorize.api.contract.v1.PaymentProfile;
import net.authorize.api.contract.v1.PaymentType;
import net.authorize.api.contract.v1.TransactionRequestType;
import net.authorize.api.contract.v1.TransactionResponse;
import net.authorize.api.contract.v1.TransactionTypeEnum;
import net.authorize.api.contract.v1.UpdateCustomerPaymentProfileRequest;
import net.authorize.api.contract.v1.UpdateCustomerPaymentProfileResponse;
import net.authorize.api.contract.v1.ValidationModeEnum;
import net.authorize.api.controller.CreateCustomerProfileController;
import net.authorize.api.controller.CreateTransactionController;
import net.authorize.api.controller.DeleteCustomerProfileController;
import net.authorize.api.controller.UpdateCustomerPaymentProfileController;
import net.authorize.api.controller.base.ApiOperationBase;

@Service
@Transactional
public class CustomerPaymentService {

	private static final Logger logger = LoggerFactory.getLogger(CustomerPaymentService.class);

	@Autowired
	private PharmacyCorporationDao pharmacyDao;

	@Value("${authorize.apiLoginId}")
	private String apiLoginId;

	@Value("${authorize.transactionKey}")
	private String transactionKey;

	@Value("${authorize.country}")
	private String country;

	@Autowired
	private PasswordGenerator passwordGenerator;

	public CreditcardDetails create(PaymentProfileDetails profileDetails) throws FastRxException {
		CreditcardDetails creditcard = new CreditcardDetails();
		CreateCustomerProfileResponse response = new CreateCustomerProfileResponse();
		try {
			ApiOperationBase.setEnvironment(Environment.PRODUCTION);
			MerchantAuthenticationType merchantAuthenticationType = new MerchantAuthenticationType();
			merchantAuthenticationType.setName(apiLoginId);
			merchantAuthenticationType.setTransactionKey(transactionKey);
			ApiOperationBase.setMerchantAuthentication(merchantAuthenticationType);

			CustomerAddressType customerAddress = new CustomerAddressType();
			customerAddress.setFirstName(profileDetails.getFirstName());
			customerAddress.setLastName(profileDetails.getLastName());
			customerAddress.setAddress(profileDetails.getAddress());
			customerAddress.setCity(profileDetails.getCity());
			customerAddress.setState(profileDetails.getState());
			customerAddress.setZip(profileDetails.getZipCode());
			customerAddress.setCountry(country);
			customerAddress.setPhoneNumber(profileDetails.getPhoneNumber());
			customerAddress.setFaxNumber(profileDetails.getFax());
			customerAddress.setCompany(profileDetails.getCompany());

			CreditCardType creditCard = new CreditCardType();
			creditCard.setCardNumber(profileDetails.getCardNumber());
			creditCard.setExpirationDate(profileDetails.getExpirationDate());
			creditCard.setCardCode(profileDetails.getCardCode());

			PaymentType paymentType = new PaymentType();
			paymentType.setCreditCard(creditCard);

			CustomerPaymentProfileType customerPaymentProfileType = new CustomerPaymentProfileType();
			customerPaymentProfileType.setCustomerType(CustomerTypeEnum.INDIVIDUAL);
			customerPaymentProfileType.setBillTo(customerAddress);
			customerPaymentProfileType.setPayment(paymentType);
			customerPaymentProfileType.setDefaultPaymentProfile(true);

			CustomerProfileType customerProfileType = new CustomerProfileType();
			customerProfileType.setMerchantCustomerId("M_" + passwordGenerator.generatePassword());
			customerProfileType.setDescription("Profile description for " + profileDetails.getEmail());
			customerProfileType.setEmail(profileDetails.getEmail());
			customerProfileType.getPaymentProfiles().add(customerPaymentProfileType);

			CreateCustomerProfileRequest apiRequest = new CreateCustomerProfileRequest();
			apiRequest.setProfile(customerProfileType);
			apiRequest.setValidationMode(ValidationModeEnum.LIVE_MODE);
			CreateCustomerProfileController controller = new CreateCustomerProfileController(apiRequest);
			controller.execute();
			response = controller.getApiResponse();
			if (response != null) {
				String profileStatus = response.getMessages().getMessage().get(0).getText().toString();
				if (!profileStatus.equalsIgnoreCase("Successful.")) {
					if("E00027".equalsIgnoreCase( response.getMessages().getMessage().get(0).getCode().toString())){
						logger.error("PaymentService:: "+ response.getMessages().getMessage().get(0).getText().toString());
						throw new FastRxException(FastRxErrorCodes.CREATE_ERROR_CODE, FastRxErrorCodes.INVALID_CREDIT_CARD_MSG);
					}else{
						logger.error("PaymentService:: "+ response.getMessages().getMessage().get(0).getText().toString());
						throw new FastRxException(FastRxErrorCodes.CREATE_ERROR_CODE,
								FastRxErrorCodes.ERROR_ADDING_PAYMENT_DETAILS__MSG);
					}
					
				}
				creditcard.setResponse(response.getMessages().getMessage().get(0).getText().toString());
				creditcard.setCustomerProfileId(response.getCustomerProfileId());
				creditcard
						.setCustomerPaymentAcctId(response.getCustomerPaymentProfileIdList().getNumericString().get(0));
				creditcard.setCorporationId(profileDetails.getCorporationId());
				creditcard.setCardHolderName(profileDetails.getCardHolderName());
				creditcard.setCardNumber(profileDetails.getCardNumber());
				FastRxAudit fx = new FastRxAudit();
				fx.setCreatedBy(profileDetails.getFastRxAudit().getCreatedBy());
				creditcard.setFastRxAudit(fx);
				pharmacyDao.createPaymentProfile(creditcard);
			} else {
				throw new FastRxException(FastRxErrorCodes.CREATE_ERROR_CODE,
						FastRxErrorCodes.ERROR_ADDING_PAYMENT_DETAILS__MSG);
			}
		} catch (FastRxException e) {
			logger.error("PaymentService::", e);
			if (null != response.getCustomerProfileId()) {
				deleteCustomerProfile(response.getCustomerProfileId());
			} else {
				throw new FastRxException(FastRxErrorCodes.CREATE_ERROR_CODE,
						FastRxErrorCodes.ERROR_ADDING_PAYMENT_DETAILS__MSG, e.getMessage());
			}

		} catch (Exception e) {
			logger.error("PaymentService::", e);
			deleteCustomerProfile(response.getCustomerProfileId());
			throw new FastRxException(FastRxErrorCodes.CREATE_ERROR_CODE,
					FastRxErrorCodes.ERROR_ADDING_PAYMENT_DETAILS__MSG, e.getMessage());
		}
		return creditcard;

	}

	public CreditcardDetails update(PaymentProfileDetails profileDetails) throws FastRxException, Exception {
		CreditcardDetails creditcard = new CreditcardDetails();
		try {
			ApiOperationBase.setEnvironment(Environment.PRODUCTION);
			MerchantAuthenticationType merchantAuthenticationType = new MerchantAuthenticationType();
			merchantAuthenticationType.setName(apiLoginId);
			merchantAuthenticationType.setTransactionKey(transactionKey);
			ApiOperationBase.setMerchantAuthentication(merchantAuthenticationType);

			// setting customer address
			CustomerAddressType customerAddress = new CustomerAddressType();
			customerAddress.setFirstName(profileDetails.getFirstName());
			customerAddress.setLastName(profileDetails.getLastName());
			customerAddress.setAddress(profileDetails.getAddress());
			customerAddress.setCity(profileDetails.getCity());
			customerAddress.setState(profileDetails.getState());
			customerAddress.setZip(profileDetails.getZipCode());
			customerAddress.setCountry(country);
			customerAddress.setPhoneNumber(profileDetails.getPhoneNumber());
			customerAddress.setFaxNumber(profileDetails.getFax());
			customerAddress.setCompany(profileDetails.getCompany());

			// credit card details
			CreditCardType creditCard = new CreditCardType();
			creditCard.setCardNumber(profileDetails.getCardNumber());
			creditCard.setExpirationDate(profileDetails.getExpirationDate());
			creditCard.setCardCode(profileDetails.getCardCode());

			PaymentType paymentType = new PaymentType();
			paymentType.setCreditCard(creditCard);

			CustomerPaymentProfileExType customer = new CustomerPaymentProfileExType();
			customer.setPayment(paymentType);
			customer.setCustomerPaymentProfileId(profileDetails.getCustomerPaymentProfileId());
			customer.setBillTo(customerAddress);
			// setting updated profile as default profile
			customer.setDefaultPaymentProfile(true);

			CustomerProfileExType customerProfileType = new CustomerProfileExType();
			customerProfileType.setMerchantCustomerId("M_" + profileDetails.getEmail());
			customerProfileType.setDescription("Profile description for " + profileDetails.getEmail());
			customerProfileType.setEmail(profileDetails.getEmail());
			customerProfileType.setCustomerProfileId(profileDetails.getCustomerProfileId());

			UpdateCustomerPaymentProfileRequest apiRequest = new UpdateCustomerPaymentProfileRequest();
			apiRequest.setCustomerProfileId(profileDetails.getCustomerProfileId());
			apiRequest.setPaymentProfile(customer);
			apiRequest.setValidationMode(ValidationModeEnum.NONE);

			UpdateCustomerPaymentProfileController controller = new UpdateCustomerPaymentProfileController(apiRequest);
			controller.execute();

			UpdateCustomerPaymentProfileResponse response = new UpdateCustomerPaymentProfileResponse();
			response = controller.getApiResponse();

			if (response != null) {
				creditcard.setResponse(response.getMessages().getMessage().get(0).getText());
				if (!response.getMessages().getMessage().get(0).getText().equalsIgnoreCase("Successful.")) {
					if("E00027".equalsIgnoreCase( response.getMessages().getMessage().get(0).getCode().toString())){
						throw new FastRxException(FastRxErrorCodes.CREATE_ERROR_CODE, FastRxErrorCodes.INVALID_CREDIT_CARD_MSG);
					}else{
						throw new FastRxException(FastRxErrorCodes.CREATE_ERROR_CODE,
								FastRxErrorCodes.ERROR_ADDING_PAYMENT_DETAILS__MSG);
					}
					
				}
				if (response.getMessages().getResultCode() == MessageTypeEnum.OK) {
					logger.info(response.getMessages().getMessage().get(0).getCode());
					logger.info(response.getMessages().getMessage().get(0).getText());
					creditcard.setResponse(response.getMessages().getMessage().get(0).getText());
					creditcard.setCorporationId(profileDetails.getCorporationId());
					creditcard.setCardHolderName(profileDetails.getCardHolderName());
					creditcard.setCardNumber(profileDetails.getCardNumber());
					FastRxAudit fastRxAudit = new FastRxAudit();
					fastRxAudit.setUpdatedBy(profileDetails.getFastRxAudit().getUpdatedBy());
					creditcard.setFastRxAudit(fastRxAudit);
					pharmacyDao.updatePaymentProfileDetails(creditcard);
					pharmacyDao.updatePaymentType(profileDetails.getPaymentType(), profileDetails.getCorporationId(),
							profileDetails.getFastRxAudit().getCreatedBy());
				} else {
					logger.info("Failed to update customer payment profile:  "
							+ response.getMessages().getMessage().get(0).getText());
					throw new FastRxException(FastRxErrorCodes.UPDATE_ERROR_CODE,
							FastRxErrorCodes.PAYMENT_UPDATE_ERROR_MSG);
				}
			}else{
				throw new FastRxException(FastRxErrorCodes.UPDATE_ERROR_CODE,
						FastRxErrorCodes.PAYMENT_UPDATE_ERROR_MSG);
			}

		} catch (Exception e) {
			throw new FastRxException(FastRxErrorCodes.UPDATE_ERROR_CODE, FastRxErrorCodes.PAYMENT_UPDATE_ERROR_MSG,
					e.getMessage());
		}

		return creditcard;
	}

	public CreditcardDetails deleteCustomerProfile(String customerProfileId) {
		logger.info("Delete payment profile:" + customerProfileId);
		ApiOperationBase.setEnvironment(Environment.PRODUCTION);
		MerchantAuthenticationType merchantAuthenticationType = new MerchantAuthenticationType();
		merchantAuthenticationType.setName(apiLoginId);
		merchantAuthenticationType.setTransactionKey(transactionKey);
		ApiOperationBase.setMerchantAuthentication(merchantAuthenticationType);

		DeleteCustomerProfileRequest apiRequest = new DeleteCustomerProfileRequest();
		apiRequest.setCustomerProfileId(customerProfileId);

		DeleteCustomerProfileController controller = new DeleteCustomerProfileController(apiRequest);
		controller.execute();

		DeleteCustomerProfileResponse response = new DeleteCustomerProfileResponse();
		response = controller.getApiResponse();

		CreditcardDetails creditcard = new CreditcardDetails();
		if (response != null) {
			creditcard.setResponse(response.getMessages().getMessage().get(0).getText());
			if (response.getMessages().getResultCode() == MessageTypeEnum.OK) {

				logger.info(response.getMessages().getMessage().get(0).getCode());
				logger.info("Delete payment profile:" + response.getMessages().getMessage().get(0).getText());
			} else {
				logger.info("Failed to delete customer profile:  " + response.getMessages().getResultCode());
			}
		}
		return creditcard;
	}

	public boolean ChargeCustomerProfile(String customerProfileId, String customerPaymentProfileId, Double amount,
			InvoiceDetails invoiceDetails) {
		boolean paid = false;
		try {
			invoiceDetails.setBillingAmount(amount);
			ApiOperationBase.setEnvironment(Environment.PRODUCTION);
			MerchantAuthenticationType merchantAuthenticationType = new MerchantAuthenticationType();
			merchantAuthenticationType.setName(apiLoginId);
			merchantAuthenticationType.setTransactionKey(transactionKey);
			ApiOperationBase.setMerchantAuthentication(merchantAuthenticationType);

			// Set the profile ID to charge
			CustomerProfilePaymentType profileToCharge = new CustomerProfilePaymentType();
			profileToCharge.setCustomerProfileId(customerProfileId);
			PaymentProfile paymentProfile = new PaymentProfile();
			paymentProfile.setPaymentProfileId(customerPaymentProfileId);
			profileToCharge.setPaymentProfile(paymentProfile);

			// Create the payment transaction request
			TransactionRequestType txnRequest = new TransactionRequestType();
			txnRequest.setTransactionType(TransactionTypeEnum.AUTH_CAPTURE_TRANSACTION.value());
			txnRequest.setProfile(profileToCharge);
			txnRequest.setAmount(new BigDecimal(amount).setScale(2, RoundingMode.CEILING));

			CreateTransactionRequest apiRequest = new CreateTransactionRequest();
			apiRequest.setTransactionRequest(txnRequest);
			CreateTransactionController controller = new CreateTransactionController(apiRequest);
			controller.execute();
			CreateTransactionResponse response = controller.getApiResponse();
			if (response != null) {
				// If API Response is ok, go ahead and check the transaction
				// response
				if (response.getMessages().getResultCode() == MessageTypeEnum.OK) {
					TransactionResponse result = response.getTransactionResponse();
					if (result.getMessages() != null) {
						paid = true;
						logger.info("Successfully created transaction with Transaction ID: " + result.getTransId());
						logger.info("Response Code: " + result.getResponseCode());
						logger.info("Message Code: " + result.getMessages().getMessage().get(0).getCode());
						logger.info("Description: " + result.getMessages().getMessage().get(0).getDescription());
						logger.info("Auth Code: " + result.getAuthCode());
						invoiceDetails.setReason(result.getMessages().getMessage().get(0).getDescription());
						invoiceDetails.setTxnRefId(result.getTransId());
					} else {
						logger.info("Failed Transaction.");
						if (response.getTransactionResponse().getErrors() != null) {
							logger.info("Error Code: "
									+ response.getTransactionResponse().getErrors().getError().get(0).getErrorCode());
							logger.info("Error message: "
									+ response.getTransactionResponse().getErrors().getError().get(0).getErrorText());
							invoiceDetails.setReason(
									response.getTransactionResponse().getErrors().getError().get(0).getErrorText());
						}
					}
				} else {
					logger.info("Failed Transaction.");
					if (response.getTransactionResponse() != null
							&& response.getTransactionResponse().getErrors() != null) {
						logger.info("Error Code: "
								+ response.getTransactionResponse().getErrors().getError().get(0).getErrorCode());
						logger.info("Error message: "
								+ response.getTransactionResponse().getErrors().getError().get(0).getErrorText());
						invoiceDetails.setReason(
								response.getTransactionResponse().getErrors().getError().get(0).getErrorText());
					} else {
						logger.info("Error Code: " + response.getMessages().getMessage().get(0).getCode());
						logger.info("Error message: " + response.getMessages().getMessage().get(0).getText());
						invoiceDetails.setReason(response.getMessages().getMessage().get(0).getText());
					}

				}
			} else {
				logger.info("Null Response.");
			}

		} catch (Exception e) {
			logger.info("ChargeCustomerProfile Error:: " + e.getMessage());
		} finally {
			if (paid) {
				invoiceDetails.setStatus("paid");
			} else {
				invoiceDetails.setStatus("unpaid");
			}
			pharmacyDao.addInvoiceDetails(invoiceDetails);
		}

		return paid;
	}

}
