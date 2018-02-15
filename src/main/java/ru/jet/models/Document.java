/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jet.models;

/**
 *
 * @author Olga Pshenichnikova <o.pshenichnikova@be-interactive.ru>
 */
public class Document 
{
	private final String filename;
	
	public Document(String filename) 
	{
		this.filename = filename;
	}
	
	public String getFileName()
	{
		return filename;
	}
}
