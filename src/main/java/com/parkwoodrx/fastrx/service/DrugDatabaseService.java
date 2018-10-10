package com.parkwoodrx.fastrx.service;

import java.io.File;
import java.util.List;

import com.parkwoodrx.fastrx.exception.FastRxException;
import com.parkwoodrx.fastrx.model.Drugs;

public interface DrugDatabaseService {
	public void uploadDrugDatabase(File file,String loginUser);
	public List<Drugs> searchDrug(String drugName) throws FastRxException;

}
