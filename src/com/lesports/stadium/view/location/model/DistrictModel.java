package com.lesports.stadium.view.location.model;

/**
 * 地区model
 * @author Administrator
 *
 */
public class DistrictModel {
	private String name;
	private String zipcode;
	private String id;
	
	public DistrictModel() {
		super();
	}

	public DistrictModel(String name, String zipcode) {
		super();
		this.name = name;
		this.zipcode = zipcode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
	
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


	@Override
	public String toString() {
		return "DistrictModel [name=" + name + ", zipcode=" + zipcode + "]";
	}

}
