package com.cnaps.model;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="SortPaymentsBfvCnaps")
public class SortPaymentProvider {
	@Id
	private String ReferenceVirement;
	private String ReferenceProvider;
	private String Date;
	private BigInteger Montant;
	private String Statut;
	private String ReferenceInterne;
	private String Libelle;
	private String Solde;
	
	public String getReferenceVirement() {
		return ReferenceVirement;
	}
	public void setReferenceVirement(String referenceVirement) {
		this.ReferenceVirement = referenceVirement;
	}
	public String getReferenceProvider() {
		return ReferenceProvider;
	}
	public void setReferenceProvider(String referenceProvider) {
		this.ReferenceProvider = referenceProvider;
	}
	public String getDate() {
		return Date;
	}
	public void setDate() {
		LocalDateTime currentDateTime = LocalDateTime.now();
		LocalDateTime newDateTime = currentDateTime.plusHours(3);
		String formattedDateTime = newDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		String generatedReference = formattedDateTime;
		this.Date = generatedReference ;
	}
	public BigInteger getMontant() {
		return Montant;
	}
	public void setMontant(BigInteger montant) {
		Montant = montant;
	}
	public String getStatut() {
		return Statut;
	}
	public void setStatut(String statut) {
		this.Statut = statut;
	}
	
	public SortPaymentProvider() {
		
	}
	
	public SortPaymentProvider(String ReferenceVirement, String ReferenceProvider, String Date, BigInteger Montant, String Statut, String ReferenceInterne, String Libelle, String Solde) {
		this.ReferenceVirement=ReferenceVirement;
		this.ReferenceProvider=ReferenceProvider;
		this.Date=Date;
		this.Montant=Montant;
		this.Statut=Statut;
		this.ReferenceInterne=ReferenceInterne;
		this.Libelle=Libelle;
		this.Solde=Solde;
	}
	
	@JsonIgnore
	public String getReferenceInterne() {
		return ReferenceInterne;
	}
	public void setReferenceInterne(String referenceInterne) {
		this.ReferenceInterne = referenceInterne;
	}
	
	@JsonIgnore
	public String getLibelle() {
		return Libelle;
	}
	public void setLibelle(String libelle) {
		this.Libelle = libelle;
	}
	
	@JsonIgnore
	public String getSolde() {
		return Solde;
	}
	public void setSolde(String solde) {
		this.Solde = solde;
	}
}
