package com.parkwoodrx.fastrx.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author PPaswan
 *
 */
public class UserLog implements Serializable {

	private static final long serialVersionUID = 1L;

	private long id;
	private String username;
	private Timestamp date;
	private String event;
	private String remarks;
	private Status status;

	public enum Status {
		SUCCESS, FAIL
	};

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

}
