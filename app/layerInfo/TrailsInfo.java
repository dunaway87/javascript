package layerInfo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import play.Logger;

public class TrailsInfo {
	public static String html;
	public static String getTrailsInfo(double lat, double lng) throws SQLException{
    	Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/automated_shapefile_uploads?user=dunawayjenckes&password=dunawayjenckes");
    	ResultSet results = conn.prepareStatement("Select \"SYSTEM_NAM\", \"TRAIL_NAME\", \"TRAIL_CLAS\", \"SURFACE\", \"LIGHTING\", \"TREADWIDTH\", \"GRADE\", \"SUMMERUSET\", \"WINTERUSET\", \"GROOMINGGO\", ST_Distance(ST_Transform(my_geom, 4326),ST_SETSRID(ST_POINT("+lng+", "+lat+"), 4326)) from trails where ST_Distance(ST_Transform(my_geom, 4326),ST_SETSRID(ST_POINT("+lng+", "+lat+"), 4326)) <.0004 order by ST_Distance(ST_Transform(my_geom, 4326),ST_SETSRID(ST_POINT("+lng+", "+lat+"), 4326)) asc  limit 1").executeQuery();
    	
    	try{
    		results.next();
    		Logger.info(results.getString(11));
    		html = "<fieldset><strong>Trail Info</strong>";
    		addLineToHtml("Name", results.getString(1)+" - "+results.getString(2));
    		addLineToHtml("Class", results.getString(3));
    		addLineToHtml("Surface", results.getString(4));
    		addLineToHtml("Lighting", results.getString(5));
    		addLineToHtml("Width", results.getString(6));
    		addLineToHtml("Grade", results.getString(7));
    		addLineToHtml("Summer Use", results.getString(8));
    		addLineToHtml("Winter Use", results.getString(9));
    		addLineToHtml("Groomed", results.getString(10));

    		
    		
    		
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
