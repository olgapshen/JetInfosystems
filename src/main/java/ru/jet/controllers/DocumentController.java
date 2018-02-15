/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jet.controllers;

import java.io.File;
import java.io.IOException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.jet.db.DBHelper;
import ru.jet.exceptions.JetSaveFileException;
import ru.jet.models.Document;

/**
 *
 * @author Olga Pshenichnikova <o.pshenichnikova@be-interactive.ru>
 */
@RestController
public class DocumentController 
{
	private final DBHelper helper;
	
	public DocumentController() 
	{
		helper = DBHelper.getInstance();
	}
	
	@PutMapping("/document")
	@CrossOrigin(origins = "*")
    public Document uploadContract(
		@RequestParam("file") MultipartFile file,
		@RequestParam(value="id") int id
	) {		
		Document document = 
			new Document(file.getOriginalFilename());
		
		File newFile = new File(document.getFileName());
		
		//newFile.getAbsolutePath().toString();
		
		try {
			file.transferTo(newFile);
		} catch (IOException e) {
			throw new JetSaveFileException(e);
		}
		
		document = new Document(newFile.getAbsolutePath());
		return document;
		
		//helper.addDocument(id, document);
	}
}
