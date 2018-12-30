package com.rhb.claw.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class RecordEntity {
	private String code;
	private String name;
	private LocalDate date;
	private BigDecimal price;
	private Map<Integer,BigDecimal> avPrice = new HashMap<Integer,BigDecimal>();
	

	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Map<Integer, BigDecimal> getAvPrice() {
		return avPrice;
	}
	public void setAvPrice(Map<Integer, BigDecimal> avPrice) {
		this.avPrice = avPrice;
	}
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	
	public void setAvPrice(Integer av, BigDecimal price) {
		this.avPrice.put(av, price);
	}
	
	public BigDecimal getAvPrice(Integer av) {
		return this.avPrice.get(av);
	}
	@Override
	public String toString() {
		return "RecordEntity [code=" + code + ", name=" + name + ", date=" + date + ", price=" + price + ", avPrice="
				+ avPrice + "]";
	}
	

}
