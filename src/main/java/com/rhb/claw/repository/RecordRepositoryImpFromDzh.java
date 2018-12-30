package com.rhb.claw.repository;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.rhb.claw.util.FileUtil;
import com.rhb.claw.util.ParseString;

@Service("recordRepositoryImpFromDzh")
public class RecordRepositoryImpFromDzh implements RecordRepository {
	protected static final Logger logger = LoggerFactory.getLogger(RecordRepositoryImpFromDzh.class);

	@Value("${dataPath}")
	private String dataPath;
	
	private String subPath = "trade/dzh/";
	
	
	@Override
	public List<RecordEntity> getRecordEntities(String code) {
		String pathAndfileName = dataPath + subPath + code + ".txt";
		
		File file = new File(pathAndfileName);
		if(!file.exists()){
			System.out.println(pathAndfileName + " do NOT exist.");
			return null;
		}
		
		List<RecordEntity> records = new ArrayList<RecordEntity>();
		
		String str = FileUtil.readTextFile(pathAndfileName);
		
		String[] lines = str.split("\n");
		String line = lines[0];
		String[] cells = line.split("\t");
		String name = cells[1];
		
		for(int i=2; i<lines.length; i++){
			line = lines[i];
			cells = line.split("\t");
			
		
			DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy/MM/dd");
			RecordEntity tre = new RecordEntity();
			tre.setCode(code);
			tre.setName(name);
			tre.setDate(LocalDate.parse(cells[0],df));
			tre.setPrice(ParseString.toBigDecimal(cells[4]));
			
			if(cells.length>8) {
				tre.setAvPrice(5,ParseString.toBigDecimal(cells[8]));
//				System.out.println("********** av5=" + tre.getAvPrice(5));
			}
			
			if(cells.length>11) {
				tre.setAvPrice(60,ParseString.toBigDecimal(cells[11]));
			}
			if(cells.length>12) {
				tre.setAvPrice(120,ParseString.toBigDecimal(cells[12]));
			}
			if(cells.length>13) {
				tre.setAvPrice(250,ParseString.toBigDecimal(cells[13]));
			}
			
			records.add(tre);

		}
		
		return records;
	}


	@Override
	public List<String> getCodes() {
		String pathAndfileName = dataPath + subPath;
		String suffix = "txt";
		boolean isdepth = true;
		List<File> files = FileUtil.getFiles(pathAndfileName, suffix, isdepth);
		List<String> codes = new ArrayList<String>();
		for(File file : files) {
			String code = file.getName().substring(0, 6);
			if(!code.startsWith("s")){
				codes.add(code);
			}
		}
		return codes;
	}

}
