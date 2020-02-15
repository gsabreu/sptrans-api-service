package br.com.abreu.sptrans.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.abreu.sptrans.api.business.BusInformartionBusiness;

@RestController
@RequestMapping(value = "/bus")
public class BusInformationController {

	@Autowired
	private BusInformartionBusiness busInformartionBusiness;

	@GetMapping
	public ResponseEntity<?> getBusLine(@RequestParam(required = true, name = "termosBusca") String termoBusca) {
		return ResponseEntity.ok(busInformartionBusiness.getBusLine(termoBusca));
	}

}