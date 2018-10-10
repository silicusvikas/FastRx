package com.parkwoodrx.fastrx.webservice;

import java.util.List;

import javax.ws.rs.QueryParam;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.parkwoodrx.fastrx.exception.FastRxException;
import com.parkwoodrx.fastrx.integration.CustomerPaymentService;
import com.parkwoodrx.fastrx.model.BillingDetails;
import com.parkwoodrx.fastrx.model.CreditcardDetails;
import com.parkwoodrx.fastrx.model.MultiSelectDropDownObject;
import com.parkwoodrx.fastrx.model.PaymentProfileDetails;
import com.parkwoodrx.fastrx.model.PaymentType;
import com.parkwoodrx.fastrx.model.PharmacyCorporation;
import com.parkwoodrx.fastrx.model.PharmacyCorporationDetails;
import com.parkwoodrx.fastrx.model.State;
import com.parkwoodrx.fastrx.service.PharmacyCorporationService;
import com.parkwoodrx.fastrx.webservice.FastRxResponse.Status;

@RestController
@RequestMapping("/pharmacy")
public class PharmacyCorporationController {

	private static final Logger logger = LoggerFactory.getLogger(PharmacyCorporationController.class);
	@Autowired
	private PharmacyCorporationService pharmacyService;

	@Autowired
	private CustomerPaymentService paymentService;

	@RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> getPharmacies() {
		/*
		 * logger.info(this.getClass().getEnclosingMethod().getName() +
		 * " get all pharmacies");
		 */
		logger.info("Test Service");
		return new ResponseEntity<String>("Test Pharmacy", HttpStatus.OK);
	}

