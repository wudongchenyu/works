package com.lt.workday.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.lt.workday.web.jpa.WorkTimeRepository;
import com.lt.workday.web.po.WorkTime;

@Service
public class WorkTimeService {
	
	private @Autowired WorkTimeRepository workTimeRepository;
	private @Autowired StringRedisTemplate stringRedisTemplate;

	@Transactional(propagation = Propagation.REQUIRED)
	public void save(Integer morningStartHour, Integer morningStartMinute, Integer afternoonStartHour,
			Integer afternoonStartMinute, Integer morningEndHour, Integer morningEndMinute, Integer afternoonEndHour, Integer afternoonEndMinute) {
		WorkTime workTime = new WorkTime();
		workTime.setMorningStartHour(morningStartHour);
		workTime.setMorningStartMinute(morningStartMinute);
		workTime.setAfternoonStartHour(afternoonStartHour);
		workTime.setAfternoonStartMinute(afternoonStartMinute);
		workTime.setMorningEndHour(morningEndHour);
		workTime.setMorningEndMinute(morningEndMinute);
		workTime.setAfternoonEndHour(afternoonEndHour);
		workTime.setAfternoonEndMinute(afternoonEndMinute);
		WorkTime time = workTimeRepository.save(workTime);
		stringRedisTemplate.opsForValue().set("work_time", JSONObject.toJSONString(time));
	}
	
	public WorkTime gainWorkTime() {
		return workTimeRepository.findTopByOrderByCreateTimeDesc();
	}

	public void delete(String id) {
		workTimeRepository.deleteById(id);
		WorkTime workTime = workTimeRepository.findTopByOrderByCreateTimeDesc();
		stringRedisTemplate.opsForValue().set("work_time", JSONObject.toJSONString(workTime));
	}

	public WorkTime findOne(String id) {
		return workTimeRepository.findById(id).orElseGet(null);
				
	}

	public List<WorkTime> findList() {
		return workTimeRepository.findAll();
	}

}
