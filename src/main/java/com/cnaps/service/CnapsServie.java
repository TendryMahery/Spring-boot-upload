package com.cnaps.service;

import java.util.List;

import com.cnaps.model.Payment;

public interface CnapsServie {
	Payment Creer (Payment p);
	List <Payment> Lire ();
}