	@RequestMapping(value = "/payment", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<FastRxResponse> createPaymentProfile(@RequestBody PaymentProfileDetails profileDetails) {
		logger.info("Payment details registration started for: " + profileDetails.getEmail());
		FastRxResponse response = new FastRxResponse();
		try {
			CreditcardDetails creditcardStatus = paymentService.create(profileDetails);
			if (null != creditcardStatus) {
				response.setData(creditcardStatus.getId());
				response.setStatus(Status.SUCCESS);
				response.setMessage("Payment details added successfully.");
				logger.info("Payment details added  successfully");
			}
		} catch (Exception e) {
			response.setStatus(Status.ERROR);
			response.setMessage(e.getMessage());
			logger.error("Error in adding payment details");

		}
		return new ResponseEntity<FastRxResponse>(response, HttpStatus.OK);

	}

	@RequestMapping(value = "/payment/profile", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<FastRxResponse> updatePaymentProfile(@RequestBody PaymentProfileDetails profileDetails) {
		logger.info("Payment details update started for: " + profileDetails.getCorporationId());
		CreditcardDetails creditcard = null;
		FastRxResponse response = new FastRxResponse();
		try {
			if ("check".equalsIgnoreCase(profileDetails.getPaymentType())) {
				pharmacyService.updatePaymentType(profileDetails.getPaymentType(), profileDetails.getCorporationId(),
						profileDetails.getFastRxAudit().getUpdatedBy());
				try {
					pharmacyService.updatedStatusOfCorporation(profileDetails.getCorporationId(),
							profileDetails.getFastRxAudit().getUpdatedBy(), "Y");
				} catch (FastRxException e) {
					logger.info(e.getMessage());
				}
				response.setData(200);
				response.setStatus(Status.SUCCESS);
				response.setMessage("Payment details updated successfully.");
				logger.info("Payment details updated sucessfully");
				return new ResponseEntity<FastRxResponse>(response, HttpStatus.OK);
			} else {
				if (pharmacyService.getPaymentProfileByCorporationId(profileDetails.getCorporationId()) != 0) {
					CreditcardDetails profile = pharmacyService
							.getPaymentProfileIdByCorporationId(profileDetails.getCorporationId());
					profileDetails.setCustomerPaymentProfileId(profile.getCustomerPaymentAcctId());
					profileDetails.getFastRxAudit().setUpdatedBy(profileDetails.getFastRxAudit().getUpdatedBy());
					profileDetails.setCustomerProfileId(profile.getCustomerProfileId());
					creditcard = paymentService.update(profileDetails);
					pharmacyService.updatePaymentType(profileDetails.getPaymentType(),
							profileDetails.getCorporationId(), profileDetails.getFastRxAudit().getUpdatedBy());
				} else {
					profileDetails.getFastRxAudit().setCreatedBy(profileDetails.getFastRxAudit().getUpdatedBy());
					creditcard = paymentService.create(profileDetails);
					pharmacyService.updatePaymentType(profileDetails.getPaymentType(),
							profileDetails.getCorporationId(), profileDetails.getFastRxAudit().getUpdatedBy());
					try {
						pharmacyService.updatedStatusOfCorporation(profileDetails.getCorporationId(),
								profileDetails.getFastRxAudit().getUpdatedBy(), "Y");
					} catch (FastRxException e) {
						logger.info(e.getMessage());
					}

				}
			}
			if ("Successful.".equalsIgnoreCase(creditcard.getResponse())) {
				response.setData(200);
				response.setStatus(Status.SUCCESS);
				response.setMessage("Payment details updated successfully.");
				logger.info("Payment details updated sucessfully");
			} else {
				response.setData(0);
				response.setStatus(Status.ERROR);
				response.setMessage(creditcard.getResponse());
				logger.info("Error in payment details update");
			}
		} catch (FastRxException e) {
			response.setData(0);
			if (null != e.getDetailMessage() || !e.getDetailMessage().isEmpty()) {
				response.setMessage(e.getDetailMessage());
			} else {
				e.getMessage();
			}
			response.setStatus(Status.ERROR);
			logger.error("Error in payment details update", e);
		} catch (Exception e) {
			response.setData(0);
			response.setStatus(Status.ERROR);
			response.setMessage("Error in payment details update");
			logger.error("Error in payment details update", e);
		}
		return new ResponseEntity<FastRxResponse>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/corporation", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<FastRxResponse> registerPharmacyCorporation(
			@RequestBody PharmacyCorporationDetails pharmacyDetails) {
		logger.info(
				"Pharmacy registration started for:" + pharmacyDetails.getPharmacyCorporation().getCorporationName());
		FastRxResponse response = new FastRxResponse();
		PaymentProfileDetails paymentProfileDetails = null;
		try {

			long corporationId = pharmacyService.registerPharmacyCorporation(pharmacyDetails);
			if ("Credit Card".equalsIgnoreCase(pharmacyDetails.getPharmacyCorporation().getPaymentType())) {
				try {
					paymentProfileDetails = pharmacyDetails.getPaymentProfileDetails();
					paymentProfileDetails.setCorporationId(corporationId);
					paymentService.create(pharmacyDetails.getPaymentProfileDetails());
					pharmacyService.updatePaymentType(paymentProfileDetails.getPaymentType(), corporationId,
							paymentProfileDetails.getFastRxAudit().getCreatedBy());
					pharmacyService.activateCorporation(corporationId,
							paymentProfileDetails.getFastRxAudit().getCreatedBy(), "Y");
				} catch (Exception e) {
					response.setData(corporationId);
					response.setStatus(Status.SUCCESS);
					response.setMessage(
							"Pharmacy corporation registered successfully but payment details not registered.");
					logger.info("Pharmacy corporation registered successfully but payment details not registered");
					return new ResponseEntity<FastRxResponse>(response, HttpStatus.OK);
				}
			} else if ("Check".equalsIgnoreCase(pharmacyDetails.getPharmacyCorporation().getPaymentType())) {
				pharmacyService.updatePaymentType(pharmacyDetails.getPharmacyCorporation().getPaymentType(),
						corporationId, pharmacyDetails.getPharmacyCorporation().getFastRxAudit().getCreatedBy());
				pharmacyService.activateCorporation(corporationId,
						pharmacyDetails.getPharmacyCorporation().getFastRxAudit().getCreatedBy(), "Y");
			}
			response.setData(corporationId);
			response.setStatus(Status.SUCCESS);
			response.setMessage("Pharmacy corporation registered successfully.");
			logger.info("Pharmacy corporation registration completed sucessfully");
		} catch (FastRxException fe) {
			response.setStatus(Status.ERROR);
			response.setMessage(fe.getMessage());
			logger.error(fe.getMessage());

		} catch (Exception e) {
			response.setStatus(Status.ERROR);
			response.setMessage("Error in pharmacy corporation registration");
			logger.error(e.getMessage());

		}
		return new ResponseEntity<FastRxResponse>(response, HttpStatus.OK);

	}

	@RequestMapping(value = "/corporation/profile", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<FastRxResponse> updatePharmacyCorporation(
			@RequestBody PharmacyCorporation pharmacyCorporation) {
		logger.info("Update corporation details started for pharmacy:" + pharmacyCorporation.getCorporationName());
		FastRxResponse response = new FastRxResponse();
		try {
			pharmacyService.updatePharmacyCorporation(pharmacyCorporation);
			response.setData(200);
			response.setStatus(Status.SUCCESS);
			response.setMessage("Pharmacy corporation details updated successfully.");
			logger.info("Pharmacy corporation details update completed sucessfully");
		} catch (FastRxException e) {
			response.setStatus(Status.ERROR);
			response.setMessage(e.getMessage());
			logger.error("Error in pharmacy corporation details updation");

		}
		return new ResponseEntity<FastRxResponse>(response, HttpStatus.OK);

	}

	@RequestMapping(value = "/corporation/billing", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<FastRxResponse> updatePharmacyCorporationBilling(@RequestBody BillingDetails billingDetails) {
		logger.info("Update billing details started for:" + billingDetails.getCorporationId());
		FastRxResponse response = new FastRxResponse();
		try {
			pharmacyService.updatePharmacyCorporationBilling(billingDetails);
			try {
				pharmacyService.updatedStatusOfCorporation(billingDetails.getCorporationId(),
						billingDetails.getFastRxAudit().getUpdatedBy(), "Y");
			} catch (FastRxException e) {
				logger.info(e.getMessage());
			}
			response.setData(200);
			response.setStatus(Status.SUCCESS);
			response.setMessage("Billing details updated successfully.");
			logger.info("Billing details updated successfully.");
		} catch (FastRxException e) {
			response.setStatus(Status.ERROR);
			response.setMessage(e.getMessage());
			logger.error("Error in updating billing details: ",e.getMessage());

		}
		return new ResponseEntity<FastRxResponse>(response, HttpStatus.OK);

	}

	@RequestMapping(value = "/corporation/", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<FastRxResponse> getPharmacyCorporationsByNameOrPhone(@QueryParam("corpName") String corpName,
			@QueryParam("phoneNo") String phoneNo) {
		logger.info("Fetching list of all pharmacy corporations.");
		List<PharmacyCorporation> pharmacyCorporationList = null;
		FastRxResponse response = new FastRxResponse();
		try {
			pharmacyCorporationList = pharmacyService.getPharmacyCorporationsByNameOrPhone(corpName, phoneNo);
			if (null != pharmacyCorporationList && !pharmacyCorporationList.isEmpty()) {
				response.setData(pharmacyCorporationList);
				response.setStatus(Status.SUCCESS);
				response.setMessage("Fetched list of all pharmacy corporations successfully.");
				logger.info("Fetched list of all pharmacy corporations successfully");
			}
		} catch (FastRxException e) {
			response.setStatus(Status.ERROR);
			response.setMessage(e.getMessage());
			logger.error("Error in fetching list of all pharmacy corporations");

		}
		return new ResponseEntity<FastRxResponse>(response, HttpStatus.OK);

	}

	@RequestMapping(value = "/corporation/list", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<FastRxResponse> getAllPharmacyCorporations() {
		logger.info("Fetching list of all pharmacy corporations.");
		List<PharmacyCorporation> pharmacyCorporationList = null;
		FastRxResponse response = new FastRxResponse();
		try {
			pharmacyCorporationList = pharmacyService.getAllPharmacyCorporations();
			if (null != pharmacyCorporationList && !pharmacyCorporationList.isEmpty()) {
				response.setData(pharmacyCorporationList);
				response.setStatus(Status.SUCCESS);
				response.setMessage("Fetched list of all pharmacy corporations successfully.");
				logger.info("Fetched list of all pharmacy corporations successfully.");
			}
		} catch (FastRxException e) {
			response.setStatus(Status.ERROR);
			response.setMessage(e.getMessage());
			logger.error("Error in fetching list of all pharmacy corporations.");

		}
		return new ResponseEntity<FastRxResponse>(response, HttpStatus.OK);

	}

	@RequestMapping(value = "/corporation/profile/{id}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<FastRxResponse> getCorporationProfileById(@PathVariable Long id) {
		logger.info("Fetching pharmacy corporations detail for corporation Id: " + id);
		PharmacyCorporationDetails corporationDetail = null;
		FastRxResponse response = new FastRxResponse();
		try {
			corporationDetail = pharmacyService.getCorporationProfileById(id);
			if (null != corporationDetail) {
				response.setData(corporationDetail);
				response.setStatus(Status.SUCCESS);
				response.setMessage("Fetched pharmacy corporations detail successfully for corporation Id :" + id);
				logger.info("Fetched pharmacy corporations detail successfully for corporation Id :" + id);
			}
		} catch (FastRxException e) {
			response.setStatus(Status.ERROR);
			response.setMessage(e.getMessage());
			logger.error("Error in fetching  pharmacy corporations detail for corporation Id:" + id);

		}
		return new ResponseEntity<FastRxResponse>(response, HttpStatus.OK);

	}

	@RequestMapping(value = "/corporation/paymentTypeList", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<FastRxResponse> getPaymentTypeList() {
		logger.info("Fetching payment Type list");
		List<PaymentType> paymentTypeList = null;
		FastRxResponse response = new FastRxResponse();
		try {
			paymentTypeList = pharmacyService.getPaymentTypeList();
			if (null != paymentTypeList || !paymentTypeList.isEmpty() || paymentTypeList.size() != 0) {
				response.setData(paymentTypeList);
				response.setStatus(Status.SUCCESS);
				response.setMessage("Fetched  payment type list successfully.");
				logger.info("Fetched  payment type list successfully");
			}
		} catch (FastRxException e) {
			response.setStatus(Status.ERROR);
			response.setMessage(e.getMessage());
			logger.error("Error in fetching payment type list");

		}
		return new ResponseEntity<FastRxResponse>(response, HttpStatus.OK);

	}

	@RequestMapping(value = "/state/list", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<FastRxResponse> getStateList() {
		logger.info("Fetching State List");
		List<State> stateList = null;
		FastRxResponse response = new FastRxResponse();
		try {
			stateList = pharmacyService.getStateList();
			if (null != stateList || !stateList.isEmpty() || stateList.size() != 0) {
				response.setData(stateList);
				response.setStatus(Status.SUCCESS);
				response.setMessage("Fetched  state list successfully.");
				logger.info("Fetched  state list successfully");
			}
		} catch (FastRxException e) {
			response.setStatus(Status.ERROR);
			response.setMessage(e.getMessage());
			logger.error("Error in fetching state list");

		}
		return new ResponseEntity<FastRxResponse>(response, HttpStatus.OK);

	}

	@RequestMapping(value = "/changeStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<FastRxResponse> activateCorporation(@RequestBody String params) {
		logger.info("Update status for corporation");
		FastRxResponse response = new FastRxResponse();
		String status = null;
		try {
			JSONObject jsonObj = new JSONObject(params);
			status = jsonObj.getString("status");
			pharmacyService.updatedStatusOfCorporation(Long.parseLong(jsonObj.getString("corpId")),
					jsonObj.getString("updatedBy"), jsonObj.getString("status"));

			response.setData(200);
			response.setStatus(Status.SUCCESS);
			if (status.equalsIgnoreCase("Y")) {
				response.setMessage("Corporation activated successfully.");
				logger.info("Corporation activated successfully.");
			} else {
				response.setMessage("Corporation deactivated successfully.");
				logger.info("Corporation deactivated successfully.");
			}

		} catch (FastRxException fx) {
			response.setStatus(Status.ERROR);
			response.setData(0);
			response.setMessage(fx.getMessage());
		} catch (Exception e) {
			response.setStatus(Status.ERROR);
			if (status.equalsIgnoreCase("Y")) {
				response.setMessage("Error in activating corporation.");
				logger.error("Error in activating corporation." + e.getMessage());
			} else {
				response.setMessage("Error in deactivating corporation.");
				logger.error("Error in deactivating corporation." + e.getMessage());
			}
		}
		return new ResponseEntity<FastRxResponse>(response, HttpStatus.OK);

	}

	@RequestMapping(value = "/corporation/listForMultiselect", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<FastRxResponse> getAllPharmacyCorporationsForMultiselect() {
		logger.info("Fetching list of all pharmacy corporations.");
		List<MultiSelectDropDownObject> pharmacyCorporationList = null;
		FastRxResponse response = new FastRxResponse();
		try {
			pharmacyCorporationList = pharmacyService.getAllPharmacyCorpsMultiSelect();
			if (null != pharmacyCorporationList && !pharmacyCorporationList.isEmpty()) {
				response.setData(pharmacyCorporationList);
				response.setStatus(Status.SUCCESS);
				response.setMessage("Fetched list of all pharmacy corporations successfully.");
				logger.info("Fetched list of all pharmacy corporations successfully.");
			}
		} catch (FastRxException e) {
			response.setStatus(Status.ERROR);
			response.setMessage(e.getMessage());
			logger.error("Error in fetching list of all pharmacy corporations.");

		}
		return new ResponseEntity<FastRxResponse>(response, HttpStatus.OK);

	}

}
