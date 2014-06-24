package com.adrater.datacollection;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.adrater.datacollection.dao.AdDao;
import com.adrater.datacollection.vo.AdVO;
import com.adrater.datacollection.vo.CategoryVO;
import com.adrater.datacollection.vo.LocationVO;

public class LinkGetter {

	private static final long RESULT_FETCH_DELAY = 3000;
	private static final String OUTPUT_DIR = "output/";
	private static int RESULT_LIMIT = 3000;
	private static final int MAX_RETRY = 2;
	
	private String baseURL;
	private String lastResultURL;
	
	public LinkGetter(String baseURL) {

		this.baseURL = baseURL;
	}

	public List<AdVO> extractLinkDetails() {

		List<AdVO> adList = new LinkedList<AdVO>();
				
		Document doc = null;
		String nextPageUrl = this.baseURL;
		// get list of all the ads from all the search pages
		do {
			int tryCnt = 1;
			try {
				// get the url to next page
				doc = getDocument(nextPageUrl);
				nextPageUrl = getNextPageLink(doc);

				List<AdVO> currList = getAdList(doc);
				if (currList != null)
					adList.addAll(currList);

				// get the ad details
				for(int i = 0; i < currList.size() ; i++){
					try{
						getAdDetails(currList.get(i));
						tryCnt = 1;
					}catch(IOException e){
						if(tryCnt >= MAX_RETRY)
							continue;
						tryCnt++;
						i--;
					}
					
				}
				
				for (AdVO adVO : currList) {
					getAdDetails(adVO);
					saveAsJSON(adVO);
					Thread.currentThread().sleep(RESULT_FETCH_DELAY);
				}
				
				lastResultURL = nextPageUrl;

				Thread.currentThread().sleep(RESULT_FETCH_DELAY);
			} catch (InterruptedException e) {
				//break from the loop
				break;
			} catch(IOException e){
				continue;
			}

		} while (nextPageUrl != null && adList.size() < RESULT_LIMIT);
		
		return adList;
	}

	private void saveAsRecord(AdVO adVO) {

		AdDao dao = null;
		try {
			dao = new AdDao();
			dao.insertAd(adVO.toString());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * This method saves the ad details in JSON format in a file. One file is
	 * created for each Ad
	 * 
	 * @param advo
	 */
	private void saveAsJSON(AdVO advo) {

		ObjectMapper objMapper = new ObjectMapper();
		try {
			objMapper.writeValue(new File(OUTPUT_DIR + advo.getId() + ".json"),
					advo);
			// System.out.println(objMapper.defaultPrettyPrintingWriter().writeValueAsString(advo));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Gets the HTML document for a given URL
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 */

	private Document getDocument(String url) throws IOException {
		Document doc = Jsoup.connect(url).get();
		return doc;
	}

	/**
	 * Gets the url of the next page of the result set if available
	 * 
	 * @param document
	 */
	private String getNextPageLink(Document document) {
		String nextUrl = null;

		Elements elems = document.getElementsByAttributeValue("class",
				"button next");
		if (elems.size() > 1)
			nextUrl = elems.get(0).absUrl("href");

		return nextUrl;

	}

	/**
	 * Gets all the ads in a given results page
	 * 
	 * @param document
	 * @return
	 * @throws IOException
	 */
	private List<AdVO> getAdList(Document document) throws IOException {

		Document doc = document;
		Elements elems = doc.getElementsByTag("P");
		// list to contain all the ads
		List<AdVO> adList = new LinkedList<>();

		for (Element elem : elems) {
			Elements spanElems = elem.getElementsByTag("SPAN");
			// get the location
			String location = null;
			if (spanElems.size() > 5) {

				location = spanElems.get(3).text();

				if (location.contains(")"))
					location = location.substring(0, location.indexOf(')'));

			}

			// get the links
			// System.out.println("Links");
			Elements linkElems = elem.getElementsByTag("A");

			// get the sub-category and the ad links
			if (linkElems.size() > 2) {
				AdVO adVo = new AdVO(linkElems.get(1).text(), linkElems.get(1)
						.absUrl("href"));
				String id = linkElems.get(1).absUrl("href");
				id = id.substring(id.lastIndexOf('/') + 1, id.lastIndexOf('.'));
				adVo.setId(id);
				// get the category details
				CategoryVO catVo = new CategoryVO(linkElems.get(2).text(),
						linkElems.get(2).absUrl("href"));
				adVo.setSubCategory(catVo);
				// get the ad details
				getAdDetails(adVo);

				adList.add(adVo);

			}
		}

		return adList;

	}

	/**
	 * The details of an ad are extracted from the url and added
	 * 
	 * @param adVo
	 * @return
	 * @throws IOException
	 */
	private AdVO getAdDetails(AdVO adVo) throws IOException {

		Document doc = Jsoup.connect(adVo.getAdLink()).get();
		// get the map details
		Elements mapElems = doc.getElementsByAttributeValue("class",
				"mapAndAttrs");
		if (mapElems != null) {
			LocationVO location = new LocationVO();
			String locationInfo = mapElems.text();
			if (locationInfo != null && locationInfo.contains("("))
				locationInfo = locationInfo.substring(0,
						locationInfo.indexOf('('));

			location.setInfo(locationInfo);
			// get the location details
			Element locDetElem = doc.getElementById("map");
			if (locDetElem != null) {
				location.setLattitude(locDetElem.attr("data-latitude"));
				location.setLongitude(locDetElem.attr("data-longitude"));
			}
			adVo.setLocation(location);
		}

		// get the post body
		Elements postBody = doc
				.getElementsByAttributeValue("id", "postingbody");
		adVo.setAdDetails(postBody.text());
		
		// get the posting time
		Elements postDateElems = doc.getElementsByTag("time");
		if(postDateElems.size() > 0){
			Element postDate = postDateElems.get(postDateElems.size()-1);
			adVo.setPostDate(postDate.attr("datetime"));
			
		}

		return adVo;

	}

}
