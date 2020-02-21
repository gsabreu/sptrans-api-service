package br.com.abreu.sptrans.api.business.impl;

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

	private final RestTemplate restTemplate;

	public BusInformartionBusinessImpl(RestTemplateBuilder restTemplateBuilder) {
		this.restTemplate = restTemplateBuilder.build();
	}

	@Override
	public String getBusLine(String termoBusca) {
		String response = "";
		try {
			response = restTemplate.getForEntity(URI_SPTRANS + "Linha/Buscar?termosBusca=" + termoBusca, String.class)
					.getBody();

		} catch (HttpClientErrorException e) {
			ResponseEntity<?> authResponse = this.authenticate();
			if (authResponse.getStatusCode().is2xxSuccessful()) {
				return restTemplate
						.exchange(URI_SPTRANS + "Linha/Buscar?termosBusca=" + termoBusca, HttpMethod.GET,
								this.createAuthHeader(authResponse.getHeaders().getFirst("Set-Cookie")), String.class)
						.getBody();

			}
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
				URI_SPTRANS + "Login/Autenticar?token=9dd7814d1c159b7df312a3a9022c0700903fdaeae3ca6a2350998cb93e25962f",
				null, String.class);
	}

	private HttpEntity<String> createAuthHeader(String cookie) {
		HttpHeaders header = new HttpHeaders();
		header.add("Cookie", cookie);
		return new HttpEntity<String>(header);
	}

}