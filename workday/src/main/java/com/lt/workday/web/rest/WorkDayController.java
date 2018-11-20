package com.lt.workday.web.rest;

import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lt.workday.web.po.WorkDay;
import com.lt.workday.web.service.WorkDayService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(tags = "工作日计算管理")
@RestController
@RequestMapping("/workday")
public class WorkDayController {
	
	private @Autowired WorkDayService workDayService;
	
	@ApiOperation(value = "保存非工作时间",notes = "保存非工作时间")
	@ApiImplicitParams(value = { @ApiImplicitParam(name = "date", value = "工作时间:2018-01-01",required = true) })
	@PostMapping("/save")
	public void save(
			@RequestParam(name = "date") String date) throws ParseException {
		workDayService.save(date);
	}
	
	@ApiOperation(value = "删除非工作时间",notes = "删除非工作时间")
	@ApiImplicitParams(value = { @ApiImplicitParam(name = "id", value = "ID",required = true) })
	@PostMapping("/delete")
	public void delete(
			@RequestParam(name = "id") String id) throws ParseException {
		workDayService.delete(id);
	}
	
	@ApiOperation(value = "非工作时间列表",notes = "非工作时间列表")
	@GetMapping("/list")
	public List<WorkDay> findList() throws ParseException {
		return workDayService.findAll();
	}
	
	@ApiOperation(value = "非工作时间列表",notes = "非工作时间列表")
	@GetMapping("/one")
	@ApiImplicitParams(value = { @ApiImplicitParam(name = "id", value = "ID",required = true) })
	public WorkDay findOne(@RequestParam(name = "id") String id) throws ParseException {
		return workDayService.findOne(id);
	}
	
	@ApiOperation(value = "获取相差days天的另一个工作日",notes = "获取相差days天的另一个工作日")
	@GetMapping("/other")
	@ApiImplicitParams(value = { 
			@ApiImplicitParam(name = "date",value = "开始日期：2018-10-10",required = true, dataType = "String"),
			@ApiImplicitParam(name = "days",value = "相差天数：10", example = "0",required = true, dataType = "int"),
			@ApiImplicitParam(name = "isBefore",value = "是否是之前：true",required = true, dataType = "Boolean")})
	public String getOtherWorkDay(
			@RequestParam(name = "date") String date,
			@RequestParam(name = "days") Integer days,
			@RequestParam(name = "isBefore") Boolean isBefore,
			@RequestParam(name = "year") String year) throws ParseException {
		return workDayService.getOtherWorkDay(date, days, isBefore, year);
	}
	
	@ApiOperation(value = "获取两个日期中间的工作日",notes = "获取两个日期中间的工作日")
	@GetMapping("/betweenWorkDays")
	@ApiImplicitParams(value = { 
			@ApiImplicitParam(name = "startDate",value = "开始日期：2018-10-10",required = true),
			@ApiImplicitParam(name = "endDate",value = "结束日期：2018-10-10",required = true)})
	public Long getBetweenWorkDays(
			@RequestParam(name = "startDate") String startDate,
			@RequestParam(name = "endDate") String endDate) {
		
		return workDayService.getBetweenWorkDays(startDate, endDate);
	}
	
	@ApiOperation(value = "获取两个日期中间的工作时间（分钟）",notes = "获取两个日期中间的工作时间（分钟）")
	@GetMapping("/betweenWorkMinutes")
	@ApiImplicitParams(value = { 
			@ApiImplicitParam(name = "startDate",value = "开始时间：2018-10-10 10:00:00",required = true),
			@ApiImplicitParam(name = "endDate",value = "结束时间：2018-10-10 10:00:00",required = true)})
	public Long getBetweenWorkMinutes(
			@RequestParam(name = "startDate") String startDate,
			@RequestParam(name = "endDate") String endDate) {
		return workDayService.getBetweenWorkMinutes(startDate, endDate);
	}
	
	@ApiOperation(value = "获取相差minutes分的另一个工作时间",notes = "获取相差minutes天的另一个工作时间")
	@GetMapping("/otherMinutes")
	@ApiImplicitParams(value = { 
			@ApiImplicitParam(name = "dateTime",value = "当前时间：2018-10-10 10:00:00",required = true),
			@ApiImplicitParam(name = "minutes",value = "相差时间（分钟）：10",required = true),
			@ApiImplicitParam(name = "isBefore",value = "是否是之前：true",required = true),
			@ApiImplicitParam(name = "year",value = "年度",required = true)})
	public String getOtherWorkMinutes(
			@RequestParam(name = "dateTime") String dateTime,
			@RequestParam(name = "minutes") Integer minutes,
			@RequestParam(name = "isBefore") Boolean isBefore,
			@RequestParam(name = "year") String year) throws ParseException {
		return workDayService.getOtherWorkMinutes(dateTime, minutes, isBefore, year);
	}

}
