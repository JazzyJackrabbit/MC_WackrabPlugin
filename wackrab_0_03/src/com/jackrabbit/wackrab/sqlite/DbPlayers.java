package com.jackrabbit.wackrab.sqlite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DbPlayers {

	public DbDataPlayer newDbDataPlayer(
			String name, 
			int allowedSubWorld, 
			float lastX, 
			float lastY, 
			float lastZ, 
			String lastWorld 
			) {
		DbDataPlayer d = new DbDataPlayer();
		d.name = name; 
		d.allowedSubWorld = allowedSubWorld; 
		d.lastX = lastX; 
		d.lastY = lastY;
		d.lastZ = lastZ;
		d.lastWorld = lastWorld;
		return d;
	}
	
	
	public void insert(Db db, String sqlitepath, DbDataPlayer d, boolean ignoreOrReplace) {
		//synchronized(main) 

		Connection connection = null;
		try {
			db.lock.lock();

		    // Section critique
		    // Votre code ici
		
			
	
	        try {
	            // Charge le pilote SQLite
	            Class.forName("org.sqlite.JDBC");
	
	            // Établit la connexion à la base de données
	            String url = "jdbc:sqlite:" + sqlitepath;//main.pathDB_SQLite;
	            connection = DriverManager.getConnection(url);
	
	            // code here:
	            
	            String insertQuery = 
	            		"INSERT INTO Players (name, allowedSubWorld, lastX, lastY, lastZ, lastWorld) "+ 
	            		"VALUES (?, ?, ?, ?, ?, ?);";
	
	            if(ignoreOrReplace) {
	            	
	            	insertQuery = 
            				"UPDATE Players SET name = ? , allowedSubWorld = ? , lastX = ? , lastY = ? , lastZ = ? , lastWorld = ? "+ 
    	            		"WHERE name = ? ";
	
	            	/*String deleteQuery = "DELETE FROM Players WHERE name = ? ;";
	                PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
	                deleteStatement.setString(1, d.name);
	
	                int rowsDeleted = deleteStatement.executeUpdate();
	
	                if(rowsDeleted > 1)
	                	System.out.println("Most data removed from 'players db' than usual.");*/
	            }
	            	// insertQuery = 
	           // 		"UPDATE Players SET (name = ?, allowedSubWorld = ?, lastX = ?, lastY = ?, lastZ = ?, lastWorld = ?) ";//+ 
	           // 		//"VALUES (?, ?, ?, ?, ?, ?);";
	        
	        
	            
	            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
	            preparedStatement.setString(1, (d.name ) );
	            preparedStatement.setInt(2, (d.allowedSubWorld ) );
	            preparedStatement.setDouble(3, (d.lastX ));
	            preparedStatement.setDouble(4, (d.lastY ));
	            preparedStatement.setDouble(5, (d.lastZ ));
	            preparedStatement.setString(6, (d.lastWorld ) );
	            if(ignoreOrReplace)
	            	preparedStatement.setString(7, (d.name ) );
	
	            int rowsAffected = preparedStatement.executeUpdate();
	/*
	            if (rowsAffected > 0) {
	                System.out.println("L'insertion a réussi !");
	            } else {
	                System.out.println("Le pseudo existe déjà. Aucune insertion n'a été effectuée.");
	            }*/
	            
	            // #### ####:
	            
	        } catch (ClassNotFoundException | SQLException e) {
	            e.printStackTrace();
	            System.out.println("DbPlayers Error on Insert. "+e.toString());
	        } 
	        
	        
		} finally {
			db.lock.unlock();
			try {
	            if (connection != null && !connection.isClosed()) {
	                connection.close();
	            }
	        } catch (SQLException e) {
	            System.out.println("DbPlayers Disconnection Error on Insert. "+e.toString());
	            e.printStackTrace();
	        }
		}
		
	}
	

	public DbDataPlayer select(Db db, String sqlitepath, String playername) {

        DbDataPlayer informativePlayerRelation = null;

		Connection connection = null;
		try {
			db.lock.lock();

		
			//return null;
			//synchronized(main) 
			
	
	        try {
	        	
	            // Charge le pilote SQLite
	            Class.forName("org.sqlite.JDBC");
	            
	            // Établit la connexion à la base de données
	            String url = "jdbc:sqlite:" + sqlitepath;
	            connection = DriverManager.getConnection(url);
	
	            // code here:
	            
	            String selectQuery = "SELECT * FROM Players WHERE name = ? ;";
	            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
	            preparedStatement.setString(1, playername);
	
	            ResultSet resultSet = preparedStatement.executeQuery();
	
	            //name, isClaimable, isClaim, playerOwner, isPvpEnable, allowedPlayers
	            
	            while (resultSet.next()) {
	                informativePlayerRelation = new DbDataPlayer();
	
	                //name, allowedSubWorld, lastX, lastY, lastZ, lastWorld
	            	informativePlayerRelation.name =resultSet.getString( "name" );
	            	informativePlayerRelation.allowedSubWorld = resultSet.getInt( "allowedSubWorld" );
	            	informativePlayerRelation.lastX = resultSet.getFloat( "lastX" );
	            	informativePlayerRelation.lastY =resultSet.getFloat( "lastY" );
	            	informativePlayerRelation.lastZ =resultSet.getFloat( "lastZ" );
	            	informativePlayerRelation.lastWorld = resultSet.getString( "lastWorld" );
	
	                
	                // ..
	            }
	            
	            // #### ####:
	            
	        } catch (ClassNotFoundException | SQLException e) {
	            e.printStackTrace();
	            
	            System.out.println("DbPlayers Error on Select. "+e.toString());
	        } 
	
	        
	
		} finally {
			db.lock.unlock();
			try {
	            if (connection != null && !connection.isClosed()) {
	                connection.close();
	
	            }
	            
	        } catch (SQLException e) {
	            System.out.println("DbPlayers Disconnection Error on Select. "+e.toString());
	            e.printStackTrace();
	        }
		}
        return informativePlayerRelation;
		
       // if(informativePlayerRelation != null)
        //	System.out.println(informativePlayerRelation.lastY);
        

	}
}
