/**
 * 
 */
package edu.buffalo.cse.di.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Calculate the metrics related to Jaccard coefficient.
 * @author sravanku@buffalo.edu
 */
public class SimilarityScore {
    
    public enum SimilarityType {
        JACCARD,
        CUSTOM
    }
    
    /**
     * Given two string as input return the JaccardCoefficient of both.
     * Make use of Stemmer for better scores.
     * @param str1
     * @param str2
     * @return <b>double</b> jaccard coefficient of str1 and str2
     */
    public static double getJaccardSimilarty(String str1, String str2) {
        List<String> tokens1 = getTokens(str1);
        List<String> tokens2 = getTokens(str2);
        
        return getJaccardSimilartyForTokens(tokens1, tokens2);
    }

    public static double getJaccardSimilartyForTokens(List<String> tokens1, List<String> tokens2) {
        Set<String> set1 = new HashSet<String>();
        Set<String> set2 = new HashSet<String>();
        Set<String> union = new HashSet<String>();
        
        set1.addAll(tokens1);
        set2.addAll(tokens2);
        union.addAll(set1);
        union.addAll(set2);
        
        return ((double)(set1.size() + set2.size() - union.size()))/union.size();
    }
    
    public static double getCustomSimilarity(String str1, String str2) {
        List<String> tokens1 = getTokens(str1);
        List<String> tokens2 = getTokens(str2);
        Set<String> set1 = new HashSet<String>();
        Set<String> set2 = new HashSet<String>();
        Set<String> union = new HashSet<String>();

        set1.addAll(tokens1);
        set2.addAll(tokens2);
        union.addAll(set1);
        union.addAll(set2);
        
        int minSize = (set1.size() <= set2.size()) ? set1.size() : set2.size();
        
        return ((double)(set1.size() + set2.size() - union.size()))/minSize;
    }
    
    /**
     * Given two list of strings as input return the SumSimilarityScore --- Used for determining the heading associated with the entity record ri
     * @param docHeadList --- Heading of Top K Documents for the entity record ri
     * @return
     */
    public static List<Double> getSumSimilarity(List<String> docHeadList) {
    	Iterator list1iter = docHeadList.iterator();
    	Iterator list2iter = null;
    	double sumSim = 0;
    	List<Double> sumSimList = new ArrayList<Double>();
    	while(list1iter.hasNext()) {
    		String str1 = (String)list1iter.next();
    		sumSim = 0;
    		list2iter = docHeadList.iterator();
    		while(list2iter.hasNext()) {
    			String str2 = (String)list2iter.next();
    			if (str1.equals(str2))
    				continue;
    			else {
    				double simH = getJaccardSimilarty(str1, str2);
        			sumSim = sumSim + simH;
    			}
    		}
    		sumSimList.add(sumSim);
    	}
    	return sumSimList;
    }
    
    private static List<String> getTokens(String str) {
        return Arrays.asList(str.toLowerCase().split("[; ,]"));
    }
    
    public static double simScoreForTitles(List<String> titles1, List<String> titles2) {
        return getJaccardSimilartyForTokens(titles1, titles2);
    }

    public static double simScoreForURLS(List<String> urls1, List<String> urls2, int urlThreshold) {
        
        if(urlThreshold <= 0) {
            throw new IllegalArgumentException("Invalid urlThreshold value ( =" +urlThreshold + ")");
        }
        else if(urlThreshold < urls1.size() || urlThreshold < urls2.size()) {
            throw new IllegalArgumentException("Required number of common URL's cannot be lesser" +
            		"than size either url's list");
        }
        
        Set<String> set1 = new HashSet<String>();
        Set<String> set2 = new HashSet<String>();
        Set<String> union = new HashSet<String>();
        
        set1.addAll(urls1);
        set2.addAll(urls2);
        union.addAll(set1);
        union.addAll(set2);
        
        int commonUrls = set1.size() + set2.size() - union.size();
        
        if(commonUrls >= urlThreshold) {
            return 1.0;
        }
        
        return ((double)commonUrls)/urlThreshold;
        
    }
    
    public static void main(String[] args) {
        //System.out.println(getTokens("Helo man,world;how,are;you"));
        System.out.println(getJaccardSimilarty("Apple iPhone 4S - 16 GB - Black",
                "Apple iPhone 4S 32 GB black GSM unlock"));
        System.out.println(getCustomSimilarity("Apple iPhone 4S - 16 GB - Black",
                "Apple iPhone 4S 32 GB black GSM unlock"));
    }
}
