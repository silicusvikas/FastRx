package com.parkwoodrx.fastrx.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.parkwoodrx.fastrx.constants.FastRxQueryConstants;
import com.parkwoodrx.fastrx.mapper.BillingDetailsRowMapper;
import com.parkwoodrx.fastrx.mapper.SecurityQuestionRowMapper;
import com.parkwoodrx.fastrx.mapper.UserListRowMapper;
import com.parkwoodrx.fastrx.mapper.UserLogRowMapper;
import com.parkwoodrx.fastrx.mapper.UserRowMapper;
import com.parkwoodrx.fastrx.model.SecurityQuestion;
import com.parkwoodrx.fastrx.model.User;
import com.parkwoodrx.fastrx.model.UserList;
import com.parkwoodrx.fastrx.model.UserLog;
import com.parkwoodrx.fastrx.model.UserSecurityQuestion;

@Repository
public class UserDaoImpl implements UserDao {

	private static final Logger logger = LoggerFactory.getLogger(UserDao.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public User getUserByUsernameAndPassword(String username, String password) throws EmptyResultDataAccessException {
		logger.debug("UserDaoImpl :: getUserByUsernameAndPassword method");
		User user = jdbcTemplate.queryForObject(FastRxQueryConstants.GET_USER_BY_USERNAME_PWD,
				new Object[] { username, password }, new UserRowMapper());
		return user;
	}

	@Override
	public String createUser(User user) {
		logger.debug("UserDaoImpl :: createUser method");
		return null;
	}

	@Override
	@Transactional(readOnly = true)
	public List<User> findAll() {
		logger.debug("UserDaoImpl :: findAll method");
		return jdbcTemplate.query("select * from users", new UserRowMapper());
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserLog> findAllUserLogs() {
		logger.debug("UserDaoImpl :: findAllUserLogs method");
		return jdbcTemplate.query("select * from user_log ORDER BY DATE DESC", new UserLogRowMapper());
	}

	@Override
	@Transactional(readOnly = true)
	public User findUserById(int id) {
		logger.debug("UserDaoImpl :: findUserById method");
		return jdbcTemplate.queryForObject("select * from users where id=?", new Object[] { id }, new UserRowMapper());
	}

	@Override
	public User create(final User user) {
		logger.debug("UserDaoImpl :: create method");
		final String sql = "insert into users(username) values(?)";

		KeyHolder holder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, user.getUsername());
				return ps;
			}
		}, holder);

		int newUserId = holder.getKey().intValue();
		user.setId(newUserId);
		return user;
	}

	@Override
	public User findByUsername(String username) throws EmptyResultDataAccessException {
		User user = null;
		try {
			logger.debug("UserDaoImpl :: findByUsername method");
			user = jdbcTemplate.queryForObject(FastRxQueryConstants.GET_USER_BY_USERNAME, new Object[] { username },
					new UserRowMapper());
		} catch (Exception e) {
			logger.info(e.getMessage());
		}

		return user;
	}

	@Override
	public long addUser(User userdetails, String owner) throws SQLException {
		logger.debug("UserDaoImpl :: addUser method");
		long userId = 0;
		KeyHolder holder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(FastRxQueryConstants.CREATE_USER,
						Statement.RETURN_GENERATED_KEYS);
				ps.setLong(1, userdetails.getRoleId());
				ps.setString(2, userdetails.getFirstName());
				ps.setString(3, userdetails.getLastName());
				ps.setString(4, userdetails.getUsername());
				ps.setString(5, userdetails.getPassword());
				ps.setString(6, userdetails.getPhoneNumber());
				ps.setString(7, userdetails.getActive());
				ps.setString(8, userdetails.getPharmacistLicenseNumber());
				ps.setString(9, owner);
				ps.setString(10, userdetails.getStateLicenseNumber());
				ps.setString(11, userdetails.getEmail());

				return ps;
			}
		}, holder);
		userId = holder.getKey().longValue();
		return userId;
	}

	@Override
	public void updateUserPassword(String password, long userId) throws DataAccessException {
		logger.debug("UserDaoImpl :: updateUserPassword method");
		jdbcTemplate.update(FastRxQueryConstants.UPDATE_PWD_FOR_USER, password, userId);
	}

	@Override
	public User findUserByIdAndPassword(long id, String password) throws EmptyResultDataAccessException {
		logger.debug("UserDaoImpl :: findUserByIdAndPassword method");
		return jdbcTemplate.queryForObject(FastRxQueryConstants.GET_USER_BY_ID_PWD, new Object[] { id, password },
				new UserRowMapper());
	}

	@Override
	public int checkIfUserExists(String username) throws DataAccessException {
		logger.debug("UserDaoImpl :: checkIfUserExists method");
		return jdbcTemplate.queryForObject(FastRxQueryConstants.COUNT_USER, new Object[] { username }, Integer.class);
	}

	@Override
	public void updateUser(User user) throws DataAccessException {
		logger.debug("UserDaoImpl :: updateUser method");
		jdbcTemplate.update(FastRxQueryConstants.UPDATE_USER,
				new Object[] { user.getFirstName(), user.getLastName(), user.getStateLicenseNumber(),
						user.getPhoneNumber(), user.getEmail(), user.getUsername(),
						java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()), user.getUsername() });
	}

	@Override
	public List<UserList> getAllLocationAdminsForCorporation(long coprorationId) throws DataAccessException {
		logger.debug("UserDaoImpl :: getAllLocationAdminsForCorporation method");
		return jdbcTemplate.query(FastRxQueryConstants.GET_ALL_LOCATION_ADMINS_BY_CORPORATIONID,
				new Object[] { coprorationId }, new UserListRowMapper());
	}

	@Override
	public List<UserList> getAllPharmacistsForCorporation(long coprorationId) throws DataAccessException {
		logger.debug("UserDaoImpl :: getAllPharmacistsForCorporation method");
		return jdbcTemplate.query(FastRxQueryConstants.GET_ALL_PHARMACISTS_BY_CORPORATIONID,
				new Object[] { coprorationId }, new UserListRowMapper());
	}

	@Override
	public List<UserList> getAllLocationAdmins() {
		logger.debug("UserDaoImpl :: getAllLocationAdmins method");
		return jdbcTemplate.query(FastRxQueryConstants.GET_ALL_LOCATION_ADMINS, new UserListRowMapper());
	}

	@Override
	public List<UserList> getAllPharmacists() {
		logger.debug("UserDaoImpl :: getAllPharmacists method");
		return jdbcTemplate.query(FastRxQueryConstants.GET_ALL_PHARMACISTS, new UserListRowMapper());
	}

	@Override
	public List<UserList> getAllPharmacyCorporationAdmins() {
		logger.debug("UserDaoImpl :: getAllPharmacyCorporationAdmins method");
		return jdbcTemplate.query(FastRxQueryConstants.GET_ALL_CORPORATION_ADMINS, new UserListRowMapper());
	}

	@Override
	public List<UserList> searchUserByCorporationId(String username, String phoneNo, String corpId, String roleId) {
		logger.debug("UserDaoImpl :: searchUserByCorporationId method");
		String name = "%" + username + "%";
		String phone = "%" + phoneNo + "%";
		List<UserList> userList = null;
		if (Long.parseLong(corpId) == 0) {
			if (!phoneNo.isEmpty() && !username.isEmpty()) {
				userList = jdbcTemplate.query(FastRxQueryConstants.SEARCH_USER_LIKE_USERNAME_AND_PHONE,
						new Object[] { name, phone, roleId }, new UserListRowMapper());
			} else if (!username.isEmpty()) {
				userList = jdbcTemplate.query(FastRxQueryConstants.SEARCH_USER_LIKE_USERNAME,
						new Object[] { name, roleId }, new UserListRowMapper());
			} else {
				userList = jdbcTemplate.query(FastRxQueryConstants.SEARCH_USER_LIKE_PHONE,
						new Object[] { phone, roleId }, new UserListRowMapper());
			}

		} else {
			if (!phoneNo.isEmpty() && !username.isEmpty()) {
				userList = jdbcTemplate.query(FastRxQueryConstants.SEARCH_USER_LIKE_USERNAME_AND_PHONE_CORPID,
						new Object[] { name, phone, roleId, corpId }, new UserListRowMapper());
			} else if (!username.isEmpty()) {
				userList = jdbcTemplate.query(FastRxQueryConstants.SEARCH_USER_LIKE_USERNAME_CORPID,
						new Object[] { name, roleId, corpId }, new UserListRowMapper());
			} else {
				userList = jdbcTemplate.query(FastRxQueryConstants.SEARCH_USER_LIKE_PHONE_CORPID,
						new Object[] { phone, roleId, corpId }, new UserListRowMapper());
			}

		}

		return userList;
	}

	@Override
	public void updateStatusOfUser(long userId, String updatedBy, String status) {
		logger.debug("UserDaoImpl :: updateStatusOfUser method");
		jdbcTemplate.update(FastRxQueryConstants.UPDATE_USER_STATUS,
				new Object[] { status, java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()), updatedBy, userId });

	}

	@Override
	public String getFaxnumberByToken(String token) throws DataAccessException {
		return jdbcTemplate.queryForObject(FastRxQueryConstants.GET_FAXNUMBER_BY_TOKEN, new Object[] { token },
				new RowMapper<String>() {
					public String mapRow(ResultSet rs, int rowNum) throws SQLException {
						return rs.getString("fax_num");
					}
				});

	}

	@Override
	public void changeEfaxStatus(String token) throws DataAccessException {
		String newEfaxSent = "Y";
		String oldEfaxSent = "N";
		String status = "Pending";
		String updatedBy = null;
		updatedBy = jdbcTemplate.queryForObject("SELECT username FROM users WHERE id=?", new Object[] { 1 },
				String.class);
		jdbcTemplate.queryForObject(FastRxQueryConstants.GET_PRESCRIPTION_ID_BY_TOKEN,
				new Object[] { token, status, oldEfaxSent }, Long.class);
		jdbcTemplate.update(FastRxQueryConstants.UPDATE_EFAX_STATUS_BY_TOKEN, new Object[] { newEfaxSent,
				java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()), updatedBy, token, status, oldEfaxSent });
		jdbcTemplate.queryForObject(FastRxQueryConstants.GET_EFAX_SENT_CHARGE_BY_PRESID, new Object[] { token },
				new BillingDetailsRowMapper());

	}

	@Override
	public List<String> getTokenListOfPendingPrescriptionsByLocationPin(String locationPin) throws DataAccessException {
		String status = "Pending";
		List<String> tokenList = null;
		tokenList = jdbcTemplate.query(FastRxQueryConstants.GET_PENDING_PRISCRIPTIONS_TOKEN_BY_LOCATION_PIN_AND_STATUS,
				new Object[] { locationPin, status }, new RowMapper<String>() {
					public String mapRow(ResultSet rs, int rowNum) throws SQLException {
						return rs.getString("req_token");
					}
				});
		return tokenList;
	}

	@Override
	public List<SecurityQuestion> getSecurityQuestionList() {
		logger.debug("UserDaoImpl :: getSecurityQuestionList method");
		return jdbcTemplate.query(FastRxQueryConstants.GET_SECURITY_QUESTION_LIST, new SecurityQuestionRowMapper());
	}

	@Override
	public void setSecurityQuestion(UserSecurityQuestion userQuestion) {
		logger.debug("UserDaoImpl :: setSecurityQuestion method");
		int count = jdbcTemplate.queryForObject(FastRxQueryConstants.GET_SECURITY_QUESTION_COUNT_BY_USERNAME,
				new Object[] { userQuestion.getCreatedBy() }, Integer.class);
		if (count == 0) {
			jdbcTemplate.update(FastRxQueryConstants.SET_SECURITY_QUESTION_FOR_USER, userQuestion.getUserId(),
					userQuestion.getQuestionId(), userQuestion.getAnswer(), userQuestion.getCreatedBy());
			jdbcTemplate.update(FastRxQueryConstants.UPDATE_FIRST_LOGIN,
					java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()), userQuestion.getUserId());

		} else if (count == 1) {
			jdbcTemplate.update(FastRxQueryConstants.UPDATE_SECURITY_QUESTION_FOR_USER, userQuestion.getQuestionId(),
					userQuestion.getAnswer(), userQuestion.getCreatedBy(),
					java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()), userQuestion.getUserId());
		} else {

		}

	}

	@Override
	public String getSecurityQuestionByUsername(String username) throws EmptyResultDataAccessException {
		return jdbcTemplate.queryForObject(FastRxQueryConstants.GET_SECURITY_QUESTION_BY_USERNAME,
				new Object[] { username }, String.class);
	}

	@Override
	public String validateSecurityQuestionByUsername(String username) throws EmptyResultDataAccessException {
		return jdbcTemplate.queryForObject(FastRxQueryConstants.GET_SECURITY_ANSWER_BY_USERNAME,
				new Object[] { username }, String.class);

	}

	@Override
	public void resetPassword(User user) throws DataAccessException {
		logger.debug("UserDaoImpl :: updateUserPassword method");
		jdbcTemplate.update(FastRxQueryConstants.UPDATE_PWD_FOR_USERNAME, user.getPassword(),
				user.getFastRxAudit().getUpdatedBy(), java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()),
				user.getUsername());

	}

	@Override
	public void addUserLog(UserLog userLog) throws DataAccessException {
		jdbcTemplate.update(FastRxQueryConstants.ADD_USER_LOG, userLog.getUsername(), userLog.getEvent(),
				userLog.getStatus().toString(), userLog.getRemarks());

	}

	public java.sql.Date convertStringToDate(String inputDate, Boolean isEndDAte) throws ParseException {
		Date convertedDate = null;
		java.sql.Date sqlStartDate = null;
		try {
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			convertedDate = (Date) formatter.parse(inputDate);
			if (isEndDAte) {
				Calendar c = Calendar.getInstance();
				c.setTime(convertedDate);
				c.add(Calendar.DATE, 1);
				convertedDate = (Date) formatter.parse(formatter.format(c.getTime()));
			}

			sqlStartDate = new java.sql.Date(convertedDate.getTime());

		} catch (Exception e) {
			logger.debug(e.getMessage());
		}

		return sqlStartDate;

	}

	@Override
	public List<UserLog> searchUserLogByUsernameAndDate(String username, String startDate, String endDate)
			throws DataAccessException, ParseException {
		String un = "%" + username + "%";
		java.sql.Date sDate = convertStringToDate(startDate, false);
		java.sql.Date eDate = convertStringToDate(endDate, true);
		List<UserLog> userLogList = null;

		if (!startDate.isEmpty() && !endDate.isEmpty()) {
			userLogList = jdbcTemplate.query(FastRxQueryConstants.SEARCH_USER_LOG_LIST_BY_USERNAME_AND_DATE,
					new Object[] { un, sDate, eDate }, new UserLogRowMapper());
		}
		return userLogList;
	}
}
