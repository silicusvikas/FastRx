package com.parkwoodrx.fastrx.service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.parkwoodrx.fastrx.constants.FastRxErrorCodes;
import com.parkwoodrx.fastrx.exception.FastRxException;
import com.parkwoodrx.fastrx.model.BillingDetails;
import com.parkwoodrx.fastrx.model.NonRegPharmacy;
import com.parkwoodrx.fastrx.model.PharmacyLocation;
import com.parkwoodrx.fastrx.model.Prescription;
import com.parkwoodrx.fastrx.model.PrescriptionLog;
import com.parkwoodrx.fastrx.model.PrescriptionLog.Event;
import com.parkwoodrx.fastrx.model.PrescriptionLog.Status;
import com.parkwoodrx.fastrx.model.RespondPrescription;
import com.parkwoodrx.fastrx.model.User;
import com.parkwoodrx.fastrx.repository.PharmacyLocationDao;
import com.parkwoodrx.fastrx.repository.PrescriptionDao;
import com.parkwoodrx.fastrx.repository.PrescriptionTransactionDao;
import com.parkwoodrx.fastrx.repository.UserDao;
import com.parkwoodrx.fastrx.security.PasswordGenerator;

@Service
@PropertySource("file:efax.properties")
public class PrescriptionServiceImpl implements PrescriptionService {
	private static final Logger logger = LoggerFactory.getLogger(PrescriptionService.class);

	@Value("${efax.prescriptions.content}")
	private String prescriptionContent;

	@Autowired
	private PrescriptionDao prescriptionDao;

	@Autowired
	private UserDao userDao;

	@Autowired
	private PrescriptionTransactionDao prescriptionTransactionDao;

	@Autowired
	private PharmacyLocationDao pharmacyDao;

	@Autowired
	private PasswordGenerator passwordGenerator;

	@Autowired
	private FaxService faxService;

