package org.johnnuy.helium.service.impl;

import org.johnnuy.helium.service.HeliumService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Component;

@Component
public class HeliumServicePostgresImpl extends JdbcDaoSupport implements HeliumService {

	private Logger logger = LoggerFactory.getLogger(getClass());

	public HeliumServicePostgresImpl(@Autowired JdbcTemplate jdbcTemplate) {
		setJdbcTemplate(jdbcTemplate);
	}
}
