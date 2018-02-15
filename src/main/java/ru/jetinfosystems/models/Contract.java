/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jetinfosystems.models;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Olga Pshenichnikova <o.pshenichnikova@be-interactive.ru>
 */
public class Contract 
{
	private final int id;
	private final String name;

	public Contract(int number) 
	{
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yy");
		Date date = new Date();
		id = number;
		name = String.format("%02d от %s", number, df.format(date));
	}
	
	public int getId()
	{
		return id;
	}
	
	public String getName()
	{
		return name;
	}
}
