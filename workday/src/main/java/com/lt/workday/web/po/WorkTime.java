package com.lt.workday.web.po;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import com.alibaba.fastjson.annotation.JSONField;

import jdk.jfr.Description;
import lombok.Data;

@Data
@Entity
@Table(name = "work_time")
public class WorkTime {
	
	@Id
    @Column(name = "id", length = 64)
	@Description(value = "主键")
	@GenericGenerator(name = "tid", strategy = "uuid")
	@GeneratedValue(generator = "tid")
	private String id;
	
	@Column(name = "morning_start_hour", length = 2)
	@Description(value = "上午上班时间小时")
    private Integer morningStartHour;
	
	@Column(name = "morning_start_minute", length = 2)
	@Description(value = "上午上班时间分钟")
    private Integer morningStartMinute;
	
	@Column(name = "afternoon_start_hour", length = 2)
	@Description(value = "下午上班时间小时")
    private Integer afternoonStartHour;
	
	@Column(name = "afternoon_start_minute", length = 2)
	@Description(value = "下午上班时间分钟")
    private Integer afternoonStartMinute;
	
	@Column(name = "morning_end_hour", length = 2)
	@Description(value = "上午下班时间小时")
    private Integer morningEndHour;
	
	@Column(name = "morning_end_minute", length = 2)
	@Description(value = "上午下班时间分钟")
    private Integer morningEndMinute;
	
	@Column(name = "afternoon_end_hour", length = 2)
	@Description(value = "下午下班时间小时")
    private Integer afternoonEndHour;
	
	@Column(name = "afternoon_end_minute", length = 2)
	@Description(value = "下午下班时间分钟")
    private Integer afternoonEndMinute;
	
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "create_time", length = 19)
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date createTime = new Date();

}
