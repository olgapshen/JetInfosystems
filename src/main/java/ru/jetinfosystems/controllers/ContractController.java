/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jetinfosystems.controllers;

import java.util.LinkedList;
import java.util.List;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.jetinfosystems.models.Contract;

/**
 *
 * @author Olga Pshenichnikova <o.pshenichnikova@be-interactive.ru>
 */
@RestController
public class ContractController 
{
	@GetMapping("/contract/list")
	@CrossOrigin(origins = "*")
    public List<Contract> getContracts() 
	{
		List<Contract> contracts = new LinkedList<>();
		contracts.add(new Contract(0));
		contracts.add(new Contract(1));
		
		return contracts;
	}
	
	@PostMapping("/contract")
	@CrossOrigin(origins = "*")
    public Contract createContract() 
	{		
		return new Contract(1);
	}
}
