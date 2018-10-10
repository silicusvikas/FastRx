package com.parkwoodrx.fastrx.repository;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;

import com.parkwoodrx.fastrx.model.UserRole;

public interface UserRoleDao {
	public UserRole getAdminUserRoleById(Long id) throws EmptyResultDataAccessException;
	public UserRole getUserRoleById(Long id) throws EmptyResultDataAccessException;
	public List<UserRole> getAllUserRoles() throws DataAccessException;
	public List<UserRole> getAllUserRolesForSuperAdmin() throws DataAccessException;
}
