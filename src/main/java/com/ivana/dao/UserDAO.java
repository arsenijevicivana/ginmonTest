package com.ivana.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ivana.model.User;

@Repository
public class UserDAO implements AbstractDAO<User> {

	@Autowired
	SessionFactory sessionFactory;

	private Session currentSession;

	private Transaction currentTransaction;

	@SuppressWarnings("unchecked")
	@Override
	public List<User> getById(String username) {
		currentSession = sessionFactory.openSession();
		Query query = currentSession.createQuery("from User where username = :username");
		query.setParameter("username", username);
		List<User> result = query.list();
		currentSession.close();
		return result;
	}

	@Override
	public void insert(User user) {
		currentSession = sessionFactory.openSession();
		try {
			currentTransaction = currentSession.beginTransaction();
			currentSession.saveOrUpdate(user);
			currentTransaction.commit(); // Flush happens automatically
		} catch (RuntimeException e) {
			currentTransaction.rollback();
			throw e; // or display error message
		} finally {
			currentSession.close();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> getAll() {
		currentSession = sessionFactory.openSession();
		List<User> user = (List<User>) currentSession.createCriteria(User.class).list();
		currentSession.close();
		return user;
	}

	@Override
	public void delete(User user) {
		currentSession = sessionFactory.openSession();
		try {
			currentTransaction = currentSession.beginTransaction();
			currentSession.delete(user);
			currentTransaction.commit(); // Flush happens automatically
		} catch (RuntimeException e) {
			currentTransaction.rollback();
			throw e; // or display error message
		} finally {
			currentSession.close();
		}

	}

	@Override
	public void insertAll(List<User> users) {
		for (User user : users) {
			this.insert(user);
		}
	}
}
