package br.com.abreu.sptrans.api.business.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import br.com.abreu.sptrans.api.business.AuthentictionBusiness;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AuthenticationBusinessImpl implements AuthentictionBusiness {

	@Value("${sptrans-uri}")
	private String URI_SPTRANS;

	@Value("${sptrans-token}")
	private String TOKEN;

	private final RestTemplate restTemplate;

	public AuthenticationBusinessImpl(RestTemplateBuilder restTemplateBuilder) {
		this.restTemplate = restTemplateBuilder.build();
	}

	@Override
	public HttpEntity<String> createAuthHeader(String cookie) {
		HttpHeaders header = new HttpHeaders();
		header.add("Cookie", cookie);
		return new HttpEntity<String>(header);
	}

	@Override
	public String validateCookie(String cookie) {
		if (StringUtils.isBlank(cookie)) {
			return this.createCookie();
		}
		return null;
	}

	@Override
	public String createCookie() {
		ResponseEntity<?> authResponse = this.authenticate();
		if (authResponse.getStatusCode().is2xxSuccessful()) {
			return authResponse.getHeaders().getFirst("Set-Cookie");
		}
		return null;
	}

	private ResponseEntity<?> authenticate() {
		log.info("Autenticando");
		return restTemplate.postForEntity(URI_SPTRANS + "Login/Autenticar?token=" + TOKEN, null, String.class);
	}

}
