package com.lt.workday.web.jpa;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.lt.workday.web.po.WorkTime;

public interface WorkTimeRepository extends CrudRepository<WorkTime, String>{
	
	WorkTime findTopByOrderByCreateTimeDesc();
	
	List<WorkTime> findAll();

}
