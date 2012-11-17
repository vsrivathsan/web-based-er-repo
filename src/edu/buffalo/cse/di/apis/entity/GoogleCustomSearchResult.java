/**
 * 
 */
package edu.buffalo.cse.di.apis.entity;

/**
 * @author sravanku@buffalo.edu
 */
public class GoogleCustomSearchResult {
    private final String title;
    private final String link;
    private final String snippet;
    
    public GoogleCustomSearchResult(String title, String link, String snippet) {
        this.title = title;
        this.link = link;
        this.snippet = snippet;
    }

    public String getTitle() {
        return title;
    }
    
    public String getLink() {
        return link;
    }
    
    public String getSnippet() {
        return snippet;
    }

    @Override
    public String toString() {
        return "GoogleCustomSearchResult [title=" + title + ", link=" + link
                + ", snippet=" + snippet + "]";
    }
    
}
