package com.adrater.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * This class is used for handling the application properties. 
 * 
 * @author Suraj Shetty
 *
 */
public final class ToolPropertiesUtil {
	
	private static Map<String, String> propertiesMap;
	
	static{
		//load the properties file
		reLoad();
	}
	
	/**
	 * Loads the properties file and extract the properties
	 */
	public static void reLoad(){
		
		//load the properties file
		Properties properties = new Properties();
		try {
			properties.load(ToolPropertiesUtil.class.getClassLoader().getResourceAsStream("tool.properties"));
			propertiesMap = new HashMap<>();
			Set propKeys = properties.keySet();
			for(Object key : propKeys){
				propertiesMap.put((String) key, (String)properties.getProperty((String)key));
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * Returns the property value if the key is present
	 * @param key
	 * @return
	 */
	public static String getProperty(String key){
	
		return (propertiesMap == null) ? null : propertiesMap.get(key);
	}
	
	
}
