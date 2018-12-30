package com.rhb.claw.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Trade {
	private BigDecimal amount = new BigDecimal(50000);
	private String code;
	private String name;
	private Integer quantity;
	private LocalDate buyDate;
	private BigDecimal buyCost;
	private LocalDate sellDate;
	private BigDecimal sellPrice;
	
	private String buynote;
	private String sellnote;
	
	public Trade(String code,String name,Integer buyorsell,LocalDate date, BigDecimal price, String note) {
		this.code = code;
		this.name = name;
		
		if(buyorsell==1) {
			buy(date,price,note);
		}else {
			sell(date,price,note);
		}
	}
	
	
	
	public BigDecimal getAmount() {
		return amount;
	}



	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}



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



	public void buy(LocalDate date, BigDecimal price, String note) {
		this.buyDate = date;
		this.buyCost = price;
		this.buynote = note;
		this.quantity = amount.divide(price,2,BigDecimal.ROUND_HALF_UP).divide(new BigDecimal(100),2,BigDecimal.ROUND_HALF_UP).intValue() * 100;
	}
	
	public void sell(LocalDate date, BigDecimal price, String note) {
		this.sellDate = date;
		this.sellPrice = price;
		this.sellnote = note;
	}
	
	public String getBuynote() {
		return buynote;
	}
	public void setBuynote(String buynote) {
		this.buynote = buynote;
	}
	public String getSellnote() {
		return sellnote;
	}
	public void setSellnote(String sellnote) {
		this.sellnote = sellnote;
	}
	
	public Integer getProfitRate(BigDecimal price) {
		Integer rate = price.subtract(buyCost).divide(buyCost,2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).intValue();
		return rate;
	}
	
	public Integer getProfitRate() {
		Integer rate = sellPrice.subtract(buyCost).divide(buyCost,2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).intValue();
		return rate;
	}
	
	public Integer getProfit() {
		return sellPrice.subtract(buyCost).multiply(new BigDecimal(quantity)).intValue();
	}

	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public LocalDate getBuyDate() {
		return buyDate;
	}
	public void setBuyDate(LocalDate buyDate) {
		this.buyDate = buyDate;
	}
	public BigDecimal getBuyCost() {
		return buyCost;
	}
	public void setBuyCost(BigDecimal buyCost) {
		this.buyCost = buyCost;
	}
	public LocalDate getSellDate() {
		return sellDate;
	}
	public void setSellDate(LocalDate sellDate) {
		this.sellDate = sellDate;
	}
	public BigDecimal getSellPrice() {
		return sellPrice;
	}
	public void setSellPrice(BigDecimal sellPrice) {
		this.sellPrice = sellPrice;
	}

	@Override
	public String toString() {
		return "Trade [profit=" + getProfit() + ", quantity=" + quantity + ", buyDate=" + buyDate + ", buyCost=" + buyCost
				+ ", sellDate=" + sellDate + ", sellPrice=" + sellPrice + ", buynote=" + buynote + ", sellnote="
				+ sellnote + "]";
	}

	
	
}
