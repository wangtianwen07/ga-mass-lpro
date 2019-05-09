package com.surfilter.lpro.java8;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.surfilter.lpro.ExtendableBean;

public class ExtendableBeanTest {

	@Test
	public void whenSerializingUsingJsonAnyGetter_thenCorrect() throws JsonProcessingException {
		ExtendableBean bean = new ExtendableBean("My bean");
	}
}
