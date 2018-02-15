/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jet.exceptions;

import java.sql.SQLException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author Olga Pshenichnikova <o.pshenichnikova@be-interactive.ru>
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class JetSqlException extends RuntimeException
{
	public JetSqlException(SQLException e) 
	{
		super("SQL Exception", e);
	}
	
	public JetSqlException(ClassNotFoundException e) 
	{
		super("Sqlite not found", e);
	}	
}
