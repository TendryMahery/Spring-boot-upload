package com.cnaps.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.cnaps.model.Payment;

@Repository
public interface CnapsRepository extends JpaRepository<Payment, String> {
	
	@Query("UPDATE Payment p SET p.statut = :statut, p.dateExecution = :dateExecution WHERE p.reference = :reference")
	@Modifying
	@Transactional
	void updatePaymentStatus(@Param("statut") String statut, @Param("dateExecution") String dateExecution, @Param("reference") String reference);

	@Query("SELECT p FROM Payment p WHERE p.reference = :reference")
    Payment findByReference(@Param("reference") String reference);
	
	@Query("SELECT p FROM Payment p WHERE p.type = :type AND p.dateExecution = :dateExecution")
	List<Payment> findByType(@Param("type") String type, @Param("dateExecution") String dateExecution);
	
	@Query("SELECT p FROM Payment p WHERE p.dateExecution = :dateExecution")
	List<Payment> findByDateExecution(@Param("dateExecution") String dateExecution);
	
	@Query("SELECT COUNT(p) FROM Payment p WHERE p.statut = 'SUCCESS' AND p.dateExecution = :dateExecution")
	Long countByStatut(@Param("dateExecution") String dateExecution);
	
	@Query("SELECT COUNT(p) FROM Payment p WHERE p.statut = 'INITIATED' AND p.dateExecution = :dateExecution")
	Long countByStatutInitated(@Param("dateExecution") String dateExecution);
	
}
