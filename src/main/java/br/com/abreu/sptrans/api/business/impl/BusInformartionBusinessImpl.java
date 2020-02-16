package br.com.abreu.sptrans.api.business.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
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

	private final RestTemplate restTemplate;

	public BusInformartionBusinessImpl(RestTemplateBuilder restTemplateBuilder) {
		this.restTemplate = restTemplateBuilder.build();
	}

	@Override
	public String getBusLine(String termoBusca) {
		this.authenticate();
		return restTemplate.getForObject(URI_SPTRANS + "Linha/Buscar?termosBusca=8000", String.class);

	}

	private Boolean authenticate() {
		log.info("Autenticando");
		ResponseEntity<String> response = restTemplate.postForEntity(
				URI_SPTRANS + "Login/Autenticar?token=9dd7814d1c159b7df312a3a9022c0700903fdaeae3ca6a2350998cb93e25962f",
				null, String.class);

		if (response.getStatusCode().equals(HttpStatus.OK)) {
			return Boolean.valueOf(response.getBody());
		}
		return false;
	}

}