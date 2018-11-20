package com.lt.base.dto;

import io.swagger.annotations.ApiModelProperty;
import jdk.jfr.Description;
import lombok.Data;

@Data
public class AreaDto {
	
	@ApiModelProperty(name = "id",value = "ID")
	@Description(value = "ID")
	private String id;
	
	@ApiModelProperty(name = "parentId",value = "父级ID")
	@Description(value = "父级ID")
    private String parentId;
	
	@ApiModelProperty(name = "name",value = "名称")
	@Description(value = "名称")
    private String name;
	
	@ApiModelProperty(name = "mergerName",value = "全称")
	@Description(value = "全称")
    private String mergerName;
	
	@ApiModelProperty(name = "mergerShortName",value = "全称简写")
	@Description(value = "全称简写")
    private String mergerShortName;
	
	@ApiModelProperty(name = "shortName",value = "简称")
	@Description(value = "简称")
    private String shortName;
	
	@ApiModelProperty(name = "pinyin",value = "拼音")
	@Description(value = "拼音")
    private String pinyin;
	
	@ApiModelProperty(name = "jianpin",value = "简拼")
	@Description(value = "简拼")
    private String jianpin;
	
	@ApiModelProperty(name = "firstChar",value = "首字母")
	@Description(value = "首字母")
    private String firstChar;
	
	@ApiModelProperty(name = "cityCode",value = "区号")
	@Description(value = "区号")
    private String cityCode;
	
	@ApiModelProperty(name = "levelType",value = "级别")
	@Description(value = "级别")
    private String levelType;
	
	@ApiModelProperty(name = "year",value = "年度")
	@Description(value = "年度")
    private String year;
	
	@ApiModelProperty(name = "longitude",value = "经度")
	@Description(value = "经度")
    private String longitude;
	
	@ApiModelProperty(name = "dimension",value = "维度")
	@Description(value = "维度")
    private String dimension;
	
	@ApiModelProperty(name = "zipCode",value = "邮编")
	@Description(value = "邮编")
    private String zipCode;

}
