package com.adrater.datacollection.manager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;

import com.adrater.datacollection.LinkGetter;
import com.adrater.datacollection.dao.AdDao;
import com.adrater.datacollection.vo.AdVO;

public class AdExtractionManager {
	
	
	public void  extractAndLoadAds(String baseURL){
		
		//get all the Ad details of all the ads starting from the base URL
		LinkGetter linkGtter = new LinkGetter(baseURL);
		try {
			List<AdVO> adDetails = linkGtter.extractLinkDetails();
			
			//store the data into the database
			AdDao adDao = new AdDao();
			for(AdVO adVo : adDetails){
				adDao.insertAd(adVo.toString());
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
	}
	
	public void loadFromFile(String srcDir) throws UnknownHostException{
		
		File dir = new File(srcDir);
		AdDao dao = new AdDao();
		for(File file : dir.listFiles()){
			BufferedReader reader;
			try {
				reader = new BufferedReader(new FileReader(file));
			
			String data = null;
			if((data =reader.readLine())!= null)
				dao.insertAd(data);
			
			reader.close();
			
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				
		}
		
	}
	
	public static void main(String[] args) throws UnknownHostException {
		AdExtractionManager adMgr = new AdExtractionManager();
	//	adMgr.extractAndLoadAds("http://sfbay.craigslist.org/bbb/");
		adMgr.loadFromFile("output");
	}
}
