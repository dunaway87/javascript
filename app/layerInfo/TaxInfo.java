package layerInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import play.Logger;
import play.libs.WS;

import com.google.gson.JsonObject;


public class TaxInfo { 
	public static String html;
	  public static String getTaxInfo(String taxID) throws IOException{
	    	Logger.info("http://anchoragelive.com/Parcel/Details?mapKey=%s",taxID);
	        	InputStream stream = WS.url("http://anchoragelive.com/Parcel/Details?mapKey="+taxID.substring(0, taxID.length()-3)).get().getStream();
	        	Reader reader = new InputStreamReader(stream);
	        	BufferedReader br = new BufferedReader(reader);
	        	String line;
	        	JsonObject obj = new JsonObject();
	        	html = "<fieldset><strong>Parcel Info</strong>";
	        	while((line = br.readLine()) != null){
	        		if(line.contains("Site Address:")){
	        			line = prepNextLine(br.readLine());
	        			addLineToHtml("Address", line);
	        		} else if(line.contains("Square Footage:")){
	        			line = prepNextLine(br.readLine());
	        			addLineToHtml("Square Footage", line);
	        		} else if(line.contains("Drainage:")){
	        			line = prepNextLine(br.readLine());
	        			addLineToHtml("Drainage", line);
	        		} else if(line.contains("Name(s)")){
	        			line = prepNextLine(br.readLine());
	        			addLineToHtml("Name", line);
	        		} else if(line.contains("Value")){
	        			br.readLine();
	        			br.readLine();
	        			line = prepNextLine(br.readLine());
	        			addLineToHtml("value", line);
	        		}

	        	}
	        	html = html.concat("</fieldset>");
	        	Logger.info("html %s", html);
	        	return html;
	        }

	        public static String prepNextLine(String line){
	        	return line.replace("<td width=\"\">", "").replace("</td>","").replace("<tr>", "").replace("</tr>", "").replace("<td>", "").trim();
	        }
	        
	        public static void addLineToHtml(String field, String value){
	        	html = html.concat("<br><tr><td>"+field+": </td><td>"+value+"</td></tr>");
	        }
	    

}
