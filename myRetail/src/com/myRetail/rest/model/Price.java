package com.myRetail.rest.model;

import java.io.Serializable;

import com.mongodb.BasicDBObject;

public class Price implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private double value;
	private String currency_code;


	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public String getCurrency_code() {
		return currency_code;
	}

	public void setCurrency_code(String currency_code) {
		this.currency_code = currency_code;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
