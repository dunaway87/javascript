package html;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JsonToHtml {
	public static void main(String[] args){


		
		JsonObject avy = new JsonObject();
			avy.addProperty("Hazard", "High");
		JsonObject obj = new JsonParser().parse("{\"TAX INFO\":{\"address\":\"4509 GRUMMAN ST\",\"area\":\"48690\",\"Drainage\":\"GOOD\",\"value\":\"$356800\",\"Name\":\"LEYLAND TODD S\"}}").getAsJsonObject();
		obj.add("Avalanche Hazard", avy);
		System.out.println(obj);
		String html= "<div>";
		Set<Entry<String, JsonElement>> layersSet = obj.entrySet();
		
	}
}