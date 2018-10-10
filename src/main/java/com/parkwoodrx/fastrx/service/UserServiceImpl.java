package com.parkwoodrx.fastrx.service;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.parkwoodrx.fastrx.constants.FastRxErrorCodes;
import com.parkwoodrx.fastrx.exception.FastRxException;
import com.parkwoodrx.fastrx.integration.EfaxService;
import com.parkwoodrx.fastrx.model.SecurityQuestion;
import com.parkwoodrx.fastrx.model.User;
import com.parkwoodrx.fastrx.model.UserList;
import com.parkwoodrx.fastrx.model.UserLog;
import com.parkwoodrx.fastrx.model.UserLog.Status;
import com.parkwoodrx.fastrx.model.UserSecurityQuestion;
import com.parkwoodrx.fastrx.repository.PharmacyCorporationDao;
import com.parkwoodrx.fastrx.repository.UserDao;
import com.parkwoodrx.fastrx.security.PasswordGenerator;

@Service
public class UserServiceImpl implements UserService {
	private static final Logger logger = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private UserDao userRepo;

	@Autowired
	private PasswordGenerator passwordGenerator;

	@Autowired
	private EmailService emailService;

	@Autowired
	private EfaxService fax;

	@Autowired
	private MappingEntitiesService mappingEntitiesService;

	@Autowired
	private PharmacyCorporationDao pharmacyDao;

	@Override
	public User authenticateUser(User user) {
		logger.debug("UserServiceImpl :: authenticateUser method");
		User userResult = null;
		UserLog userLog = new UserLog();
		String encryptedPassword;
		try {
			userLog.setUsername(user.getUsername());
			userLog.setEvent("LOGIN");
			encryptedPassword = passwordGenerator.generateHash(user.getPassword());
		} catch (Exception e) {
			userLog.setStatus(Status.FAIL);
			userLog.setRemarks(FastRxErrorCodes.INVALID_CREDENTIALS_MSG);
			userRepo.addUserLog(userLog);
			logger.error(e.getMessage());
			throw new FastRxException(FastRxErrorCodes.PWD_ENCRYPTION_ERROR_CODE,
					FastRxErrorCodes.PWD_ENCRYPTION_ERROR_MSG);
		}
		try {
			int count = userRepo.checkIfUserExists(user.getUsername());
			if (count > 0) {
				userResult = userRepo.getUserByUsernameAndPassword(user.getUsername(), encryptedPassword);
			} else {
				throw new FastRxException(FastRxErrorCodes.INVALID_CODE, FastRxErrorCodes.INCORRECT_USERNAME);
			}

		} catch (EmptyResultDataAccessException e) {
			userLog.setStatus(Status.FAIL);
			userLog.setRemarks(FastRxErrorCodes.INCORRECT_PASSWORD);
			userRepo.addUserLog(userLog);
			logger.error(e.getMessage());
			throw new FastRxException(FastRxErrorCodes.INVALID_CODE, FastRxErrorCodes.INCORRECT_PASSWORD);
		}

		return userResult;
	}

	@Override
	@Transactional
	public long addUser(User userdetails, String owner, long corporationId, long locationId) {
		logger.debug("UserServiceImpl :: addUser mehtod");
		long userId = 0;
		String corporationName = null;
		String encryptedPassword;
		if (0 < userRepo.checkIfUserExists(userdetails.getUsername())) {
			throw new FastRxException(FastRxErrorCodes.DUPLICATE_CODE, FastRxErrorCodes.USER_EXISTS_MSG);
		}
		try {
			encryptedPassword = passwordGenerator.generateHash(userdetails.getPassword());
			userdetails.setPassword(encryptedPassword);
			userdetails.setActive("Y");
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new FastRxException(FastRxErrorCodes.CREATE_ERROR_CODE, FastRxErrorCodes.ERROR_CREATING_USER_MSG);
		}

		try {
			userId = userRepo.addUser(userdetails, owner);
			corporationName = pharmacyDao.getPharmacyCorporationById(corporationId).getCorporationName();
			emailService.sendUserRegistrationEmail(userdetails.getUsername(), corporationName);
		} catch (Exception e1) {
			logger.error(e1.getMessage());
			throw new FastRxException(FastRxErrorCodes.CREATE_ERROR_CODE, FastRxErrorCodes.ERROR_CREATING_USER_MSG);
		}
		if (userId > 0) {
			mappingEntitiesService.addCorporationUserMapping(corporationId, userId);
			if (locationId != 0) {
				mappingEntitiesService.addLocationUserMapping(locationId, userId);
			}

		} else {
			throw new FastRxException(FastRxErrorCodes.CREATE_ERROR_CODE, FastRxErrorCodes.ERROR_CREATING_USER_MSG);
		}
		return userId;
	}

