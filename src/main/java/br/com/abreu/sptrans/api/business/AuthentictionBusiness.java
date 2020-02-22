package br.com.abreu.sptrans.api.business;

import org.springframework.http.HttpEntity;

public interface AuthentictionBusiness {

	public HttpEntity<String> createAuthHeader(String cookie);

	public String validateCookie(String cookie);

	public String createCookie();

}
