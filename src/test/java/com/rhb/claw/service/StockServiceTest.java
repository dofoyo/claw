package com.rhb.claw.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.rhb.claw.model.Trade;
import com.rhb.claw.repository.RecordEntity;
import com.rhb.claw.repository.RecordRepository;
import com.rhb.claw.service.StockServiceImp;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class StockServiceTest {
	
	@Autowired
	@Qualifier("stockServiceImp")
	StockServiceImp stockServiceImp;

	
	@Test
	public void testGetTrades(){
		List<Trade> list = stockServiceImp.getTrades();
		
		StringBuffer sb = new StringBuffer();	
		sb.append(this.getTradeDetailTitle());
		for(Trade tre : list){
//			System.out.println(tre);
			sb.append(this.getTradeDetail(tre));
		}
		com.rhb.claw.util.FileUtil.writeTextFile("d:\\" + this.getDateString() + ".csv", sb.toString(), false);
	}
	
	private String getTradeDetailTitle() {
		StringBuffer sb = new StringBuffer();
		sb.append("code");
		sb.append(",");
//		sb.append("name");
//		sb.append(",");
		sb.append("quantity");
		sb.append(",");
		sb.append("buydate");
		sb.append(",");
		sb.append("buyprice");
		sb.append(",");
		sb.append("selldate");
		sb.append(",");
		sb.append("sellprice");
		sb.append(",");
		sb.append("profit");
		sb.append(",");
		sb.append("buynote");		
		sb.append(",");
		sb.append("sellnote");
		sb.append("\n");
		
		return sb.toString();
	}
	
	private String getTradeDetail(Trade trade) {
		StringBuffer sb = new StringBuffer();
		sb.append("'" + trade.getCode());
		sb.append(",");
//		sb.append(trade.getName());
//		sb.append(",");
		sb.append(trade.getQuantity());
		sb.append(",");
		sb.append(trade.getBuyDate().toString());
		sb.append(",");
		sb.append(trade.getBuyCost().toString());
		sb.append(",");
		sb.append(trade.getSellDate()==null ? "" : trade.getSellDate().toString());
		sb.append(",");
		sb.append(trade.getSellPrice().toString());
		sb.append(",");
		sb.append(trade.getProfit());
		sb.append(",");
		sb.append(trade.getBuynote());		
		sb.append(",");
		sb.append(trade.getSellnote());
		sb.append("\n");
		
		return sb.toString();
	}
	
	private String getDateString() {
		Date d = new Date();
		//System.out.println(d);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String dateNowStr = sdf.format(d);
		//System.out.println("格式化后的日期：" + dateNowStr);
		return dateNowStr;

	}
}
