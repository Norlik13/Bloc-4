package com.example.bloc4.model;

public class Site {
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
		Site site = (Site) obj;
		return name != null ? name.equals(site.name) : site.name == null;
	}

//	@Override
//	public int hashCode() {
//		return name != null ? name.hashCode() : 0;
//	}
}