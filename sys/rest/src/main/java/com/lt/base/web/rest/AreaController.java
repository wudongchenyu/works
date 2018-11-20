package com.lt.base.web.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.lt.base.entry.Area;
import com.lt.base.service.AreaService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(tags = "区域管理")
@RestController
public class AreaController {
	
	private @Autowired AreaService areaService;
	
	@ApiOperation(value = "新增区域", notes = "新增区域")
	@PostMapping(value = "/add")
	public Area save(@RequestBody @ApiParam(name = "区域对象", value = "传入json格式", required=true) Area area) {
		Area save = areaService.save(area);
		return save;
	}
	
	@ApiOperation(value = "删除区域", notes = "删除区域")
	@ApiImplicitParam(name = "areaId", value = "区域ID", required = true, paramType = "from",dataTypeClass = String.class)
	@PostMapping(value = "/delete")
	public Boolean save(String areaId) {
		return areaService.delete(areaId);
	}
	
	@ApiOperation(value = "修改区域", notes = "修改区域")
	@PostMapping(value = "/edit")
	public Area edit(@RequestBody @ApiParam(name = "区域对象", value = "传入json格式", required=true) Area area) {
		Area save = areaService.edit(area);
		return save;
	}
	
	@ApiOperation(value = "查询区域", notes = "查询区域")
	@ApiImplicitParam(name = "areaId", value = "区域ID", required = true, paramType = "from",dataTypeClass = String.class)
	@PostMapping(value = "/getArea")
	public Area getArea(String areaId) {
		Area save = areaService.getArea(areaId);
		return save;
	}
	
	@ApiOperation(value = "查询区域", notes = "查询区域")
	@ApiImplicitParams(value = { 
			@ApiImplicitParam(name = "currentPage", value = "当前页", defaultValue = "1", paramType = "from",dataTypeClass = String.class),
			@ApiImplicitParam(name = "pageSize", value = "每页个数", defaultValue = "10", paramType = "from",dataTypeClass = String.class),
			@ApiImplicitParam(name = "areaId", value = "区域ID", paramType = "from",dataTypeClass = String.class),
			@ApiImplicitParam(name = "parentId", value = "父级区域ID", paramType = "from",dataTypeClass = String.class),
			@ApiImplicitParam(name = "name", value = "名称", paramType = "from",dataTypeClass = String.class),
			@ApiImplicitParam(name = "cityCode", value = "区号", paramType = "from",dataTypeClass = String.class),
			@ApiImplicitParam(name = "levelType", value = "级别", paramType = "from",dataTypeClass = String.class),
			@ApiImplicitParam(name = "zipCode", value = "邮编", paramType = "from",dataTypeClass = String.class)})
	@PostMapping(value = "/getAreaList")
	public List<Area> getAreaList(Integer currentPage, Integer pageSize, String areaId, String parentId, String name, String cityCode, String levelType, String zipCode) {
		List<Area> list = areaService.getAreaList(areaId);
		return list;
	}

}
