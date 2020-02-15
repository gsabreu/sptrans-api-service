package br.com.abreu.sptrans.api.business.impl;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import br.com.abreu.sptrans.api.business.BusInformartionBusiness;

@Service
public class BusInformartionBusinessImpl implements BusInformartionBusiness {

	private final RestTemplate restTemplate;

	public BusInformartionBusinessImpl(RestTemplateBuilder restTemplateBuilder) {
		this.restTemplate = restTemplateBuilder.build();
	}

	@Override
	public String getBusLine(String termoBusca) {
		return restTemplate.getForObject("http://api.olhovivo.sptrans.com.br/v2.1/Linha/Buscar?termosBusca=8000",
				String.class);

	}

}