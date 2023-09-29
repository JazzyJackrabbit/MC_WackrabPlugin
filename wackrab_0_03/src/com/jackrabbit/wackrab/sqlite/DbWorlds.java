package com.jackrabbit.wackrab.sqlite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.jackrabbit.wackrab.Main;

public class DbWorlds {
	
	public DbDataWorld newDbDataWorld(
			String _worldName, 
			int _isclaimable, 
			int _isClaim, 
			String _playerOwner, 
			int _ispvpenable,
			String _allowedPlayersEnterOn,
			int _isAllowedAccess
			) {
		DbDataWorld d = new DbDataWorld();
		d.worldName = _worldName; 
		d.isclaimable = _isclaimable; 
		d.isClaim = _isClaim; 
		d.playerOwner = _playerOwner;
		d.ispvpenable = _ispvpenable;
		d.allowedPlayersEnterOn = _allowedPlayersEnterOn;
		d.isAllowedAccess = _isAllowedAccess;
		return d;
	}


	public void insert(Db db, Main main, DbDataWorld d, boolean ignoreOrReplace) {

		Connection connection = null;
        try {
        	
        	db.lock.lock();
		

	        try {
	        	

	            // Charge le pilote SQLite
	            Class.forName("org.sqlite.JDBC");
	
	            // Établit la connexion à la base de données
	            String url = "jdbc:sqlite:" + main.pathDB_SQLite;
	            connection = DriverManager.getConnection(url);
	
	            // code here:
	           
	            String insertQuery = 
	            		"INSERT INTO Worlds (name, isClaimable, isClaim, playerOwner, isPvpEnable, allowedPlayers, isAllowedAccess) "+ 
	            		"VALUES (?, ?, ?, ?, ?, ?, ?);";
 
            	if(ignoreOrReplace) {

            		/*
            		 *  UPDATE nom_de_la_table
						SET nom_de_la_colonne = nouvelle_valeur
						WHERE condition;
            		 * */
            		/*
                	String deleteQuery = "DELETE FROM Worlds WHERE name = ? ;";
                    PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
                    deleteStatement.setString(1, d.worldName);

                    int rowsDeleted = deleteStatement.executeUpdate();

                    if(rowsDeleted > 1)
                    	System.out.println("Most data removed from 'worlds db' than usual.");*/
            		
            		insertQuery = 
            				"UPDATE Worlds SET name = ? , isClaimable = ? , isClaim = ? , playerOwner = ? , isPvpEnable = ? , allowedPlayers = ? , isAllowedAccess = ? "+ 
    	            		"WHERE name = ? ";
                }
	            //insertQuery = 
	           // 		"UPDATE Worlds SET (name = ?, isClaimable = ?, isClaim = ?, playerOwner = ?, isPvpEnable = ?, allowedPlayers = ?) ";//+ 
	            //		//"VALUES (?, ?, ?, ?, ?, ?);";
	        
	            
            	
	            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
	            preparedStatement.setString(1, d.worldName );
	            preparedStatement.setInt(2, d.isclaimable );
	            preparedStatement.setInt(3, d.isClaim );
	            preparedStatement.setString(4, d.playerOwner );
	            preparedStatement.setInt(5, d.ispvpenable );
	            preparedStatement.setString(6, d.allowedPlayersEnterOn );
	            preparedStatement.setInt(7, d.isAllowedAccess );
	            
	            if(ignoreOrReplace)
	            	preparedStatement.setString(8, d.worldName );
	
	            int rowsAffected = preparedStatement.executeUpdate();


	            if (rowsAffected > 0) {
	                System.out.println("ok");
	            } else {
	                System.out.println("already exist");
	            }
	            
	            // #### ####:
	            
	        } catch (Exception e) { //  ( ClassNotFoundException | SQLException e )
	            e.printStackTrace();
	            System.out.println("DbWorld Error on Insert. "+e.toString());
	        } 


		} finally {
			db.lock.unlock();
	        try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
		}
	}
	

