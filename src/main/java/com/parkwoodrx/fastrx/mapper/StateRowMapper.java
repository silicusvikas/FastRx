package com.parkwoodrx.fastrx.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.parkwoodrx.fastrx.model.State;

public class StateRowMapper implements RowMapper<State> {

	@Override
	public State mapRow(ResultSet rs, int rowNum) throws SQLException {
		State state=new State();
		state.setId(rs.getLong("id"));
		state.setStateCode(rs.getString("state_code"));
		state.setStateName(rs.getString("state_name"));
		return state;
	}

}
