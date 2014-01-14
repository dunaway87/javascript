package controllers;

import parcels.ParcelFromName;
import play.*;
import play.libs.WS;
import play.libs.WS.WSRequest;
import play.mvc.*;
import geoserver.Layers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import org.geoserver.security.impl.GeoServerUserDao;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.thoughtworks.xstream.converters.collections.MapConverter;

import layerInfo.LayersInfo;
import layerInfo.TaxInfo;
import models.*;

public class Application extends Controller {
	public static void getParcelByName(String firstName, String lastName) throws IOException{
		renderJSON(ParcelFromName.getInfo(firstName, lastName));
	}
	public static void mapper() {
		render();
	}

	public static void mapTest(){
		render();
	}

	public static void main(String[] args) throws SQLException{
		//getLayerInfo(61.15709362938342, -149.8395171761512, "");

	}
	public static void getLayerInfo(double lat, double lng, String layers) throws SQLException, IOException{
		Logger.info("request recieved");
		JsonObject obj = new JsonObject();
		String taxID =null;

		if(layers.contains("Parcels")){
			Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/automated_shapefile_uploads?user=dunawayjenckes&password=dunawayjenckes");

			String sql = "SELECT ST_ASTEXT(ST_transform(my_geom,4326)), \"PARCEL_NUM\" from parcels where ST_CONTAINS(ST_Transform(my_geom, 4326),ST_SETSRID(ST_POINT("+lng+", "+lat+"), 4326))  limit 1";
			Logger.info("sending query");

			try{
				ResultSet results = conn.prepareStatement(sql).executeQuery();
				Logger.info(sql);
				Logger.info("results recieved, manipulating polygon");
				results.next();
				String polygonForLeaflet = results.getString(1).replace("MULTIPOLYGON(((", "").replace(")))","");
				JsonArray array = switchLatLng(polygonForLeaflet);
				Logger.info("returning polygon");
				taxID = results.getString(2);
				Logger.info(results.getString(2));
				obj.add("elements", array);
			} catch(Exception e){
				obj.addProperty("elements", "");
			}
		} else {
			obj.addProperty("elements", "");
		}
		Logger.info(lat+" "+lng+" "+layers);
		obj.addProperty("layersInfo", LayersInfo.getAll(layers, taxID, lat, lng));

		renderJSON(obj);
	}
	public static JsonArray switchLatLng(String lngLat){
		JsonArray array = new JsonArray();
		String[] points = lngLat.split(",");
		for(int i = 0; i < points.length;i ++){
			String[] pointArray = points[i].split(" ");
			JsonArray point = new JsonArray();
			point.add(new JsonPrimitive(Double.parseDouble(pointArray[1])));
			point.add(new JsonPrimitive(Double.parseDouble(pointArray[0])));
			array.add(point);
		}

		Logger.info("%s",array);

		return array;
	}

	public static void getTaxInfo(String taxID) throws IOException{
		renderJSON(TaxInfo.getTaxInfo(taxID));
	}

	public static void getLatLon(String address){
		
		JsonObject obj = WS.url("http://maps.googleapis.com/maps/api/geocode/json?sensor=false&address="+address+" anchorage alaska").get().getJson().getAsJsonObject();
		Logger.info("%s", "http://maps.googleapis.com/maps/api/geocode/json?sensor=false&address="+address+" anchorage alaska");
		JsonArray results = obj.get("results").getAsJsonArray();
		JsonObject wantedResult = new JsonObject();
		for(int i = 0; i < results.size(); i++){
			JsonObject result = results.get(i).getAsJsonObject();
			String fullAddress = result.get("formatted_address").getAsString();
			if(fullAddress.contains("Anchorage")){
				wantedResult = result;
				break;
			}
		} 
		
		String fulladdress = wantedResult.get("formatted_address").getAsString();
		double lat = wantedResult.get("geometry").getAsJsonObject().get("location").getAsJsonObject().get("lat").getAsDouble();
		double lng = wantedResult.get("geometry").getAsJsonObject().get("location").getAsJsonObject().get("lng").getAsDouble();
		int addressComponents = wantedResult.get("address_components").getAsJsonArray().size();
		int zoom = addressComponents*2+1;

		
		
		JsonObject toReturn = new JsonObject();
		toReturn.addProperty("address", fulladdress);
		toReturn.addProperty("lat", lat);
		toReturn.addProperty("lng", lng);
		toReturn.addProperty("zoom", zoom);
		renderJSON(toReturn);
	}

	
	public static void getLayers() throws MalformedURLException{
	    String layers = Layers.getLayers();
		Logger.info(layers.toString());
		renderText(layers);
	}
	

}
