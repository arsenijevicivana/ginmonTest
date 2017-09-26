package com.ivana.dao;

import java.util.List;

public interface AbstractDAO<T> {

	public List<T> getById(String c);
	
	public void insert(T c);
	
	public void insertAll(List<T> c);

	public List<T> getAll();
	
	void delete(T c);
}
