package com.spring.batch.api.exception.advice;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ExceptionMapper {
	@ExceptionHandler(RecordIndexOutOfBoundException.class)
	public ModelAndView handelException(RecordIndexOutOfBoundException ex) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("page");
		mav.addObject("error", ex.getLocalizedMessage());
		return mav;
	}
}
