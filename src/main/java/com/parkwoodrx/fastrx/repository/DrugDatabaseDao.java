package com.parkwoodrx.fastrx.repository;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.parkwoodrx.fastrx.model.Drugs;

public interface DrugDatabaseDao {
	
	public void uploadDrugDatabase(List<Drugs> drugList);
	public List<Drugs> searchDrug(String drugName) throws DataAccessException;

}
