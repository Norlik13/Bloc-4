package com.example.bloc4.model;

public class Salarie {
	private int id;
	private String nom;
	private String prenom;
	private String telephoneFixe;
	private String telephonePortable;
	private String email;
	private Site site;
	private Department department;

	// Getters and setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public String getTelephoneFixe() {
		return telephoneFixe;
	}

	public void setTelephoneFixe(String telephoneFixe) {
		this.telephoneFixe = telephoneFixe;
	}

	public String getTelephonePortable() {
		return telephonePortable;
	}

	public void setTelephonePortable(String telephonePortable) {
		this.telephonePortable = telephonePortable;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getDepartmentID(Department department) {
		return department.getId();
	}

	public Department getDepartment() {
		return this.department;
	}

	public String getDepartmentName(Department department) {
		return department.getName();
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public Site getSite() {
		return this.site;
	}

	public String getSiteName(Site site) {
		return site.getName();
	}

	public int getSiteID(Site site) {
		return site.getId();
	}

	public void setSite(Site site) {
		this.site = site;
	}
}
