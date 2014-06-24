package com.adrater.datacollection;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputField;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.adrater.datacollection.vo.AdVO;

public class Indexer {
	
	private static final String HOST = "http://localhost:8983/solr/";
	
	private HttpSolrServer server;
	
	public Indexer() {
			
		server = new HttpSolrServer(HOST);
		
				
	}
	
	public void getAllDocs() throws SolrServerException{
		//query to get all documents
		SolrQuery query = new SolrQuery("*:*");
		
		
		QueryResponse queryResponse = server.query(query); 
		SolrDocumentList docList = queryResponse.getResults();
		
		for(int i = 0; i < docList.size(); i++){
			System.out.println(docList.get(i));
		}
		
	}
	
	
	public void  deleteAllDocs(){
		
			
		try {
			UpdateResponse response = server.deleteByQuery("*:*");
			server.commit();
			System.out.println(response.getStatus());
			
		} catch (SolrServerException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	private void addDocuments(){
		//add the ad details for indexing
		
		File dir = new File("output");
		
		File [] jsonFiles = dir.listFiles();
		ObjectMapper mapper = new ObjectMapper();
		
		for(File adInfoFile : jsonFiles){
			
				try {
					AdVO adVo = mapper.readValue(adInfoFile, AdVO.class);
					System.out.println(adVo);
					server.addBean(adVo);
					
				} catch (IOException e) {
					System.err.println("Exception in parsing to object");
				} catch (SolrServerException e) {
					System.err.print("SOLR exception");
				}
		
				
		}
		
		try {
			server.commit();
		} catch (Exception e) {
			System.err.println("Error during commit");
		}
		
		//method 2: using the HTTPClient to add new documents
		/*
		DefaultHttpClient httpClient = null;
		HttpPost postReq = new HttpPost(HOST+"update/json");
		
		//get the JSON entities
		
		for(File adInfoFile : jsonFiles){
			
				try {
					AdVO adVo = mapper.readValue(adInfoFile, AdVO.class);
					
					httpClient = new DefaultHttpClient(); 
					StringEntity entity = new StringEntity(adVo.toString());
					entity.setContentType("application/json");
					postReq.setEntity(entity);
					HttpResponse response = httpClient.execute(postReq);
					System.out.println(response.toString());
					httpClient.close();
					
				} catch (IOException e) {
					System.err.println("Exception in parsing to object");
				}
				
		}
		*/
	}
	
	public void findDocument(){
		
		SolrQuery query = new SolrQuery();
		
		
		
	}
	
	

	
	public static void main(String[] args) throws SolrServerException {
		
	//	Indexer indexer = new Indexer();
	//	indexer.addDocuments();
	//	indexer.deleteAllDocs();
	//	indexer.getAllDocs();
	}

}
