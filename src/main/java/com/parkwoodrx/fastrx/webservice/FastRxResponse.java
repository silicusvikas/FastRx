package com.parkwoodrx.fastrx.webservice;

public class FastRxResponse {
   
	
	private String message;
    private Status status;
    private Object data;
    
    
    public enum Status {
        SUCCESS, ERROR
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
    
    
    
}
