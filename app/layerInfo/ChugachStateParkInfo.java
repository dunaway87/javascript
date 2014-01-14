package layerInfo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import play.Logger;

public class ChugachStateParkInfo {
	public static String html;
	public static String getChugachStateParkInfo(double lat, double lng) throws SQLException{
	Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/automated_shapefile_uploads?user=dunawayjenckes&password=dunawayjenckes");
	ResultSet results = conn.prepareStatement("Select name from chugach_sp where ST_CONTAINS(ST_Transform(my_geom, 4326),ST_SETSRID(ST_POINT("+lng+", "+lat+"), 4326))  limit 1").executeQuery();
	try{
		Logger.info("Select name from chugach_sp where ST_CONTAINS(ST_Transform(my_geom, 4326),ST_SETSRID(ST_POINT("+lng+", "+lat+"), 4326))  limit 1");
		results.next();
    	html = "<fieldset><strong>"+results.getString(1)+"</strong></fieldset>";
	} catch (Exception e){
		Logger.info("there was an error, %s", e);
		html = "";
	}
	Logger.info("there was no error %s", html);
	return html;
}	


}
