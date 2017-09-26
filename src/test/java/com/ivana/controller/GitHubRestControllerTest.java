package com.ivana.controller;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import com.ivana.configuration.AppConfig;
import com.ivana.configuration.ServletConfig;

@WebAppConfiguration
@RunWith( SpringJUnit4ClassRunner.class )
@ContextConfiguration(classes = {ServletConfig.class, AppConfig.class})
public class GitHubRestControllerTest{

    @Autowired 
    private GithubRestController controller;

    @Test
	public void shouldImportUser() {
		// given
		String username = "octocat";
 
       try {
		ResponseEntity<String> resp = controller.importUser(username);
		assertEquals(200, resp.getStatusCodeValue());
		assertEquals("{\"code\":200,\"message\":\"Import successful.\"}", resp.getBody());
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		// when
		// then
	}
    
    @Test
   	public void shouldReturnNoUserFound() {
   		// given
   		String username = "octocat323882";
    
   		try {
   		ResponseEntity<String> resp = controller.importUser(username);
   		assertEquals(404, resp.getStatusCodeValue());
   		assertEquals("{\"code\":404,\"message\":\"404 Not Found\"}", resp.getBody());
   	} catch (IOException e) {
   		// TODO Auto-generated catch block
   		e.printStackTrace();
   	}
   		// when
   		// then
   	}

    @Test
   	public void shouldShowUser() {
   		// given
   		String username = "test";
    
          try {
   		ResponseEntity<String> resp = controller.showUsers(username);
   		assertEquals(200, resp.getStatusCodeValue());
   		assertEquals("{\"Java\":10}", resp.getBody());
   	} catch (IOException e) {
   		// TODO Auto-generated catch block
   		e.printStackTrace();
   	}
   		// when
   		// then
   	}
    
    @Test
   	public void shouldShowBadRequest() {
   		// given
   		String username = "octocat323882";
    
          try {
   		ResponseEntity<String> resp = controller.showUsers(username);
   		assertEquals(400, resp.getStatusCodeValue());
   		assertEquals("{\"code\":400,\"message\":\"Bad request!\"}", resp.getBody());
   	} catch (IOException e) {
   		// TODO Auto-generated catch block
   		e.printStackTrace();
   	}
   		// when
   		// then
   	}
       
    
}
