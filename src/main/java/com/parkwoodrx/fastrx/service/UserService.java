package com.parkwoodrx.fastrx.service;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import com.parkwoodrx.fastrx.model.SecurityQuestion;
import com.parkwoodrx.fastrx.model.User;
import com.parkwoodrx.fastrx.model.UserList;
import com.parkwoodrx.fastrx.model.UserLog;
import com.parkwoodrx.fastrx.model.UserSecurityQuestion;

public interface UserService {
	public User authenticateUser(User user);

	public User checkIfUserExists(String username);

	public List<User> getUserList();

	public List<User> getUserListByPharmacyId();

	public long addUser(User userdetails, String owner, long corporationId, long locationId);

	public void generateAndUpdateUserPassword(User user);

	public String generatePassword() throws NoSuchAlgorithmException;

	public void updateUserPassword(long id, String password);

	public User getUserByIdAndPassword(long id, String password);

	public void updateUser(User user);

	public List<UserList> getLocationAdminUserListForCorporationId(long corporationId);

	public List<UserList> getPharmacistUserListForCorporationId(long corporationId);

	public List<UserList> getLocationAdminUserList();

	public List<UserList> getPharmacistUserList();

	public List<UserList> getCorporationAdminUserList();

	public List<UserList> searchUserByCorporationId(String username, String phoneNo, String corpId, String roleId);

	public void updatedStatusOfUser(long userId, String updatedBy, String status);

	public List<SecurityQuestion> getSecurityQuestionList();

	public void setSecurityQuestion(UserSecurityQuestion userQuestion);

	public String getSecurityQuestionByUsername(String username);

	public Boolean validateSecurityQuestionByUsername(String username, String answer);

	public void resetPassword(User user);

	public void logout(String username);

	public List<UserLog> getUserLogs();

	public List<UserLog> searchUserLogByUsernameAndDate(String username, String startDate, String endDate);

	public void addUserLog(UserLog userLog);
}
