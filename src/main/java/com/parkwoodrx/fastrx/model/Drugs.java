package com.parkwoodrx.fastrx.model;

import java.io.Serializable;

/**
 * @author PPaswan
 *
 */

public class Drugs implements Serializable {

	private long id;
	private static final long serialVersionUID = 1L;
	private String productId;
	private String proprietaryName;
	private String proprietaryNameSuffix;
	private String nonProprietaryName;
	private String dosageFormName;
	private String activeNumeratorStrength;
	private String activeIngredUnit;
	private String deaSchedule;
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getProprietaryName() {
		return proprietaryName;
	}
	public void setProprietaryName(String proprietaryName) {
		this.proprietaryName = proprietaryName;
	}
	public String getProprietaryNameSuffix() {
		return proprietaryNameSuffix;
	}
	public void setProprietaryNameSuffix(String proprietaryNameSuffix) {
		this.proprietaryNameSuffix = proprietaryNameSuffix;
	}
	public String getNonProprietaryName() {
		return nonProprietaryName;
	}
	public void setNonProprietaryName(String nonProprietaryName) {
		this.nonProprietaryName = nonProprietaryName;
	}
	public String getDosageFormName() {
		return dosageFormName;
	}
	public void setDosageFormName(String dosageFormName) {
		this.dosageFormName = dosageFormName;
	}
	public String getActiveNumeratorStrength() {
		return activeNumeratorStrength;
	}
	public void setActiveNumeratorStrength(String activeNumeratorStrength) {
		this.activeNumeratorStrength = activeNumeratorStrength;
	}
	public String getActiveIngredUnit() {
		return activeIngredUnit;
	}
	public void setActiveIngredUnit(String activeIngredUnit) {
		this.activeIngredUnit = activeIngredUnit;
	}
	public String getDeaSchedule() {
		return deaSchedule;
	}
	public void setDeaSchedule(String deaSchedule) {
		this.deaSchedule = deaSchedule;
	}
	
	
	
	

}
