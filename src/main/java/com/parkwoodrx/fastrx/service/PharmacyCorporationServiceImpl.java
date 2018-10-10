package com.parkwoodrx.fastrx.service;

import java.sql.Timestamp;
import java.util.List;

import org.joda.time.MutableDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.parkwoodrx.fastrx.constants.FastRxErrorCodes;
import com.parkwoodrx.fastrx.exception.FastRxException;
import com.parkwoodrx.fastrx.model.BillingDetails;
import com.parkwoodrx.fastrx.model.CreditcardDetails;
import com.parkwoodrx.fastrx.model.MultiSelectDropDownObject;
import com.parkwoodrx.fastrx.model.PaymentType;
import com.parkwoodrx.fastrx.model.Pharmacy;
import com.parkwoodrx.fastrx.model.PharmacyCorporation;
import com.parkwoodrx.fastrx.model.PharmacyCorporationDetails;
import com.parkwoodrx.fastrx.model.State;
import com.parkwoodrx.fastrx.repository.PharmacyCorporationDao;
import com.parkwoodrx.fastrx.security.PasswordGenerator;

@Service
public class PharmacyCorporationServiceImpl implements PharmacyCorporationService {
	private static final Logger logger = LoggerFactory.getLogger(PharmacyCorporationServiceImpl.class);

	@Autowired
	private PharmacyCorporationDao pharmacyDao;

	@Autowired
	private PasswordGenerator passwordGenerator;

	@Autowired
	private EmailService emailService;

	@Override
	public List<Pharmacy> getPharmacies() {
		return null;

	}

	@Override
	@Transactional
	public long registerPharmacyCorporation(PharmacyCorporationDetails pharmacyDetails) throws FastRxException {
		long corporationId = 0;
		try {
			if (pharmacyDao.getCorporationCount(pharmacyDetails.getPharmacyCorporation().getCorporationName()) != 0) {
				throw new FastRxException(FastRxErrorCodes.DUPLICATE_CODE, FastRxErrorCodes.DUPLICATE_PHARMACY_MSG);
			}
			String encryptedPassword;
			encryptedPassword = passwordGenerator.generateHash(pharmacyDetails.getUserDetails().getPassword());
			pharmacyDetails.getUserDetails().setPassword(encryptedPassword);
			corporationId = pharmacyDao.registerPharmacyCorporation(pharmacyDetails);
			emailService.sendUserRegistrationEmail(pharmacyDetails.getUserDetails().getUsername(),
					pharmacyDetails.getPharmacyCorporation().getCorporationName());
		} catch (FastRxException e) {
			throw e;
		} catch (Exception e) {
			logger.info("SERVICE::" + e.getMessage());
			throw new FastRxException(FastRxErrorCodes.CREATE_ERROR_CODE, FastRxErrorCodes.ERROR_CREATING_PHARMACY_MSG,
					e.getMessage());

		}
		return corporationId;

	}

	@Override
	public List<PharmacyCorporation> getPharmacyCorporationsByNameOrPhone(String corpName, String phoneNo)
			throws FastRxException {
		List<PharmacyCorporation> corporationList = null;
		if (corpName.isEmpty() && phoneNo.isEmpty()) {
			throw new FastRxException(FastRxErrorCodes.EMPTY_FIELD_CODE, FastRxErrorCodes.EMPTY_SEARCH_FIELDS_MSG);
		}
		try {
			corporationList = pharmacyDao.getPharmacyCorporationsByNameOrPhone(corpName, phoneNo);
			if (null == corporationList || corporationList.size() == 0) {
				throw new FastRxException(FastRxErrorCodes.NOT_FOUND_CODE, FastRxErrorCodes.NO_CORPORATION_FOUND_MSG);
			}
		} catch (Exception e) {
			logger.info("SERVICE::" + e.getMessage());
			throw new FastRxException(FastRxErrorCodes.NOT_FOUND_CODE, FastRxErrorCodes.NO_CORPORATION_FOUND_MSG);
		}

		return corporationList;
	}

	@Override
	public List<PharmacyCorporation> getAllPharmacyCorporations() throws FastRxException {
		List<PharmacyCorporation> corporationList = null;
		try {
			corporationList = pharmacyDao.getAllPharmacyCorporations();
		} catch (Exception e) {
			logger.info("SERVICE::" + e.getMessage());
			throw new FastRxException(FastRxErrorCodes.NOT_FOUND_CODE, FastRxErrorCodes.NO_CORPORATION_FOUND_MSG);
		}

		return corporationList;
	}