	@Override
	@Transactional
	public void batchInsertPrescriptionReqs(List<Prescription> prescriptions) {
		logger.debug("PrescriptionServiceImpl :: batchInsertPrescriptionReqs method");
		String pharmacist = prescriptions.get(0).getPharmacist();
		String reqToken = generateRequestToken();
		PharmacyLocation reqPharmacyLocation;
		int size = prescriptions.size();
		List<PrescriptionLog> prescriptionLogList = new ArrayList<PrescriptionLog>();
		PrescriptionLog prescriptionLog = new PrescriptionLog();
		PrescriptionLog prescriptionLogNew = null;
		String content = null;
		CompletableFuture<Map<String, String>> faxdDetails = null;
		try {
			logger.info("Fetching corpId  for location id: {}",prescriptions.get(0).getRequestingPharmacyId());
			/* fetch location from location id to get corpId */
			reqPharmacyLocation = pharmacyDao.getPharmacyLocationById(prescriptions.get(0).getRequestingPharmacyId());
		} catch (Exception e) {
			prescriptionLog.setRemarks(FastRxErrorCodes.NO_PHARMACY_FOUND_MSG);
			prescriptionLog.setStatus(Status.FAIL);
			for (int i = 0; i < size; i++) {
				prescriptionLogNew = setPrescriptionLogObject(prescriptions.get(i), Status.FAIL, Event.TRANSFER_IN,
						FastRxErrorCodes.NO_PHARMACY_FOUND_MSG);
				prescriptionLogList.add(prescriptionLogNew);
			}
			/* Adding prescription log */
			prescriptionTransactionDao.batchInsertPrescriptionLog(prescriptionLogList);
			logger.error("Error in fetching corpId by location id: {}",e.getMessage());
			throw new FastRxException(FastRxErrorCodes.NOT_FOUND_CODE, FastRxErrorCodes.NO_PHARMACY_FOUND_MSG);
		}
		NonRegPharmacy resPharmacyLocation;
		try {
			/*
			 * Fetching non-registered responding pharmacy by location id
			 */
			logger.info("Fetching non-registered responding pharmacy details of location id: {}",prescriptions.get(0).getRespondingPharmacyId());
			resPharmacyLocation = pharmacyDao.getNonRegPharmacyById(prescriptions.get(0).getRespondingPharmacyId());
		} catch (Exception e) {
			prescriptionLog.setRemarks(FastRxErrorCodes.NO_PHARMACY_FOUND_MSG);
			prescriptionLog.setStatus(Status.FAIL);
			for (int i = 0; i < size; i++) {
				prescriptionLogNew = setPrescriptionLogObject(prescriptions.get(i), Status.FAIL, Event.TRANSFER_IN,
						FastRxErrorCodes.NO_PHARMACY_FOUND_MSG);
				prescriptionLogList.add(prescriptionLogNew);
			}

			/* Adding prescription log */
			prescriptionTransactionDao.batchInsertPrescriptionLog(prescriptionLogList);
			logger.error("Error: {}",e.getMessage());
			throw new FastRxException(FastRxErrorCodes.NOT_FOUND_CODE, FastRxErrorCodes.NO_PHARMACY_FOUND_MSG);
		}

		BillingDetails billingDetails;
		try {
			/* fetch billing for requesting for particular corporation */
			logger.info("Fetching billing details of corporation id: {}",reqPharmacyLocation.getCorporationId());
			billingDetails = prescriptionTransactionDao
					.getBillingDetailsForCorpId(reqPharmacyLocation.getCorporationId());
		} catch (Exception e) {
			prescriptionLog.setRemarks(FastRxErrorCodes.BILLING_DETAILS_NOT_FOUND);
			prescriptionLog.setStatus(Status.FAIL);
			for (int i = 0; i < size; i++) {
				prescriptionLogNew = setPrescriptionLogObject(prescriptions.get(i), Status.FAIL, Event.TRANSFER_IN,
						FastRxErrorCodes.BILLING_DETAILS_NOT_FOUND);
				prescriptionLogList.add(prescriptionLogNew);
			}
			/* Adding prescription log */
			prescriptionTransactionDao.batchInsertPrescriptionLog(prescriptionLogList);
			logger.error("Error: {}",e.getMessage());
			throw new FastRxException(FastRxErrorCodes.NOT_FOUND_CODE, FastRxErrorCodes.BILLING_DETAILS_NOT_FOUND);
		}

		try {
			logger.info("Adding prescriptions to database");
			/* Adding prescription */
			prescriptionDao.batchInsertPrescriptionReqs(prescriptions, reqToken, reqPharmacyLocation.getCorporationId(),
					0);

			/* Adding prescription log */
			logger.info("Adding prescription log to database");
			for (int i = 0; i < size; i++) {
				prescriptionLogNew = setPrescriptionLogObject(prescriptions.get(i), Status.SUCCESS, Event.TRANSFER_IN,
						"");
				prescriptionLogList.add(prescriptionLogNew);
			}
			prescriptionTransactionDao.batchInsertPrescriptionLog(prescriptionLogList);

		} catch (DataAccessException e) {

			logger.error("Error: {}",e.getMessage());
			throw new FastRxException(FastRxErrorCodes.CREATE_ERROR_CODE,
					FastRxErrorCodes.ERROR_ADDING_PRESCRIPTION_REQUEST);
		}

		try {
			/* fetch prescriptions for reqToken */
			logger.info("Fetching prescription list by reqToken: {}",reqToken);
			prescriptions = prescriptionDao.getPrescriptionListForReqToken(reqToken);
		} catch (Exception e) {
			logger.error("Error: {}",e.getMessage());
			throw new FastRxException(FastRxErrorCodes.NOT_FOUND_CODE, FastRxErrorCodes.PRESCRIPTION_LIST_NOT_FOUND);
		}

		try {

			/* insert records in transaction table */
			logger.info("Adding records in transaction table");
			prescriptionTransactionDao.batchInsertPrescriptionTransactions(prescriptions, billingDetails, null);
		} catch (Exception e) {
			logger.error("Error: {}",e.getMessage());
			throw new FastRxException(FastRxErrorCodes.CREATE_ERROR_CODE,
					FastRxErrorCodes.ERROR_ADDING_TRANSACTION_REQUEST);
		}

		/* Send Fax */
		logger.info("Sending Fax......");
		for (int i = 0; i < size; i++) {
			try {
				prescriptions.get(i).setPharmacist(pharmacist);
				content = createContentForHtmlFile(prescriptions.get(i), resPharmacyLocation, reqPharmacyLocation);
				createHTMLFile(content);
				faxdDetails = faxService.sendFax(resPharmacyLocation.getPhy_efax(), "prescription.html");
				if (null != faxdDetails.get().get("Status") && null != faxdDetails.get().get("FaxJobId")) {
					prescriptionDao.addFaxDetails(faxdDetails.get().get("FaxJobId"), faxdDetails.get().get("Status"),
							prescriptions.get(0).getFastRxAudit().getCreatedBy(), prescriptions.get(i).getId());
				}

			} catch (Exception e) {
				logger.info("Error: {}", e);
				throw new FastRxException(FastRxErrorCodes.CREATE_ERROR_CODE, e.getMessage());
			}
		}

	}

