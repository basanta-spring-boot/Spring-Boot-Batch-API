package com.spring.batch.api.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.spring.batch.api.model.Employee;

@Repository
public class BatchEmployeeDao {

	@Autowired(required = true)
	private SessionFactory sessionFactory;

	// Avoid duplicate session creation
	private Session getSession() {
		Session session = sessionFactory.getCurrentSession();
		if (session == null) {
			session = sessionFactory.openSession();
		}
		return session;
	}

	@SuppressWarnings("unchecked")
	public List<Employee> getEmployees(int startIndex, int endIndex) {
		return getSession().createCriteria(Employee.class)
				.setFirstResult(startIndex).setMaxResults(endIndex).list();
	}

	public int recordCount() {
		return getSession().createCriteria(Employee.class).list().size();
	}
}
