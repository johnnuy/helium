package org.johnnuy.helium.test;


import java.util.Iterator;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

@TestInstance(Lifecycle.PER_CLASS)
public abstract class AbstractValidationTests {

	protected Validator validator = null;

	@BeforeAll
	public void init() {
		validator = Validation.buildDefaultValidatorFactory().getValidator();
	}

	protected <T> void assertContains(String message, Set<ConstraintViolation<T>> results) {
		Iterator<ConstraintViolation<T>> iter = results.iterator();
		while (iter.hasNext()) {
			ConstraintViolation<?> cv = iter.next();
			if (StringUtils.equals(message, cv.getMessageTemplate())) {
				return;
			}
		}
		Assertions.fail(message + " was not contained in " + results);
	}	
}