	public String createContentForHtmlFile(Prescription prescription, NonRegPharmacy resPharmacyLocation,
			PharmacyLocation reqPharmacyLocation) {
		String result = null;
		try {
			String reqadd = reqPharmacyLocation.getCity() + "," + reqPharmacyLocation.getState() + ","
					+ reqPharmacyLocation.getZipcode();
			String resadd = resPharmacyLocation.getPhy_city() + "," + resPharmacyLocation.getPhy_state() + ","
					+ resPharmacyLocation.getPhy_zip();
			String pharmacistName = prescription.getPharmacist();

			if (prescription.getRespTime() != null) {
				result = MessageFormat.format(prescriptionContent, reqPharmacyLocation.getFaxNumber(), prescription.getRespTime(),
						reqPharmacyLocation.getPharmacyName(), reqPharmacyLocation.getAddress(), reqadd,
						reqPharmacyLocation.getPhoneNumber(), reqPharmacyLocation.getFaxNumber(),
						reqPharmacyLocation.getDeaNumber(), resPharmacyLocation.getPharmacy_name()+" "+resPharmacyLocation.getStore_number(),
						resPharmacyLocation.getStore_address(), resadd, resPharmacyLocation.getPhy_phone(),
						resPharmacyLocation.getPhy_efax(), resPharmacyLocation.getDea(), pharmacistName,
						passwordGenerator.getDecodedString(prescription.getPatientFirstName()),
						passwordGenerator.getDecodedString(prescription.getPatientLastName()),
						passwordGenerator.getDecodedString(prescription.getPatientDob()), prescription.getPrescNumber(),
						prescription.getPrescDrugName(), prescription.getReqPharmacyComments(),
						passwordGenerator.getDecodedString(prescription.getPatientAddress()));

			} else {
				result = MessageFormat.format(prescriptionContent, reqPharmacyLocation.getFaxNumber(), "00:00",
						reqPharmacyLocation.getPharmacyName(), reqPharmacyLocation.getAddress(), reqadd,
						reqPharmacyLocation.getPhoneNumber(), reqPharmacyLocation.getFaxNumber(),
						reqPharmacyLocation.getDeaNumber(), resPharmacyLocation.getPharmacy_name()+" "+resPharmacyLocation.getStore_number(),
						resPharmacyLocation.getStore_address(), resadd, resPharmacyLocation.getPhy_phone(),
						resPharmacyLocation.getPhy_efax(), resPharmacyLocation.getDea(), pharmacistName,
						passwordGenerator.getDecodedString(prescription.getPatientFirstName()),
						passwordGenerator.getDecodedString(prescription.getPatientLastName()),
						passwordGenerator.getDecodedString(prescription.getPatientDob()), prescription.getPrescNumber(),
						prescription.getPrescDrugName(), prescription.getReqPharmacyComments(),
						passwordGenerator.getDecodedString(prescription.getPatientAddress()));
			}

		} catch (Exception e) {
			logger.info("Exception in sending fax:: {}", e);
			throw new FastRxException(FastRxErrorCodes.CREATE_ERROR_CODE, FastRxErrorCodes.EFAX_SENDING_ERROR_MSG);
		}

		return result;

	}

