package com.rhb.claw.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Stock {
	protected static final Logger logger = LoggerFactory.getLogger(Stock.class);
	private String code;
	private String name;
	private List<Record> records = new ArrayList<Record>();
	private List<Trade> trades = new ArrayList<Trade>();

	//Integer[] slopePeriod = {3,6,18};  // 164218.00
	//Integer[] slopePeriod = {3,10,30};  // 164218.00
	Integer[] slopePeriod = {3};  // 148212.00

	//Integer[] slopePeriod = {5,20,100};  //117836.00
	//Integer[] slopePeriod = {5,10,30};  // 108229.00
	

	//Integer[] slopePeriod = {10,20,60};  // 113148

	
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

	public void addRecord(Record record) {
		this.records.add(record);
	}
	
	public void refresh() {
		if(records.size()>0) {
			int period = 0;
			Record record;
			for(int i=1; i<records.size(); i++) {
				record = records.get(i);
				
				period = slopePeriod[0];
				record.setSlope(period,this.calSlope(records.subList(i>period ? i-period : 0, i),5,period));
//
//				period = slopePeriod[1];
//				record.setSlope(period,this.calSlope(records.subList(i>period ? i-period : 0, i),250,period));
//
//				period = slopePeriod[2];
//				record.setSlope(period,this.calSlope(records.subList(i>period ? i-period : 0, i),250,period));
				
				//period = 50;
				//record.setAboveAvDays(60,this.calAboveAvDays(records.subList(i>period ? i-period : 0, i),60));
				//record.setAboveAvDays(120,this.calAboveAvDays(records.subList(i>period ? i-period : 0, i),120));
				//record.setAboveAvDays(250,this.calAboveAvDays(records.subList(i>period ? i-period : 0, i),250));

				//period = 7; //近7个交易日,计算有多少个交易日是低于250日均线的
				//record.setBelowAvDays(250,calBelowAvDays(records.subList(i>period ? i-period : 0, i),250));
				
				period = 750; //近三年的中值
				record.setMidPrice(calMidPrice(records.subList(i>period ? i-period : 0, i),record.getPrice()));
				
				
//				logger.info(record.toString());  //**************************
			}
		}
	}
	
	
	/*
	 * 长期下跌后，突然启动
	 * 买入：
	 * 5日线快速上穿60、120、250线后，回调到250线附近，
	 * 
	 * 
	 * 
	 * 卖出：
	 * 买入后上涨超过20%，止盈
	 * 买入后下跌超过20%，止损
	 * 
	 */
	public List<Trade> fish(LocalDate beginDate) {
		//List<Trade> list = new ArrayList<Trade>();

		Integer yellow = 0;  
		Integer red = 0;
		//Integer green = 1;
		Record previous;
		Record record;
		String[] note = new String[4];
		String str = null;
		BigDecimal[] av5  = new BigDecimal[2];
		if(records.size()>0) {
			previous = records.get(0);
			for(int i=1; i<records.size(); i++) {
				record = records.get(i);
				if(record.getAvPrice(250) != null && record.getDate().isAfter(beginDate)) {
					if(yellow==0 
						&& record.isAboveRate(250,120)>3 
						&& record.isAboveRate(120,60)>3 
						&& previous.isAbove(60,5) && record.isAbove(5,60)
						&& record.getPrice().compareTo(record.getMidPrice())!=1
						) {
						yellow ++;
						note[0] = record.getDate().toString() + "，5上穿60";
//						System.out.println(note[0]);
					}
					
					if(yellow == 1 
						&& previous.isAbove(120,5) && record.isAbove(5,120) 
						) {
						yellow ++;
						note[1] =  record.getDate().toString() + "，5上穿120";
//						System.out.println(note[1]);
					}
					
					if(yellow == 2 
						&& previous.isAbove(250,5) && record.isAbove(5,250) 
						) {
						yellow ++;
						note[2] =  record.getDate().toString() + "，5上穿250";
//						System.out.println(note[2]);
					}

					if(yellow == 3 
						&& record.getSlope(slopePeriod[0]).compareTo(BigDecimal.ZERO) == -1  //5日线掉头
						) {
						av5[0] = record.getAvPrice(250);
						av5[1] = record.getAvPrice(5);
						if(record.getBias(av5[1], av5[0])>10) {
							yellow ++;
							note[3] =  record.getDate().toString() + "，5日较250高"+record.getBias(av5[1], av5[0])+"，超过10%";
//							System.out.println(note[3]);
						}else {
							//失败
//							System.out.println(record.getDate().toString() + "涨幅"+record.getBias(av5[1], av5[0])+"，失败");
							yellow = 0;
							note = new String[4];
							av5 = new BigDecimal[2];
						}
					}

					if(yellow > 0 
						&& (previous.isAbove(5,60) && record.isAbove(60,5) 
								|| previous.isAbove(5,120) && record.isAbove(120,5)
								|| previous.isAbove(5,250) && record.isAbove(250,5))
						) {
						//失败
						yellow = 0;
						note = new String[4];
						av5 = new BigDecimal[2];
//						System.out.println(record.getDate().toString() + "5下穿长期线，失败");

					}

					if(yellow==4 
							&& record.getBiasOfAv(250)<3 
							&& !this.onHand()) {
						str = note[0] + "；" + note[1] + "；" + note[2] + "；" + note[3] + "； " + record.getDate().toString() + "，买入。";
						this.buy(record.getCode(),record.getName(),record.getDate(),record.getPrice(),str);
						yellow = 0;
						note = new String[4];
						av5 = new BigDecimal[2];
					}	
					
					
					if(this.onHand()  && this.getProfitRate(record.getPrice())>20) {
						str = record.getDate().toString() + "上涨" + this.getProfitRate(record.getPrice()) + "%，超过20%，止盈。";
						this.sell(record.getDate(),record.getPrice(),str);
					}

					
					if(this.onHand() && this.getProfitRate(record.getPrice())<-20) {
						str = record.getDate().toString() + "下跌" + this.getProfitRate(record.getPrice()) + "%，超过20%，止损。";
						this.sell(record.getDate(),record.getPrice(),str);
					}
		
				}
				
				previous = record;
			}
		}
		
		
		this.getProfit();
		
		return this.trades;
	}
	
	private void buy(String code, String name, LocalDate date, BigDecimal price, String note) {
		trades.add(new Trade(code,name,1,date,price,note));
	}
	
	private void sell(LocalDate date, BigDecimal price, String note) {
		for(Trade trade : trades) {
			if(trade.getSellDate()==null) {
				trade.sell(date, price, note);
			}
		}
	}
	
	private Integer getProfitRate(BigDecimal price) {
		Integer rate = 0;
		for(Trade trade : trades) {
			if(trade.getSellDate()==null) {
				rate = trade.getProfitRate(price);
				break;
			}
		}
		
		return rate;
	}
	
	private Integer getProfitRateOfLastBuy(BigDecimal price) {
		Integer rate = 0;
		if(trades.size()>0) {
			rate = trades.get(trades.size()-1).getProfitRate(price);
		}
		return rate;
	}
	
	public Integer getProfit() {
		Integer amount = 0;
		if(records.size()>0) {
			Record record = records.get(records.size()-1);
			for(Trade trade : trades) {
				if(trade.getSellDate()==null) {
					trade.setSellPrice(record.getPrice());
				}
			}
			
			for(Trade trade : trades) {
				logger.info(trade.toString());//**********************8
				amount = amount + trade.getProfit();
			}
		}
		return amount;
	}
	
	private boolean onHand() {
		boolean onHand = false;
		for(Trade trade : trades) {
			if(trade.getSellDate()==null) {
				onHand = true;
				break;
			}
		}
		return onHand;
	}

	private BigDecimal calSlope(List<Record> records,Integer av,Integer period) {
		BigDecimal slope = BigDecimal.ZERO;
		if(records.size()>0) {		
			BigDecimal first = records.get(0).getAvPrice(av);
			BigDecimal last = records.get(records.size()-1).getAvPrice(av);
			if(first!=null && last!=null) {
				//slope = last.subtract(first);
				slope = last.subtract(first).divide(new BigDecimal(period),5,BigDecimal.ROUND_HALF_UP);
			}
		}

		return slope;		
	}
	
	private Integer calAboveAvDays(List<Record> records,Integer av) {
		int above = 0;
		for(Record tr : records){
			if(tr.isPriceAboveAv(av)){
				above++;
			}
		}
		return above;		
	}
	
	private Integer calBelowAvDays(List<Record> records,Integer av){
		int below = 0;
		for(Record tr : records){
			if(!tr.isPriceAboveAv(av)){   
				below++;
			}
		}
		return below;
	}
	
	
	private BigDecimal calMidPrice(List<Record> records,BigDecimal price){
		Set<BigDecimal> prices = new HashSet<BigDecimal>();
		prices.add(price);
		for(Record entity : records){
			prices.add(entity.getPrice());
		}
		
		List<BigDecimal> list = new ArrayList<BigDecimal>(prices);
		
		Collections.sort(list);
		
		return list.get(prices.size()/2);
	}
	
	/*
	 * 一般股票的走势是底部慢慢启动，顶部冲高回落
	 * 买入：
	 * 60日均线上穿120
	 * 250均线开始向上
	 * 
	 * 
	 * 卖出：
	 * 连续10个交易日股价低于60均线
	 * 
	 */
	public List<Trade> getBuyAndSell(LocalDate beginDate) {
		//List<Trade> list = new ArrayList<Trade>();

		Integer yellow = 0;  
		Integer red = 0;
		//Integer green = 1;
		Record previous;
		Record record;
		String note1 = null;
		String note2 = null;
		String str = null;
		BigDecimal[] av250  = new BigDecimal[2];
		if(records.size()>0) {
			previous = records.get(0);
			for(int i=1; i<records.size(); i++) {
				record = records.get(i);
				if(record.getAvPrice(250) != null && record.getDate().isAfter(beginDate)) {
					if(yellow==0 
						&& previous.isAbove(250,60) && record.isAbove(60,250) 
						&& record.getBiasOf250And120()>2
						&& record.getBiasOfAv(250)>2
						&& record.getPrice().compareTo(record.getMidPrice())!=1
						) {
						yellow ++;
						av250[0] = record.getAvPrice(250);
						note1 = record.getDate().toString() + "，60上穿250";
					}
					
					if(yellow == 1 
							&& previous.isAbove(250,120) && record.isAbove(120,250) 
						) {
						yellow ++;
						av250[1] = record.getAvPrice(250);
						note2 =  record.getDate().toString() + "，120上穿250";
					}
					
					if(yellow > 0 
							&& (previous.isAbove(60,250) && record.isAbove(250,60) || previous.isAbove(120,250) && record.isAbove(250,120)) 
						) {
						yellow = 0;
						av250[0] = null;
						av250[1] = null;
						note1 = "";
						note2 = "";
					}
					
//					if(yellow > 0 
//							
//						) {
//						yellow = 0;
//						av250[1] = record.getAvPrice(250);
//
//						note[0] =  "";
//						note[1] =  "";
//					}	
					
//					if(yellow>0 && red==0
//						&& record.getSlope(slopePeriod[2]).compareTo(record.getSlope(slopePeriod[1])) == -1
//						&& record.getSlope(slopePeriod[1]).compareTo(record.getSlope(slopePeriod[0])) == -1
//						&& record.getSlope(slopePeriod[0]).compareTo(BigDecimal.ZERO)==1
//							) {
//						red = 1;
//						note = note + "， " + record.getDate().toString() + "，250切线由负变正";  //比较60上穿250时（a）和买入时的250值(b)，b>a才能买入
//						//list.add(record.getDate().toString() + "," + record.getPrice().toString() + "," + note); 
//					}
//					
//					if(yellow>0 && red==1 
//							&& record.getSlope(slopePeriod[2]).compareTo(record.getSlope(slopePeriod[1])) == 1
//							&& record.getSlope(slopePeriod[1]).compareTo(BigDecimal.ZERO) == -1
//							&& record.getSlope(slopePeriod[0]).compareTo(BigDecimal.ZERO) == -1
//						) {
//						red = 0;
//						yellow = 0;
//						//note = "250切线由正变负，红灯灭";
//						//list.add(record.getDate().toString() + "," + record.getPrice().toString() + "," + note); 
//					}
					
					
					
					if(yellow==2 
							&& av250[0].compareTo(record.getAvPrice(250))==-1 
							&& record.getBiasOfAv(250)<5 
							&& !this.onHand()) {
						str = note1 + "；" + note2 + "； " + record.getDate().toString() + "，买入。";
						this.buy(record.getCode(),record.getName(),record.getDate(),record.getPrice(),str);
						red=0;
						yellow=0;
						note1 = "";
						note2 = "";
						//list.add(record.getDate().toString() + "," + record.getPrice().toString() + "," + note);
					}	
					
					
					if(this.onHand()  && this.getProfitRate(record.getPrice())>20) {
						str = record.getDate().toString() + "上涨" + this.getProfitRate(record.getPrice()) + "%，超过20%，止盈。";
						this.sell(record.getDate(),record.getPrice(),str);
						note1 = "";
						note2 = "";

						//list.add(record.getDate().toString() + "," + record.getPrice().toString() + "," + note);
					}

					
					if(this.onHand() && this.getProfitRate(record.getPrice())<-20) {
						str = record.getDate().toString() + "下跌" + this.getProfitRate(record.getPrice()) + "%，超过20%，止损。";
						this.sell(record.getDate(),record.getPrice(),str);
						note1 = "";
						note2 = "";

						//list.add(record.getDate().toString() + "," + record.getPrice().toString() + "," + note);
					}
		
				}
				
				previous = record;
			}
		}
		
		
		this.getProfit();
		
		return this.trades;
	}


	
}