	@Override
	public User checkIfUserExists(String username) {
		logger.info("UserServiceImpl :: checkIfUserExists mehtod for:" + username);
		User user = null;
		try {
			user = userRepo.findByUsername(username);
		} catch (Exception e) {
			logger.info(e.getMessage());
			throw new FastRxException(FastRxErrorCodes.NOT_FOUND_CODE, FastRxErrorCodes.USER_DOESNT_EXISTS_MSG);
		}
		return user;
	}

	@Override
	public void generateAndUpdateUserPassword(User user) {
		logger.debug("UserServiceImpl :: generateAndUpdateUserPassword mehtod");
		String newPassword = passwordGenerator.generatePassword();
		String encryptedPassword;
		try {
			encryptedPassword = passwordGenerator.generateHash(newPassword);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new FastRxException(FastRxErrorCodes.UPDATE_ERROR_CODE, FastRxErrorCodes.UPDATE_PWD_ERROR_MSG);
		}
		try {
			userRepo.updateUserPassword(encryptedPassword, user.getId());
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new FastRxException(FastRxErrorCodes.UPDATE_ERROR_CODE, FastRxErrorCodes.UPDATE_PWD_ERROR_MSG);
		}
		try {
			emailService.sendPwdResetEmail(newPassword, user.getUsername());
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new FastRxException(FastRxErrorCodes.EMAIL_SENDING_ERROR_CODE,
					FastRxErrorCodes.EMAIL_SENDING_ERROR_MSG);
		}

	}

	@Override
	public User getUserByIdAndPassword(long id, String password) {
		logger.debug("UserServiceImpl :: getUserByIdAndPassword mehtod");
		String encryptedPassword;
		try {
			encryptedPassword = passwordGenerator.generateHash(password);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new FastRxException(FastRxErrorCodes.PWD_ENCRYPTION_ERROR_CODE,
					FastRxErrorCodes.PWD_ENCRYPTION_ERROR_MSG);
		}
		User user;
		try {
			user = userRepo.findUserByIdAndPassword(id, encryptedPassword);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new FastRxException(FastRxErrorCodes.INVALID_CODE, FastRxErrorCodes.INVALID_OLD_PWD_MSG);
		}
		if (null == user) {
			logger.info("User does not exist");
			throw new FastRxException(FastRxErrorCodes.NOT_FOUND_CODE, FastRxErrorCodes.USER_DOESNT_EXISTS_MSG);
		}
		return user;
	}

	@Override
	@Transactional
	public void updateUserPassword(long id, String password) {
		logger.debug("UserServiceImpl :: updateUserPassword mehtod");

		String encryptedPassword;
		try {
			encryptedPassword = passwordGenerator.generateHash(password);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new FastRxException(FastRxErrorCodes.PWD_ENCRYPTION_ERROR_CODE,
					FastRxErrorCodes.PWD_ENCRYPTION_ERROR_MSG);
		}
		try {
			userRepo.updateUserPassword(encryptedPassword, id);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new FastRxException(FastRxErrorCodes.UPDATE_ERROR_CODE, FastRxErrorCodes.UPDATE_PWD_ERROR_MSG);
		}
	}

	@Override
	public String generatePassword() throws NoSuchAlgorithmException {
		logger.debug("UserServiceImpl :: generatePassword mehtod");
		return passwordGenerator.generateHash(passwordGenerator.generatePassword());
	}

	@Override
	public List<User> getUserListByPharmacyId() {
		return null;
	}

	@Override
	public List<User> getUserList() {
		return userRepo.findAll();
	}

	@Override
	public void updateUser(User user) {
		logger.debug("UserServiceImpl :: updateUser mehtod");
		try {
			userRepo.updateUser(user);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new FastRxException(FastRxErrorCodes.UPDATE_ERROR_CODE, FastRxErrorCodes.USER_UPDATION_ERROR_MSG);
		}

	}

	@Override
	public List<UserList> getLocationAdminUserListForCorporationId(long corporationId) {
		logger.debug("UserServiceImpl :: updateUser mehtod");
		try {
			return userRepo.getAllLocationAdminsForCorporation(corporationId);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new FastRxException(FastRxErrorCodes.NOT_FOUND_CODE, FastRxErrorCodes.USER_LIST_NOT_FOUND);
		}
	}

