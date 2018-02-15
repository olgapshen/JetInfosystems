/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jet.controllers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.jet.db.DBHelper;
import ru.jet.models.Contract;

/**
 *
 * @author Olga Pshenichnikova <o.pshenichnikova@be-interactive.ru>
 */
@RestController
public class ContractController 
{
	private final AtomicInteger contractCounter;
	private final DBHelper helper;
	
	public ContractController() 
	{
		helper = DBHelper.getInstance();
		contractCounter = new AtomicInteger(helper.getLastContractId());
	}
	
	@GetMapping("/contract/list")
	@CrossOrigin(origins = "*")
    public List<Contract> getContracts() 
	{
		return helper.getContracts();
	}
	
	@PostMapping("/contract")
	@CrossOrigin(origins = "*")
    public Contract createContract() 
	{		
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yy");
		Date date = new Date();
		int id = contractCounter.incrementAndGet();
		String name = String.format("%02d от %s", id, df.format(date));
		
		return helper.addContract(name);
	}
}
