package com.parkwoodrx.fastrx.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.parkwoodrx.fastrx.constants.FastRxErrorCodes;
import com.parkwoodrx.fastrx.exception.FastRxException;
import com.parkwoodrx.fastrx.model.UserRole;
import com.parkwoodrx.fastrx.repository.UserRoleDao;

@Service
public class UserRoleServiceImpl implements UserRoleService {
	private static final Logger logger = LoggerFactory.getLogger(UserRoleService.class);

	@Autowired
	private UserRoleDao userRoleRepo;

	@Override
	public UserRole getAdminUserRoleById(Long id) {
		logger.debug("UserRoleServiceImpl :: getAdminUserRoleById method");
		UserRole userRole = null;
		try {
			userRole = userRoleRepo.getAdminUserRoleById(id);
			return userRole;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			throw new FastRxException(FastRxErrorCodes.USER_NOT_ADMIN_CODE, FastRxErrorCodes.USER_NOT_ADMIN_MSG);
		}
	}

	@Override
	public UserRole getUserRoleById(Long id) {
		logger.debug("UserRoleServiceImpl :: getUserRoleById method");
		UserRole userRole = null;
		try {
			userRole = userRoleRepo.getUserRoleById(id);
			return userRole;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			throw new FastRxException(FastRxErrorCodes.NOT_FOUND_CODE,
					FastRxErrorCodes.USER_ROLE_MAPPING_NOT_FOUND_MSG);
		}
	}

	@Override
	public List<UserRole> getAllUserRole() {
		logger.debug("UserRoleServiceImpl :: getAllUserRole method");
		List<UserRole> userRoleList = null;
		try {
			userRoleList = userRoleRepo.getAllUserRoles();
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new FastRxException(FastRxErrorCodes.NOT_FOUND_CODE, FastRxErrorCodes.NO_ROLES_FOUND_MSG);
		}
		return userRoleList;
	}

	@Override
	public List<UserRole> getAllUserRoleForSuperAdmin() {
		logger.debug("UserRoleServiceImpl :: getAllUserRoleForSuperAdmin method");
		List<UserRole> userRoleList = null;
		try {
			userRoleList = userRoleRepo.getAllUserRolesForSuperAdmin();
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new FastRxException(FastRxErrorCodes.NOT_FOUND_CODE, FastRxErrorCodes.NO_ROLES_FOUND_MSG);
		}

		return userRoleList;
	}

}
