package com.parkwoodrx.fastrx.webservice;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.QueryParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.parkwoodrx.fastrx.constants.FastRxErrorCodes;
import com.parkwoodrx.fastrx.exception.FastRxException;
import com.parkwoodrx.fastrx.model.PharmacyLocation;
import com.parkwoodrx.fastrx.model.User;
import com.parkwoodrx.fastrx.model.UserLog;
import com.parkwoodrx.fastrx.model.UserRole;
import com.parkwoodrx.fastrx.security.TokenAuthenticationService;
import com.parkwoodrx.fastrx.service.MappingEntitiesService;
import com.parkwoodrx.fastrx.service.PharmacyCorporationService;
import com.parkwoodrx.fastrx.service.PharmacyLocationService;
import com.parkwoodrx.fastrx.service.UserRoleService;
import com.parkwoodrx.fastrx.service.UserService;
import com.parkwoodrx.fastrx.webservice.FastRxResponse.Status;

@RestController
@RequestMapping("/login")
public class LoginController {

	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

	@Autowired
	private UserService userService;

	@Autowired
	private UserRoleService userRoleService;

	@Autowired
	private PharmacyLocationService pharmacyLocationService;

	@Autowired
	private PharmacyCorporationService pharmacyService;

	@Autowired
	private MappingEntitiesService mappingEntitiesService;

	@Autowired
	private TokenAuthenticationService tokenAuthenticationService;

