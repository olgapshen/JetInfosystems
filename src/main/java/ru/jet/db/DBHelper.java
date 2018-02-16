package ru.jet.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.jet.constants.Constants;
import ru.jet.exceptions.JetSqlException;
import ru.jet.models.Contract;
import ru.jet.models.Document;

public class DBHelper 
{
    private final Connection connection;
	private static volatile DBHelper instance;
	
	private final Logger log = 
		LogManager.getLogger(DBHelper.class.getName());
	
	public static DBHelper getInstance() 
		throws JetSqlException
	{
		DBHelper localInstance = instance;
		
		if (localInstance == null) 
		{
			synchronized (DBHelper.class) 
			{
				localInstance = instance;
				
				if (localInstance == null)
				{
					instance = localInstance = new DBHelper();
				}
			}
		}
		
		return localInstance;
	}
	
    private DBHelper()
	{		
        try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			throw new JetSqlException(e);
		}
		
		try {
			connection = 
				DriverManager.getConnection(String.format(
					"jdbc:sqlite:%s/jetinfosystems.s3db",
					Constants.DATA_FOLDER
				));
			
			Statement statmt = connection.createStatement();
			connection.setAutoCommit(false);

			statmt.execute(
				"CREATE TABLE if not exists `documents` (" +
					"`id` INTEGER PRIMARY KEY AUTOINCREMENT, " +
					"`filename` TEXT NOT NULL" +
				")");

			statmt.execute(
				"CREATE TABLE if not exists `contracts` (" +
					"`id` INTEGER PRIMARY KEY AUTOINCREMENT, " +
					"`name` TEXT NOT NULL, " +
					"`document_id` INTEGER NULL, " +
					"FOREIGN KEY(document_id) REFERENCES documents(id)" +
				")");
		} catch (SQLException e) {
			log.error(e);
			throw new JetSqlException(e);
		}
    }

    public void close()
	{
        if (instance != null) 
		{
            instance.closeConnection();
        }
    }

    private void closeConnection()
	{
		try {
			if (connection != null && !connection.isClosed()) 
			{
				connection.close();
			}
		} catch (SQLException e) {
			log.error(e);
			throw new JetSqlException(e);
		}
    }

    public int getLastContractId()
	{
		try {
			PreparedStatement statmt = connection.prepareStatement(
				"SELECT max(id) as last FROM contracts"
			);

			ResultSet resSet = statmt.executeQuery();
			
			resSet.next();
			return resSet.getInt("last");
		} catch (SQLException e) {
			log.error(e);
			throw new JetSqlException(e);
		}
	}
	
	public Contract getContract(int id)
	{
		try {
			PreparedStatement statmt = connection.prepareStatement(
				"SELECT name, document_id " + 
				"FROM 'contracts' " +
				"WHERE `id` = ?"
			);
			
			statmt.setInt(1, id);

			ResultSet resSet = statmt.executeQuery();
			resSet.next();
			
			Contract contract = new Contract(
				id, 
				resSet.getString("name")
			);
			
			int documentId = resSet.getInt("document_id");
				
			if (documentId > 0)
			{
				contract.setDocument(documentId);
			}
			
			return contract;
		} catch (SQLException e) {
			log.error(e);
			throw new JetSqlException(e);
		}		
	}
	
	public Contract addContract(String name)
	{
		try {
			PreparedStatement statmt = connection.prepareStatement(
				"INSERT INTO 'contracts' ('name') VALUES (?)"
			);

			statmt.setString(1, name);
			statmt.execute();
			
			statmt = connection.prepareStatement(
				"SELECT last_insert_rowid() as last"
			);

			ResultSet resSet = statmt.executeQuery();
			int id = resSet.getInt("last");

			connection.commit();
			
			return new Contract(id, name);
		} catch (SQLException e) {
			log.error(e);
			throw new JetSqlException(e);
		}
    }
	
	public Document getDocument(int id)
	{
		try {
			PreparedStatement statmt = connection.prepareStatement(
				"SELECT filename FROM documents WHERE `id` = ?"
			);

			statmt.setInt(1, id);
			ResultSet resSet = statmt.executeQuery();
			
			resSet.next();
			String fileName = resSet.getString("filename");
			
			return new Document(id, fileName);
		} catch (SQLException e) {
			log.error(e);
			throw new JetSqlException(e);
		}
	}
	
	public Document addDocument(int contractId, String fileName) 
	{
        try {
			PreparedStatement statmt = connection.prepareStatement(
				"INSERT INTO 'documents' ('filename') VALUES (?)"
			);

			statmt.setString(1, fileName);
			statmt.execute();

			statmt = connection.prepareStatement(
				"SELECT last_insert_rowid()"
			);

			ResultSet resSet = statmt.executeQuery();
			resSet.next();
			int documentId = resSet.getInt(1);
			
			statmt = connection.prepareStatement(
				"UPDATE 'contracts' SET `document_id` = ? WHERE `id` = ?"
			);

			statmt.setInt(1, documentId);
			statmt.setInt(2, contractId);
			statmt.executeUpdate();

			connection.commit();
			
			return new Document(documentId, fileName);
		} catch (SQLException e) {
			log.error(e);
			throw new JetSqlException(e);
		}
    }

    public List<Contract> getContracts()
	{
        List<Contract> contracts = new LinkedList<>();
		
		try {
			PreparedStatement statmt = connection.prepareStatement(
				"SELECT id, name, document_id FROM 'contracts'"
			);

			ResultSet resSet = statmt.executeQuery();

			while (resSet.next()) {
				Contract contract = new Contract(
					resSet.getInt("id"), 
					resSet.getString("name")
				);
				
				int documentId = resSet.getInt("document_id");
				
				if (documentId > 0)
				{
					contract.setDocument(documentId);
				}
				
				contracts.add(contract);
			}
		} catch (SQLException e) {
			log.error(e);
			throw new JetSqlException(e);
		}
		
		return contracts;
    }
}
