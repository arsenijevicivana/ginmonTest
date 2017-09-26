package com.ivana.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.ivana.dao.AbstractDAO;
import com.ivana.model.Repository;
import com.ivana.model.User;
import com.ivana.model.UserLanguageCount;

@RestController
public class GithubRestController {

	private final Logger log = Logger.getLogger(this.getClass().getName());

	@Autowired
	private AbstractDAO<User> userDAO;

	@Autowired
	private AbstractDAO<UserLanguageCount> userLanguageDAO;

	/** URLs for Github API **/
	final static String uriUser = "https://api.github.com/users/{user}/repos";
	final static String uriLang = "https://api.github.com/repos/{full-name}/languages";
	
	final static String token = "7bf2a688205ba8736e8c6eefebbb673152f923f0";
    final static String tokenParam = "?state=closed&access_token={token}";
    
	RestTemplate restTemplate = new RestTemplate();

	/**
	 * GET method for importing list of user repos to db.
	 * 
	 * @throws IOException
	 **/

	@GetMapping(value = "/import/{name}", produces = "application/json")
	@ResponseBody
	public ResponseEntity<String> importUser(@PathVariable String name)
			throws IOException {

		List<Repository> repositories = null;
		try {
			repositories = getUserRepos(name);
		} catch (HttpClientErrorException ex) {
			return ResponseEntity.status(ex.getStatusCode())
					.body(JsonHelper.createResponseJson(ex.getRawStatusCode(), ex.getMessage()));
		}

		User user = null;
		if (repositories == null) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(JsonHelper.createResponseJson(500, "Something went wrong"));
		}
		if (repositories.size() > 0) {
			user = repositories.get(0).getUser();
			userDAO.insert(user);
		} else {
			return ResponseEntity.status(HttpStatus.OK).body(JsonHelper.createResponseJson(200, "Nothing to import."));
		}

		List<UserLanguageCount> userLang = null;
		try {
			userLang = sortLanguagesByUser(repositories, user);
		} catch (HttpClientErrorException ex) {
			return ResponseEntity.status(ex.getStatusCode())
					.body(JsonHelper.createResponseJson(ex.getRawStatusCode(), ex.getMessage()));
		}
		if (userLang == null) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(JsonHelper.createResponseJson(500, "Something went wrong"));
		}
		if (userLang.size() > 0) {
			userLanguageDAO.insertAll(userLang);
		} else {
			return ResponseEntity.status(HttpStatus.OK).body(JsonHelper.createResponseJson(200, "Nothing to import."));
		}
		return ResponseEntity.status(HttpStatus.OK).body(JsonHelper.createResponseJson(200, "Import successful."));
	}

	/**
	 * GET method returning json file from db.
	 * 
	 * @throws IOException
	 **/

	@GetMapping(value = "/show/{name}", produces = "application/json")
	@ResponseBody
	public ResponseEntity<String> showUsers(@PathVariable String name)
			throws IOException {

		List<UserLanguageCount> userLang = userLanguageDAO.getById(name);
		if (userLang != null) {
			return ResponseEntity.status(HttpStatus.OK).body(JsonHelper.mapJson(userLang));
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(JsonHelper.createResponseJson(400, "Bad request!"));
		}
	}

	/**
	 * Consuming Github Endpoint - getting list of repositories for specified user
	 * 
	 * @param user
	 * @return
	 **/
	private List<Repository> getUserRepos(String user) {

		Map<String, String> params = new HashMap<String, String>();
		params.put("user", user);
        params.put("token", token);

		String repos = restTemplate.getForObject(uriUser + tokenParam, String.class, params);
		log.info("Json for Repositories is: " + repos);
		return JsonHelper.parseRepositories(repos);
	}

	/**
	 * Consuming GitHub Endpoint - getting summarized json file with number of code
	 * line for each programming language
	 * 
	 * @param repo
	 *            full name
	 * @return json
	 */
	private String getLanguagesForRepo(String fullname) {

		Map<String, String> params = new HashMap<String, String>();
		params.put("full-name", fullname);
        params.put("token", token);
		
        String jsonResponse = restTemplate.getForObject(uriLang  + tokenParam, String.class, params);
		return jsonResponse;
	}

	/** Method returns list of UserLanguageCount objects ready for storing **/

	private List<UserLanguageCount> sortLanguagesByUser(List<Repository> repositories, User user) {

		List<String> repoLanguages = new ArrayList<String>();
		for (Repository repo : repositories) {
			repoLanguages.add(this.getLanguagesForRepo(repo.getFullname()));
		}
		return JsonHelper.populateLanguages(repoLanguages, user);
	}
}