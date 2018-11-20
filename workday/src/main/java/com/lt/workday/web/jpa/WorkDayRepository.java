package com.lt.workday.web.jpa;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.lt.workday.web.po.WorkDay;
import java.lang.Boolean;

public interface WorkDayRepository extends CrudRepository<WorkDay, String>{
	
	List<WorkDay> findByUnWorkDay(Boolean unworkday);
	
	Integer countByDateTimeBetween(Date start, Date end);

	Boolean existsByDateString(String dateString);
}
