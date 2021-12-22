package com.wildcodeschool.wildandwizard.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.wildcodeschool.wildandwizard.entity.School;

import util.JdbcUtils;

public class SchoolRepository {

    private final static String DB_URL = "jdbc:mysql://localhost:3306/spring_jdbc_quest?serverTimezone=GMT";
    private final static String DB_USER = "h4rryp0tt3r";
    private final static String DB_PASSWORD = "Horcrux4life!";

    public School save(String name, Long capacity, String country) {

    	 Connection connection = null;
         PreparedStatement statement = null;
         ResultSet generatedKeys = null;
         
         try {
			connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
			//return_generated_keys = retourne l'id de la nouvelle école créée
			statement = connection.prepareStatement("INSERT INTO school (name, capacity, country) VALUES (?, ?, ?)",Statement.RETURN_GENERATED_KEYS);
			//Set des différents valeurs de la requête
			statement.setString(1, name);
			statement.setLong(2, capacity);
			statement.setString(3, country);
			
			//executeUpdate = nombre de tuples créés. Si ce n'est pas égale à 1, c'est qu'il y a eu un problème dans l'insert
			if(statement.executeUpdate() !=1 ) {
				throw new SQLException("Failed to insert date");
			}
			
			//Récupération de l'id de la nouvelle école
			generatedKeys = statement.getGeneratedKeys();
			
			//Si on a bien un id, alors on instancie une nouvelle école
			if(generatedKeys.next()) {
				Long id = generatedKeys.getLong(1);
				return new School(id, name, capacity, country);
			} else {
				throw new SQLException("Failed to get instead id");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
            JdbcUtils.closeResultSet(generatedKeys);
            JdbcUtils.closeStatement(statement);
            JdbcUtils.closeConnection(connection);
        }
        
        return null;
    }
}
