package com.spring.batch.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.spring.batch.api.exception.advice.RecordIndexOutOfBoundException;
import com.spring.batch.api.model.Employee;
import com.spring.batch.api.service.BatchEmployeeService;

@Controller
public class EmployeeBatchController {
	@Autowired
	private BatchEmployeeService service;
	int count = 0;

	@RequestMapping("/home")
	public String getHomePage(Model model) {
		count = service.count();
		String message = "Total Records Available is :  " + count + "   Max : "
				+ count + " / " + "  Min : 0 ";
		model.addAttribute("message", message);
		return "home";
	}

	@RequestMapping("/loadPage")
	public String showPaginationPage(@RequestParam("start") int start,
			@RequestParam("end") int end, Model model)
			throws RecordIndexOutOfBoundException {
		List<Employee> employees = null;
		String errorMessage = "Index should not be more than Record count "
				+ count;
		employees = service.findAllPageable(start, end);
		if (!employees.isEmpty()) {
			model.addAttribute("employees", employees);
		} else {
			throw new RecordIndexOutOfBoundException(errorMessage);
		}
		return "page";
	}
}
