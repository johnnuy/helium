package org.johnnuy.cesium.service.impl;

import org.johnnuy.cesium.service.CesiumService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Component;

@Component
public class CesiumServicePostgresImpl extends JdbcDaoSupport implements CesiumService {

	private Logger logger = LoggerFactory.getLogger(getClass());

	public CesiumServicePostgresImpl(@Autowired JdbcTemplate jdbcTemplate) {
		setJdbcTemplate(jdbcTemplate);
	}
}
