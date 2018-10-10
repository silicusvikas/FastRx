package com.parkwoodrx.fastrx.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.parkwoodrx.fastrx.model.MultiSelectDropDownObject;

public class MultiSelectDropDownObjectRowMapper implements RowMapper<MultiSelectDropDownObject>{

	@Override
	public MultiSelectDropDownObject mapRow(ResultSet result, int rowNum) throws SQLException {
		MultiSelectDropDownObject multiSelectObj= new MultiSelectDropDownObject();
		multiSelectObj.setId(result.getLong("id"));
		multiSelectObj.setItemName(result.getString("itemName"));
		return multiSelectObj;
	}

}
