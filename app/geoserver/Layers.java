package geoserver;

import java.net.MalformedURLException;
import java.util.List;

import play.Logger;
import play.Play;
import it.geosolutions.geoserver.rest.GeoServerRESTReader;
import it.geosolutions.geoserver.rest.decoder.RESTLayerList;

import com.google.gson.JsonArray;
import com.google.gson.JsonPrimitive;

public class Layers {

	public static final String RESTURL  =Play.configuration.getProperty("RESTURL");
	public static final String RESTUSER = Play.configuration.getProperty("RESTUSER");
	public static final String RESTPW   = Play.configuration.getProperty("RESTPW");
	
	public static String getLayers() throws MalformedURLException {
	    GeoServerRESTReader reader = new GeoServerRESTReader(RESTURL, RESTUSER, RESTPW);
	    RESTLayerList layers = reader.getLayers();
	    List<String> names = layers.getNames();
	    JsonArray array = new JsonArray();
	   String toReturn = "";
	    for(int i = 0; i < names.size(); i ++){
	    	Logger.info(names.get(i));
	    	array.add(new JsonPrimitive(names.get(i)));
	    	toReturn = toReturn.concat(names.get(i)+"*");
	    }
	    toReturn = toReturn.substring(0, toReturn.length()-1);
	    Logger.info(array.toString());
		return toReturn;
	}

}
