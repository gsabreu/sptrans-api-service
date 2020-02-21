package br.com.abreu.sptrans.api.business.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import br.com.abreu.sptrans.api.business.BusInformartionBusiness;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BusInformartionBusinessImpl implements BusInformartionBusiness {

	@Value("${sptrans-uri}")
	private String URI_SPTRANS;

	@Value("${sptrans-token}")
	private String TOKEN;

	private String cookie = "";

	private final RestTemplate restTemplate;

	public BusInformartionBusinessImpl(RestTemplateBuilder restTemplateBuilder) {
		this.restTemplate = restTemplateBuilder.build();
	}

	@Override
	public String getBusLine(String termoBusca) {
		this.validateCookie();
		String response = "";
		try {
			response = restTemplate.exchange(URI_SPTRANS + "Linha/Buscar?termosBusca=" + termoBusca, HttpMethod.GET,
					this.createAuthHeader(this.cookie), String.class).getBody();

		} catch (HttpClientErrorException e) {
			this.createCookie();
			return restTemplate.exchange(URI_SPTRANS + "Linha/Buscar?termosBusca=" + termoBusca, HttpMethod.GET,
					this.createAuthHeader(this.cookie), String.class).getBody();

		}
		return response;

	}

	@Override
	public String getBusPosition(String line) {
//		if (restTemplate.getForEntity(URI_SPTRANS + "Posicao", String.class).getStatusCode()
//				.equals(HttpStatus.BAD_REQUEST)) {
//			if (this.authenticate()) {
//				return restTemplate.getForEntity(URI_SPTRANS + "Posicao", String.class).getBody();
//			}
//		}
		return "Ainda não está autenticado";
	}

	private ResponseEntity<?> authenticate() {
		log.info("Autenticando");
		return restTemplate.postForEntity(
				URI_SPTRANS + "Login/Autenticar?token="+ TOKEN,
				null, String.class);
	}

	private HttpEntity<String> createAuthHeader(String cookie) {
		HttpHeaders header = new HttpHeaders();
		header.add("Cookie", cookie);
		return new HttpEntity<String>(header);
	}

	private void validateCookie() {
		if (StringUtils.isBlank(this.cookie)) {
			this.createCookie();
		}
	}

	private void createCookie() {
		ResponseEntity<?> authResponse = this.authenticate();
		if (authResponse.getStatusCode().is2xxSuccessful()) {
			this.cookie = authResponse.getHeaders().getFirst("Set-Cookie");
		}
	}

}