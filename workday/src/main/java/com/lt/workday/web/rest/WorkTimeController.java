package com.lt.workday.web.rest;

import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lt.workday.web.po.WorkTime;
import com.lt.workday.web.service.WorkTimeService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(tags = "每天工作时间管理")
@RestController
@RequestMapping("/worktime")
public class WorkTimeController {
	
	private @Autowired WorkTimeService workTimeService;
	
	@ApiOperation(value = "保存每日工作时间",notes = "保存每日工作时间")
	@ApiImplicitParams(value = { 
			@ApiImplicitParam(name = "morningStartHour", value = "上午开始时间小时",required = true, dataTypeClass = Integer.class),
			@ApiImplicitParam(name = "morningStartMinute", value = "上午开始时间分钟",required = true, dataTypeClass = Integer.class),
			@ApiImplicitParam(name = "afternoonStartHour", value = "下午开始时间小时",required = true, dataTypeClass = Integer.class),
			@ApiImplicitParam(name = "afternoonStartMinute", value = "下午开始时间分钟",required = true, dataTypeClass = Integer.class),
			@ApiImplicitParam(name = "morningEndHour", value = "上午结束时间小时",required = true, dataTypeClass = Integer.class),
			@ApiImplicitParam(name = "morningEndMinute", value = "上午结束时间分钟",required = true, dataTypeClass = Integer.class),
			@ApiImplicitParam(name = "afternoonEndHour", value = "下午结束时间小时",required = true, dataTypeClass = Integer.class),
			@ApiImplicitParam(name = "afternoonEndMinute", value = "下午结束时间分钟",required = true, dataTypeClass = Integer.class)})
	@PostMapping("/save")
	public void save(
			@RequestParam(name = "morningStartHour") Integer morningStartHour,
			@RequestParam(name = "morningStartMinute") Integer morningStartMinute,
			@RequestParam(name = "afternoonStartHour") Integer afternoonStartHour,
			@RequestParam(name = "afternoonStartMinute") Integer afternoonStartMinute,
			@RequestParam(name = "morningEndHour") Integer morningEndHour,
			@RequestParam(name = "morningEndMinute") Integer morningEndMinute,
			@RequestParam(name = "afternoonEndHour") Integer afternoonEndHour,
			@RequestParam(name = "afternoonEndMinute") Integer afternoonEndMinute) throws ParseException {
		workTimeService.save(
				morningStartHour,
				morningStartMinute,
				afternoonStartHour,
				afternoonStartMinute,
				morningEndHour,
				morningEndMinute,
				afternoonEndHour,
				afternoonEndMinute);
	}
	
	@ApiOperation(value = "删除每日工作时间",notes = "删除每日工作时间")
	@ApiImplicitParams(value = { @ApiImplicitParam(name = "id", value = "ID",required = true) })
	@PostMapping("/delete")
	public void delete(@RequestParam(name = "id") String id) throws ParseException {
		workTimeService.delete(id);
	}
	
	@ApiOperation(value = "删除每日工作时间",notes = "删除每日工作时间")
	@ApiImplicitParams(value = { @ApiImplicitParam(name = "id", value = "ID",required = true) })
	@GetMapping("/one")
	public WorkTime findOne(@RequestParam(name = "id") String id) throws ParseException {
		return workTimeService.findOne(id);
	}
	
	@ApiOperation(value = "删除每日工作时间",notes = "删除每日工作时间")
	@GetMapping("/list")
	public List<WorkTime> findList() throws ParseException {
		return workTimeService.findList();
	}

}
