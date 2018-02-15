/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jet.controllers;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.servlet.http.HttpServletResponse;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.jet.constants.Constants;
import ru.jet.db.DBHelper;
import ru.jet.exceptions.JetDocumentNotFoundException;
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
		String fileName = file.getOriginalFilename();
		Path out = Paths.get(Constants.DATA_FOLDER, fileName);
		File newFile = out.toFile();
		
		try {
			BufferedOutputStream bos = 
				new BufferedOutputStream(new FileOutputStream(newFile));
			BufferedInputStream bis = 
				new BufferedInputStream(file.getInputStream());
			
			byte[] buffer = new byte[8 * 1024];
			int bytesRead;
			while ((bytesRead = bis.read(buffer)) != -1) {
				bos.write(buffer, 0, bytesRead);
			}
			
			bos.flush();
		} catch (IOException e) {
			throw new JetSaveFileException(e);
		}
		
		return helper.addDocument(id, fileName);
	}
	
	@RequestMapping(
		value = "/document/{document_id}", 
		method = RequestMethod.GET
	)
	public void getFile(
		@PathVariable("document_id") int documentId, 
		HttpServletResponse response) 
	{
		
		Document document = helper.getDocument(documentId);
		
		try {
			Path in = Paths.get(Constants.DATA_FOLDER, document.getFileName());
			File docFile = in.toFile();
			
			String mimeType= 
				URLConnection.guessContentTypeFromName(docFile.getName());
			response.setContentType(mimeType);
			response.setHeader(
				"Content-Disposition", 
				"attachment; filename=\"" + docFile.getName() +"\""
			);
			
			response.setContentLength((int)docFile.length());
 
			InputStream inputStream = 
				new BufferedInputStream(new FileInputStream(docFile));
 
        
			FileCopyUtils.copy(inputStream, response.getOutputStream());
		} catch (IOException e) {
			throw new JetDocumentNotFoundException(e);
		}
	}
}