	@Override
	public PharmacyCorporationDetails getCorporationProfileById(Long id) throws FastRxException {
		PharmacyCorporationDetails corporationDetail = null;
		if (id == 0) {
			throw new FastRxException(FastRxErrorCodes.EMPTY_FIELD_CODE, FastRxErrorCodes.EMPTY_CORPORATION_ID_MSG);
		}
		try {
			corporationDetail = pharmacyDao.getCorporationProfileById(id);
			logger.info("Corporation Detail::" + corporationDetail);
		} catch (Exception e) {
			logger.info("SERVICE::" + e.getMessage());
			throw new FastRxException(FastRxErrorCodes.NOT_FOUND_CODE, FastRxErrorCodes.NO_CORPORATION_FOUND_MSG);
		}

		return corporationDetail;
	}

	@Override
	public List<PaymentType> getPaymentTypeList() {
		List<PaymentType> paymentTypeList = null;
		try {
			paymentTypeList = pharmacyDao.getPaymentTypeList();
		} catch (Exception e) {
			logger.info("SERVICE::" + e.getMessage());
			throw new FastRxException(FastRxErrorCodes.NOT_FOUND_CODE, FastRxErrorCodes.NO_CORPORATION_FOUND_MSG);
		}

		return paymentTypeList;
	}

	@Override
	public List<State> getStateList() {
		List<State> stateList = null;
		try {
			stateList = pharmacyDao.getStateList();
		} catch (Exception e) {
			logger.info("SERVICE::" + e.getMessage());
			throw new FastRxException(FastRxErrorCodes.NOT_FOUND_CODE, FastRxErrorCodes.NO_STATE_FOUND_MSG);
		}

		return stateList;
	}

	@Override
	@Transactional
	public void updatePharmacyCorporation(PharmacyCorporation pharmacyCorporation) throws FastRxException {
		try {
			pharmacyDao.updatePharmacyCorporation(pharmacyCorporation);
		} catch (Exception e) {
			logger.info("SERVICE::" + e.getMessage());
			throw new FastRxException(FastRxErrorCodes.UPDATE_ERROR_CODE,
					FastRxErrorCodes.CORPORATION_UPDATE_ERROR_MSG);
		}
	}

	@Override
	public void updatePharmacyCorporationBilling(BillingDetails billingDetails) throws FastRxException {
		BillingDetails lastBillingDetails = new BillingDetails();
		try {
			logger.info("fetching billing details for corporation Id: ",billingDetails.getCorporationId());
			if (pharmacyDao.getBillingDetailsByCorporationId(billingDetails.getCorporationId()) != 0) {
				lastBillingDetails = pharmacyDao.getBillingDetailByCorporationId(billingDetails.getCorporationId());
				if (pharmacyDao.getTransactionType(billingDetails.getCorporationId())
						.contains(billingDetails.getTransactionType())) {
					billingDetails.setStart_Day(lastBillingDetails.getStart_Day());
					billingDetails.setStart_txnType(billingDetails.getTransactionType());
					pharmacyDao.updatePharmacyCorporationBilling(billingDetails);
				} else {
					if (null != billingDetails.getTransactionType()) {
						billingDetails.setStart_txnType(billingDetails.getTransactionType());
					} else {
						throw new FastRxException(FastRxErrorCodes.UPDATE_ERROR_CODE,
								FastRxErrorCodes.TRANSACTION_TYPE_EMPTY_MSG);
					}
					billingDetails.setTransactionType(lastBillingDetails.getTransactionType());
					billingDetails.setTransactionTypeChangeLog(new Timestamp(System.currentTimeMillis()));
					MutableDateTime dateTime = new MutableDateTime();
					dateTime.addMonths(1);
					dateTime.setDayOfMonth(1);
					dateTime.setMillisOfDay(0);
					Timestamp ts = new Timestamp(dateTime.getMillis());
					// insert start_date
					billingDetails.setStart_Day(ts);
				}
				pharmacyDao.updatePharmacyCorporationBilling(billingDetails);

			} else {
				if (null != billingDetails.getTransactionType()) {
					lastBillingDetails.setTransactionType(billingDetails.getTransactionType());
				} else {
					throw new FastRxException(FastRxErrorCodes.UPDATE_ERROR_CODE,
							FastRxErrorCodes.TRANSACTION_TYPE_EMPTY_MSG);
				}
				billingDetails.getFastRxAudit().setCreatedBy(billingDetails.getFastRxAudit().getUpdatedBy());
				pharmacyDao.addBillingdetails(billingDetails);
			}

		} catch (Exception e) {
			logger.info("SERVICE::" + e.getMessage());
			throw new FastRxException(FastRxErrorCodes.UPDATE_ERROR_CODE, FastRxErrorCodes.BILLING_UPDATE_ERROR_MSG);
		}

	}

