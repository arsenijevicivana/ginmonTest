package com.ivana.controller;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.junit.Test;

import com.ivana.model.Repository;
import com.ivana.model.User;

public class JsonHelperTest {
  
	@Test
	public void shouldParseRepositories() throws IOException, URISyntaxException {
		
		//given 
		URI uri = this.getClass().getResource("testRepos.json").toURI();
		byte[] file = Files.readAllBytes(Paths.get(uri));
		String repositories = new String(file,Charset.defaultCharset());
		int repoCount = 7;
		User user;
		
		//when
		List<Repository> repositoriesList = JsonHelper.parseRepositories(repositories);
		user = repositoriesList.get(0).getUser();

		//then
		assertNotNull(repositoriesList);
		assertFalse(repositoriesList.isEmpty());
		assertEquals(repositoriesList.size(),repoCount);
		assertNotNull(user);
		assertEquals(user.getUsername(),"octocat");
		
	}
	
	

}