	@RequestMapping(method = RequestMethod.POST, value = "/adminAuthenticate")
	public ResponseEntity<FastRxResponse> authenticateAdminUser(@RequestBody User user, HttpServletResponse res) {
		logger.info("UserController :: authenticateAdminUser method");
		logger.debug("User authentication for : " + user.getUsername());
		FastRxResponse response = new FastRxResponse();
		UserRole userRole = null;
		String pharmacyName = "";
		UserLog userLog = new UserLog();
		long corporationId = 0;
		long locationId = 0;
		try {
			userLog.setUsername(user.getUsername());
			userLog.setEvent("LOGIN");
			user = userService.authenticateUser(user);
			userRole = userRoleService.getAdminUserRoleById(Long.valueOf(user.getRoleId()));

			if (!userRole.getRoleName().equalsIgnoreCase("Super admin")) {
				corporationId = mappingEntitiesService.getCorporationIdForUser(user.getId());
				if (!pharmacyService.isCorporationActive(corporationId)) {
					userLog.setStatus(com.parkwoodrx.fastrx.model.UserLog.Status.FAIL);
					userLog.setRemarks(FastRxErrorCodes.CORPORATION_INACTIVE_lOG_MSG);
					userService.addUserLog(userLog);
					throw new FastRxException(FastRxErrorCodes.INACTIVE_CODE,
							FastRxErrorCodes.CORPORATION_INACTIVE_MSG);
				}
				if (userRole.getRoleName().equalsIgnoreCase("Location Admin")) {
					locationId = mappingEntitiesService.getLocationIdForUser(user.getId());
					if (!pharmacyLocationService.isLocationActive(locationId)) {
						userLog.setStatus(com.parkwoodrx.fastrx.model.UserLog.Status.FAIL);
						userLog.setRemarks(FastRxErrorCodes.LOCATION_INACTIVE_MSG);
						userService.addUserLog(userLog);
						throw new FastRxException(FastRxErrorCodes.INACTIVE_CODE,
								FastRxErrorCodes.LOCATION_INACTIVE_MSG);
					}
					pharmacyName = pharmacyLocationService.getPharmacyLocationNameByUserId(user.getId());
				} else {
					pharmacyName = pharmacyService.getPharmacyCorpNameById(corporationId);
				}

			}
			tokenAuthenticationService.addAuthentication(res, user.getUsername());
			userLog.setStatus(com.parkwoodrx.fastrx.model.UserLog.Status.SUCCESS);
			userService.addUserLog(userLog);
			response.setData(new ResponseData(userRole.getRoleName(), userRole.getId(), String.valueOf(user.getId()),
					String.valueOf(corporationId), user.getUsername(), String.valueOf(locationId),
					user.getFirstName() + " " + user.getLastName(), pharmacyName,
					String.valueOf(user.getFirstLogin())));
			response.setStatus(Status.SUCCESS);
			response.setMessage("User authentication successful.");
			logger.info("User authentication successful.");
		} catch (FastRxException e) {
			response.setStatus(Status.ERROR);
			response.setMessage("Invalid credentials");
			if (1011 == e.getCode() || 1004 == e.getCode()) {
				response.setMessage(e.getMessage());
			}
			logger.error("Authentication failed. Exception: " + e.getMessage());
		}
		return new ResponseEntity<FastRxResponse>(response, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/authenticate/{deaNumber}")
	public ResponseEntity<FastRxResponse> authenticateUser(@RequestBody User user,
			@PathVariable(value = "deaNumber") String deaNumber, HttpServletResponse res) {
		logger.info("UserController :: authenticateUser method");
		logger.debug("User authentication for Pharmacist: " + user.getUsername());
		FastRxResponse response = new FastRxResponse();
		UserRole userRole = null;
		PharmacyLocation pharmacyLocation = null;
		UserLog userLog = new UserLog();
		try {
			userLog.setUsername(user.getUsername());
			userLog.setEvent("LOGIN");
			user = userService.authenticateUser(user);
			userRole = userRoleService.getUserRoleById(Long.valueOf(user.getRoleId()));
			pharmacyLocation = pharmacyLocationService.getPharmacyLocationByDEANumber(deaNumber);
			if (pharmacyLocation.getActive().equalsIgnoreCase("N")) {
				userLog.setStatus(com.parkwoodrx.fastrx.model.UserLog.Status.FAIL);
				userLog.setRemarks(FastRxErrorCodes.LOCATION_INACTIVE_MSG);
				userService.addUserLog(userLog);
				throw new FastRxException(FastRxErrorCodes.INACTIVE_CODE, FastRxErrorCodes.LOCATION_INACTIVE_MSG);
			}
			long corporationId = 0;
			if (userRole.getRoleName().equalsIgnoreCase("Super Admin")) {
				corporationId = pharmacyLocation.getCorporationId();
				if (!pharmacyService.isCorporationActive(corporationId)) {
					userLog.setStatus(com.parkwoodrx.fastrx.model.UserLog.Status.FAIL);
					userLog.setRemarks(FastRxErrorCodes.CORPORATION_INACTIVE_lOG_MSG);
					userService.addUserLog(userLog);
					throw new FastRxException(FastRxErrorCodes.INACTIVE_CODE,
							FastRxErrorCodes.CORPORATION_INACTIVE_MSG);
				}
			} else {
				corporationId = mappingEntitiesService.getCorporationIdForUser(user.getId());
				if (!pharmacyService.isCorporationActive(corporationId)) {
					userLog.setStatus(com.parkwoodrx.fastrx.model.UserLog.Status.FAIL);
					userLog.setRemarks(FastRxErrorCodes.CORPORATION_INACTIVE_lOG_MSG);
					userService.addUserLog(userLog);
					throw new FastRxException(FastRxErrorCodes.INACTIVE_CODE,
							FastRxErrorCodes.CORPORATION_INACTIVE_MSG);
				}
				if (pharmacyLocation.getCorporationId() != corporationId) {
					userLog.setStatus(com.parkwoodrx.fastrx.model.UserLog.Status.FAIL);
					userLog.setRemarks(FastRxErrorCodes.USER_NOT_LINKED_WITH_CORPORATION_OF_PROVIDED_LOCATION_PIN);
					userService.addUserLog(userLog);
					throw new FastRxException(FastRxErrorCodes.NOT_FOUND_CODE,
							FastRxErrorCodes.USER_NOT_LINKED_WITH_CORPORATION_OF_PROVIDED_LOCATION_PIN);
				}
			}

			tokenAuthenticationService.addAuthentication(res, user.getUsername());
			userLog.setStatus(com.parkwoodrx.fastrx.model.UserLog.Status.SUCCESS);
			userService.addUserLog(userLog);
			response.setData(new ResponseData(userRole.getRoleName(), userRole.getId(), String.valueOf(user.getId()),
					String.valueOf(corporationId), user.getUsername(), String.valueOf(pharmacyLocation.getId()),
					user.getFirstName() + " " + user.getLastName(), pharmacyLocation.getPharmacyName(),
					String.valueOf(user.getFirstLogin())));
			response.setStatus(Status.SUCCESS);
			response.setMessage("User authentication successful.");
			logger.info("User authentication successful.");
		} catch (FastRxException e) {
			response.setStatus(Status.ERROR);
			response.setMessage("Invalid credentials");
			if (1011 == e.getCode()) {
				response.setMessage(e.getMessage());
			}
			logger.error("Authentication failed. Exception: " + e.getMessage());
		}
		return new ResponseEntity<FastRxResponse>(response, HttpStatus.OK);

	}

	@RequestMapping(method = RequestMethod.GET, value = "/get-security-question/")
	public ResponseEntity<FastRxResponse> getSecurityQuestionByUsername(@QueryParam("username") String username) {
		logger.info("=====================Get user security question method=====================");
		logger.debug("Fetching security question for username:" + username);
		FastRxResponse response = new FastRxResponse();
		String securityQues = null;
		try {
			securityQues = userService.getSecurityQuestionByUsername(username);
			response.setStatus(Status.SUCCESS);
			response.setData(securityQues);
			response.setMessage("Security question fetched successfully.");
			logger.info("Security question fetched successfully.");
		} catch (FastRxException e) {
			response.setStatus(Status.ERROR);
			response.setData(000);
			response.setMessage(e.getMessage());
			logger.error("Error in fetching security question. Exception: " + e.getMessage());
		}
		return new ResponseEntity<FastRxResponse>(response, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/forgot-password")
	public ResponseEntity<FastRxResponse> resetPassword(@RequestBody User user) {
		logger.info("=====================Reset password method=====================");
		logger.debug("Reset password for username:" + user.getUsername());
		FastRxResponse response = new FastRxResponse();
		try {
			userService.resetPassword(user);
			response.setStatus(Status.SUCCESS);
			response.setData(200);
			response.setMessage("Password Reset Successfully.");
			logger.info("Password Reset Successfully..");
		} catch (FastRxException e) {
			response.setStatus(Status.ERROR);
			response.setData(000);
			response.setMessage(e.getMessage());
			logger.error("Error in reset password. Exception: " + e.getMessage());
		}
		return new ResponseEntity<FastRxResponse>(response, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/validate-security-answer/")
	public ResponseEntity<FastRxResponse> validateSecurityQuestionByUsername(@QueryParam("username") String username,
			@QueryParam("answer") String answer) {
		logger.info("=====================Validate user security answer method=====================");
		logger.debug("Validating security question for username:" + username);
		FastRxResponse response = new FastRxResponse();
		Boolean isSucessful = null;
		try {
			isSucessful = userService.validateSecurityQuestionByUsername(username, answer);
			response.setStatus(Status.SUCCESS);
			response.setData(isSucessful);
			response.setMessage("Security question validated successfully.");
			logger.info("Security question validated successfully.");
		} catch (FastRxException e) {
			response.setStatus(Status.ERROR);
			response.setData(000);
			response.setMessage(e.getMessage());
			logger.error("Error in validating security question. Exception: " + e.getMessage());
		}
		return new ResponseEntity<FastRxResponse>(response, HttpStatus.OK);
	}
}
