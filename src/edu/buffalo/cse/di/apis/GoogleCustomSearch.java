/**
 * 
 */
package edu.buffalo.cse.di.apis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import edu.buffalo.cse.di.apis.entity.GoogleCustomSearchResult;
import edu.buffalo.cse.di.util.GoogleAPIKey;

/**
 * This class is used to search for a query against the google search, and get the appropriate content.
 * @author sravanku@buffalo.edu
 */
public class GoogleCustomSearch extends GoogleSearch {
    private static final String BASE_URL = "https://www.googleapis.com/customsearch/v1";
    private static final String outputFormat = "&alt=json";
    private static final String customSearchEngineRef = "&cx=001411437529243436513:yxjsvl3ddv4";
    
    /**
     * Construct the URL that can query against the google API.
     * @param query
     * @return
     */
    public static String constructURL(String query) {
        return ( BASE_URL + "?key=" + GoogleAPIKey.getGoogleAPIKey() 
                + customSearchEngineRef + "&q=" + formatQuery(query) + outputFormat );
    }
    
    public static String queryGoogleCustomSearch(String query) {

        try {
            InputStream stream = new URL(constructURL(query)).openStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            
            String content = "";
            String line = null;
            while ((line = reader.readLine()) != null) {
                content += line;
            }
            //System.out.println(content);
            return content;
        } catch (MalformedURLException e) {
            // TODO Add LOG statement here.
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Add LOG statement here.
            e.printStackTrace();
        }
        return null;
    }
    
    public static List<GoogleCustomSearchResult> getItemNames(String query) {
        String content = queryGoogleCustomSearch(query);

        JSONObject obj = (JSONObject) JSONSerializer.toJSON(content);
        JSONArray items = obj.getJSONArray("items");
        List<GoogleCustomSearchResult> itemNames = new ArrayList<GoogleCustomSearchResult>();
        for(int i=0; i<items.size(); i++) {
            JSONObject item = items.getJSONObject(i);
            String title = item.getString("title");
            String link = item.getString("link");
            String snippet = item.getString("snippet");
            GoogleCustomSearchResult result = new GoogleCustomSearchResult(title, link, snippet);
            itemNames.add(result);
            //String title = 
            //System.out.println(item.get("product"));
            System.out.println(result);
        }
        return itemNames;
    }
    
    
    public static void main(String[] args) {
        //GoogleProductSearch.queryGoogleProductSearch("iphone");
        GoogleCustomSearch.getItemNames("iphone+4s");
    }
    
}
