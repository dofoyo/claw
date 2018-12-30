package com.rhb.claw.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Record {
	private String code;
	private String name;
	private LocalDate date;
	private BigDecimal price;
	private BigDecimal midPrice;  
	private Map<Integer,BigDecimal> avPrice = new HashMap<Integer,BigDecimal>();
	private Map<Integer,BigDecimal> slope = new HashMap<Integer,BigDecimal>();
	private Map<Integer,Integer> aboveAvDays = new HashMap<Integer,Integer>();
	private Map<Integer,Integer> belowAvDays = new HashMap<Integer,Integer>();
	
	
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

	public void setAvPrice(Integer av, BigDecimal price) {
		this.avPrice.put(av, price);
	}
	
	public BigDecimal getAvPrice(Integer av) {
		return this.avPrice.get(av);
	}

	public void setSlope(Integer av, BigDecimal slope) {
		this.slope.put(av, slope);
	}
	
	public BigDecimal getSlope(Integer av) {
		return this.slope.get(av);
	}

	public void setAboveAvDays(Integer av, Integer days) {
		this.aboveAvDays.put(av, days);
	}
	
	public Integer getAboveAvDays(Integer av) {
		return this.aboveAvDays.get(av);
	}

	public void setBelowAvDays(Integer av, Integer days) {
		this.belowAvDays.put(av, days);
	}
	
	public Integer getBelowAvDays(Integer av) {
		return this.belowAvDays.get(av);
	}
	
	public BigDecimal getMidPrice() {
		return midPrice;
	}
	public void setMidPrice(BigDecimal midPrice) {
		this.midPrice = midPrice;
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
	
	
	public Integer getBiasOfAv(Integer av){
		BigDecimal i = new BigDecimal(0);
		BigDecimal avPrice = this.getAvPrice(av);
		if(price!=null && avPrice!=null && !avPrice.equals(BigDecimal.ZERO)){
			i = price.subtract(avPrice).divide(avPrice,2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
		}
		int bias = i.intValue();
		
		return bias;
	}

		
	public Integer getBiasOfMidPrice(){
		BigDecimal i = new BigDecimal(0);
		if(price!=null && midPrice!=null && !midPrice.equals(BigDecimal.ZERO)){
			//i = ((price.subtract(midPrice)).divide(midPrice,2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)));
			i = ((price.subtract(midPrice)).divide(midPrice,2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).abs());
		}
		int bias = i.intValue()/2;  //经测试，取中位数的半值结果最好

		return bias;
	}
	
	
	public boolean isPriceAboveAv(Integer av) {
		BigDecimal avPrice = this.getAvPrice(av);
		return (price!=null && avPrice!=null && price.compareTo(avPrice)==1) ? true : false;

	}
	
	public boolean isAbove(Integer av1, Integer av2) {
		BigDecimal avPrice1 = this.getAvPrice(av1);
		BigDecimal avPrice2 = this.getAvPrice(av2);

		return (avPrice2!=null && avPrice1!=null && avPrice1.compareTo(avPrice2)==1) ? true : false;
	}
	
	public Integer isAboveRate(Integer av1, Integer av2) {
		BigDecimal avPrice1 = this.getAvPrice(av1);
		BigDecimal avPrice2 = this.getAvPrice(av2);

		return getBias(avPrice1, avPrice2);
	}
	
	public Integer getBias(BigDecimal b1, BigDecimal b2) {
		Integer i = b1.subtract(b2).divide(b2,2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).intValue();
		return i;
	}
	
	public Integer getBiasOf250And120() {
		return this.getBias(this.getAvPrice(250), this.getAvPrice(120));
	}

	@Override
	public String toString() {
		return "Record [date=" + date + ", price=" + price + ", midPrice=" + midPrice + ", avPrice=" + avPrice
				+ ", slope=" + slope + ", aboveAvDays=" + aboveAvDays + ", belowAvDays=" + belowAvDays + "]";
	}

	
	
}
