package org.websure.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest("server.port:8181")
public class ApplicationIntegrationTests {

	@Test(expected = HttpClientErrorException.class)
	public void testRootGetAnonymous() {
		
		doAnonymousExchange(HttpMethod.GET, "/", null);
	}
	
	@Test
	public void testUserRegistrationPostAnonymous() {
		
		doAnonymousExchange(HttpMethod.POST, "/user", "{\"username\":\"someone@onthe.net\", \"password\": \"test12345\"}");
	}	

	private String doAnonymousExchange(final HttpMethod method, final String path, String request) {
		
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> testRequest = new HttpEntity<>(request, httpHeaders);
		HttpEntity<String> testResponse = restTemplate.exchange("http://localhost:8181/" + path, method, testRequest,
				String.class);
		return testResponse.getBody();
	}
}
