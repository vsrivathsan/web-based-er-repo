/**
 * 
 */
package edu.buffalo.cse.di.util;

import java.util.Arrays;
import java.util.HashSet;
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
    
    private static List<String> getTokens(String str) {
        return Arrays.asList(str.toLowerCase().split("[; ,]"));
    }
    
    public static void main(String[] args) {
        //System.out.println(getTokens("Helo man,world;how,are;you"));
        System.out.println(getJaccardSimilarty("Apple iPhone 4S - 16 GB - Black",
                "Apple iPhone 4S 32 GB black GSM unlock"));
        System.out.println(getCustomSimilarity("Apple iPhone 4S - 16 GB - Black",
                "Apple iPhone 4S 32 GB black GSM unlock"));
    }
}
