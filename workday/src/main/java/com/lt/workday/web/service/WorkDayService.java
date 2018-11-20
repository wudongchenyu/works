package com.lt.workday.web.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lt.workday.web.jpa.WorkDayRepository;
import com.lt.workday.web.jpa.WorkTimeRepository;
import com.lt.workday.web.po.WorkDay;
import com.lt.workday.web.po.WorkTime;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class WorkDayService {

	private @Autowired WorkDayRepository workDayRepository;
	private @Autowired WorkTimeRepository workTimeRepository;
	private @Autowired StringRedisTemplate stringRedisTemplate;

	@Transactional(propagation = Propagation.REQUIRED)
	public void save(WorkDay workDay) {
		workDayRepository.save(workDay);
	}

	/**
	 * 保存非工作时间格式：2018-01-01
	 * @param date
	 * @throws ParseException
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void save(String date) throws ParseException {
		try {
			WorkDay workDay = new WorkDay();
			workDay.setDateString(date);
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			workDay.setUnWorkDay(true);
			Date dateTimeꦠꦆꦩꦌꦠꦠꦆꦩꦌꦠ = simpleDateFormat.parse(date);
			workDay.setDateTime(dateTimeꦠꦆꦩꦌꦠꦠꦆꦩꦌꦠ);
			workDay.setYear(date.substring(0, 4));
			workDay.setMergerName(new SimpleDateFormat("yyyy年MM月dd").format(workDay.getDateTime()));
			workDayRepository.save(workDay);
			this.saveWorkDayToRedis();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			//手动回滚
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		
	}

	/**
	 * 获取相差days天的另一个工作日
	 * @param date
	 * @param days
	 * @param isBefore
	 * @return
	 */
	public String getOtherWorkDay(String date, Integer days, Boolean isBefore, String year) {
		try {
			LocalDate localDate = LocalDate.parse(date);
			List<String> collect = stringRedisTemplate.opsForList().range("work_date_year:" + year, 0, -1);
			Boolean unWorkDay = false; 
			//判断当前日期是否为工作日,若不是工作日则转到相邻的工作日内
			while(collect.contains(localDate.toString())) {
				if (isBefore) {
					localDate = localDate.minusDays(1);
				}else {
					localDate = localDate.plusDays(1);
				}
			}
			
			//移动传入天数
			for (int i = 0; i < days; i++) {
				//移动一天
				if (isBefore) {
					localDate = localDate.minusDays(1);
				}else {
					localDate = localDate.plusDays(1);
				}
				//如果是工作日,i不变
				unWorkDay = collect.contains(localDate.toString());
				if (unWorkDay) {
					i--;
				}
			}
			return localDate.toString();
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 获取两个日期中间的工作日
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public Long getBetweenWorkDays(String startDate, String endDate) {
		LocalDate start = LocalDate.parse(startDate);
		LocalDate end = LocalDate.parse(endDate);
		return this.getBetweenWorkDays(LocalDateTime.of(start, LocalTime.now()), LocalDateTime.of(end, LocalTime.now()));
	}
	
	/**
	 * 获取两个日期中间的工作时间（分钟）
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public Long getBetweenWorkMinutes(String startDate, String endDate) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime start = LocalDateTime.parse(startDate, formatter);
		LocalDateTime end = LocalDateTime.parse(endDate, formatter);
		
		//获取每天工作时间
		WorkTime workTime = JSON.parseObject(stringRedisTemplate.opsForValue().get("work_time"), WorkTime.class);
		LocalTime morningStart = LocalTime.of(workTime.getMorningStartHour(), workTime.getMorningStartMinute());
		LocalTime morningEnd = LocalTime.of(workTime.getMorningEndHour(), workTime.getMorningEndMinute());
		LocalTime afternoonStart = LocalTime.of(workTime.getAfternoonStartHour(), workTime.getAfternoonStartMinute());
		LocalTime afternoonEnd = LocalTime.of(workTime.getAfternoonEndHour(), workTime.getAfternoonEndMinute());
		
		
		//计算相差天数
		Long daysMinutes = this.getBetweenWorkDays(start,  end)
				*getOneDayWorkMinutes(morningStart, morningEnd, afternoonStart, afternoonEnd);
		//计算相差分钟
		Long dayMinutes = this.getBetweenWorkMinutes(
				start,  
				end, 
				morningStart, 
				morningEnd, 
				afternoonStart, 
				afternoonEnd);
		return daysMinutes + dayMinutes;
	}
	
	/**
	 * 获取相差minutes分的另一个工作时间
	 * @param dateTime
	 * @param minutes
	 * @param isBefore
	 * @param year 
	 * @return
	 */
	public String getOtherWorkMinutes(String dateTime, Integer minutes, Boolean isBefore, String year) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		
		//传入日期时间
		LocalDateTime startTime = LocalDateTime.parse(dateTime, formatter);
		//传入的时间
		LocalTime startLocalTime = startTime.toLocalTime();
		
		WorkTime workTime = JSON.parseObject(stringRedisTemplate.opsForValue().get("work_time"), WorkTime.class);
		LocalTime morningStart = LocalTime.of(workTime.getMorningStartHour(), workTime.getMorningStartMinute());
		LocalTime morningEnd = LocalTime.of(workTime.getMorningEndHour(), workTime.getMorningEndMinute());
		LocalTime afternoonStart = LocalTime.of(workTime.getAfternoonStartHour(), workTime.getAfternoonStartMinute());
		LocalTime afternoonEnd = LocalTime.of(workTime.getAfternoonEndHour(), workTime.getAfternoonEndMinute());
		
		Integer oneDayWorkMinutes = getOneDayWorkMinutes(morningStart, morningEnd, afternoonStart, afternoonEnd);
		
		//计算工作天数
		Integer days = (int) (minutes/oneDayWorkMinutes);
		
		//获取剩余时间
		Integer residueMinutes =  (minutes % oneDayWorkMinutes);
		
		Boolean existsByDateString = workDayRepository.existsByDateString(dateTime.substring(0, 10));
		
		//获取与当前时间相差days天的另一个工作日
		List<String> collect = stringRedisTemplate.opsForList().range("work_date_year:" + year, 0, -1);
		
		
		LocalDate otherWorkDay = this.getOtherWorkDayLocalDate(dateTime, days, isBefore, collect);
		
		LocalDateTime localDateTime = LocalDateTime.of(otherWorkDay, startLocalTime);
		Long cdMinutes = Duration.between(afternoonStart, afternoonEnd).getSeconds()/60;
		Long bcMinutes = Duration.between(morningEnd, afternoonStart).getSeconds()/60;
		Long bdMinutes = Duration.between(morningEnd, afternoonEnd).getSeconds()/60;
		Long adMinutes = Duration.between(morningStart, afternoonEnd).getSeconds()/60;
		Long abMinutes = Duration.between(morningStart, morningEnd).getSeconds()/60;
		Long ceMinutes = null;
		Long beMinutes = null;
		Long aeMinutes = null;
		Long deMinutes = null;
		//获取当前时间之前的时间
		if (isBefore) {
			if (existsByDateString) {
				localDateTime = LocalDateTime.of(otherWorkDay, afternoonEnd);
				startLocalTime = afternoonEnd;
			}
			ceMinutes = Duration.between(afternoonStart, startLocalTime).getSeconds()/60;
			beMinutes = Duration.between(morningEnd, startLocalTime).getSeconds()/60;
			aeMinutes = Duration.between(morningStart, startLocalTime).getSeconds()/60;
			deMinutes = Duration.between(afternoonEnd, startLocalTime).getSeconds()/60;
			//5
			if (startLocalTime.isAfter(afternoonEnd)) {
				//54
				localDateTime = LocalDateTime.of(otherWorkDay, afternoonEnd);
				if (residueMinutes <= cdMinutes) {
					localDateTime = localDateTime.minusMinutes(residueMinutes);
					return localDateTime.format(formatter);
				}
				//53
				if (residueMinutes > cdMinutes && residueMinutes <= bdMinutes) {
					localDateTime = localDateTime.minusMinutes(residueMinutes + bcMinutes);
					return localDateTime.format(formatter);
				}
				
				//522
				if (residueMinutes > bdMinutes && residueMinutes <= adMinutes - bcMinutes) {
					localDateTime = localDateTime.minusMinutes(residueMinutes + bcMinutes);
					return localDateTime.format(formatter);
				}
			}
			
			//4
			if ((startLocalTime.isBefore(afternoonEnd) 
					||startLocalTime.equals(afternoonEnd)) 
					&& startLocalTime.isAfter(afternoonStart)) {
				//44
				if (residueMinutes <= ceMinutes) {
					localDateTime = localDateTime.minusMinutes(residueMinutes);
					return localDateTime.format(formatter);
				}
				//43
				if (residueMinutes > ceMinutes && residueMinutes <= beMinutes) {
					localDateTime = localDateTime.minusMinutes(residueMinutes);
					return localDateTime.format(formatter);
				}
				//422
				if (residueMinutes > beMinutes && residueMinutes <= aeMinutes - bcMinutes) {
					localDateTime = localDateTime.minusMinutes(residueMinutes + bcMinutes);
					return localDateTime.format(formatter);
				}
				
				//421/44
				if (residueMinutes > aeMinutes - bcMinutes) {
					LocalDate localDate = this.getOtherWorkDayLocalDate(localDateTime.toLocalDate().toString(), 1, isBefore, collect);
					localDateTime = LocalDateTime.of(localDate, afternoonEnd).minusMinutes(residueMinutes - aeMinutes + bcMinutes);
					return localDateTime.format(formatter);
				}
			}
			
			//3
			if (startLocalTime.isAfter(morningEnd) 
					&& (startLocalTime.isBefore(afternoonStart) 
							|| startLocalTime.equals(afternoonStart)))  {
				localDateTime = LocalDateTime.of(otherWorkDay, morningEnd);
				//32
				if (residueMinutes <= abMinutes) {
					localDateTime = localDateTime.minusMinutes(residueMinutes);
					return localDateTime.format(formatter);
				}
				
				//34
				if (residueMinutes > abMinutes && residueMinutes <= abMinutes + cdMinutes) {
					LocalDate localDate = this.getOtherWorkDayLocalDate(localDateTime.toLocalDate().toString(), 1, isBefore, collect);
					localDateTime = LocalDateTime.of(localDate, afternoonEnd).minusMinutes(residueMinutes - abMinutes);
					return localDateTime.format(formatter);
				}
			}
			//2
			if (startLocalTime.isAfter(morningStart) 
					&& (startLocalTime.isBefore(morningEnd) 
							|| startLocalTime.equals(morningEnd)))  {
				//22
				if (residueMinutes <= aeMinutes) {
					localDateTime = localDateTime.minusMinutes(residueMinutes);
					return localDateTime.format(formatter);
				}
				
				//24
				if (residueMinutes > aeMinutes && residueMinutes <= aeMinutes + cdMinutes) {
					LocalDate localDate = this.getOtherWorkDayLocalDate(localDateTime.toLocalDate().toString(), 1, isBefore, collect);
					localDateTime = LocalDateTime.of(localDate, afternoonEnd).minusMinutes(residueMinutes - aeMinutes);
					return localDateTime.format(formatter);
				}
				
				//23
				if (residueMinutes <= aeMinutes + bdMinutes && residueMinutes > aeMinutes + cdMinutes) {
					LocalDate localDate = this.getOtherWorkDayLocalDate(localDateTime.toLocalDate().toString(), 1, isBefore, collect);
					localDateTime = LocalDateTime.of(localDate, morningEnd).minusMinutes(residueMinutes - aeMinutes - cdMinutes);
					return localDateTime.format(formatter);
				}
				
				//222
				if (residueMinutes > aeMinutes + bdMinutes) {
					LocalDate localDate = this.getOtherWorkDayLocalDate(localDateTime.toLocalDate().toString(), 1, isBefore, collect);
					localDateTime = LocalDateTime.of(localDate, morningEnd).minusMinutes(residueMinutes - aeMinutes - cdMinutes + bcMinutes);
					return localDateTime.format(formatter);
				}
			}
			
			//1
			if (startLocalTime.isBefore(morningStart) || startLocalTime.equals(morningStart)) {
				LocalDate localDate = this.getOtherWorkDayLocalDate(localDateTime.toLocalDate().toString(), 1, isBefore, collect);
				localDateTime = LocalDateTime.of(localDate, afternoonEnd);
				//14
				if (residueMinutes <= cdMinutes) {
					localDateTime = localDateTime.minusMinutes(residueMinutes);
					return localDateTime.format(formatter);
				}
				//13
				if (residueMinutes > cdMinutes && residueMinutes <= bdMinutes) {
					localDateTime = localDateTime.minusMinutes(residueMinutes + bcMinutes);
					return localDateTime.format(formatter);
				}
				
				//12
				if (residueMinutes > bdMinutes && residueMinutes <= adMinutes - bcMinutes) {
					localDateTime = localDateTime.minusMinutes(residueMinutes + bcMinutes);
					return localDateTime.format(formatter);
				}
			}
		}else {
			if (existsByDateString) {
				localDateTime = LocalDateTime.of(otherWorkDay, morningEnd);
				startLocalTime = morningEnd;
			}
			ceMinutes = Duration.between(afternoonStart, startLocalTime).getSeconds()/60;
			beMinutes = Duration.between(morningEnd, startLocalTime).getSeconds()/60;
			aeMinutes = Duration.between(morningStart, startLocalTime).getSeconds()/60;
			deMinutes = Duration.between(afternoonEnd, startLocalTime).getSeconds()/60;
			//1
			if (startLocalTime.isBefore(morningStart) || startLocalTime.equals(morningStart)) {
				//11/12
				if (residueMinutes <= abMinutes) {
					localDateTime = LocalDateTime.of(otherWorkDay, morningStart).plusMinutes(residueMinutes);
					return localDateTime.format(formatter);
				}
				//13//14
				if (residueMinutes <= abMinutes + cdMinutes && residueMinutes <= abMinutes + bcMinutes) {
					localDateTime = LocalDateTime.of(otherWorkDay, morningStart).plusMinutes(residueMinutes + bcMinutes);
					return localDateTime.format(formatter);
				}
			}
			//2
			if (startLocalTime.isAfter(morningStart) && (startLocalTime.isBefore(morningEnd) || startLocalTime.equals(morningEnd))) {
				//22
				if (residueMinutes <= -beMinutes) {
					localDateTime = localDateTime.plusMinutes(residueMinutes);
					return localDateTime.format(formatter);
				}
				//23/24
				if (residueMinutes > -beMinutes && residueMinutes <= -deMinutes) {
					localDateTime = localDateTime.plusMinutes(residueMinutes + bcMinutes);
					return localDateTime.format(formatter);
				}
				
				//25
				if (residueMinutes > -beMinutes && residueMinutes <= -deMinutes) {
					LocalDate localDate = this.getOtherWorkDayLocalDate(localDateTime.toLocalDate().toString(), 1, isBefore, collect);
					localDateTime = LocalDateTime.of(localDate, morningStart).plusMinutes(residueMinutes + beMinutes - cdMinutes);
					return localDateTime.format(formatter);
				}
			}
			//3
			if (startLocalTime.isAfter(morningEnd) 
					&& (startLocalTime.isBefore(afternoonStart) 
							|| startLocalTime.equals(afternoonStart))) {
				//34
				if (residueMinutes <= cdMinutes) {
					localDateTime = LocalDateTime.of(otherWorkDay, afternoonStart).plusMinutes(residueMinutes);
					return localDateTime.format(formatter);
				}
				
				//31
				if (residueMinutes > cdMinutes && residueMinutes<= abMinutes + cdMinutes) {
					LocalDate localDate = this.getOtherWorkDayLocalDate(localDateTime.toLocalDate().toString(), 1, isBefore, collect);
					localDateTime = LocalDateTime.of(localDate, morningStart).plusMinutes(residueMinutes - cdMinutes);
					return localDateTime.format(formatter);
				}
			}
			//4
			if (startLocalTime.isAfter(afternoonStart) 
					&& (startLocalTime.isBefore(afternoonEnd) 
							|| startLocalTime.equals(afternoonEnd))) {
				//44
				if (residueMinutes <= -deMinutes) {
					localDateTime = localDateTime.plusMinutes(residueMinutes);
					return localDateTime.format(formatter);
				}
				
				//42
				if (residueMinutes> -deMinutes & residueMinutes <= -deMinutes + abMinutes) {
					LocalDate localDate = this.getOtherWorkDayLocalDate(localDateTime.toLocalDate().toString(), 1, isBefore, collect);
					localDateTime = LocalDateTime.of(localDate, morningStart).plusMinutes(residueMinutes + deMinutes);
					return localDateTime.format(formatter);
				}
				//44
				if (residueMinutes > -deMinutes + abMinutes && residueMinutes <= abMinutes + cdMinutes) {
					LocalDate localDate = this.getOtherWorkDayLocalDate(localDateTime.toLocalDate().toString(), 1, isBefore, collect);
					localDateTime = LocalDateTime.of(localDate, morningStart).plusMinutes(residueMinutes + deMinutes + bcMinutes);
					return localDateTime.format(formatter);
				}
			}
			//5
			if (startLocalTime.isAfter(afternoonEnd)) {
				LocalDate localDate = this.getOtherWorkDayLocalDate(localDateTime.toLocalDate().toString(), 1, isBefore, collect);
				//52
				if (residueMinutes <= abMinutes) {
					localDateTime = LocalDateTime.of(localDate, morningStart).plusMinutes(residueMinutes);
					return localDateTime.format(formatter);
				}
				//54
				if (residueMinutes <= abMinutes + cdMinutes && residueMinutes <= abMinutes + bcMinutes) {
					localDateTime = LocalDateTime.of(localDate, morningStart).plusMinutes(residueMinutes + bcMinutes);
					return localDateTime.format(formatter);
				}
			}
		}
		return localDateTime.format(formatter);
	}
	
	private Integer getOneDayWorkMinutes(
			LocalTime morningStart, 
			LocalTime morningEnd, 
			LocalTime afternoonStart,
			LocalTime afternoonEnd) {
		return (int) ((Duration.between(morningStart, morningEnd).getSeconds() + 
				Duration.between(afternoonStart, afternoonEnd).getSeconds())/60);
	}

	private LocalDate getOtherWorkDayLocalDate(String dateTime, Integer days, Boolean isBefore, List<String> collect) {
		try {
			LocalDate date =  LocalDate.parse(dateTime.substring(0,10));
			Boolean isWorkDay = false;
			//判断当前日期是否为工作日
			while(collect.contains(date.toString())) {
				if (isBefore) {
					date = date.minusDays(1);
				}else {
					date = date.plusDays(1);
				}
			}
			
			//循环
			for (int i = 0; i < days; i++) {
				if (isBefore) {
					date = date.minusDays(1);
				}else {
					date = date.plusDays(1);
				}
				isWorkDay = collect.contains(date.toString());
				if (isWorkDay) {
					i--;
				}
			}
			return date;
		} catch (Exception e) {
			return null;
		}
	}

	private Long getBetweenWorkDays(LocalDateTime startDate, LocalDateTime endDate) {
		LocalDate start = startDate.toLocalDate();
		LocalDate end = endDate.toLocalDate();
		
		Long startLong = start.toEpochDay();
		Long endLong = end.toEpochDay();
		
		Long a = 0L;
		if (startLong > endLong) {
			a = startLong - endLong;
		}else {
			a = endLong - startLong;
		}
		Date beginDate = Date.from(start.atStartOfDay(ZoneId.systemDefault()).toInstant());
		
		Date finishDate = Date.from(end.atStartOfDay(ZoneId.systemDefault()).toInstant());
		
		Integer timeBetween;
		
		if (beginDate.before(finishDate)) {
			timeBetween = workDayRepository
					.countByDateTimeBetween(beginDate, finishDate);
		}else {
			timeBetween = workDayRepository
					.countByDateTimeBetween(finishDate, beginDate);
		}
		
		return a - timeBetween;
	}
	
	private Long getBetweenWorkMinutes(
			LocalDateTime startDate, 
			LocalDateTime endDate, 
			LocalTime morningStart, 
			LocalTime morningEnd, 
			LocalTime afternoonStart, 
			LocalTime afternoonEnd) {
		LocalTime startDateTime = startDate.toLocalTime();
		LocalTime endDateTime = endDate.toLocalTime();
		Long betweenMinus = 0L;
		
		if (startDateTime.equals(endDateTime)) {
			betweenMinus = 0L;
		}
		
		//11
		if ((startDateTime.isBefore(morningStart) || startDateTime.equals(morningStart)) 
				&& (endDateTime.isBefore(morningStart) || endDateTime.equals(morningStart))) {
			betweenMinus = 0L;
		}
		
		//12
		if ((startDateTime.isBefore(morningStart) || startDateTime.equals(morningStart)) 
				&& endDateTime.isAfter(morningStart) 
				&& (endDateTime.isBefore(morningEnd) || endDateTime.equals(morningEnd))) {
			betweenMinus = Duration.between(morningStart, endDateTime).get(ChronoUnit.SECONDS);
		}
		
		//13
		if ((startDateTime.isBefore(morningStart) || startDateTime.equals(morningStart)) 
				&& endDateTime.isAfter(morningEnd) 
				&& (endDateTime.isBefore(afternoonStart) || endDateTime.equals(afternoonStart))) {
			betweenMinus = Duration.between(morningStart, morningEnd).get(ChronoUnit.SECONDS);
		}
		
		//14
		if ((startDateTime.isBefore(morningStart) || startDateTime.equals(morningStart)) 
				&& endDateTime.isAfter(afternoonStart) 
				&& (endDateTime.isBefore(afternoonEnd) || endDateTime.equals(afternoonEnd))) {
			betweenMinus = Duration.between(morningStart, morningEnd).get(ChronoUnit.SECONDS) 
					+ Duration.between(afternoonStart, endDateTime).get(ChronoUnit.SECONDS);
		}
		
		//15
		if ((startDateTime.isBefore(morningStart) || startDateTime.equals(morningStart)) 
				&& endDateTime.isAfter(afternoonEnd)) {
			betweenMinus = Duration.between(morningStart, morningEnd).get(ChronoUnit.SECONDS) 
					+ Duration.between(afternoonStart, afternoonEnd).get(ChronoUnit.SECONDS);
		}
		
		//22
		if ((startDateTime.isBefore(morningEnd) || startDateTime.equals(morningEnd))
				&& startDateTime.isAfter(morningStart) 
				&& endDateTime.isAfter(morningStart) 
				&& (endDateTime.isBefore(morningEnd) || endDateTime.equals(morningEnd))) {
			betweenMinus = Duration.between(startDateTime, endDateTime).get(ChronoUnit.SECONDS);
		}
		
		//23
		if ((startDateTime.isBefore(morningEnd) || startDateTime.equals(morningEnd))
				&& startDateTime.isAfter(morningStart) 
				&& endDateTime.isAfter(morningEnd) 
				&& (endDateTime.isBefore(afternoonStart) || endDateTime.equals(afternoonStart))) {
			betweenMinus = Duration.between(startDateTime, morningEnd).get(ChronoUnit.SECONDS);
		}
		
		//24
		if ((startDateTime.isBefore(morningEnd) || startDateTime.equals(morningEnd))
				&& startDateTime.isAfter(morningStart) 
				&& endDateTime.isAfter(afternoonStart) 
				&& (endDateTime.isBefore(afternoonEnd) || endDateTime.equals(afternoonEnd))) {
			betweenMinus = Duration.between(startDateTime, morningEnd).get(ChronoUnit.SECONDS) + 
					Duration.between(afternoonStart, endDateTime).get(ChronoUnit.SECONDS);
		}
		
		//25
		if ((startDateTime.isBefore(morningEnd) || startDateTime.equals(morningEnd))
				&& startDateTime.isAfter(morningStart) 
				&& endDateTime.isAfter(afternoonEnd)) {
			betweenMinus = Duration.between(startDateTime, morningEnd).get(ChronoUnit.SECONDS) +
					Duration.between(afternoonStart, afternoonEnd).get(ChronoUnit.SECONDS);
		}
		
		//33
		if ((startDateTime.isBefore(afternoonStart) || startDateTime.equals(afternoonStart))
				&& startDateTime.isAfter(morningEnd) 
				&& endDateTime.isAfter(morningEnd)
				&& (endDateTime.isBefore(afternoonStart) || endDateTime.equals(afternoonStart))) {
			betweenMinus = Duration.between(startDateTime, endDateTime).get(ChronoUnit.SECONDS);
		}
		
		//34
		if ((startDateTime.isBefore(afternoonStart) || startDateTime.equals(afternoonStart))
				&& startDateTime.isAfter(morningEnd) 
				&& endDateTime.isAfter(afternoonStart)
				&& (endDateTime.isBefore(afternoonEnd) || endDateTime.equals(afternoonEnd))) {
			betweenMinus = Duration.between(afternoonStart, endDateTime).get(ChronoUnit.SECONDS);
		}
		
		//35
		if ((startDateTime.isBefore(afternoonStart) || startDateTime.equals(afternoonStart))
				&& startDateTime.isAfter(morningEnd) 
				&& endDateTime.isAfter(afternoonEnd)) {
			betweenMinus = Duration.between(afternoonStart, afternoonEnd).get(ChronoUnit.SECONDS);
		}
		
		//44
		if ((startDateTime.isBefore(afternoonEnd) || startDateTime.equals(afternoonEnd))
				&& startDateTime.isAfter(afternoonStart) 
				&& endDateTime.isAfter(afternoonStart)
				&& (endDateTime.isBefore(afternoonEnd) || endDateTime.equals(afternoonEnd))) {
			betweenMinus = Duration.between(startDateTime, endDateTime).get(ChronoUnit.SECONDS);
		}
		
		//45
		if ((startDateTime.isBefore(afternoonEnd) || startDateTime.equals(afternoonEnd)) 
				&& startDateTime.isAfter(afternoonStart) 
				&& endDateTime.isAfter(afternoonEnd)) {
			betweenMinus = Duration.between(startDateTime, afternoonEnd).get(ChronoUnit.SECONDS);
		}
		
		//55
		if (startDateTime.isAfter(afternoonEnd) 
				&& endDateTime.isAfter(afternoonEnd)) {
			betweenMinus = 0L;
		}
		
		//21
		if ((startDateTime.isBefore(morningEnd) || startDateTime.equals(morningEnd))
				&& startDateTime.isAfter(morningStart) 
				&& (endDateTime.isBefore(morningStart) || endDateTime.equals(morningStart))) {
			betweenMinus = Duration.between(startDateTime, morningStart).get(ChronoUnit.SECONDS);
		}
		
		//31
		if ((startDateTime.isBefore(afternoonStart) || startDateTime.equals(afternoonStart))
				&& startDateTime.isAfter(morningEnd) 
				&& (endDateTime.isBefore(morningStart) || endDateTime.equals(morningStart))) {
			betweenMinus = Duration.between(morningEnd, morningStart).get(ChronoUnit.SECONDS);
		}
		
		//32
		if ((startDateTime.isBefore(afternoonStart) || startDateTime.equals(afternoonStart))
				&& startDateTime.isAfter(morningEnd) 
				&& (endDateTime.isBefore(morningEnd) || endDateTime.equals(morningEnd))
				&& endDateTime.isAfter(morningStart)) {
			betweenMinus = Duration.between(morningEnd, endDateTime).get(ChronoUnit.SECONDS);
		}
		
		//41
		if ((startDateTime.isBefore(afternoonEnd) || startDateTime.equals(afternoonEnd))
				&& startDateTime.isAfter(afternoonStart) 
				&& (endDateTime.isBefore(morningStart) || endDateTime.equals(morningStart))) {
			betweenMinus = Duration.between(morningEnd, morningStart).get(ChronoUnit.SECONDS) + 
					Duration.between(endDateTime, afternoonStart).get(ChronoUnit.SECONDS);
		}
		
		//42
		if ((startDateTime.isBefore(afternoonEnd) || startDateTime.equals(afternoonEnd))
				&& startDateTime.isAfter(afternoonStart) 
				&& (endDateTime.isBefore(morningEnd) || endDateTime.equals(morningEnd))
				&& endDateTime.isAfter(morningStart)) {
			betweenMinus = Duration.between(morningEnd, endDateTime).get(ChronoUnit.SECONDS) + 
					Duration.between(startDateTime, afternoonStart).get(ChronoUnit.SECONDS);
		}
		
		//43
		if ((startDateTime.isBefore(afternoonEnd) || startDateTime.equals(afternoonEnd))
				&& startDateTime.isAfter(afternoonStart) 
				&& (endDateTime.isBefore(afternoonStart) || endDateTime.equals(afternoonStart))
				&& endDateTime.isAfter(morningEnd)) {
			betweenMinus =  Duration.between(startDateTime, afternoonStart).get(ChronoUnit.SECONDS);
		}
		
		//51
		if (startDateTime.isAfter(afternoonEnd)
				&& (endDateTime.isBefore(morningStart) || endDateTime.equals(morningStart))) {
			betweenMinus =  Duration.between(morningEnd, morningStart).get(ChronoUnit.SECONDS) + 
					Duration.between(afternoonEnd, afternoonStart).get(ChronoUnit.SECONDS);
		}
		
		//52
		if (startDateTime.isAfter(afternoonEnd)
				&& (endDateTime.isBefore(morningEnd) || endDateTime.equals(morningEnd))
				&& endDateTime.isAfter(morningStart)) {
			betweenMinus =  Duration.between(morningEnd, endDateTime).get(ChronoUnit.SECONDS) + 
					Duration.between(afternoonEnd, afternoonStart).get(ChronoUnit.SECONDS);
		}
		
		//53
		if (startDateTime.isAfter(afternoonEnd)
				&& (endDateTime.isBefore(afternoonStart) || endDateTime.equals(afternoonStart))
				&& endDateTime.isAfter(morningEnd)) {
			betweenMinus = Duration.between(afternoonEnd, afternoonStart).get(ChronoUnit.SECONDS);
		}
		
		//54
		if (startDateTime.isAfter(afternoonEnd) 
				&& endDateTime.isAfter(afternoonStart)
				&& (endDateTime.isBefore(afternoonEnd) || endDateTime.equals(afternoonEnd))) {
			betweenMinus =  Duration.between(afternoonEnd, endDateTime).get(ChronoUnit.SECONDS);
		}
		return betweenMinus/60L;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(String id) {
		workDayRepository.deleteById(id);
		this.saveWorkDayToRedis();
	}

	public List<WorkDay> findAll() {
		List<WorkDay> list = workDayRepository.findByUnWorkDay(true);
		return list;
	}

	public WorkDay findOne(String id) {
		return workDayRepository.findById(id).orElseGet(null);
	}

	public void saveWorkDayToRedis() {
		Map<String, List<String>> map = this.findAll()
			.parallelStream()
			.collect(
					Collectors.groupingBy(
							WorkDay::getYear,
							Collectors.mapping(
									WorkDay::getDateString,
									Collectors.toList())));
		ListOperations<String, String> listOperations = stringRedisTemplate.opsForList();
		stringRedisTemplate.delete(stringRedisTemplate.keys("work_date_year:"));
		map.keySet().forEach(key->{
			listOperations.leftPushAll("work_date_year:" + key, map.get(key));
		});
		
		WorkTime workTime = workTimeRepository.findTopByOrderByCreateTimeDesc();
		stringRedisTemplate.opsForValue().set("work_time", JSONObject.toJSONString(workTime));
	}

}
