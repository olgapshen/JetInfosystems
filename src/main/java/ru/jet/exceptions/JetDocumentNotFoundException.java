/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jet.exceptions;

import java.io.IOException;

/**
 *
 * @author Olga Pshenichnikova <o.pshenichnikova@be-interactive.ru>
 */
public class JetDocumentNotFoundException extends RuntimeException
{
	public JetDocumentNotFoundException(IOException e) 
	{
		super("Document not found on the server", e);
	}
}