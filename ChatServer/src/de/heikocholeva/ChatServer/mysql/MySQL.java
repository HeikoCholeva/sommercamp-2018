package de.heikocholeva.ChatServer.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import de.heikocholeva.ChatServer.Main;
import de.heikocholeva.ChatServer.log.Log.LogLevel;

/**
 * @author Heiko Choleva
 * https://heikocholeva.de
 * @since 1st March 2018
 */

public class MySQL {
	
	String hostname, database, username, password, args;
	int port;
	Connection con;
	
	/**
	 * 
	 * @param hostname host-address of your MySQL server.
	 * @param port port of your MySQL server.
	 * @param database database which will be used.
	 * @param username username to login with.
	 * @param password password for the user.
	 */
	
	public MySQL(String hostname, int port, String database, String username, String password) {
		this.hostname = hostname;
		this.port = port;
		this.database = database;
		this.username = username;
		this.password = password;
		this.args = "?autoReconnect=true&useUnicode=true&characterEncoding=utf8&useSSL=false";
		Main.getLogger().write(LogLevel.DEBUG, "MySQL was initialized.");
	}
	
	/**
	 * 
	 * @param hostname host-address of your MySQL server.
	 * @param port port of your MySQL server.
	 * @param database database which will be used.
	 * @param username username to login with.
	 * @param password password for the user.
	 * @param args additional arguments (like '?autoReconnect=true'; default: '?autoReconnect=true&useUnicode=true&characterEncoding=utf8&useSSL=false')
	 */
	
	public MySQL(String hostname, int port, String database, String username, String password, String args) {
		this.hostname = hostname;
		this.port = port;
		this.database = database;
		this.username = username;
		this.password = password;
		this.args = args;
		Main.getLogger().write(LogLevel.DEBUG, "MySQL was initialized.");
	}
	
	/**
	 * 
	 * @return true if connection was successfully created; false if there was an error or it's already connected.
	 */
	
	public boolean connect() {
		Main.getLogger().write(LogLevel.DEBUG, "MySQL > Trying to connect to database server.");
		if(!isConnected()) {
			try {
				con = DriverManager.getConnection("jdbc:mysql://" + hostname + ":" + port + "/" + database + args, username, password);
				Main.getLogger().write(LogLevel.DEBUG, "MySQL > Successfully connected to database server.");
				return true;
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
		Main.getLogger().write(LogLevel.DEBUG, "MySQL > Could not connect to database server!");
		return false;
	}
	
	/**
	 * 
	 * @return true if connection was successfully removed; false if there was an error or it wasn't connected.
	 */
	
	public boolean disconnect() {
		Main.getLogger().write(LogLevel.DEBUG, "MySQL > Trying to disconnect from database server.");
		if(isConnected()) {
			try {
				con.close();
				Main.getLogger().write(LogLevel.DEBUG, "MySQL > Successfully disconnected from database server.");
				return true;
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
		Main.getLogger().write(LogLevel.DEBUG, "MySQL > Could not disconnect from database server!");
		return false;
	}
	
	/**
	 * 
	 * @return if MySQL is connected to database or not.
	 */
	
	public boolean isConnected() {
		return(con == null ? false : true);
	}
	
	/**
	 * 
	 * @return connection if established.
	 */
	
	public Connection getConnection() {
		return con;
	}
	
	/**
	 * 
	 * @param statement your MySQL statement.
	 * @return true if successfully executed; false if there was an error.
	 */
	
	public boolean update(String statement) {
		Main.getLogger().write(LogLevel.DEBUG, "MySQL > Executing update \"" + statement + "\"");
		try {
			PreparedStatement ps = getConnection().prepareStatement(statement);
			ps.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 
	 * @param statement your MySQL statement.
	 * @return true if successfully executed; false if there was an error
	 */
	
	public boolean update(PreparedStatement statement) {
		Main.getLogger().write(LogLevel.DEBUG, "MySQL > Executing update \"" + statement.toString() + "\"");
		try {
			statement.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 
	 * @param statement your MySQL statement.
	 * @return ResultSet with requested results.
	 */
	
	public ResultSet query(String statement) {
		Main.getLogger().write(LogLevel.DEBUG, "MySQL > Executing query \"" + statement + "\"");
		try {
			PreparedStatement ps = getConnection().prepareStatement(statement);
			return ps.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		return null;
	}
	
	/**
	 * 
	 * @param statement your MySQL statement.
	 * @return ResultSet with requested results.
	 */
	
	public ResultSet query(PreparedStatement statement) {
		Main.getLogger().write(LogLevel.DEBUG, "MySQL > Executing query \"" + statement.toString() + "\"");
		try {
			return statement.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		return null;
	}
}
