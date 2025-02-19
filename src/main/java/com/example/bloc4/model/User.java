package com.example.bloc4.model;

public class User {
	public enum Role {
		INVITED,
		ADMIN
	}

	private Role role;

	public User(Role role) {
		this.role = role;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
}