package com.parkwoodrx.fastrx.repository;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.parkwoodrx.fastrx.constants.FastRxQueryConstants;
import com.parkwoodrx.fastrx.mapper.UserRoleRowMapper;
import com.parkwoodrx.fastrx.model.UserRole;

@Repository
public class UserRoleDaoImpl implements UserRoleDao {

	private static final Logger logger = LoggerFactory.getLogger(UserRoleDao.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	@Override
	public UserRole getAdminUserRoleById(Long id) throws EmptyResultDataAccessException {
		logger.debug("UserRoleDaoImpl :: getAdminUserRoleById method");
		UserRole userRole = jdbcTemplate.queryForObject(FastRxQueryConstants.GET_ADMIN_USER_ROLE_BY_ID,
				new Object[] { id }, new UserRoleRowMapper());
		return userRole;
	}

	@Override
	public UserRole getUserRoleById(Long id) throws EmptyResultDataAccessException {
		logger.debug("UserRoleDaoImpl :: getUserRoleById method");
		UserRole userRole = jdbcTemplate.queryForObject(FastRxQueryConstants.GET_USER_ROLE_BY_ID, new Object[] { id },
				new UserRoleRowMapper());
		return userRole;
	}

	@Override
	public List<UserRole> getAllUserRoles() throws DataAccessException {
		logger.debug("UserRoleDaoImpl :: getAllUserRoles method");
		List<UserRole> userRoleList = jdbcTemplate.query(FastRxQueryConstants.GET_ALL_USER_ROLE,
				new UserRoleRowMapper());
		return userRoleList;
	}

	@Override
	public List<UserRole> getAllUserRolesForSuperAdmin() throws DataAccessException {
		logger.debug("UserRoleDaoImpl :: getAllUserRolesForSuperAdmin method");
		List<UserRole> userRoleList = jdbcTemplate.query(FastRxQueryConstants.GET_ALL_USER_ROLE_FOR_SUPER_ADMIN,
				new UserRoleRowMapper());
		return userRoleList;
	}

}
