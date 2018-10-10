package com.parkwoodrx.fastrx.model;

import java.io.Serializable;

import org.omg.CORBA.PRIVATE_MEMBER;

public class NonRegPharmacy implements Serializable {
	
	private long id;
	private static final long serialVersionUID = 1L;
	
	private String pharmacy_name;
	private String	store_address;
	private String	phy_city;
	private String phy_state;
	private String phy_zip;	
	private String phy_phone;	
	private String phy_ext;
	private String phy_efax;	
	private String npi;
	private String dea;
	private String store_number;
	
	public String getStore_number() {
		return store_number;
	}
	public void setStore_number(String store_number) {
		this.store_number = store_number;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getPharmacy_name() {
		return pharmacy_name;
	}
	public void setPharmacy_name(String pharmacy_name) {
		this.pharmacy_name = pharmacy_name;
	}
	public String getStore_address() {
		return store_address;
	}
	public void setStore_address(String store_address) {
		this.store_address = store_address;
	}
	public String getPhy_city() {
		return phy_city;
	}
	public void setPhy_city(String phy_city) {
		this.phy_city = phy_city;
	}
	public String getPhy_state() {
		return phy_state;
	}
	public void setPhy_state(String phy_state) {
		this.phy_state = phy_state;
	}
	public String getPhy_zip() {
		return phy_zip;
	}
	public void setPhy_zip(String phy_zip) {
		this.phy_zip = phy_zip;
	}
	public String getPhy_phone() {
		return phy_phone;
	}
	public void setPhy_phone(String phy_phone) {
		this.phy_phone = phy_phone;
	}
	public String getPhy_ext() {
		return phy_ext;
	}
	public void setPhy_ext(String phy_ext) {
		this.phy_ext = phy_ext;
	}
	public String getPhy_efax() {
		return phy_efax;
	}
	public void setPhy_efax(String phy_efax) {
		this.phy_efax = phy_efax;
	}
	
	public String getNpi() {
		return npi;
	}
	public void setNpi(String npi) {
		this.npi = npi;
	}
	public String getDea() {
		return dea;
	}
	public void setDea(String dea) {
		this.dea = dea;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	
	
}