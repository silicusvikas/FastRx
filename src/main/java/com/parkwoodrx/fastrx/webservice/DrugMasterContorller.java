package com.parkwoodrx.fastrx.webservice;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.QueryParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.parkwoodrx.fastrx.exception.FastRxException;
import com.parkwoodrx.fastrx.model.Drugs;
import com.parkwoodrx.fastrx.service.DrugDatabaseService;
import com.parkwoodrx.fastrx.webservice.FastRxResponse.Status;

@RestController
@RequestMapping("/drug")
public class DrugMasterContorller {
	private static final Logger logger = LoggerFactory.getLogger(DrugMasterContorller.class);

	@Autowired
	private DrugDatabaseService drugService;

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/searchMedicine/", method = RequestMethod.GET)
	public ResponseEntity<FastRxResponse> searchDrugsByName(@QueryParam("name") String name) {
		logger.info("DrugMasterContorller :: searchDrugsByName method :" + name);
		List<String> drugList = new ArrayList<>();
		RestTemplate restTemplate = new RestTemplate();
		FastRxResponse response = new FastRxResponse();
		HashMap<String, Object> result;
		ArrayList<HashMap<String, Object>> conceptGroupObjects;
		try {
			restTemplate.setMessageConverters(Arrays.asList(new MappingJackson2HttpMessageConverter()));
			result = (HashMap<String, Object>) restTemplate
					.getForObject("https://rxnav.nlm.nih.gov/REST/drugs?name=" + name, HashMap.class);
			result = (HashMap<String, Object>) result.get("drugGroup");
			if (result != null && result.get("conceptGroup") != null) {
				conceptGroupObjects = (ArrayList<HashMap<String, Object>>) result.get("conceptGroup");
				for (HashMap<String, Object> conceptGroupObject : conceptGroupObjects) {
					if (conceptGroupObject.get("conceptProperties") != null
							&& conceptGroupObject.get("tty").equals("SBD")) {
						ArrayList<HashMap<String, Object>> medicineList = (ArrayList<HashMap<String, Object>>) conceptGroupObject
								.get("conceptProperties");
						for (HashMap<String, Object> medicine : medicineList) {
							drugList.add((String) medicine.get("name"));
							response.setData(drugList);
							response.setStatus(Status.SUCCESS);
							response.setMessage("Fetched list of drugs by name successfully.");
							logger.info("Fetched list of drugs by name successfully");
						}
					}
				}
			} else {
				response.setStatus(Status.ERROR);
				response.setMessage("Error in fetching list of drugs by name");
				logger.error("Error in fetching list of drugs by name");
			}
		} catch (Exception e) {
			response.setStatus(Status.ERROR);
			response.setMessage("Error in fetching list of drugs by name");
			logger.error("Error in fetching list of drugs by name. Exception:" + e.getMessage());
		}
		return new ResponseEntity<FastRxResponse>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/upload-drug-database", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<FastRxResponse> uploadDrugDatabase(@RequestPart("file") MultipartFile fileInput,
			@RequestParam(value = "loginUser") String loginUser, HttpServletResponse httpResponse) {

		logger.info("****************************** START Uploading Drug Database *********************** ");
		FastRxResponse response = new FastRxResponse();
		File file = null;
		FileOutputStream fos = null;
		try {
			file = new File(fileInput.getOriginalFilename());
			file.createNewFile();
			fos = new FileOutputStream(file);
			fos.flush();
			fos.write(fileInput.getBytes());
			drugService.uploadDrugDatabase(file, loginUser);
			response.setStatus(Status.SUCCESS);
			response.setData(200);
			response.setMessage("Drug database uploaded successfully");
			logger.info("Drug database uploaded successfully");
		} catch (Exception e) {
			response.setStatus(Status.ERROR);
			response.setData(000);
			response.setMessage(e.getMessage());
			logger.error("Error in uploading drug database :: " + e);
		} finally {
			if (file != null) {
				file.delete();
			}
			try {
				fos.close();
			} catch (IOException e) {
				logger.error("Error in while closing drug database :: " + e);
			}
		}
		logger.info("****************************** END Uploading Drug Database ***********************");
		return new ResponseEntity<FastRxResponse>(response, HttpStatus.OK);

	}

	@RequestMapping(value = "/search", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<FastRxResponse> searchPharmacyLocation(@QueryParam("drugName") String drugName) {
		logger.info("Fetching list of drugs:" + drugName);
		List<Drugs> drugList = null;
		FastRxResponse response = new FastRxResponse();
		try {
			drugList = drugService.searchDrug(drugName);
			if (null != drugList && !drugList.isEmpty()) {
				logger.info("list size::" + drugList.size());
				response.setData(drugList);
				response.setStatus(Status.SUCCESS);
				response.setMessage("Successfully Fetched list of drugs");
				logger.info("Successfully fetched list of drugs");
			}
		} catch (FastRxException f) {
			response.setStatus(Status.ERROR);
			response.setMessage(f.getMessage());
			logger.error("Error in fetching drug list :: " + f.getMessage());

		} catch (Exception e) {
			logger.error("Error in fetching drug list :: " + e);
		}
		return new ResponseEntity<FastRxResponse>(response, HttpStatus.OK);
	}

}