	@Override
	public List<UserList> getPharmacistUserListForCorporationId(long corporationId) {
		logger.debug("UserServiceImpl :: updateUser mehtod");
		try {
			return userRepo.getAllPharmacistsForCorporation(corporationId);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new FastRxException(FastRxErrorCodes.NOT_FOUND_CODE, FastRxErrorCodes.USER_LIST_NOT_FOUND);
		}
	}

	@Override
	public List<UserList> getLocationAdminUserList() {
		logger.debug("UserServiceImpl :: updateUser mehtod");
		try {
			return userRepo.getAllLocationAdmins();
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new FastRxException(FastRxErrorCodes.NOT_FOUND_CODE, FastRxErrorCodes.USER_LIST_NOT_FOUND);
		}
	}

	@Override
	public List<UserList> getPharmacistUserList() {
		logger.debug("UserServiceImpl :: updateUser mehtod");
		try {
			return userRepo.getAllPharmacists();
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new FastRxException(FastRxErrorCodes.NOT_FOUND_CODE, FastRxErrorCodes.USER_LIST_NOT_FOUND);
		}
	}

	@Override
	public List<UserList> getCorporationAdminUserList() {
		logger.debug("UserServiceImpl :: updateUser mehtod");
		List<UserList> list = null;
		try {
			list = userRepo.getAllPharmacyCorporationAdmins();
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new FastRxException(FastRxErrorCodes.NOT_FOUND_CODE, FastRxErrorCodes.USER_LIST_NOT_FOUND);
		}
		return list;
	}

	@Override
	public List<UserList> searchUserByCorporationId(String username, String phoneNo, String corpId, String roleId) {
		List<UserList> userList = null;
		try {
			userList = userRepo.searchUserByCorporationId(username, phoneNo, corpId, roleId);
			if (null == userList || userList.size() == 0) {
				throw new FastRxException(FastRxErrorCodes.NOT_FOUND_CODE, FastRxErrorCodes.USER_LIST_NOT_FOUND);
			}
		} catch (Exception e) {
			logger.info("SERVICE::" + e.getMessage());
			throw new FastRxException(FastRxErrorCodes.NOT_FOUND_CODE, FastRxErrorCodes.USER_LIST_NOT_FOUND);
		}

		return userList;
	}

	@Override
	public void updatedStatusOfUser(long userId, String updatedBy, String status) {
		try {
			userRepo.updateStatusOfUser(userId, updatedBy, status);
		} catch (Exception e) {
			logger.info("SERVICE::" + e.getMessage());
			throw new FastRxException(FastRxErrorCodes.UPDATE_ERROR_CODE,
					FastRxErrorCodes.USER_STATUS_UPDATE_ERROR_MSG);
		}
	}

	@Transactional
	public List<String> sendEfaxForPendingPrescription(String locationPin) throws FastRxException, Exception {
		List<String> tokenList = null;
		String faxnumber = null;
		try {
			tokenList = userRepo.getTokenListOfPendingPrescriptionsByLocationPin(locationPin);
			if (null != tokenList && (!tokenList.isEmpty() || tokenList.size() != 0)) {
				for (String token : tokenList) {
					faxnumber = userRepo.getFaxnumberByToken(token);
					try {
						fax.sendFax(faxnumber,null,null,null);
						userRepo.changeEfaxStatus(token);
					} catch (Exception e) {
						logger.error(e.getMessage());
						throw new FastRxException(FastRxErrorCodes.EMAIL_SENDING_ERROR_CODE,
								FastRxErrorCodes.EFAX_SENDING_ERROR_MSG);
					}

				}
			}
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			throw new FastRxException(FastRxErrorCodes.NOT_FOUND_CODE, FastRxErrorCodes.PRESCRIPTION_LIST_NOT_FOUND);
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			throw new FastRxException(FastRxErrorCodes.EMAIL_SENDING_ERROR_CODE,
					FastRxErrorCodes.EFAX_SENDING_ERROR_MSG);
		}
		return tokenList;
	}

	@Override
	public List<SecurityQuestion> getSecurityQuestionList() {
		List<SecurityQuestion> questionList = null;
		try {
			questionList = userRepo.getSecurityQuestionList();
			if (null == questionList || questionList.size() == 0) {
				throw new FastRxException(FastRxErrorCodes.NOT_FOUND_CODE, FastRxErrorCodes.QUESTION_LIST_NOT_FOUND);
			}
		} catch (Exception e) {
			logger.info("SERVICE::" + e.getMessage());
			throw new FastRxException(FastRxErrorCodes.NOT_FOUND_CODE, FastRxErrorCodes.QUESTION_LIST_NOT_FOUND);
		}

		return questionList;
	}

