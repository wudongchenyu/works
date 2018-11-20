package com.lt.base.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lt.base.contants.Constants;
import com.lt.base.entry.Area;
import com.lt.base.util.CommonsUtils;
import com.lt.base.web.jpa.AreaRepository;

@Service
public class AreaService {
	
	private @Autowired AreaRepository areaRepository;
	
	@Transactional(propagation = Propagation.REQUIRED)
	public Area save(Area area) {
		area.setId(CommonsUtils.getUUID());
		area.setFlag(Constants.FLAG_IS);
		Area save = areaRepository.save(area);
		return save;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public boolean delete(String areaId) {
		Optional<Area> areaOptional = areaRepository.findById(areaId);
		if (areaOptional.isPresent()) {
			Area area = areaOptional.get();
			area.setFlag(Constants.FLAG_NOT);
			areaRepository.save(area);
			return true;
		}
		return false;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public Area edit(Area area) {
		Area save = areaRepository.save(area);
		return save;
	}

	@Transactional(readOnly = true)
	public Area getArea(String areaId) {
		Optional<Area> areaOptional = areaRepository.findById(areaId);
		if (areaOptional.isPresent()) {
			return areaOptional.get();
		}
		return null;
	}

	@Transactional(readOnly = true)
	public List<Area> getAreaList(String areaId) {
		return null;
	}

}
