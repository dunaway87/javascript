package layerInfo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import play.Logger;

public class WetlandsInfo {
	public static String html;
	public static String getWetlandsInfo(double lat, double lng) throws SQLException{
    	Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/automated_shapefile_uploads?user=dunawayjenckes&password=dunawayjenckes");
    	ResultSet results = conn.prepareStatement("Select type from wetlands where ST_CONTAINS(ST_Transform(my_geom, 4326),ST_SETSRID(ST_POINT("+lng+", "+lat+"), 4326))  limit 1").executeQuery();
    	try{
    		results.next();
        	html = "<fieldset><strong>Wetlands Info</strong>";
    		addLineToHtml("Type", results.getString(1));
    		html = html.concat("</fieldset>");
    	} catch (Exception e){
    		Logger.info("there was an error, %s", e);
    		html = "";
    	}
    	Logger.info("there was no error %s", html);
    	return html;
	}	
	
	
    public static void addLineToHtml(String field, String value){
    	html = html.concat("<br><tr><td>"+field+": </td><td>"+value+"</td></tr>");
    }
	
}
