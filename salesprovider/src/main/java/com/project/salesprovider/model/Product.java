package com.project.salesprovider.model;

import javax.validation.constraints.NotNull;

public class Product {

	// Identificacao unica no Google Cloud Datastore
	public long id;

	// Id da entidade
	private String objId;

	@NotNull
	public String name;

	@NotNull
	public String description;

	@NotNull
	public String code;

	@NotNull
	public double price;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public String getObjId() {
		return objId;
	}

	public void setObjId(String objId) {
		this.objId = objId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

}
