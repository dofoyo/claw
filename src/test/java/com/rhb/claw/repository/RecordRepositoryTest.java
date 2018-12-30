package com.rhb.claw.repository;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.rhb.claw.repository.RecordEntity;
import com.rhb.claw.repository.RecordRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class RecordRepositoryTest {
	
	@Autowired
	@Qualifier("recordRepositoryImpFromDzh")
	RecordRepository recordRepositoryImpFromDzh;

	
	@Test
	public void test(){
		List<RecordEntity> records = recordRepositoryImpFromDzh.getRecordEntities("603999");
		
		for(RecordEntity tre : records){
			System.out.println(tre);
		}

	}
	
	
}
