package me.remind.userMicroservice.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.client.RestTemplate;

import me.remind.userMicroservice.model.Github;
import me.remind.userMicroservice.service.GithubService;

import static java.util.Arrays.asList;

public class GithubServiceImpl implements GithubService {
	
	// External Link
	private final String GITHUBLINK = "https://github.com/";
	private final String APIGITHUBLINKPART1 = "https://api.github.com/users/";
	private final String APIGITHUBLINKPART2 = "/repos";

	@Override
	public String findExternalResources(String link) {
		
		final RestTemplate restTemplate = new RestTemplate();
		String url = "";

		// check external link
		if (link.contains(GITHUBLINK)) {
			// build url
			url += APIGITHUBLINKPART1 + link.toLowerCase().replace(GITHUBLINK, "") + APIGITHUBLINKPART2;

			// collect data
			List<Github> list;
			
			try {
				list = asList(restTemplate.getForObject(url, Github[].class));
				// catch HTTP 404
			} catch (org.springframework.web.client.HttpClientErrorException e) {
				// return empty String
				return "";
			}
			// convert list into String and return
			return list.stream().map(Object::toString).collect(Collectors.joining(", "));
		} else {
			return "";
		}
	}

}