	/* Create Dynamic HTML for prescription */
	public static void createHTMLFile(String content) {
		BufferedWriter bw = null;
		FileWriter fw = null;
		try {
			fw = new FileWriter("prescription.html");
			bw = new BufferedWriter(fw);
			bw.write(content);

		} catch (IOException e) {
			logger.info("Exception in sending fax:: {}", e);
		} finally {

			try {

				if (bw != null)
					bw.close();

				if (fw != null)
					fw.close();

			} catch (IOException ex) {
				logger.info("Exception in sending fax:: {}", ex);
			}

		}

	}

	public PrescriptionLog setPrescriptionLogObject(Prescription prescription, Status status, Event event,
			String remarks) {
		PrescriptionLog presLog = new PrescriptionLog();
		presLog.setPatientFirstName(prescription.getPatientFirstName());
		presLog.setPatientLastName(prescription.getPatientLastName());
		presLog.setPatientAddress(prescription.getPatientAddress());
		presLog.setPatientDob(prescription.getPatientDob());
		presLog.setPrescNumber(prescription.getPrescNumber());
		presLog.setPrescDrugName(prescription.getPrescDrugName());

		/* setting audit fields */
		presLog.setStatus(status);
		presLog.setEvent(event);
		presLog.setRemarks(remarks);
		presLog.setUpdatedBy(prescription.getFastRxAudit().getCreatedBy());
		return presLog;

	}

	@Override
	public List<Prescription> getListOfPrescriptionsForLocation(long locationId) {
		logger.debug("PrescriptionServiceImpl :: getListOfPrescriptionsForLocation method");
		List<Prescription> prescriptions = null;
		try {
			prescriptions = prescriptionDao.getListOfPrescriptionsForLocation(locationId);
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			throw new FastRxException(FastRxErrorCodes.NOT_FOUND_CODE, FastRxErrorCodes.PRESCRIPTION_LIST_NOT_FOUND);
		}
		return prescriptions;
	}

	@Override
	public RespondPrescription getPrescriptionDetailsById(long prescriptionId) {
		logger.debug("PrescriptionServiceImpl :: getPrescriptionDetailsById method");
		RespondPrescription respondPrescription = null;
		try {
			respondPrescription = prescriptionDao.getPrescriptionDetailsById(prescriptionId);
			if (respondPrescription.getPrescription().getFastRxAudit().getUpdatedBy() != null) {
				User user = userDao
						.findByUsername(respondPrescription.getPrescription().getFastRxAudit().getUpdatedBy());
				respondPrescription.getRespondingUser().setFirstName(user.getFirstName());
				respondPrescription.getRespondingUser().setLastName(user.getLastName());
			}
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			throw new FastRxException(FastRxErrorCodes.NOT_FOUND_CODE, FastRxErrorCodes.PRESCRIPTION_NOT_FOUND);
		}

		return respondPrescription;
	}

	private String generateRequestToken() {
		Random random = new Random();
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		StringBuilder token = new StringBuilder();
		token.append(random.nextInt(10000));
		token.append(timestamp.getTime());
		return token.toString();
	}