	public DbDataWorld select(Db db, Main main, String worldname) {

		Connection connection = null;

        try {
        	db.lock.lock();
		
	        try {
	            // Charge le pilote SQLite
	            Class.forName("org.sqlite.JDBC");
	
	            // Établit la connexion à la base de données
	            String url = "jdbc:sqlite:" + main.pathDB_SQLite;
	            connection = DriverManager.getConnection(url);
	
	            // code here:
	            
	            String selectQuery = "SELECT * FROM Worlds WHERE name = ? ;";
	            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
	            preparedStatement.setString(1, worldname);
	
	            ResultSet resultSet = preparedStatement.executeQuery();
	
	            //name, isClaimable, isClaim, playerOwner, isPvpEnable, allowedPlayers
	            
	            while (resultSet.next()) {
	                String wn = resultSet.getString("name");
	                String playerowner = resultSet.getString("playerOwner");
	                String allowedPlayers = resultSet.getString("allowedPlayers");
	                int isPvpEnable = resultSet.getInt("isPvpEnable");
	                int isClaim = resultSet.getInt("isClaim");
	                int isClaimable = resultSet.getInt("isClaimable");
	                int isAllowedAccess = resultSet.getInt("isAllowedAccess");
	
	                
	                // just for currents informations (bad code):
	                
	                DbDataWorld informativeWorldRelation = new DbDataWorld();
	                
	                informativeWorldRelation.worldName = wn;
	                informativeWorldRelation.allowedPlayersEnterOn = allowedPlayers;
	                informativeWorldRelation.ispvpenable = isPvpEnable;
	                informativeWorldRelation.isClaim = isClaim;
	                informativeWorldRelation.isclaimable = isClaimable;
	                informativeWorldRelation.playerOwner = playerowner;
	                informativeWorldRelation.isAllowedAccess = isAllowedAccess;

	                return informativeWorldRelation;
	                
	                // ..
	            }
	
	            
	            // #### ####:
	            
	        } catch (ClassNotFoundException | SQLException e) {
	            e.printStackTrace();
	            System.out.println("DbWorlds Error on Select. "+e.toString());
	        } 

	      
		} finally {
			db.lock.unlock();
			  try {
	                if (connection != null && !connection.isClosed()) {
	                    connection.close();
	                }
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
		}
	        return null;

	}
	
	
	
	
	
	
	
	
	
	
	public ArrayList<DbDataWorld> selectClaimableWorlds(Main main) {

		Connection connection = null;
		ArrayList<DbDataWorld> returns = new ArrayList<DbDataWorld>();
		
        try {
        	main.DTB.lock.lock();
		
	        try {
	            // Charge le pilote SQLite
	            Class.forName("org.sqlite.JDBC");
	
	            // Établit la connexion à la base de données
	            String url = "jdbc:sqlite:" + main.pathDB_SQLite;
	            connection = DriverManager.getConnection(url);
	
	            // code here:
	            
	            String selectQuery = "SELECT * FROM Worlds WHERE isClaimable = true AND isClaim = 0; ";
	            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
	
	            ResultSet resultSet = preparedStatement.executeQuery();
	
	            //name, isClaimable, isClaim, playerOwner, isPvpEnable, allowedPlayers
	            
	            
	            while (resultSet.next()) {
	                String wn = resultSet.getString("name");
	                String playerowner = resultSet.getString("playerOwner");
	                String allowedPlayers = resultSet.getString("allowedPlayers");
	                int isPvpEnable = resultSet.getInt("isPvpEnable");
	                int isClaim = resultSet.getInt("isClaim");
	                int isClaimable = resultSet.getInt("isClaimable");
	                int isAllowedAccess = resultSet.getInt("isAllowedAccess");
	
	                
	                // just for currents informations (bad code):
	                
	                DbDataWorld informativeWorldRelation = new DbDataWorld();
	                
	                informativeWorldRelation.worldName = wn;
	                informativeWorldRelation.allowedPlayersEnterOn = allowedPlayers;
	                informativeWorldRelation.ispvpenable = isPvpEnable;
	                informativeWorldRelation.isClaim = isClaim;
	                informativeWorldRelation.isclaimable = isClaimable;
	                informativeWorldRelation.playerOwner = playerowner;
	                informativeWorldRelation.isAllowedAccess = isAllowedAccess;

	                
	                returns.add(informativeWorldRelation);
	                
	                // ..
	            }
	
	            
	            // #### ####:
	            
	        } catch (ClassNotFoundException | SQLException e) {
	            e.printStackTrace();
	            System.out.println("DbWorlds Error on Select. "+e.toString());
	        } 

	      
		} finally {
			main.DTB.lock.unlock();
			  try {
	                if (connection != null && !connection.isClosed()) {
	                    connection.close();
	                }
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
		}
	        return returns;

	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
