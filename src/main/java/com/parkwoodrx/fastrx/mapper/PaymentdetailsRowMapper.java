package com.parkwoodrx.fastrx.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.parkwoodrx.fastrx.model.PaymentProfileDetails;

public class PaymentdetailsRowMapper implements RowMapper<PaymentProfileDetails>  {

	@Override
	public PaymentProfileDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
		PaymentProfileDetails paymentDetails=new PaymentProfileDetails();
		paymentDetails.setCardHolderName(rs.getString("card_holder_name"));
		paymentDetails.setCardNumber(rs.getString("card_last_4digit"));
		return paymentDetails;
	}

}