	@Override
	@Transactional
	public void setSecurityQuestion(UserSecurityQuestion userQuestion) {
		logger.debug("UserServiceImpl :: updateUserPassword mehtod");

		String encryptedAnswer;
		try {
			encryptedAnswer = passwordGenerator.generateHash(userQuestion.getAnswer());
			userQuestion.setAnswer(encryptedAnswer);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new FastRxException(FastRxErrorCodes.PWD_ENCRYPTION_ERROR_CODE,
					FastRxErrorCodes.ERROR_ADDING_SECURITY_QUESTION);
		}
		try {
			userRepo.setSecurityQuestion(userQuestion);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new FastRxException(FastRxErrorCodes.CREATE_ERROR_CODE,
					FastRxErrorCodes.ERROR_ADDING_SECURITY_QUESTION);
		}

	}

	@Override
	public String getSecurityQuestionByUsername(String username) {
		String securityQuestion = null;
		User user = null;
		try {
			user = userRepo.findByUsername(username);
			if (null == user) {
				throw new FastRxException(FastRxErrorCodes.NOT_FOUND_CODE, FastRxErrorCodes.INCORRECT_USERNAME);
			}
		} catch (EmptyResultDataAccessException e) {
			throw new FastRxException(FastRxErrorCodes.NOT_FOUND_CODE, FastRxErrorCodes.INCORRECT_USERNAME);
		}
		try {
			securityQuestion = userRepo.getSecurityQuestionByUsername(username);
		} catch (EmptyResultDataAccessException e) {
			throw new FastRxException(FastRxErrorCodes.NOT_FOUND_CODE, FastRxErrorCodes.SECURITY_QUESTION_NOT_SET);
		}
		return securityQuestion;

	}

	@Override
	public Boolean validateSecurityQuestionByUsername(String username, String answer) {
		String answerFromDB = null;
		String encryptAnswer = null;
		boolean result = false;
		try {
			encryptAnswer = passwordGenerator.generateHash(answer);
			answerFromDB = userRepo.validateSecurityQuestionByUsername(username);
			if (answerFromDB.equalsIgnoreCase(encryptAnswer)) {
				result = true;
			}
		} catch (Exception e) {
			throw new FastRxException(FastRxErrorCodes.PWD_ENCRYPTION_ERROR_CODE,
					FastRxErrorCodes.ERROR_VALIDATING_SECURITY_ANSWER);
		}
		return result;
	}

	@Override
	public void resetPassword(User user) {
		String encryptedPassword = null;
		try {
			encryptedPassword = passwordGenerator.generateHash(user.getPassword());
			user.setPassword(encryptedPassword);
			userRepo.resetPassword(user);
		} catch (Exception e) {
			throw new FastRxException(FastRxErrorCodes.PWD_ENCRYPTION_ERROR_CODE,
					FastRxErrorCodes.ERROR_RESET_PASSWORD);
		}

	}

	@Override
	public void logout(String username) {
		try {
			UserLog userLog=new UserLog();
			userLog.setUsername(username);
			userLog.setEvent("LOGOUT");
			userLog.setStatus(Status.SUCCESS);
			userRepo.addUserLog(userLog);
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			throw new FastRxException(FastRxErrorCodes.CREATE_ERROR_CODE,
					FastRxErrorCodes.ERROR_LOGOUT);
		}
		
	}


       @Override
	   public List<UserLog> getUserLogs() {  
             return userRepo.findAllUserLogs();
		
	}

	@Override
	public List<UserLog> searchUserLogByUsernameAndDate(String username, String startDate, String endDate) {
		List<UserLog> userLogList=null;
		try {
			userLogList= userRepo.searchUserLogByUsernameAndDate(username, startDate, endDate);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new FastRxException(FastRxErrorCodes.NOT_FOUND_CODE,
					FastRxErrorCodes.NO_LOGS_FOUND);
		}
		return userLogList;
	}

	@Override
	public void addUserLog(UserLog userLog) {
		try {
			userRepo.addUserLog(userLog);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new FastRxException(FastRxErrorCodes.CREATE_ERROR_CODE,
					FastRxErrorCodes.ERROR_ADDING_AUDIT_LOG);
		}
		
	}

}
