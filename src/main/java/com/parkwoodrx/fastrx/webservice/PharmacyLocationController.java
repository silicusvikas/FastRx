package com.parkwoodrx.fastrx.webservice;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.parkwoodrx.fastrx.exception.FastRxException;
import com.parkwoodrx.fastrx.model.MultiSelectDropDownObject;
import com.parkwoodrx.fastrx.model.NonRegPharmacy;
import com.parkwoodrx.fastrx.model.PharmacyLocation;
import com.parkwoodrx.fastrx.model.PharmacyLocationWithCorporation;
import com.parkwoodrx.fastrx.service.PharmacyLocationService;
import com.parkwoodrx.fastrx.webservice.FastRxResponse.Status;

@RestController
@RequestMapping("/pharmacyLocation")
public class PharmacyLocationController {
	private static final Logger logger = LoggerFactory.getLogger(PharmacyLocationController.class);

	@Autowired
	private PharmacyLocationService pharmacyLocationService;

	@RequestMapping(method = RequestMethod.GET, value = "/getLocList/{corporationId}")
	public ResponseEntity<FastRxResponse> getAllLocationListByCorpId(
			@PathVariable(value = "corporationId") String corporationId, HttpServletRequest req) {
		logger.info("PharmacyLocationController :: getAllLocationList method for corporation: " + corporationId);
		List<PharmacyLocation> locationList = new ArrayList<PharmacyLocation>();
		FastRxResponse response = new FastRxResponse();
		try {

			locationList = pharmacyLocationService
					.getAllPharmacyLocationForCorporationId(Long.parseLong(corporationId));
			if (!locationList.isEmpty()) {
				logger.info("Fetched list of all locations Successfully!!");
				response.setData(locationList);
				response.setStatus(Status.SUCCESS);
				response.setMessage("Fetched list of all locations Successfully!!");
			} else {
				response.setStatus(Status.ERROR);
				response.setMessage("Pharmacy Location not found");
			}
		} catch (FastRxException e) {
			logger.error("Error in Fetching list of all locations.");
			response.setStatus(Status.ERROR);
			response.setMessage(e.getMessage());
		}
		return new ResponseEntity<FastRxResponse>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/pharmacy", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<FastRxResponse> registerPharmacyLocation(@RequestBody PharmacyLocation pharmacyLocation) {
		FastRxResponse response = new FastRxResponse();
		try {
			logger.error("Pharmacy location registration started for:" + pharmacyLocation.getPhoneNumber());
			pharmacyLocationService.registerPharmacyLocation(pharmacyLocation);
			response.setStatus(Status.SUCCESS);
			response.setMessage("Pharmacy location registered successfully");
			logger.info("Pharmacy location registration completed sucessfully");
		} catch (FastRxException e){
			response.setStatus(Status.ERROR);
			response.setMessage(e.getMessage());
			logger.error("Error in pharmacy location registration");
		}
		return new ResponseEntity<FastRxResponse>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<FastRxResponse> updatePharmacyLocation(@RequestBody PharmacyLocation pharmacyLocation) {
		FastRxResponse response = new FastRxResponse();
		try {
			logger.error("Pharmacy location update started for:" + pharmacyLocation.getPhoneNumber());
			pharmacyLocationService.updatePharmacyLocation(pharmacyLocation);
			response.setStatus(Status.SUCCESS);
			response.setMessage("Pharmacy location updated successfully");
			logger.info("Pharmacy location updated  sucessfully");
		} catch (FastRxException e) {
			response.setStatus(Status.ERROR);
			response.setMessage(e.getMessage());
			logger.error("Error in pharmacy location update");
		}
		return new ResponseEntity<FastRxResponse>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/pharmacy/", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<FastRxResponse> searchPharmacyLocation(@QueryParam("name") String name,
			@QueryParam("phoneNo") String phoneNo, @QueryParam("corpId") String corpId) {
		logger.info("Fetching list of all pharmacy locations for corporation:" + corpId);
		List<PharmacyLocationWithCorporation> pharmacyList = null;
		FastRxResponse response = new FastRxResponse();
		try {
			pharmacyList = pharmacyLocationService.searchPharmacyLocationAndCorpId(name, phoneNo, corpId);
			if (null != pharmacyList && !pharmacyList.isEmpty()) {
				response.setData(pharmacyList);
				response.setStatus(Status.SUCCESS);
				response.setMessage("Successfully Fetched list of all pharmacy locations");
				logger.info("Successfully fetched list of all pharmacy locations");
			}
		} catch (FastRxException e) {
			response.setStatus(Status.ERROR);
			response.setMessage(e.getMessage());
			logger.error("Error in fetching list of all pharmacy locations");

		}
		return new ResponseEntity<FastRxResponse>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/pharmacyList/", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<FastRxResponse> searchLocationsByNameAndPhone(@QueryParam("name") String name,
			@QueryParam("phoneNo") String phoneNo, @QueryParam("city") String city, @QueryParam("state") String state,
			@QueryParam("zip") String zip) {
		logger.info("PharmacyLocationController :: searchLocationsByNameAndPhone method :" + name + " " + phoneNo);
		List<NonRegPharmacy> pharmacyList = null;
		FastRxResponse response = new FastRxResponse();
		try {
			pharmacyList = pharmacyLocationService.searchPharmacyLocation(name, phoneNo, city, state, zip);
			if (null != pharmacyList && !pharmacyList.isEmpty()) {
				response.setData(pharmacyList);
				response.setStatus(Status.SUCCESS);
				response.setMessage("Successfully fetched list of all pharmacy locations");
				logger.info("Successfully fetched list of all pharmacy locations successfully");
			}
		} catch (FastRxException e) {
			response.setStatus(Status.ERROR);
			response.setMessage(e.getMessage());
			logger.error("Error in fetching list of all pharmacy locations");

		}
		return new ResponseEntity<FastRxResponse>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/{corpId}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<FastRxResponse> getPharmacyLocationByCorpId(@PathVariable("corpId") long corpId) {
		logger.info("Get list of location for corporation id :" + corpId);
		List<PharmacyLocation> pharmacyList = null;
		FastRxResponse response = new FastRxResponse();
		try {
			pharmacyList = pharmacyLocationService.getPharmacyLocationByCorpId(corpId);
			if (null != pharmacyList && !pharmacyList.isEmpty()) {
				response.setData(pharmacyList);
				response.setStatus(Status.SUCCESS);
				response.setMessage("Successfully fetched list of all pharmacy locations");
				logger.info("Successfully fetched list of all pharmacy locations successfully");
			}
		} catch (FastRxException e) {
			response.setStatus(Status.ERROR);
			response.setMessage(e.getMessage());
			logger.error("Error in fetching list of all pharmacy locations");

		}
		return new ResponseEntity<FastRxResponse>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/{corpId}/{id}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<FastRxResponse> getPharmacyLocationById(@PathVariable("id") long id,
			@PathVariable("corpId") long corpId) {
		logger.info("Get details of locations with id:" + id);
		PharmacyLocation pharmacyLocation = null;
		FastRxResponse response = new FastRxResponse();
		try {
			pharmacyLocation = pharmacyLocationService.getPharmacyLocationById(id, corpId);
			response.setData(pharmacyLocation);
			response.setStatus(Status.SUCCESS);
			response.setMessage("Fetched detail of pharmacy locations successfully.");
			logger.info("Successfully fetched detail of pharmacy locations .");
		} catch (FastRxException e) {
			response.setData(0);
			response.setStatus(Status.ERROR);
			response.setMessage(e.getMessage());
			logger.error("Error in fetching details of pharmacy locations");

		}
		return new ResponseEntity<FastRxResponse>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/user/{userId}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<FastRxResponse> getPharmacyLocationByUserId(@PathVariable("userId") long userId) {
		logger.info("Get details of locations for user id:" + userId);
		PharmacyLocation pharmacyLocation = null;
		FastRxResponse response = new FastRxResponse();
		try {
			pharmacyLocation = pharmacyLocationService.getPharmacyLocationByUserId(userId);
			response.setData(pharmacyLocation);
			response.setStatus(Status.SUCCESS);
			response.setMessage("Fetched detail of pharmacy locations successfully.");
			logger.info("Successfully fetched detail of pharmacy locations .");
		} catch (FastRxException e) {
			response.setData(0);
			response.setStatus(Status.ERROR);
			response.setMessage(e.getMessage());
			logger.error("Error in fetching details of pharmacy locations");

		}
		return new ResponseEntity<FastRxResponse>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/changeStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<FastRxResponse> changeStatusPharmacyLocation(@RequestBody String params) {
		logger.info("Update status for pharmacy location");
		FastRxResponse response = new FastRxResponse();
		String status = null;
		try {
			JSONObject jsonObj = new JSONObject(params);
			status = jsonObj.getString("status");
			pharmacyLocationService.updatedStatusOfLocation(Long.parseLong(jsonObj.getString("locationId")),
					jsonObj.getString("updatedBy"), jsonObj.getString("status"));
			response.setData(200);
			response.setStatus(Status.SUCCESS);
			if (status.equalsIgnoreCase("Y")) {
				response.setMessage("Pharmacy location activated successfully.");
				logger.info("Pharmacy location activated successfully.");
			} else {
				response.setMessage("Pharmacy location deactivated successfully.");
				logger.info("Pharmacy location deactivated successfully.");
			}

		} catch (Exception e) {
			response.setStatus(Status.ERROR);
			if (status.equalsIgnoreCase("Y")) {
				response.setMessage("Error in activating pharmacy location.");
				logger.error("Error in activating pharmacy location." + e.getMessage());
			} else {
				response.setMessage("Error in deactivating pharmacy location.");
				logger.error("Error in deactivating pharmacy location." + e.getMessage());
			}
		}
		return new ResponseEntity<FastRxResponse>(response, HttpStatus.OK);

	}

	@RequestMapping(method = RequestMethod.GET, value = "/getAllLocations")
	public ResponseEntity<FastRxResponse> getPharmacyLocationList() {
		logger.info("Fetching list of all pharmacy locations");
		List<PharmacyLocationWithCorporation> locationList = new ArrayList<PharmacyLocationWithCorporation>();
		FastRxResponse response = new FastRxResponse();
		try {

			locationList = pharmacyLocationService.getPharmacyLocationList();
			if (!locationList.isEmpty()) {
				logger.info("Fetched list of all pharmacy locations Successfully!!");
				response.setData(locationList);
				response.setStatus(Status.SUCCESS);
				response.setMessage("Fetched list of all pharmacy locations Successfully!!");
			} else {
				response.setStatus(Status.ERROR);
				response.setMessage("Pharmacy Location not found");
			}
		} catch (FastRxException e) {
			logger.error("Error in Fetching list of all pharmacy locations.");
			response.setStatus(Status.ERROR);
			response.setMessage(e.getMessage());
		}
		return new ResponseEntity<FastRxResponse>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/uploadPharmacyloactions", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<FastRxResponse> uploadPharmacyloactions(@RequestPart("file") MultipartFile fileInput,
			@RequestParam(value = "loginUser") String loginUser, HttpServletResponse httpResponse) {

		logger.info("****************************** START uploadPharmacyloactions*********************** ");
		List<PharmacyLocation> failedPharmacyLocationList = null;
		FastRxResponse response = new FastRxResponse();
		File file = null;
		FileOutputStream fos = null;
		try {
			file = new File(fileInput.getOriginalFilename());
			file.createNewFile();
			fos = new FileOutputStream(file);
			fos.flush();
			fos.write(fileInput.getBytes());
			failedPharmacyLocationList = pharmacyLocationService.uploadPharmacyloactions(file, loginUser);
			response.setStatus(Status.SUCCESS);
			response.setMessage("Pharmacy locations uploaded successfully");
			logger.info("Pharmacy locations uploaded successfully");
		} catch (Exception e) {
			response.setStatus(Status.ERROR);
			response.setMessage(e.getMessage());
			logger.error("Error in upload Pharmacy loactions");
		} finally {
			if (file != null) {
				file.delete();
			}
			try {
				fos.close();
			} catch (IOException e) {
				logger.error("Error in while closing Pharmacy loactions file");
			}
		}
		response.setData(failedPharmacyLocationList);
		logger.info("****************************** END uploadPharmacyloactions***********************");
		return new ResponseEntity<FastRxResponse>(response, HttpStatus.OK);

	}

	@RequestMapping(method = RequestMethod.GET, value = "/getLocListMultiselect/{corporationId}")
	public ResponseEntity<FastRxResponse> getAllLocationsForMultiSelectByCorpId(
			@PathVariable(value = "corporationId") String corporationId, HttpServletRequest req) {
		logger.info("PharmacyLocationController :: getAllLocationList method for corporation: " + corporationId);
		List<MultiSelectDropDownObject> locationList = new ArrayList<MultiSelectDropDownObject>();
		FastRxResponse response = new FastRxResponse();
		try {

			locationList = pharmacyLocationService
					.getAllLocationMultiSelectForCorporationId(Long.parseLong(corporationId));
			if (!locationList.isEmpty()) {
				logger.info("Fetched list of all locations Successfully!!");
				response.setData(locationList);
				response.setStatus(Status.SUCCESS);
				response.setMessage("Fetched list of all locations Successfully!!");
			} else {
				response.setStatus(Status.ERROR);
				response.setMessage("Pharmacy Location not found");
			}
		} catch (FastRxException e) {
			logger.error("Error in Fetching list of all locations.");
			response.setStatus(Status.ERROR);
			response.setMessage(e.getMessage());
		}
		return new ResponseEntity<FastRxResponse>(response, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/getLocMultiselectForIds")
	public ResponseEntity<FastRxResponse> getLocForMultiSelectForCorpIds(
			@RequestBody List<MultiSelectDropDownObject> corporations, HttpServletRequest req) {
		logger.info("PharmacyLocationController :: getLocForMultiSelectForCorpIds method for multiple corporations ");
		List<MultiSelectDropDownObject> locationList = new ArrayList<MultiSelectDropDownObject>();
		FastRxResponse response = new FastRxResponse();
		try {

			locationList = pharmacyLocationService.getAllLocationsMultiSelectForCorpIds(corporations);
			if (!locationList.isEmpty()) {
				logger.info("Fetched list of all locations Successfully!!");
				response.setData(locationList);
				response.setStatus(Status.SUCCESS);
				response.setMessage("Fetched list of all locations Successfully!!");
			} else {
				response.setStatus(Status.ERROR);
				response.setMessage("Pharmacy Location not found");
			}
		} catch (FastRxException e) {
			logger.error("Error in Fetching list of all locations.");
			response.setStatus(Status.ERROR);
			response.setMessage(e.getMessage());
		}
		return new ResponseEntity<FastRxResponse>(response, HttpStatus.OK);
	}
	@RequestMapping(value = "/uploadnonrephar", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<FastRxResponse> uploadNonRegPhar(@RequestPart("file") MultipartFile fileInput,
			@RequestParam(value = "loginUser") String loginUser, HttpServletResponse httpResponse) {

		logger.info("****************************** START Uploading Non registered pharmacy Database *********************** ");
		FastRxResponse response = new FastRxResponse();
		File file = null;
		FileOutputStream fos = null;
		try {
			file = new File(fileInput.getOriginalFilename());
			file.createNewFile();
			fos = new FileOutputStream(file);
			fos.flush();
			fos.write(fileInput.getBytes());
			pharmacyLocationService.uploadNonRegPhar(file, loginUser);
			response.setStatus(Status.SUCCESS);
			response.setData(200);
			response.setMessage("Non registered pharmacy uploaded successfully");
			logger.info("Non registered pharmacy uploaded successfully");
		} catch (Exception e) {
			response.setStatus(Status.ERROR);
			response.setData(000);
			response.setMessage(e.getMessage());
			logger.error("Error in uploading Non registered pharmacy database :: " + e);
		} finally {
			if (file != null) {
				file.delete();
			}
			try {
				fos.close();
			} catch (IOException e) {
				logger.error("Error in while closing Non registered pharmacy database :: " + e);
			}
		}
		logger.info("****************************** END Uploading Non registered pharmacy Database ***********************");
		return new ResponseEntity<FastRxResponse>(response, HttpStatus.OK);

	}

}
