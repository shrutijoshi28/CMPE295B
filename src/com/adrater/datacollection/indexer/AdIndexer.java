package com.adrater.datacollection.indexer;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.util.DateUtil;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ext.JodaSerializers.DateTimeSerializer;

import com.adrater.datacollection.vo.AdVO;
import com.adrater.util.ToolPropertiesUtil;

/**
 * This class provides methods to add and retrieve AdVO objects to be indexed
 * into SOLR
 * 
 * @author Suraj Shetty
 * 
 */

public class AdIndexer {

	private static final String HOST = ToolPropertiesUtil
			.getProperty("SOLR_HOST");
	private HttpSolrServer server;

	public AdIndexer() {

		server = new HttpSolrServer(HOST);
	}

	private void add(AdVO adVO) {

		SolrInputDocument inputDocument = new SolrInputDocument();
		inputDocument.addField("id", adVO.getId());
		inputDocument.addField("adHeader_txt", adVO.getAdHeader());
		inputDocument.addField("url", adVO.getAdLink());
		inputDocument.addField("adDetails_txt", adVO.getAdDetails());
		inputDocument.addField("postDate_dt", toSolrDateFormat(adVO.getPostDate()));
		inputDocument.addField("category_s", adVO.getSubCategory().getCategory());
		inputDocument.addField("area_txt", adVO.getLocation().getInfo());
		
		 try {
			server.add(inputDocument);
			server.commit();

		} catch (SolrServerException | IOException e) {
			System.err.println("Error adding new entry" + e.getMessage());
		} 
		
	}

	/**
	 * Converts the date to solr date format. The format of the input must be
	 * yyyy-MM-dd'T'HH:mm:ss.SSSZ and the output would be in the format 
	 * yyyy-MM-dd'T'HH:mm:sss'Z'
	 * @param date
	 * @return
	 */
	private String toSolrDateFormat(String date) {
		System.out.println(date);
		// change the format of the date into the required format
		SimpleDateFormat inFormat = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ssZ");
		SimpleDateFormat outFormat = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss'Z'");

		try {
			Date postDate = inFormat.parse(date);
			return outFormat.format(postDate);

		} catch (ParseException e) {
			System.err.println("Unable to parse the date" + e.getMessage());
			return null;
		}

	}
	
	public static void main(String[] args) {
		
		File dir = new File("output");
		ObjectMapper objMapper = new ObjectMapper();
		AdIndexer indexer = new AdIndexer();
		
				
		for(int i=0;i < 10 || i< dir.listFiles().length ; i++ ){
		   File file = dir.listFiles()[i];
			try {
				AdVO advo = objMapper.readValue(file, AdVO.class);
				indexer.add(advo);
			} catch ( IOException e) {
				System.err.println("Error parsing the JSON to object");
			}
		}
		
	}

}
