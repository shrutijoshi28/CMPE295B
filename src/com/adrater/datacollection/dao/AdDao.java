package com.adrater.datacollection.dao;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.codehaus.jackson.map.ObjectMapper;

import com.adrater.datacollection.vo.AdVO;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.util.JSON;

/**
 * This class is provides the data access layer for the ad details
 * @author Suraj Shetty
 *
 */
public class AdDao {

	
	private static final String HOST = "ds033018.mongolab.com";//"localhost";
	private static final int PORT = 33018;//27017;
	private static final String DB_NAME = "craigslist";
	private static final String AD_COLLECTION_NAME = "ad";
	private static final String USERNAME ="cmpe295b";
	private static final String PASSWORD = "cmpe295b";
	
	private DB db;
	
	
	public AdDao() throws UnknownHostException{
		
			
		MongoClient client = new MongoClient(HOST, PORT);
		
		db = client.getDB(DB_NAME);
		db.authenticateCommand(USERNAME, PASSWORD.toCharArray());
		
	}
	/**
	 * This method inserts an ad which is in form of a JSON string into the
	 * collection
	 * @param adVo
	 */
	public void insertAd(String adVo){
		
		BasicDBObject dbObj = (BasicDBObject)JSON.parse(adVo);
		DBCollection adCollection =  db.getCollection(AD_COLLECTION_NAME);
		adCollection.insert(dbObj);
		
	}
	/**
	 * This method returns a list of all the ads. In case of any exception or
	 * if there are no items found in the collection, the method would return
	 * an empty list
	 * @return
	 */
	public List<AdVO> getAllAds(){
		
		DBCollection adCollection = db.getCollection(AD_COLLECTION_NAME);
		
		//set the fields  that are required
		BasicDBObject fields = new BasicDBObject().append("_id",false).append("id", true).append("adHeader", true).append("adLink", true)
				.append("adDetails", true).append("subCategory", true).append("location", true);
		
		
		DBCursor cursor = adCollection.find(null,fields);
					
		ObjectMapper mapper = new ObjectMapper();
		//list of all the ads
		List<AdVO> adList = new LinkedList<>();
		AdVO adVo=null;
		
		while(cursor.hasNext()){
			
			try {
				adVo = mapper.readValue(cursor.next().toString(), AdVO.class);
				adList.add(adVo);
				
			} catch (IOException e) {
				//do nothing. Just continue
				
			}
						
		}
		return adList;
	}
	
	private void createCollections(){
		
		db.createCollection("ad", new BasicDBObject());
		
	}
	
	private void  getAllCollections(){
		
		Set<String> collSet = db.getCollectionNames();
		for(String collection : collSet){
			System.out.println(collection);
		}
		
	}
	
	private void deleteAllItems(){
		
		DBCollection adCollection = db.getCollection(AD_COLLECTION_NAME);
		DBCursor cursor = adCollection.find();
		
		while(cursor.hasNext()){
			
				BasicDBObject obj =  (BasicDBObject) cursor.next();
				adCollection.remove(obj);
						
		}
	}
	
	public static void main(String[] args) throws UnknownHostException {
		AdDao dao = new AdDao();
	//	dao.getAllCollections();
		dao.deleteAllItems();
	}
	
}