	private PrescriptionLog comparePrescription(Prescription oldPrescription, Prescription newPrescription) {
		PrescriptionLog prescriptionLog = new PrescriptionLog();
		try {

			if (!oldPrescription.getPrescNumber().equals(newPrescription.getPrescNumber())) {
				prescriptionLog.setPrescNumber(newPrescription.getPrescNumber());
			}
			if (!passwordGenerator.getDecodedString(oldPrescription.getPatientFirstName())
					.equals(newPrescription.getPatientFirstName())) {
				prescriptionLog.setPatientFirstName(newPrescription.getPatientFirstName());
			}
			if (!passwordGenerator.getDecodedString(oldPrescription.getPatientLastName())
					.equals(newPrescription.getPatientLastName())) {
				prescriptionLog.setPatientLastName(newPrescription.getPatientLastName());
			}
			if (!passwordGenerator.getDecodedString(oldPrescription.getPatientAddress())
					.equals(newPrescription.getPatientAddress())) {
				prescriptionLog.setPatientAddress(newPrescription.getPatientAddress());
			}
			if (!oldPrescription.getPrescDrugName().equals(newPrescription.getPrescDrugName())) {
				prescriptionLog.setPrescDrugName(newPrescription.getPrescDrugName());
			}
		} catch (Exception e) {
			logger.debug(e.getMessage());
		}
		return prescriptionLog;

	}

	@Override
	public void respondPrescription(Prescription prescription) {
		logger.debug("PrescriptionServiceImpl :: respondPrescription method");
		PharmacyLocation pharmacyLocation;
		PrescriptionLog prescriptionLog = null;
		Prescription oldPrescription = null;
		try {
			oldPrescription = prescriptionDao.getPrescriptionDetailsById(prescription.getId()).getPrescription();
			prescriptionLog = comparePrescription(oldPrescription, prescription);
			prescriptionLog.setEvent(Event.TRANSFER_OUT);
			prescriptionLog.setUpdatedBy(prescription.getFastRxAudit().getUpdatedBy());
			// fetch location from location id to get corpId
			pharmacyLocation = pharmacyDao.getPharmacyLocationById(prescription.getRespondingPharmacyId());
		} catch (Exception e) {
			prescriptionLog.setRemarks(FastRxErrorCodes.NO_PHARMACY_FOUND_MSG);
			prescriptionLog.setStatus(Status.FAIL);
			prescriptionTransactionDao.addPrescriptionLog(prescriptionLog);
			logger.error(e.getMessage());
			throw new FastRxException(FastRxErrorCodes.NOT_FOUND_CODE, FastRxErrorCodes.NO_PHARMACY_FOUND_MSG);
		}

		BillingDetails billingDetails;
		try {
			// fetch pricing for requesting for particular corporation
			billingDetails = prescriptionTransactionDao.getBillingDetailsForCorpId(pharmacyLocation.getCorporationId());
		} catch (Exception e) {
			prescriptionLog.setRemarks(FastRxErrorCodes.BILLING_DETAILS_NOT_FOUND);
			prescriptionLog.setStatus(Status.FAIL);
			prescriptionTransactionDao.addPrescriptionLog(prescriptionLog);
			logger.error(e.getMessage());
			throw new FastRxException(FastRxErrorCodes.NOT_FOUND_CODE, FastRxErrorCodes.BILLING_DETAILS_NOT_FOUND);
		}

		try {
			prescriptionDao.respondPrescription(prescription);
		} catch (DataAccessException e) {
			prescriptionLog.setRemarks(FastRxErrorCodes.PRESCRIPTION_UPDATION_ERROR_MSG);
			prescriptionLog.setStatus(Status.FAIL);
			prescriptionTransactionDao.addPrescriptionLog(prescriptionLog);
			logger.error(e.getMessage());
			throw new FastRxException(FastRxErrorCodes.UPDATE_ERROR_CODE,
					FastRxErrorCodes.PRESCRIPTION_UPDATION_ERROR_MSG);
		}

		try {
			// insert records in transaction table
			prescriptionTransactionDao.updatePrescriptionResponse(prescription, billingDetails);
			prescriptionLog.setStatus(Status.SUCCESS);
			prescriptionTransactionDao.addPrescriptionLog(prescriptionLog);
		} catch (Exception e) {
			prescriptionLog.setRemarks(FastRxErrorCodes.PRESCRIPTION_TRANSAC_UPDATION_ERROR_MSG);
			prescriptionLog.setStatus(Status.FAIL);
			prescriptionTransactionDao.addPrescriptionLog(prescriptionLog);
			logger.error(e.getMessage());
			throw new FastRxException(FastRxErrorCodes.UPDATE_ERROR_CODE,
					FastRxErrorCodes.PRESCRIPTION_TRANSAC_UPDATION_ERROR_MSG);
		}
	}

