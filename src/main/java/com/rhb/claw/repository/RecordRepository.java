package com.rhb.claw.repository;

import java.util.List;

public interface RecordRepository {
	public List<RecordEntity> getRecordEntities(String code);
	public List<String> getCodes();

}
