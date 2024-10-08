package com.cnaps.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cnaps.model.SortPaymentProvider;
import com.cnaps.repository.SortPaymentProviderRepository;

@Service
public class ServiceImplSortPaymentProvider implements ServiceSortPaymentProvider {
	@Autowired
	SortPaymentProviderRepository sortPaymentProviderRepository;
	
	@Override
	public SortPaymentProvider nouvellevirement(SortPaymentProvider sortPayment) {
		// TODO Auto-generated method stub
		return sortPaymentProviderRepository.save(sortPayment);
	}

}
