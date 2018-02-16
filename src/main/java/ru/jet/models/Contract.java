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
public class Contract 
{
	private final int id;
	private final String name;
	private Integer documentId;

	public Contract(int id, String name) 
	{
		this.id = id;
		this.name = name;
		documentId = null;
	}

	public void setDocument(int documentId)
	{
		this.documentId = documentId;
	}
	
	public Integer getDocumentId()
	{
		return documentId;
	}
	
	public boolean documentAppended()
	{
		return documentId != null;
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
