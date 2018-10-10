package com.parkwoodrx.fastrx.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.parkwoodrx.fastrx.model.UserLog;
import com.parkwoodrx.fastrx.model.UserLog.Status;

public class UserLogRowMapper implements RowMapper<UserLog> {

	@Override
	public UserLog mapRow(ResultSet rs, int rowNum) throws SQLException {
		UserLog userLog=new UserLog();
		userLog.setId(rs.getLong("id"));
		userLog.setDate(rs.getTimestamp("date"));
		userLog.setUsername(rs.getString("username"));
		userLog.setEvent(rs.getString("event"));
		userLog.setRemarks(rs.getString("remarks"));
		userLog.setStatus(Status.valueOf(rs.getString("status")));
		return userLog;
	}

}
