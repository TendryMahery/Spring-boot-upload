package com.cnaps.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cnaps.model.Payment;
import com.cnaps.repository.CnapsRepository;

@Service
public class CnapsServiceImpl implements CnapsServie {
	@Autowired
	private CnapsRepository cnapsRepository;
	
	@Override
	public Payment Creer(Payment p) {
		return cnapsRepository.save(p);
	}

	@Override
	public List<Payment> Lire() {
		return cnapsRepository.findAll();
	}

}