	@Override
	public int getPaymentProfileByCorporationId(long corporationId) {
		int count;
		try {
			count = pharmacyDao.getPaymentProfileByCorporationId(corporationId);
		} catch (Exception e) {
			logger.info("SERVICE::" + e.getMessage());
			throw new FastRxException(FastRxErrorCodes.UPDATE_ERROR_CODE, FastRxErrorCodes.PAYMENT_UPDATE_ERROR_MSG);
		}
		return count;
	}

	@Override
	public void updatePaymentType(String paymentType, long corporationId, String updatedBy) {
		try {
			pharmacyDao.updatePaymentType(paymentType, corporationId, updatedBy);
		} catch (Exception e) {
			logger.info("SERVICE::" + e.getMessage());
			throw new FastRxException(FastRxErrorCodes.UPDATE_ERROR_CODE, FastRxErrorCodes.PAYMENT_UPDATE_ERROR_MSG);
		}

	}

	@Override
	public CreditcardDetails getPaymentProfileIdByCorporationId(long corporationId) {
		CreditcardDetails profile;
		try {
			profile = pharmacyDao.getPaymentProfileIdByCorporationId(corporationId);
		} catch (Exception e) {
			logger.info("SERVICE::" + e.getMessage());
			throw new FastRxException(FastRxErrorCodes.UPDATE_ERROR_CODE, FastRxErrorCodes.PAYMENT_UPDATE_ERROR_MSG);
		}
		return profile;
	}

	@Override
	public void updatedStatusOfCorporation(long corporationId, String updatedBy, String status) {
		PharmacyCorporationDetails corporationDetail = null;
		try {
			corporationDetail = pharmacyDao.getCorporationProfileById(corporationId);
			if (!status.equalsIgnoreCase(corporationDetail.getPharmacyCorporation().getActive())
					&& null != corporationDetail.getPharmacyCorporation().getPaymentType()
					&& !corporationDetail.getPharmacyCorporation().getPaymentType().isEmpty()) {
				pharmacyDao.updateStatusOfCorporation(corporationId, updatedBy, status);
			} else {
				throw new FastRxException(FastRxErrorCodes.UPDATE_ERROR_CODE,
						FastRxErrorCodes.PHARMACY_CORPORATION_ACTIVE_ERROR_MSG);
			}

		} catch (Exception e) {
			logger.info("SERVICE::" + e.getMessage());
			throw e;
		}

	}

	@Override
	public void activateCorporation(long corporationId, String updatedBy, String status) {
		try {
			pharmacyDao.updateStatusOfCorporation(corporationId, updatedBy, status);

		} catch (Exception e) {
			logger.info("SERVICE::" + e.getMessage());
			throw new FastRxException(FastRxErrorCodes.UPDATE_ERROR_CODE,
					FastRxErrorCodes.PHARMACY_CORPORATION_ACTIVE_ERROR_MSG);
		}

	}

	@Override
	public boolean isCorporationActive(long corporationId) {
		PharmacyCorporation pharmacyCorporation = null;
		Boolean isCorporationActive = false;
		try {
			pharmacyCorporation = pharmacyDao.getPharmacyCorporationById(corporationId);
			if (pharmacyCorporation.getActive().equalsIgnoreCase("Y")) {
				isCorporationActive = true;
			}
		} catch (Exception e) {
			logger.error("Exception in retriving corporation: ", e);
			throw new FastRxException(FastRxErrorCodes.NOT_FOUND_CODE, FastRxErrorCodes.NO_CORPORATION_FOUND_MSG);
		}

		return isCorporationActive;
	}

	@Override
	public List<MultiSelectDropDownObject> getAllPharmacyCorpsMultiSelect() throws FastRxException {
		List<MultiSelectDropDownObject> corporationList = null;
		try {
			corporationList = pharmacyDao.getAllPharmacyCorpsForMultiSelect();
		} catch (Exception e) {
			logger.info("SERVICE::" + e.getMessage());
			throw new FastRxException(FastRxErrorCodes.NOT_FOUND_CODE, FastRxErrorCodes.NO_CORPORATION_FOUND_MSG);
		}

		return corporationList;
	}

	@Override
	public String getPharmacyCorpNameById(long corporationId) {
		String corpName = "";
		try {
			corpName = pharmacyDao.getPharmacyCorporationById(corporationId).getCorporationName();
		} catch (Exception e) {
			logger.info("SERVICE::" + e.getMessage());
		}
		return corpName;
	}

}
