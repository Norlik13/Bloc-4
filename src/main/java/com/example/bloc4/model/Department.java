package com.example.bloc4.model;

public class Department {
	private int id;
	private String name;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		Department department = (Department) obj;
		return name != null ? name.equals(department.name) : department.name == null;
	}

//	@Override
//	public int hashCode() {
//		return name != null ? name.hashCode() : 0;
//	}
}
