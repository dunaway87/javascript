package layerInfo;

import java.io.IOException;
import java.sql.SQLException;

import play.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class LayersInfo {
	public static String getAll(String arraySTR, String taxID, double lat, double lng) throws IOException, SQLException{
		String html ="<div>";
		String[] array = arraySTR.split(",");

		for(int i = 0; i < array.length; i++){
			Logger.info("%s", array[i]);
			String layer = array[i];
			if(layer.equals("Parcels")&& taxID != null){
				html = html.concat(TaxInfo.getTaxInfo(taxID));
			} else if (layer.equals("Avalanche")){
				html = html.concat(AvyInfo.getAvyInfo(lat, lng));
			} else if (layer.equals("Military")){
				html = html.concat(MilitaryInfo.getMiliteryInfo(lat, lng));
			} else if (layer.equals("Wetlands")){
				html = html.concat(WetlandsInfo.getWetlandsInfo(lat, lng));
			} else if (layer.equals("Chugach_State_Park")){
				html = html.concat(ChugachStateParkInfo.getChugachStateParkInfo(lat, lng));
			} else if (layer.equals("Trails")){
				html = html.concat(TrailsInfo.getTrailsInfo(lat, lng));
			} else if (layer.equals("Chugach_National_Forest")){
				html = html.concat(CNFInfo.getInfo(lat, lng));
			} else if (layer.equals("Zoning")){
				html = html.concat(Zoning.getInfo(lat, lng));

			}

		}
		html = html.concat("</div>");

		if(html.equals("<div></div>")){
			return "";
		} else {
			return html;
		}
	}








}
