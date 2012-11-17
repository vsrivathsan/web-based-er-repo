package edu.buffalo.cse.di.apis;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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



import edu.buffalo.cse.di.apis.entity.GoogleProductSearchResult;
import edu.buffalo.cse.di.util.GoogleAPIKey;
import edu.buffalo.cse.di.util.SimilarityScore.SimilarityType;
import edu.buffalo.cse.di.util.algorithm.KNNAlgorithm;
import edu.buffalo.cse.di.util.entity.Node;

/**
 * Class to search against the google products using google product SearchAPI
 * 
 * @author sravanku@buffalo.edu
 */
public class GoogleProductSearch extends GoogleSearch {

    private static final String BASE_URL 
        = "https://www.googleapis.com/shopping/search/v1/public/products?";
    
    private static final String optionalParms = "&country=US";
    private static final String outputFormat = "&alt=json";
    
    /**
     * Returns the JSONOutput
     * @param searchString
     * @return
     */
    public static String queryGoogleProductSearch(String searchString) {
        
        String completeURL = BASE_URL + "key="+GoogleAPIKey.getGoogleAPIKey() +
                optionalParms + "&q=" + formatQuery(searchString) + outputFormat;
        
        try {
            InputStream stream = new URL(completeURL).openStream();
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
    
    /**
     * Returns the list of GoogleProductSearchResults
     * @param query
     * @return List<GoogleProductSearchResult> - List of product search results
     */
    public static List<GoogleProductSearchResult> searchProducts(String query) {
        String content = queryGoogleProductSearch(query);

        JSONObject obj = (JSONObject) JSONSerializer.toJSON(content);
        JSONArray items = obj.getJSONArray("items");
        List<GoogleProductSearchResult> itemNames = new ArrayList<GoogleProductSearchResult>();
        for(int i=0; i<items.size(); i++) {
            JSONObject item = items.getJSONObject(i);
            JSONObject product = item.getJSONObject("product");
            GoogleProductSearchResult result = new GoogleProductSearchResult(product.getString("title")); 
            itemNames.add(result);
            //String title = 
            //System.out.println(item.get("product"));
            //System.out.println(result);
        }
        return itemNames;
    }
    
    public static void main(String[] args) throws IOException {
        //GoogleProductSearch.queryGoogleProductSearch("iphone");
        GoogleProductSearch.searchProducts("iphone+4s");
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("MobilePhonesSample.txt")));
        String line = null;
        List<Node> nodes = new ArrayList<Node>();
        while((line = reader.readLine()) != null) {
            if(!line.equals("")) {
                List<GoogleProductSearchResult> results = GoogleProductSearch.searchProducts(line);
                for(GoogleProductSearchResult result : results) {
                    nodes.add(new Node(result.getTitle()));
                }
            }
        }
        reader.close();
        
        // TODO Parameters selected below are for test search.
        KNNAlgorithm algorithm = new KNNAlgorithm(nodes, 3, 0.5);
        List<List<Node>> clusters = algorithm.generateClusters(SimilarityType.CUSTOM);
        System.out.println(clusters.size());
        for(List<Node> cluster: clusters) {
            System.out.println(cluster);
        }
        
    }
}
