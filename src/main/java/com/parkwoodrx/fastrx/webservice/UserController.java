package com.parkwoodrx.fastrx.webservice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.QueryParam;

import org.json.JSONException;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.parkwoodrx.fastrx.exception.FastRxException;
import com.parkwoodrx.fastrx.model.SecurityQuestion;
import com.parkwoodrx.fastrx.model.User;
import com.parkwoodrx.fastrx.model.UserList;
import com.parkwoodrx.fastrx.model.UserLog;
import com.parkwoodrx.fastrx.model.UserRole;
import com.parkwoodrx.fastrx.model.UserSecurityQuestion;
import com.parkwoodrx.fastrx.service.MappingEntitiesService;
import com.parkwoodrx.fastrx.service.UserRoleService;
import com.parkwoodrx.fastrx.service.UserService;
import com.parkwoodrx.fastrx.webservice.FastRxResponse.Status;

@RestController
@RequestMapping("/user")
public class UserController {
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	@Autowired
	private UserRoleService userRoleService;

	@Autowired
	private MappingEntitiesService mappingEntitiesService;

	@RequestMapping(method = RequestMethod.POST, value = "/createUser")
	public ResponseEntity<FastRxResponse> createUser(@RequestBody String params) {
		logger.info("=====================UserController :: createUser method=======================");
		FastRxResponse response = new FastRxResponse();
		try {
			String corporationName = null;
			long corporationId = 0;
			long locationId = 0;
			JSONObject jsonObj = new JSONObject(params);
			ObjectMapper mapper = new ObjectMapper();
			User user = mapper.readValue(jsonObj.getString("user"), User.class);
			corporationId = jsonObj.getLong("corporationId");
			locationId = jsonObj.getLong("locationId");
			long userId = userService.addUser(user, jsonObj.getString("owner"), corporationId, locationId);
			if (userId != 0) {
				response.setData(userId);
				response.setStatus(Status.SUCCESS);
				response.setMessage("User created successfully.");
				logger.info("User creation successful.");
			}
		} catch (FastRxException e) {
			response.setStatus(Status.ERROR);
			response.setMessage(e.getMessage());
			logger.error("User creation failed: " + e.getMessage());
		} catch (JSONException | IOException e) {
			response.setStatus(Status.ERROR);
			response.setMessage("User creation failed.");
			logger.error("User creation failed. Exception: " + e.getMessage());
		}
		return new ResponseEntity<FastRxResponse>(response, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/changePassword")
	public ResponseEntity<FastRxResponse> changePassword(@RequestBody String params, HttpServletRequest req) {
		logger.info("=====================UserController :: changePassword method=====================");
		FastRxResponse response = new FastRxResponse();
		try {

			JSONObject jsonObj = new JSONObject(params);
			User user = null;
			user = userService.getUserByIdAndPassword(Long.parseLong(jsonObj.getString("userId")),
					jsonObj.getString("oldPassword"));

			userService.updateUserPassword(user.getId(), jsonObj.getString("newPassword"));
			response.setStatus(Status.SUCCESS);
			response.setMessage("Password updated successfully.");
			logger.info("Changing password successful.");
		}catch (FastRxException fe) {
			response.setStatus(Status.ERROR);
			response.setMessage(fe.getMessage());
			logger.error("Exception: " + fe.getMessage());
		}catch (Exception e) {
			response.setStatus(Status.ERROR);
			response.setMessage("Password updation failed.");
			logger.error("Changing password failed. Exception: " + e.getMessage());
		}
		return new ResponseEntity<FastRxResponse>(response, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/roleList")
	public ResponseEntity<FastRxResponse> getAllUserRoleList() {
		logger.info("=====================UserController :: getAllUserRoleList method=====================");
		List<UserRole> userRoleList = new ArrayList<UserRole>();
		FastRxResponse response = new FastRxResponse();
		try {
			userRoleList = userRoleService.getAllUserRole();
			if (!userRoleList.isEmpty()) {
				response.setData(userRoleList);
				response.setStatus(Status.SUCCESS);
				response.setMessage("User Role list fetched successfully.");
				logger.info("User Role list fetched successfully.");
			}
		} catch (FastRxException e) {
			response.setStatus(Status.ERROR);
			response.setMessage("Error in fetching list of User Roles.");
			logger.error("Error in fetching list of all User Roles. Exception: " + e.getMessage());
		}
		return new ResponseEntity<FastRxResponse>(response, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/roleListForSuperAdmin")
	public ResponseEntity<FastRxResponse> getRoleListForSuperAdmin() {
		logger.info("=====================UserController :: getRoleListForSuperAdmin method=====================");
		List<UserRole> userRoleList = new ArrayList<UserRole>();
		FastRxResponse response = new FastRxResponse();
		try {
			userRoleList = userRoleService.getAllUserRoleForSuperAdmin();
			if (!userRoleList.isEmpty()) {
				response.setData(userRoleList);
				response.setStatus(Status.SUCCESS);
				response.setMessage("User Role list fetched successfully.");
				logger.info("User Role list fetched successfully.");
			}
		} catch (FastRxException e) {
			response.setStatus(Status.ERROR);
			response.setMessage("Error in fetching list of User Roles");
			logger.error("Error in fetching list of all User Roles. Exception: " + e.getMessage());

		}
		return new ResponseEntity<FastRxResponse>(response, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/forgotPwd")
	public ResponseEntity<FastRxResponse> setNewPassword(@RequestBody String username) {
		logger.info("=====================UserController :: setNewPassword method=====================");
		logger.debug("Password generation for :" + username);
		FastRxResponse response = new FastRxResponse();
		User user = null;
		try {
			user = userService.checkIfUserExists(username);
			userService.generateAndUpdateUserPassword(user);
			response.setStatus(Status.SUCCESS);
			response.setMessage("Password updated successfully.");
			logger.info("Password updated successfully.");
		} catch (FastRxException e) {
			response.setStatus(Status.ERROR);
			response.setMessage("Password updation failed.");
			logger.error("Password updation failed. Exception: " + e.getMessage());
		}
		return new ResponseEntity<FastRxResponse>(response, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/getProfile/")
	public ResponseEntity<FastRxResponse> getUserByUsername(@QueryParam(value = "username") String username) {
		logger.info("=====================UserController :: getUserByUsername method=====================");
		FastRxResponse response = new FastRxResponse();
		User user = null;
		try {
			user = userService.checkIfUserExists(username);
			response.setData(user);
			response.setStatus(Status.SUCCESS);
			response.setMessage("User details found.");
			logger.info("User details found.");
		} catch (FastRxException e) {
			response.setStatus(Status.ERROR);
			response.setMessage("User details not available");
			logger.error("User details not available. Exception: " + e.getMessage());
		}
		return new ResponseEntity<FastRxResponse>(response, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/updateUserProfile")
	public ResponseEntity<FastRxResponse> updateUserProfile(@RequestBody User user) {
		logger.info("=====================UserController :: updateUserProfile method=====================");
		FastRxResponse response = new FastRxResponse();
		try {
			userService.updateUser(user);
			response.setStatus(Status.SUCCESS);
			response.setMessage("User details updated successfully.");
			logger.info("User details updated successfully.");
		} catch (FastRxException e) {
			response.setStatus(Status.ERROR);
			response.setMessage("User profile updation failed.");
			logger.error("User profile updation failed. Exception: " + e.getMessage());
		}
		return new ResponseEntity<FastRxResponse>(response, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/pharmacist/{id}")
	public ResponseEntity<FastRxResponse> getPharmacistUserListForCorpId(@PathVariable Long id) {
		logger.info(
				"=====================UserController :: getPharmacistUserListForCorpId method=====================");
		List<UserList> userList = new ArrayList<UserList>();
		FastRxResponse response = new FastRxResponse();
		try {
			userList = userService.getPharmacistUserListForCorporationId(id);
			if (!userList.isEmpty()) {
				response.setData(userList);
				response.setStatus(Status.SUCCESS);
				response.setMessage("Pharmacist Users list found.");
				logger.info("Pharmacist Users list found.");
			}
		} catch (FastRxException e) {
			response.setStatus(Status.ERROR);
			response.setMessage("Pharmacist Users list not found");
			logger.error("Pharmacist Users list not fetched. Exception:" + e.getMessage());
		}
		return new ResponseEntity<FastRxResponse>(response, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/location/{id}")
	public ResponseEntity<FastRxResponse> getLocationAdminUserListForCorpId(@PathVariable Long id) {
		logger.info(
				"=====================UserController :: getLocationAdminUserListForCorpId method=====================");
		List<UserList> userList = new ArrayList<UserList>();
		FastRxResponse response = new FastRxResponse();
		try {
			userList = userService.getLocationAdminUserListForCorporationId(id);
			if (!userList.isEmpty()) {
				response.setData(userList);
				response.setStatus(Status.SUCCESS);
				response.setMessage("Location Users list found.");
				logger.info("Location Users list found.");
			}
		} catch (FastRxException e) {
			response.setStatus(Status.ERROR);
			response.setMessage("Location Users list not found.");
			logger.error("Location Users list found. Exception: " + e.getMessage());
		}
		return new ResponseEntity<FastRxResponse>(response, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/location")
	public ResponseEntity<FastRxResponse> getLocationAdminUserList() {
		logger.info("=====================UserController :: getLocationAdminUserList method=====================");
		List<UserList> userList = new ArrayList<UserList>();
		FastRxResponse response = new FastRxResponse();
		try {
			userList = userService.getLocationAdminUserList();
			if (!userList.isEmpty()) {
				response.setData(userList);
				response.setStatus(Status.SUCCESS);
				response.setMessage("Location Users list found.");
				logger.info("Location Users list found.");
			}
		} catch (FastRxException e) {
			response.setStatus(Status.ERROR);
			response.setMessage("Location Users list not found.");
			logger.error("Location Users list not found. Exception: " + e.getMessage());
		}
		return new ResponseEntity<FastRxResponse>(response, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/pharmacist")
	public ResponseEntity<FastRxResponse> getPharmacistUserList() {
		logger.info("=====================UserController :: getPharmacistUserList method=====================");
		List<UserList> userList = new ArrayList<UserList>();
		FastRxResponse response = new FastRxResponse();
		try {
			userList = userService.getPharmacistUserList();
			if (!userList.isEmpty()) {
				response.setData(userList);
				response.setStatus(Status.SUCCESS);
				response.setMessage("Pharmacist Users list found.");
				logger.info("Pharmacist Users list found.");
			}
		} catch (FastRxException e) {
			response.setStatus(Status.ERROR);
			response.setMessage("Pharmacist Users list not found.");
			logger.error("Pharmacist Users list found. Exception: " + e.getMessage());
		}
		return new ResponseEntity<FastRxResponse>(response, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/corporation")
	public ResponseEntity<FastRxResponse> getCorporationAdminUserList() {
		logger.info("=====================UserController :: getCorporationAdminUserList method=====================");
		List<UserList> userList = new ArrayList<UserList>();
		FastRxResponse response = new FastRxResponse();
		try {
			userList = userService.getCorporationAdminUserList();
			logger.info(userList.toString());
			if (!userList.isEmpty()) {
				response.setData(userList);
				response.setStatus(Status.SUCCESS);
				response.setMessage("Corporation Users list found.");
				logger.info("Corporation Users list found.");
			}
		} catch (FastRxException e) {
			response.setStatus(Status.ERROR);
			response.setMessage("Corporation Users list not found.");
			logger.error("Corporation Users list found. Exception: " + e.getMessage());
		}
		return new ResponseEntity<FastRxResponse>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<FastRxResponse> searchUserByCorporationId(@QueryParam("username") String username,
			@QueryParam("phoneNo") String phoneNo, @QueryParam("corpId") String corpId,
			@QueryParam("roleId") String roleId) {
		logger.info("Fetching list of all users for corporationId:" + corpId);
		List<UserList> userList = null;
		FastRxResponse response = new FastRxResponse();
		try {
			userList = userService.searchUserByCorporationId(username, phoneNo, corpId, roleId);
			if (null != userList && !userList.isEmpty()) {
				response.setData(userList);
				response.setStatus(Status.SUCCESS);
				response.setMessage("Corporation Users list found.");
				logger.info("Corporation Users list found.");
			}
		} catch (FastRxException e) {
			response.setStatus(Status.ERROR);
			response.setMessage("Corporation Users list not found.");
			logger.error("Corporation Users list found. Exception:" + e.getMessage());

		}
		return new ResponseEntity<FastRxResponse>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/changeStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<FastRxResponse> changeStatusUser(@RequestBody String params) {
		logger.info("=====================Update status for user=====================");
		FastRxResponse response = new FastRxResponse();
		String status = null;
		try {
			JSONObject jsonObj = new JSONObject(params);
			status = jsonObj.getString("status");
			userService.updatedStatusOfUser(Long.parseLong(jsonObj.getString("userId")), jsonObj.getString("updatedBy"),
					jsonObj.getString("status"));
			response.setData(200);
			response.setStatus(Status.SUCCESS);
			if (status.equalsIgnoreCase("Y")) {
				response.setMessage("User activated successfully.");
				logger.info("User activated successfully.");
			} else {
				response.setMessage("User deactivated successfully.");
				logger.info("User deactivated successfully.");
			}

		} catch (Exception e) {
			response.setStatus(Status.ERROR);
			if (status.equalsIgnoreCase("Y")) {
				response.setMessage("Error in activating user.");
				logger.error("Error in activating user. Exception:" + e.getMessage());
			} else {
				response.setMessage("Error in deactivating user.");
				logger.error("Error in deactivating user. Exception:" + e.getMessage());
			}
		}
		return new ResponseEntity<FastRxResponse>(response, HttpStatus.OK);

	}

	@RequestMapping(value = "/security-question-list", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<FastRxResponse> getSecurityQuestionList() {
		logger.info("Fetching State List");
		List<SecurityQuestion> questionList = null;
		FastRxResponse response = new FastRxResponse();
		try {
			questionList = userService.getSecurityQuestionList();
			if (null != questionList || !questionList.isEmpty() || questionList.size() != 0) {
				response.setData(questionList);
				response.setStatus(Status.SUCCESS);
				response.setMessage("Fetched  security question list successfully.");
				logger.info("Fetched  security question list successfully");
			}
		} catch (FastRxException e) {
			response.setStatus(Status.ERROR);
			response.setMessage(e.getMessage());
			response.setData(000);
			logger.error("Error in fetching security question list");

		}
		return new ResponseEntity<FastRxResponse>(response, HttpStatus.OK);

	}

	@RequestMapping(method = RequestMethod.POST, value = "/set-security-question")
	public ResponseEntity<FastRxResponse> setSecurityQuestion(@RequestBody UserSecurityQuestion User) {
		logger.info("=====================Set user security question method=====================");
		logger.debug("Setting security question for user ID:" + User.getId());
		FastRxResponse response = new FastRxResponse();
		try {
			userService.setSecurityQuestion(User);
			response.setStatus(Status.SUCCESS);
			response.setData(200);
			response.setMessage("Security question set successfully.");
			logger.info("Security question set successfully.");
		} catch (FastRxException e) {
			response.setStatus(Status.ERROR);
			response.setData(000);
			response.setMessage(e.getMessage());
			logger.error("Error in adding security question. Exception: " + e.getMessage());
		}
		return new ResponseEntity<FastRxResponse>(response, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/logout")
	public ResponseEntity<FastRxResponse> logoutUser(@QueryParam("username") String username) {
		logger.info("=====================Logout method=====================");
		logger.debug("Logout user with username:" + username);
		FastRxResponse response = new FastRxResponse();
		try {
			userService.logout(username);
			response.setStatus(Status.SUCCESS);
			response.setData(200);
			response.setMessage("Logout sucessful");
			logger.info("Logout sucessful");
		} catch (FastRxException e) {
			response.setStatus(Status.ERROR);
			response.setData(000);
			response.setMessage(e.getMessage());
			logger.error("Error in logout,Exception: " + e.getMessage());
		}
		return new ResponseEntity<FastRxResponse>(response, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/userLogs")
	public ResponseEntity<FastRxResponse> getUserLogs() {
		logger.info("=====================GetUserLogs=====================");

		FastRxResponse response = new FastRxResponse();
		List<UserLog> userLogs = null;

		try {
			userLogs = userService.getUserLogs();
			if (null != userLogs || !userLogs.isEmpty() || userLogs.size() != 0) {
				response.setData(userLogs);
				response.setStatus(Status.SUCCESS);
				response.setMessage("Fetched Userlogs list successfully.");
				logger.info("Fetched Userlogs list successfully.");
			}
		} catch (FastRxException e) {
			response.setStatus(Status.ERROR);
			response.setMessage(e.getMessage());
			response.setData(000);
			logger.error("Fetched Userlogs list successfully.");

		}
		return new ResponseEntity<FastRxResponse>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/user-logs", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<FastRxResponse> searchUserLogByUsernameAndDate(@QueryParam("username") String username,
			@QueryParam("startDate") String startDate, @QueryParam("endDate") String endDate) {
		logger.info("************** Fetching user audit log list ****************");
		List<UserLog> userLog = null;
		FastRxResponse response = new FastRxResponse();
		try {
			userLog = userService.searchUserLogByUsernameAndDate(username, startDate, endDate);
			if (null != userLog && !userLog.isEmpty()) {
				response.setData(userLog);
				response.setStatus(Status.SUCCESS);
				response.setMessage(userLog.size()+" user audit log found.");
				logger.info(userLog.size()+" user audit log found.");
			}else{
				response.setData(0);
				response.setStatus(Status.SUCCESS);
				response.setMessage("No user audit log not found.");
				logger.info("No user audit log not found.");
			}
		} catch (FastRxException e) {
			response.setData(0);
			response.setStatus(Status.ERROR);
			response.setMessage("No user audit log not found."); 
			logger.error("User audit log Exception:" + e.getMessage());

		}
		return new ResponseEntity<FastRxResponse>(response, HttpStatus.OK);
	}

}
