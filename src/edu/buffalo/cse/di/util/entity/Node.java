/**
 * 
 */
package edu.buffalo.cse.di.util.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Node for the KNN Algorithm
 * @author sravanku@buffalo.edu
 */
public class Node {
    // TODO This very basic representation has only a single attribute.
    private final String string;
    private final List<String> urls;
    private final List<String> titles;
    private final String header;
    private final String text;

    /**
     * Create a Node object
     * @param string
     * @param urls
     * @param titles
     * @param header
     * @param text
     */
    public Node(String string, List<String> urls, List<String> titles,
            String header, String text) {
        // Avoid null check at later calls when ever used.
        if(urls == null) {
            urls = new ArrayList<String>(0);
        }
        if(titles == null) {
            titles = new ArrayList<String>(0);
        }

        this.string = string;
        // Convert to lower case Strings
        for(int i=0; i<urls.size(); i++) {
            urls.set(i, urls.get(i).toLowerCase());
        }        
        for(int i=0; i<titles.size(); i++) {
            titles.set(i, titles.get(i).toLowerCase());
        }

        this.urls   = urls;
        this.titles = titles;
        this.header = header;
        this.text = text;
    }

    public String getString() {
        return string;
    }

    public List<String> getUrls() {
        return urls;
    }

    public List<String> getTitles() {
        return titles;
    }

    public String getHeader() {
        return header;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "Node [string=" + string + ", urls=" + urls + ", titles="
                + titles + ", header=" + header + ", text=" + text + "]";
    }

}
