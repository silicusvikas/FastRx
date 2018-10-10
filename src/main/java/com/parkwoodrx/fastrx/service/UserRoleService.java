package com.parkwoodrx.fastrx.service;

import java.util.List;

import com.parkwoodrx.fastrx.model.UserRole;

public interface UserRoleService {
	public UserRole getAdminUserRoleById(Long id);
	public UserRole getUserRoleById(Long id);
	public List<UserRole> getAllUserRole();
	public List<UserRole> getAllUserRoleForSuperAdmin();
}
