package com.parkwoodrx.fastrx.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.parkwoodrx.fastrx.model.PaymentType;


public class PaymentTypeRowMapper implements RowMapper<PaymentType>{

	@Override
	public PaymentType mapRow(ResultSet rs, int rowNum) throws SQLException {
		PaymentType paymentType=new PaymentType();
		paymentType.setId(rs.getLong("id"));
		paymentType.setPaymentType(rs.getString("payment_type"));
		return paymentType;
	}

}
