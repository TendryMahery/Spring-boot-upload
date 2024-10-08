package com.cnaps.model;

import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="PaymentsCnaps")
public class Payment {
	@Id
	private String reference;
	private String idClient;
	private String nomBeneficiaire;
	@Column(name = "numeroCompteBeneficiaire", length = 27, nullable = false)
	private String numeroCompteBeneficiaire;
	@Column(name = "numeroCompteAdebiter", length = 27, nullable = false)
	private String numeroCompteAdebiter;
	private BigInteger montant;
	private String devise;
	private String dateExecution;
	private String type;
	private String statut;
	
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}
	public String getNomBeneficiaire() {
		return nomBeneficiaire;
	}
	public void setNomBeneficiaire(String nomBeneficiaire) {
		this.nomBeneficiaire = nomBeneficiaire;
	}
	public String getNumeroCompteBeneficiaire() {
		return numeroCompteBeneficiaire;
	}
	public void setNumeroCompteBeneficiaire(String numeroCompteBeneficiaire) {
		if (numeroCompteBeneficiaire != null && numeroCompteBeneficiaire.length() == 27) {
			this.numeroCompteBeneficiaire = numeroCompteBeneficiaire;
		} else {
			throw new IllegalArgumentException("Le numéro de compte bénéficiaire doit être composé de 27 caractères.");
		}
	}
	public BigInteger getMontant() {
		return montant;
	}
	public void setMontant(BigInteger montant) {
		this.montant = montant;
	}
	public String getDevise() {
		return devise;
	}
	public void setDevise(String devise) {
		this.devise = devise;
	}
	public String getDateExecution() {
		return dateExecution;
	}
	public void setDateExecution(String dateExecution) {
		this.dateExecution = dateExecution;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public Payment () {
		
	}
	
	public Payment (String reference, String idClient, String nomBeneficiaire, String numeroCompteBeneficiaire, String numeroCompteAdebiter, BigInteger montant, String devise, String dateExecution, String type, String statut) {
		this.reference = reference;
		this.idClient = idClient;
		this.nomBeneficiaire = nomBeneficiaire;
		this.numeroCompteBeneficiaire = numeroCompteBeneficiaire;
		this.numeroCompteAdebiter = numeroCompteAdebiter;
		this.montant = montant;
		this.devise = devise;
		this.dateExecution = dateExecution;
		this.type = type;
		this.statut = statut;
	}
	public String getStatut() {
		return statut;
	}
	public void setStatut(String statut) {
		this.statut = statut;
	}
	public String getidClient() {
		return idClient;
	}
	public void setidClient(String idClient) {
		this.idClient = idClient;
	}
	public String getNumeroCompteAdebiter() {
		return numeroCompteAdebiter;
	}
	public void setNumeroCompteAdebiter(String numeroCompteAdebiter) {
		if (numeroCompteAdebiter != null && numeroCompteAdebiter.length() == 27) {
			this.numeroCompteAdebiter = numeroCompteAdebiter;
		} else {
			throw new IllegalArgumentException("Le numéro de compte à débiter doit être composé de 27 caractères.");
		}
	}
}
