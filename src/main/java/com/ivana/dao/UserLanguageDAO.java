package com.ivana.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ivana.model.User;
import com.ivana.model.UserLanguageCount;

@Repository
public class UserLanguageDAO implements AbstractDAO<UserLanguageCount> {

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	UserDAO userDAO;

	private Session currentSession;

	private Transaction currentTransaction;

	@SuppressWarnings("unchecked")
	@Override
	public List<UserLanguageCount> getById(String username) {

		List<User> user = userDAO.getById(username);
		if (user.size() > 0) {
			currentSession = sessionFactory.openSession();
			Query query = currentSession.createQuery("from UserLanguageCount where user = :user");
			query.setParameter("user", user.get(0));
			List<UserLanguageCount> result = query.list();
			currentSession.close();
			return result;
		}
		else {
			return null;
		}
	}


	@Override
	public void insert(UserLanguageCount c) {
		currentSession = sessionFactory.openSession();
		try {
			currentTransaction = currentSession.beginTransaction();
			currentSession.saveOrUpdate(c);
			currentTransaction.commit();
		} catch (RuntimeException e) {
			currentTransaction.rollback();
			e.printStackTrace();
		} finally {
			currentSession.close();
		}
	}

	public void insertAll(List<UserLanguageCount> c) {
		for (UserLanguageCount userLang : c) {
			this.insert(userLang);
		}
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public List<UserLanguageCount> getAll() {
		currentSession = sessionFactory.openSession();
		List<UserLanguageCount> user = (List<UserLanguageCount>) currentSession.createCriteria(UserLanguageCount.class)
				.list();
		currentSession.close();
		return user;
	}

	@Override
	public void delete(UserLanguageCount c) {
		currentSession = sessionFactory.openSession();
		try {
			currentTransaction = currentSession.beginTransaction();
			currentSession.delete(c);
			currentTransaction.commit();
		} catch (RuntimeException e) {
			currentTransaction.rollback();
			e.printStackTrace();
		} finally {
			currentSession.close();
		}
	}

}
