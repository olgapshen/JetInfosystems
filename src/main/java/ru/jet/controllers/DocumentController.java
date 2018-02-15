/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jet.controllers;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.jet.constants.Constants;
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
		
		String fileName = document.getFileName();
		Path out = Paths.get(Constants.DATA_FOLDER, fileName);
		File newFile = out.toFile();
		
		//newFile.getAbsolutePath().toString();
		
		try {
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(newFile));
			BufferedInputStream bis = new BufferedInputStream(file.getInputStream());
			
			byte[] buffer = new byte[8 * 1024];
			int bytesRead;
			while ((bytesRead = bis.read(buffer)) != -1) {
				bos.write(buffer, 0, bytesRead);
			}
			
			bos.flush();
		} catch (IOException e) {
			throw new JetSaveFileException(e);
		}
		
		helper.addDocument(id, document);
		
		return document;
	}
}