	@Override
	public List<PrescriptionLog> getPrescriptionLogs() {
		List<PrescriptionLog> prescriptionLog = null;
		try {
			prescriptionLog = prescriptionTransactionDao.getPrescriptionLogs();
		} catch (Exception e) {
			logger.info("SERVICE::" + e.getMessage());
			throw new FastRxException(FastRxErrorCodes.NOT_FOUND_CODE, FastRxErrorCodes.NO_LOGS_FOUND);
		}

		return prescriptionLog;
	}

	@Override
	public List<PrescriptionLog> searchPrescriptionLogByNameAndDate(String name, String startDate, String endDate) {
		List<PrescriptionLog> prescriptionLog = null;
		try {
			prescriptionLog = prescriptionTransactionDao.searchUserLogByNameAndDate(name, startDate, endDate);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new FastRxException(FastRxErrorCodes.NOT_FOUND_CODE, FastRxErrorCodes.NO_LOGS_FOUND);
		}
		return prescriptionLog;

	}

	@Override
	public void updateSendFaxStatus(String prescriptionId) {
		try {
			prescriptionTransactionDao.updateSendFaxStatus(prescriptionId);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new FastRxException(FastRxErrorCodes.UPDATE_ERROR_CODE,
					FastRxErrorCodes.PRESCRIPTION_UPDATION_ERROR_MSG);
		}
	}

	@Override
	@Transactional
	public void updateFaxStatusByFaxId(String updatedBy, Map<String, Integer> faxStatusDetail) {
		try {
			for (String faxJobId : faxStatusDetail.keySet()) {
				if (faxStatusDetail.get(faxJobId)==1) {
					prescriptionDao.updateFaxStatusByFaxId(updatedBy, faxJobId,"Success");
				} else if (faxStatusDetail.get(faxJobId)==2) {
					prescriptionDao.updateFaxStatusByFaxId(updatedBy, faxJobId, "Fail");
				}

			}

		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new FastRxException(FastRxErrorCodes.UPDATE_ERROR_CODE,
					FastRxErrorCodes.PRESCRIPTION_UPDATION_ERROR_MSG);
		}
	}

	@Override
	public void resendPrescription(RespondPrescription prescription) {
		String content = null;
		CompletableFuture<Map<String, String>> faxdDetails = null;
		try {

			content = createContentForHtmlFile(prescription.getPrescription(), prescription.getRespondingPharmacy(),
					prescription.getRequestingPharmacy());
			createHTMLFile(content);
			faxdDetails = faxService.sendFax(prescription.getRespondingPharmacy().getPhy_efax(), "prescription.html");
			if (null != faxdDetails.get().get("Status") && null != faxdDetails.get().get("FaxJobId")) {
				prescriptionDao.addFaxDetails(faxdDetails.get().get("FaxJobId"), faxdDetails.get().get("Status"),
						prescription.getPrescription().getFastRxAudit().getCreatedBy(),
						prescription.getPrescription().getId());
				prescriptionTransactionDao.updateSendFaxStatus(String.valueOf(prescription.getPrescription().getId()));
			}

		} catch (Exception e) {
			throw new FastRxException(FastRxErrorCodes.UPDATE_ERROR_CODE,
					FastRxErrorCodes.PRESCRIPTION_RESEND_ERROR_MSG);
		}

	}
}
