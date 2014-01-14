package layerInfo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import play.Logger;

public class Zoning {
	public static String html;
	public static String getInfo(double lat, double lng) throws SQLException{
    	Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/automated_shapefile_uploads?user=dunawayjenckes&password=dunawayjenckes");
    	ResultSet results = conn.prepareStatement("Select my_zone_column,\"ZONING_DES\" from zoning where ST_CONTAINS(ST_Transform(my_geom, 4326),ST_SETSRID(ST_POINT("+lng+", "+lat+"), 4326))  limit 1").executeQuery();
    	try{
    		results.next();
        	html = "<fieldset><strong>Zoning</strong>";
    		addLineToHtml("Type", results.getString(1));
    		addLineToHtml("Zoning Designation", results.getString(2));
    		addLineToHtml("For information on "+results.getString(2)+" click <a href=\"http://www.muni.org/Departments/OCPD/Planning/zoning/Pages/"+results.getString(2)+".aspx\" target=\"_blank\"> here</a>.", "");
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
