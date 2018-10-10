package com.parkwoodrx.fastrx.webservice;

import java.util.List;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.parkwoodrx.fastrx.exception.FastRxException;
import com.parkwoodrx.fastrx.model.MultiSelectDropDownObject;
import com.parkwoodrx.fastrx.model.RespondPrescription;
import com.parkwoodrx.fastrx.model.RevenueReport;
import com.parkwoodrx.fastrx.model.TransactionTable;
import com.parkwoodrx.fastrx.security.PasswordGenerator;
import com.parkwoodrx.fastrx.service.ReportsService;
import com.parkwoodrx.fastrx.webservice.FastRxResponse.Status;

@RestController
@RequestMapping("/reports")
public class ReportsController {
	private static final Logger logger = LoggerFactory.getLogger(ReportsController.class);

	@Autowired
	private ReportsService reportsService;
	
	@Autowired
	private PasswordGenerator passwordGenerator;

	@RequestMapping(method = RequestMethod.POST, value = "/getNoResponseReport")
	public ResponseEntity<FastRxResponse> getNoResponseReport(@RequestBody String params) {
		logger.info("ReportsController :: getNoResponseReport method");
		FastRxResponse response = new FastRxResponse();
		try {
			JSONObject jsonObj = new JSONObject(params);
			ObjectMapper mapper = new ObjectMapper();
			List<MultiSelectDropDownObject> locationList = mapper.readValue(jsonObj.getString("locationList"),
					new TypeReference<List<MultiSelectDropDownObject>>() {
					});

			List<RespondPrescription> prescriptionList = reportsService.getNoResponseReportForLocationIds(locationList,
					jsonObj.getString("fromDate"), jsonObj.getString("toDate"));
			
			for (RespondPrescription respondPrescObj : prescriptionList) {
				respondPrescObj.getPrescription().setPatientAddress(
						passwordGenerator.getDecodedString(
								respondPrescObj.getPrescription().getPatientAddress()));
				respondPrescObj.getPrescription().setPatientDob(
						passwordGenerator.getDecodedString(
								respondPrescObj.getPrescription().getPatientDob()));
				respondPrescObj.getPrescription().setPatientFirstName(
						passwordGenerator.getDecodedString(
								respondPrescObj.getPrescription().getPatientFirstName()));
				
				respondPrescObj.getPrescription().setPatientLastName(
						passwordGenerator.getDecodedString(
								respondPrescObj.getPrescription().getPatientLastName()));
			}
			
			response.setData(prescriptionList);
			response.setStatus(Status.SUCCESS);
			response.setMessage("No-response report fetched successfully.");
			logger.info("No-response report fetched successfully.");

		} catch (FastRxException e) {
			response.setStatus(Status.ERROR);
			response.setMessage("No-response report fetching failed.");
			logger.error("No-response report fetching failed. Exception: " + e.getMessage());
		} catch (Exception e) {
			response.setStatus(Status.ERROR);
			response.setMessage("No-response report fetching failed.");
			logger.error("No-response report fetching failed. Exception: " + e.getMessage());
		}
		return new ResponseEntity<FastRxResponse>(response, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/getTransactionReport")
	public ResponseEntity<FastRxResponse> getTransactionReport(@RequestBody String params) {
		logger.info("ReportsController :: getTransactionReport method");
		FastRxResponse response = new FastRxResponse();
		try {

			JSONObject jsonObj = new JSONObject(params);
			ObjectMapper mapper = new ObjectMapper();

			List<MultiSelectDropDownObject> locationList = mapper.readValue(jsonObj.getString("locationList"),
					new TypeReference<List<MultiSelectDropDownObject>>() {
					});

			List<TransactionTable> transactionList = reportsService.getTransactionReportForLocationIds(locationList,
					jsonObj.getString("fromDate"), jsonObj.getString("toDate"), jsonObj.getString("transactionType"));

			response.setData(transactionList);

			response.setStatus(Status.SUCCESS);
			response.setMessage("Transaction report fetched successfully.");
			logger.info("Transaction report fetched successfully.");

		} catch (FastRxException e) {
			response.setStatus(Status.ERROR);
			response.setMessage("Transaction report fetching failed.");
			logger.error("Transaction report fetching failed. Exception: " + e.getMessage());
		} catch (Exception e) {
			response.setStatus(Status.ERROR);
			response.setMessage("Transaction report fetching failed.");
			logger.error("Transaction report fetching failed. Exception: " + e.getMessage());
		}
		return new ResponseEntity<FastRxResponse>(response, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/getRevenueReport")
	public ResponseEntity<FastRxResponse> getRevenueReport(@RequestBody String params) {
		logger.info("ReportsController :: getRevenueReport method");
		FastRxResponse response = new FastRxResponse();
		try {

			JSONObject jsonObj = new JSONObject(params);
			ObjectMapper mapper = new ObjectMapper();

			List<MultiSelectDropDownObject> corporationList = mapper.readValue(jsonObj.getString("corpList"),
					new TypeReference<List<MultiSelectDropDownObject>>() {
					});
			List<RevenueReport> revenueReport = null;
			// handle empty dates
			if (jsonObj.getString("toDate").equals("") || jsonObj.getString("fromDate").equals("")) {
				revenueReport = reportsService.getRevenueReportForLocationIds(corporationList);
			} else {
				revenueReport = reportsService.getRevenueReportForLocationIdsForDateRange(corporationList,
						jsonObj.getString("fromDate"), jsonObj.getString("toDate"));
			}

			response.setData(revenueReport);

			response.setStatus(Status.SUCCESS);
			response.setMessage("Revenue report fetched successfully.");
			logger.info("Revenue report fetched successfully.");

		} catch (FastRxException e) {
			response.setStatus(Status.ERROR);
			response.setMessage("Revenue report fetching failed.");
			logger.error("Revenue report fetching failed. Exception: " + e.getMessage());
		} catch (Exception e) {
			response.setStatus(Status.ERROR);
			response.setMessage("Revenue report fetching failed.");
			logger.error("Revenue report fetching failed. Exception: " + e.getMessage());
		}
		return new ResponseEntity<FastRxResponse>(response, HttpStatus.OK);
	}
}
