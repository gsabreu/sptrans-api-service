package br.com.abreu.sptrans.api.business.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import br.com.abreu.sptrans.api.business.AuthentictionBusiness;
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

	@Autowired
	private AuthentictionBusiness authentictionBusiness;

	public BusInformartionBusinessImpl(RestTemplateBuilder restTemplateBuilder) {
		this.restTemplate = restTemplateBuilder.build();
	}

	@Override
	public String getBusLine(String termoBusca) {
		log.info("Buscando informações da linha");
		this.cookie = authentictionBusiness.validateCookie(this.cookie);
		String response = "";
		try {
			response = restTemplate.exchange(URI_SPTRANS + "Linha/Buscar?termosBusca=" + termoBusca, HttpMethod.GET,
					authentictionBusiness.createAuthHeader(this.cookie), String.class).getBody();

		} catch (HttpClientErrorException e) {
			this.cookie = authentictionBusiness.createCookie();
			return restTemplate.exchange(URI_SPTRANS + "Linha/Buscar?termosBusca=" + termoBusca, HttpMethod.GET,
					authentictionBusiness.createAuthHeader(this.cookie), String.class).getBody();

		}
		return response;

	}

	@Override
	public String getBusPosition(String line) {
		log.info("Buscando informações da posição do onibus");
//		if (restTemplate.getForEntity(URI_SPTRANS + "Posicao", String.class).getStatusCode()
//				.equals(HttpStatus.BAD_REQUEST)) {
//			if (this.authenticate()) {
//				return restTemplate.getForEntity(URI_SPTRANS + "Posicao", String.class).getBody();
//			}
//		}
		return "Ainda não está autenticado";
	}

}