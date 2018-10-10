package com.parkwoodrx.fastrx.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.parkwoodrx.fastrx.model.SecurityQuestion;

public class SecurityQuestionRowMapper implements RowMapper<SecurityQuestion> {

	@Override
	public SecurityQuestion mapRow(ResultSet rs, int rowNum) throws SQLException {
		SecurityQuestion question=new SecurityQuestion();
		question.setId(rs.getLong("id"));
		question.setQuestion(rs.getString("question"));
		return question;
	}

}
