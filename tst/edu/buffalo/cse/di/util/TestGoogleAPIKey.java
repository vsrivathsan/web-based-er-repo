/**
 * 
 */
package edu.buffalo.cse.di.util;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.buffalo.cse.di.util.GoogleAPIKey;

/**
 * @author sravanku@buffalo.edu
 */
public class TestGoogleAPIKey {
    
    private static final String firstValue  = "AIzaSyA-u-Wm_M0-HD2DzaFbZeQayeW8HwFnilE";
    private static final String secondValue = "AIzaSyBAlOkodxfPmHDqj6JGjQ1c03H8LoAtDjs";
    private static int keyValuePairsCount = 0;

    static {
        keyValuePairsCount = GoogleAPIKey.getKeyValuePairsCount();
    }
    
    @Test
    public void testGetGoogleAPIKey() {
        assertEquals(GoogleAPIKey.getGoogleAPIKey(), firstValue);
    }
    
    @Test
    public void testGetNextGoogleAPIKey() {
        assertEquals(GoogleAPIKey.getGoogleAPIKey(), secondValue);
    }
    
    @Test
    public void testGetNextGoogleAPIKeyAfterAllKeys() {
        for(int i=0; i<keyValuePairsCount-2; i++) {
            GoogleAPIKey.getGoogleAPIKey();
        }
        assertEquals(GoogleAPIKey.getGoogleAPIKey(), firstValue);
    }

}
