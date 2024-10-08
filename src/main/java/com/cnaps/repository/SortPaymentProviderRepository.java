package com.cnaps.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cnaps.model.SortPaymentProvider;

@Repository
public interface SortPaymentProviderRepository extends JpaRepository<SortPaymentProvider, String> {

}
