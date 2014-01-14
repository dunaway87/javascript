package parcels;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import layerInfo.TaxInfo;

import play.Logger;
import play.libs.WS;
import play.libs.WS.WSRequest;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import controllers.Application;

public class ParcelFromName {
	String polygon;
	private static double lat;
	private static double lng;
	public static JsonObject getInfo(String firstName, String lastName) throws IOException {
		WSRequest request = WS.url("http://anchoragelive.com/Parcel/Search");
		
		request.setParameter("q", lastName+" "+firstName);
		Logger.info(request.parameters.toString());
		JsonArray array  = request.post().getJson().getAsJsonArray();
		String parcelId = array.get(0).getAsJsonObject().get("MapKey").getAsString();
		JsonArray polygon = getPolygon(parcelId);
		String html = "<div> "+ TaxInfo.getTaxInfo(parcelId+"000")+" </div>";
		JsonObject obj = new JsonObject();
		obj.add("polygon", polygon);
		obj.addProperty("html", html);
		obj.addProperty("lng", lng);
		obj.addProperty("lat", lat);
		return obj;
	}
	
	
	private static JsonArray getPolygon(String id){
		Connection conn;
		JsonArray array = null;
		try {
			conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/automated_shapefile_uploads?user=dunawayjenckes&password=dunawayjenckes");
		Logger.info("SELECT ST_ASTEXT(ST_transform(my_geom,4326)), ST_X(ST_Centroid(ST_transform(my_geom,4326))),  ST_Y(ST_Centroid(ST_transform(my_geom,4326))) from parcels where \"PARCEL_NUM\" = "+id);	

		ResultSet results = conn.prepareStatement("SELECT ST_ASTEXT(ST_transform(my_geom,4326)), ST_X(ST_Centroid(ST_transform(my_geom,4326))),  ST_Y(ST_Centroid(ST_transform(my_geom,4326))) from parcels where \"PARCEL_NUM\" like '%"+id+"%'").executeQuery();
		results.next();
		String polygon = results.getString(1).replace("MULTIPOLYGON(((", "").replace(")))","");
		lng = results.getDouble(2);
		lat = results.getDouble(3);
		Logger.error("blabla");
		Logger.info(results.getDouble(2)+ " "+ results.getDouble(3));
		array = Application.switchLatLng(polygon);
		
		
		} catch (SQLException e) {
			Logger.error("%s", e.toString());
		}
		return array;
	}
}
