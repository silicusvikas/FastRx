package com.parkwoodrx.fastrx.repository;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;

import com.parkwoodrx.fastrx.model.SecurityQuestion;
import com.parkwoodrx.fastrx.model.User;
import com.parkwoodrx.fastrx.model.UserList;
import com.parkwoodrx.fastrx.model.UserLog;
import com.parkwoodrx.fastrx.model.UserSecurityQuestion;

public interface UserDao {
	public User getUserByUsernameAndPassword(String username, String password) throws EmptyResultDataAccessException;

	public String createUser(User user);

	public List<User> findAll();
	//userlogs
	List<UserLog> findAllUserLogs();

	public User findUserById(int id);

	public User create(final User user);

	public User findByUsername(String username);

	public int checkIfUserExists(String username);

	public long addUser(User userdetails, String owner) throws SQLException;

	public void updateUserPassword(String password, long userId);

	public User findUserByIdAndPassword(long id, String password);

	public void updateUser(User user);

	public List<UserList> getAllLocationAdminsForCorporation(long coprorationId);

	public List<UserList> getAllPharmacistsForCorporation(long coprorationId);

	public List<UserList> getAllLocationAdmins();

	public List<UserList> getAllPharmacists();

	public List<UserList> getAllPharmacyCorporationAdmins();

	public List<UserList> searchUserByCorporationId(String username, String phoneNo, String corpId, String roleId);

	public void updateStatusOfUser(long userId, String updatedBy, String status);

	public List<String> getTokenListOfPendingPrescriptionsByLocationPin(String locationPin) throws DataAccessException;

	public String getFaxnumberByToken(String token) throws DataAccessException;

	public void changeEfaxStatus(String token) throws DataAccessException;

	public List<SecurityQuestion> getSecurityQuestionList();

	public void setSecurityQuestion(UserSecurityQuestion userQuestion);

	public String getSecurityQuestionByUsername(String username) throws EmptyResultDataAccessException;

	public String validateSecurityQuestionByUsername(String username) throws EmptyResultDataAccessException;

	public void resetPassword(User user) throws DataAccessException;
	
	public void addUserLog(UserLog userLog) throws DataAccessException;
	
	public List<UserLog> searchUserLogByUsernameAndDate(String username, String startDate, String endDate) throws DataAccessException,ParseException;
	
}
