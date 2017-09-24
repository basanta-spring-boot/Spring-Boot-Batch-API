package com.spring.batch.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.batch.api.dao.BatchEmployeeDao;
import com.spring.batch.api.model.Employee;

@Service
@Transactional
public class BatchEmployeeService {
	@Autowired
	private BatchEmployeeDao employeeDao;

	public List<Employee> findAllPageable(int start, int end) {
		return employeeDao.getEmployees(start, end);
	}

	public int count() {
		return employeeDao.recordCount();
	}
}
