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
				DriverManager.getConnection("jdbc:sqlite:jetinfosystems.s3db");
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
					"`document_id` TEXT NULL, " +
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

    public int getLastContractId() throws JetSqlException
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
	
	public Contract addContract(String name) throws JetSqlException
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
	
	public void addDocument(int contractId, Document document) 
	{
        try {
			PreparedStatement statmt = connection.prepareStatement(
				"INSERT INTO 'documents' ('filename') VALUES (?)"
			);

			statmt.setString(1, document.getFileName());
			statmt.execute();

			statmt = connection.prepareStatement(
				"SELECT last_insert_rowid() as last"
			);

			ResultSet resSet = statmt.executeQuery();
			int documentId = resSet.getInt("last");

			statmt = connection.prepareStatement(
				"UPDATE 'contracts' set ('document_id' = ?) where 'id' = ?"
			);

			statmt.setInt(1, documentId);
			statmt.setInt(1, contractId);
			statmt.executeUpdate();

			connection.commit();
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
				"SELECT id, name FROM 'contracts'"
			);

			ResultSet resSet = statmt.executeQuery();

			while (resSet.next()) {
				Contract contract = new Contract(
					resSet.getInt("id"), 
					resSet.getString("name")
				);
				
				contracts.add(contract);
			}
		} catch (SQLException e) {
			log.error(e);
			throw new JetSqlException(e);
		}
		
		return contracts;
    }
}
