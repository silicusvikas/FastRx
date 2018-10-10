package com.parkwoodrx.fastrx.webservice;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.QueryParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.parkwoodrx.fastrx.exception.FastRxException;
import com.parkwoodrx.fastrx.model.FaxDetails;
import com.parkwoodrx.fastrx.model.Prescription;
import com.parkwoodrx.fastrx.model.PrescriptionLog;
import com.parkwoodrx.fastrx.model.RespondPrescription;
import com.parkwoodrx.fastrx.security.PasswordGenerator;
import com.parkwoodrx.fastrx.service.FaxService;
import com.parkwoodrx.fastrx.service.PrescriptionService;
import com.parkwoodrx.fastrx.webservice.FastRxResponse.Status;

@RestController
@RequestMapping("/prescription")
public class PrescriptionController {
	private static final Logger logger = LoggerFactory.getLogger(PrescriptionController.class);

	@Autowired
	PrescriptionService prescriptionService;

	@Autowired
	private PasswordGenerator passwordGenerator;

	@Autowired
	private FaxService faxService;

	@RequestMapping(method = RequestMethod.PUT, value = "/addPrescriptionReqs")
	public ResponseEntity<FastRxResponse> addPrescriptionReqs(@RequestBody List<Prescription> prescriptions) {
		logger.info("PrescriptionController :: addPrescription method");

		FastRxResponse response = new FastRxResponse();
		try {
			prescriptionService.batchInsertPrescriptionReqs(prescriptions);
			response.setStatus(Status.SUCCESS);
			response.setMessage("Prescription requested successfully.");
			logger.info("Prescription added successfully.");

		} catch (FastRxException e) {
			if(e.getMessage().contains("Invalid Fax Number")){
				response.setMessage("Invalid Fax Number");
			}else{
				response.setMessage("Error requesting prescription.");
			}
			response.setStatus(Status.ERROR);
			logger.error("Adding prescription request failed. Exception: " + e.getMessage());
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/getPrescriptionList", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<FastRxResponse> getListOfPrescriptionsForLocation(
			@QueryParam("locationId") String locationId) {
		logger.info("Fetching list of all prescriptions for location : {}", locationId);
		List<Prescription> prescriptionList = null;
		FastRxResponse response = new FastRxResponse();
		try {
			prescriptionList = prescriptionService.getListOfPrescriptionsForLocation(Long.parseLong(locationId));
			for (Prescription prescription : prescriptionList) {
				prescription
						.setPatientFirstName(passwordGenerator.getDecodedString(prescription.getPatientFirstName()));
				prescription.setPatientLastName(passwordGenerator.getDecodedString(prescription.getPatientLastName()));
				prescription.setPatientDob(passwordGenerator.getDecodedString(prescription.getPatientDob()));
				prescription.setPatientAddress(passwordGenerator.getDecodedString(prescription.getPatientAddress()));
			}
			response.setData(prescriptionList);
			response.setStatus(Status.SUCCESS);
			response.setMessage("Fetched list of all prescriptions successfully.");
			logger.info("Fetched list of all prescriptions successfully");
		} catch (FastRxException e) {
			response.setStatus(Status.ERROR);
			response.setMessage("Error in fetching list of all prescriptions");
			logger.error("Error in fetching list of all prescriptions. Exception:" + e.getMessage());

		}
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	@RequestMapping(value = "/getPrescriptionDetails", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<FastRxResponse> getPrescriptionDetailsById(
			@QueryParam("prescriptionId") String prescriptionId) {
		logger.info("Fetching prescription details for id : {}", prescriptionId);
		RespondPrescription respondPrescription = null;
		FastRxResponse response = new FastRxResponse();
		try {
			respondPrescription = prescriptionService.getPrescriptionDetailsById(Long.parseLong(prescriptionId));
			respondPrescription.getPrescription().setPatientFirstName(
					passwordGenerator.getDecodedString(respondPrescription.getPrescription().getPatientFirstName()));
			respondPrescription.getPrescription().setPatientLastName(
					passwordGenerator.getDecodedString(respondPrescription.getPrescription().getPatientLastName()));
			respondPrescription.getPrescription().setPatientDob(
					passwordGenerator.getDecodedString(respondPrescription.getPrescription().getPatientDob()));
			respondPrescription.getPrescription().setPatientAddress(
					passwordGenerator.getDecodedString(respondPrescription.getPrescription().getPatientAddress()));

			response.setData(respondPrescription);
			response.setStatus(Status.SUCCESS);
			response.setMessage("Fetched prescription details successfully.");
			logger.info("Fetched prescription details successfully");
		} catch (FastRxException e) {
			response.setStatus(Status.ERROR);
			response.setMessage("Error in fetching prescription details");
			logger.error("Error in fetching prescription details. Exception:" + e.getMessage());
		}
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	@RequestMapping(value = "/resendefax", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<FastRxResponse> reSendEfax(@QueryParam("prescriptionId") String prescriptionId,
			@QueryParam("pharmacist") String pharmacist) {
		logger.info("Fetching prescription details for id : {}", prescriptionId);
		RespondPrescription prescriptionDetails = null;
		FastRxResponse response = new FastRxResponse();
		try {
			prescriptionDetails = prescriptionService.getPrescriptionDetailsById(Long.parseLong(prescriptionId));
			prescriptionDetails.getPrescription().setPharmacist(pharmacist);
			prescriptionService.resendPrescription(prescriptionDetails);
			response.setStatus(Status.SUCCESS);
			response.setMessage("Resend prescription Fax successfully.");
			logger.info("Resend prescription Fax successfully.");
		} catch (FastRxException e) {
			logger.error("Prescription resending failed. Exception: " + e.getMessage());
			response.setStatus(Status.ERROR);
			response.setMessage("Error in resending prescription FAX");
		}

		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	@RequestMapping(method = RequestMethod.POST, value = "/respondPresc")
	public ResponseEntity<FastRxResponse> respondPrescription(@RequestBody Prescription prescription) {
		logger.info("PrescriptionController :: respondPrescription method");
		FastRxResponse response = new FastRxResponse();
		try {
			prescriptionService.respondPrescription(prescription);
			response.setStatus(Status.SUCCESS);
			response.setMessage("Prescription Responded successfully.");
			logger.info("Prescription details updated successfully.");
		} catch (FastRxException e) {
			response.setStatus(Status.ERROR);
			response.setMessage("Prescription Responding failed.");
			logger.error("Prescription Responding failed. Exception: " + e.getMessage());
		}
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	@RequestMapping(method = RequestMethod.GET, value = "/prescription-logs")
	public ResponseEntity<FastRxResponse> getPrescriptionLogs() {
		logger.info("=====================GetPrescriptionLogs=====================");

		FastRxResponse response = new FastRxResponse();
		List<PrescriptionLog> prescriptionLogList = null;

		try {
			prescriptionLogList = prescriptionService.getPrescriptionLogs();
			if (null != prescriptionLogList || !prescriptionLogList.isEmpty() || prescriptionLogList.size() != 0) {

				for (PrescriptionLog prescriptionLog : prescriptionLogList) {
					prescriptionLog.setPatientFirstName(
							passwordGenerator.getDecodedString(prescriptionLog.getPatientFirstName()));
					prescriptionLog.setPatientLastName(
							passwordGenerator.getDecodedString(prescriptionLog.getPatientLastName()));
					prescriptionLog.setPatientDob(passwordGenerator.getDecodedString(prescriptionLog.getPatientDob()));
					prescriptionLog
							.setPatientAddress(passwordGenerator.getDecodedString(prescriptionLog.getPatientAddress()));

				}
				response.setData(prescriptionLogList);
				response.setStatus(Status.SUCCESS);
				response.setMessage("Fetched prescription logs list successfully.");
				logger.info("Fetched prescription logs list successfully.");
			}
		} catch (FastRxException e) {
			response.setStatus(Status.ERROR);
			response.setMessage(e.getMessage());
			response.setData(000);
			logger.error("Error in fetching prescription logs list successfully.");

		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "search-logs", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<FastRxResponse> searchPrescriptionLogByUsernameAndDate(@QueryParam("name") String name,
			@QueryParam("startDate") String startDate, @QueryParam("endDate") String endDate) {
		logger.info("************** Fetching prescription audit log list ****************");
		List<PrescriptionLog> prescriptionLogList = null;
		FastRxResponse response = new FastRxResponse();
		try {
			prescriptionLogList = prescriptionService.searchPrescriptionLogByNameAndDate(name, startDate, endDate);
			if (null != prescriptionLogList && !prescriptionLogList.isEmpty()) {

				for (PrescriptionLog prescriptionLog : prescriptionLogList) {
					prescriptionLog.setPatientFirstName(
							passwordGenerator.getDecodedString(prescriptionLog.getPatientFirstName()));
					prescriptionLog.setPatientLastName(
							passwordGenerator.getDecodedString(prescriptionLog.getPatientLastName()));
					prescriptionLog.setPatientDob(passwordGenerator.getDecodedString(prescriptionLog.getPatientDob()));
					prescriptionLog
							.setPatientAddress(passwordGenerator.getDecodedString(prescriptionLog.getPatientAddress()));

				}
				response.setData(prescriptionLogList);
				response.setStatus(Status.SUCCESS);
				response.setMessage(prescriptionLogList.size() + " user prescription log found.");
			} else {
				response.setData(0);
				response.setStatus(Status.SUCCESS);
				response.setMessage("No prescription audit log not found.");
			}
		} catch (FastRxException e) {
			response.setData(0);
			response.setStatus(Status.ERROR);
			response.setMessage("No prescription audit log not found.");
			logger.error("prescription audit log Exception:" + e.getMessage());

		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/update-fax-status")
	public ResponseEntity<FastRxResponse> updateFaxStatus(@RequestBody FaxDetails faxDetails) {
		logger.info("PrescriptionController :: respondPrescription method");
		FastRxResponse response = new FastRxResponse();
		List<String> faxjobIds = new ArrayList<>();
		Map<String, Integer> faxStatusDetails = null;
		String updatedBy = null;
		try {
			faxjobIds = faxDetails.getFaxJobIds();
			updatedBy = faxDetails.getUpdatedBy();

			faxStatusDetails = faxService.getFaxStatusByFaxJobId(faxjobIds);
			prescriptionService.updateFaxStatusByFaxId(updatedBy, faxStatusDetails);
			response.setStatus(Status.SUCCESS);
			response.setMessage("Fax status updated successfully.");
			logger.info("Fax status updated successfully.");
		} catch (FastRxException e) {
			response.setStatus(Status.ERROR);
			response.setMessage("Fax status update failed.");
			logger.error("Fax status update failed. Exception: " + e.getMessage());
		}
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

}
