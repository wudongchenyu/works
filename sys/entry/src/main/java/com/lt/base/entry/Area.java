package com.lt.base.entry;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import io.swagger.annotations.ApiModelProperty;
import jdk.jfr.Description;
import lombok.Data;

@Data
@Entity
@Table(name = "administrative_area")
public class Area {
	
	@Id
    @Column(name = "id", length = 64)
	@Description(value = "主键")
	@ApiModelProperty(name = "id",value = "ID")
	private String id;
	
	@Column(name = "parent_id", length = 64)
	@Description(value = "父级ID")
	@ApiModelProperty(name = "parentId",value = "父级ID")
    private String parentId;
	
	@Column(name = "name", length = 32)
	@Description(value = "名称")
	@ApiModelProperty(name = "name",value = "名称")
    private String name;
	
	@ApiModelProperty(name = "mergerName",value = "全称")
	@Column(name = "merger_name", length = 50)
	@Description(value = "全称")
    private String mergerName;
	
	@Column(name = "merger_short_name", length = 50)
	@Description(value = "全称简写")
	@ApiModelProperty(name = "mergerShortName",value = "全称简写")
    private String mergerShortName;
	
	@ApiModelProperty(name = "shortName",value = "简称")
	@Column(name = "short_name", length = 4)
	@Description(value = "简称")
    private String shortName;
	
	@Column(name = "pinyin", length = 50)
	@ApiModelProperty(name = "pinyin",value = "拼音")
	@Description(value = "拼音")
    private String pinyin;
	
	@ApiModelProperty(name = "jianpin",value = "简拼")
	@Column(name = "jianpin", length = 20)
	@Description(value = "简拼")
    private String jianpin;
	
	@ApiModelProperty(name = "firstChar",value = "首字母")
	@Column(name = "first_char", length = 2)
	@Description(value = "首字母")
    private String firstChar;
	
	@ApiModelProperty(name = "levelType",value = "级别")
	@Column(name = "city_code", length = 12)
	@Description(value = "区号")
    private String cityCode;
	
	@ApiModelProperty(name = "year",value = "年度")
	@Column(name = "year",length = 20)
	@Description(value = "年度")
    private String year;
	
	@ApiModelProperty(name = "longitude",value = "经度")
	@Column(name = "longitude", length = 20)
	@Description(value = "经度")
    private String longitude;
	
	@ApiModelProperty(name = "dimension",value = "维度")
	@Column(name = "dimension", length = 20)
	@Description(value = "维度")
    private String dimension;
	
	@ApiModelProperty(name = "levelType",value = "级别")
	@Column(name = "level_type", length = 1)
	@Description(value = "级别")
    private String levelType;
	
	@ApiModelProperty(name = "zipCode",value = "邮编")
	@Column(name = "zip_code", length = 2)
	@Description(value = "邮编")
    private String zipCode;
	
	@ApiModelProperty(name = "flag",value = "删除标志")
	@Column(name = "flag", length = 1)
	@Description(value = "删除标志")
    private String flag;
	
}
