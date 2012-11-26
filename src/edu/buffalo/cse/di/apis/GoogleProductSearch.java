package edu.buffalo.cse.di.apis;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;



import edu.buffalo.cse.di.apis.entity.GoogleCustomSearchResult;
import edu.buffalo.cse.di.apis.entity.GoogleProductSearchResult;
import edu.buffalo.cse.di.util.GoogleAPIKey;
import edu.buffalo.cse.di.util.SimilarityScore;
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
     * Returns the list of GoogleProductSearchResults for a query
     * (to support API call for blank ignoreList)
     * @param query
     * @return List<GoogleProductSearchResult> - List of product search results
     */
    public static List<GoogleProductSearchResult> searchProducts(String query) {
        return searchProducts(query, new ArrayList<String>());
    }

    /**
     * Returns the list of GoogleProductSearchResults. <br/>
     * Ignores records if it matches with any word in ignore list.
     * @param query
     * @param ignoreList
     * @return List<GoogleProductSearchResult> - List of product search results
     */
    public static List<GoogleProductSearchResult> searchProducts(String query, List<String> ignoreList) {
        String content = queryGoogleProductSearch(query);

        JSONObject obj = (JSONObject) JSONSerializer.toJSON(content);
        JSONArray items = obj.getJSONArray("items");
        List<GoogleProductSearchResult> itemNames = new ArrayList<GoogleProductSearchResult>();
        for(int i=0; i<items.size(); i++) {
            JSONObject item = items.getJSONObject(i);
            JSONObject product = item.getJSONObject("product");

            //Filtered the titles to ignore 
            String title = product.getString("title");
            boolean flag = false;
            if(ignoreList != null && ignoreList.size() != 0) {
                for (String ignoreStr: ignoreList) {
                    if (title.toLowerCase().contains(ignoreStr.toLowerCase())) {
                        flag = true;
                    }	
                }
            }

            if (!(flag)) {
                GoogleProductSearchResult result = new GoogleProductSearchResult(title); 
                itemNames.add(result);
            }
            //String title = 
            //System.out.println(item.get("product"));
            //System.out.println(result);
        }
        return itemNames;
    }

    public static Node getNodesForProductSearch(GoogleProductSearchResult request ) {

        List<GoogleCustomSearchResult> list = GoogleCustomSearch.getItemNames(request.getTitle());
        if(list != null) { // Happened for one String.
            List<String> titles = new ArrayList<String>(list.size());
            List<String> urls = new ArrayList<String>(list.size());
            List<String> headings = new ArrayList<String>(list.size());
            for(GoogleCustomSearchResult item:list) {
                titles.add(item.getTitle());
                urls.add(item.getLink());
                headings.add(item.getHeading());
            }
            return new Node(request.getTitle(), urls, titles, SimilarityScore.getMaxSumSimilarity(headings), null);
        }
        return null;
    }

    /**
     * Wrapper for the Algorithm when the product names are directly available.
     * @param data
     * @return 
     */
    public static List<List<Node>> runAlgorithmforStrings(List<String> data) {
        List<GoogleProductSearchResult> results = new ArrayList<GoogleProductSearchResult>(data.size());
        for(String item: data) {
            results.add(new GoogleProductSearchResult(item));
        }
        return runAlgorithm(results);
    }

    /**
     * Runs the algorithm and returns the clusters.
     * @param data
     * @return 
     */
    public static List<List<Node>> runAlgorithm(List<GoogleProductSearchResult> data) {

        List<Node> nodes = new ArrayList<Node>();
        for(GoogleProductSearchResult item: data) {
            Node node = getNodesForProductSearch(item);
            if(node != null) {
                nodes.add(node);
            }
        }

        KNNAlgorithm algorithm = new KNNAlgorithm(nodes, 3, 0.4);
        List<List<Node>> clusters = algorithm.generateClusters(SimilarityType.CUSTOM);
        System.out.println(clusters.size());
        for(List<Node> cluster: clusters) {
            System.out.println(cluster);
        }
        return clusters;
    }

    public static void main(String[] args) throws IOException {
        //actualTest("MobilePhonesFiltered.txt");
        String[] ignoreList = new String[] {"case","Charger"};
        testData(Arrays.asList(ignoreList));
    }

    public static void actualTest(String fileName) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
        String line = null;
        List<String> productTitles = new ArrayList<String>();
        while((line = reader.readLine()) != null) {
            if(!line.equals("")) {
                productTitles.add(line);
            }
        }
        reader.close();
        runAlgorithmforStrings(productTitles);
    }
    
    public static void testData(List<String> ignoreList) throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("MobilePhonesSample.txt")));
        String line = null;
        while((line = reader.readLine()) != null) {
            if(!line.equals("")) {
                System.out.println("---------------------------------------------------------------");
                System.out.println(line);
                System.out.println("---------------------------------------------------------------");
                List<GoogleProductSearchResult> results = GoogleProductSearch.searchProducts(line, ignoreList);
                for(GoogleProductSearchResult result : results) {
                    System.out.println(result.getTitle());
                }
            }
        }
        reader.close();
    }
}
