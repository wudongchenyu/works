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
@Table(name = "work_day")
public class WorkDay {
	
	@Id
    @Column(name = "id", length = 64)
	@Description(value = "主键")
	@GenericGenerator(name = "wid", strategy = "uuid")
	@GeneratedValue(generator = "wid")
	private String id;
	
	@Column(name = "merger_name", length = 50)
	@Description(value = "全称")
    private String mergerName;
	
	@Column(name = "date_string", length = 50)
	@Description(value = "时间字符串")
	private String dateString;
	
	@JSONField(format = "yyyy-MM-dd")
	@Column(name = "date_time", length = 10)
	@Temporal(value = TemporalType.DATE)
	@Description(value = "时间")
	private Date dateTime;
	
	@Description(value = "年份")
	@Column(name = "year", length = 4)
	private String year;
	
	@Column(name = "un_work_day", length = 1)
	@Description(value = "是否是工作日")
	private Boolean unWorkDay;
	
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "create_time", length = 19)
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date createTime = new Date();

}
