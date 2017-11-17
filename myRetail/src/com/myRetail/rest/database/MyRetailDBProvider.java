package com.myRetail.rest.database;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

/**
 * 
 * @author Gundu This Class consists of methods to get the Mongo DB Connection
 *         and return collection instance
 */
public class MyRetailDBProvider {

	static MongoClient mongo;
	static DB database;

	public static DB getDB() {
		getClient();
		// Get the Database instance if present already, else creates a new
		// Database
		return mongo.getDB("myRetail");
	}

	/**
	 * Method to get the MongoClient
	 */
	private static void getClient() {
		mongo = new MongoClient("localhost", 27017);
	}

	/**
	 * Method to return the collection
	 * 
	 * @param collectionName
	 * @return
	 */
	public static DBCollection getCollection(String collectionName) {
		database = getDB();
		// Returns the collection if present already, else creates a new
		// collection
		return database.getCollection(collectionName);

	}

}
