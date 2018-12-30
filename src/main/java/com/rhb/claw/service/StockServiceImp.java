package com.rhb.claw.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.rhb.claw.model.Record;
import com.rhb.claw.model.Stock;
import com.rhb.claw.model.Trade;
import com.rhb.claw.repository.RecordEntity;
import com.rhb.claw.repository.RecordRepository;

@Service("stockServiceImp")
public class StockServiceImp implements StockService {
	@Autowired
	@Qualifier("recordRepositoryImpFromDzh")
	RecordRepository recordRepository;
	
	@Override
	public List<Trade> getTrades() {
		List<Trade> list = new ArrayList<Trade>();
		Stock stock;
		List<RecordEntity> entities;
		
		List<String> codes = recordRepository.getCodes();
		
//		List<String> codes = new ArrayList<String>() ;
//		codes.add("300177");
		
		int i = 0;
		int size = codes.size();
		for(String code : codes) {
			System.out.println(i++ + "/" + size + ", " + code);
			stock = new Stock();
			stock.setCode(code);
			entities = recordRepository.getRecordEntities(code);
			for(RecordEntity entity : entities) {
				stock.addRecord(this.getRecord(entity));
			}
			stock.refresh();
			
			list.addAll(stock.fish(LocalDate.of(2010, 1, 1)));
		}
		
		return list;
	}
	
	private Record getRecord(RecordEntity entity) {
		Record record = new Record();
		record.setCode(entity.getCode());
		record.setName(entity.getName());
		record.setDate(entity.getDate());
		record.setPrice(entity.getPrice());
		record.setAvPrice(5,entity.getAvPrice(5));
		record.setAvPrice(60,entity.getAvPrice(60));
		record.setAvPrice(120,entity.getAvPrice(120));
		record.setAvPrice(250,entity.getAvPrice(250));
		return record;
	}

}
