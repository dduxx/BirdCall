package ca.unb.cs.cs2063g8.birdcall.web;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
 * @author nmagee
 * date: 2018-02-04
 * Uses java's http and url implementation to pass requests to a server and return responses
 *     this is meant to be a helper class and cannot be instantiated. use this only as a uniform
 *     way of gathering information from UNB's resources.
 *
 * these curl commands were used to gather data from UNB's timetables and faculty list:
 *     curl -X POST -F 'level=UG' http://es.unb.ca/apps/timetable/ajax/get-subjects.cgi
 *
 *     curl -X POST -F 'action=index' -F 'noncredit=0' -F 'term=2018/WI' -F 'level=UG'
 *         -F 'subject=ALLSUBJECTS' -F 'location=FR' -F 'format=CLASS'
 *         http://es.unb.ca/apps/timetable/index.cgi
 */

public class UNBAccess {
    private final static String TAG = "UNBAccess";
    private final static String DEFAULT_ENCODING = "UTF-8";

    private UNBAccess(){
        //make it so you cannot instantiate this class
    }

    /**
     * builds a request header from form parameters and sends it to a url
     * @param formParams parameters sent in http request from the form on the unb timetables site
     * @param url the url to send the request
     * @return a string response from the server. could be json or html depending on the
     *     response. if there was an issue hitting the server this will return the empty string
     */
    public static String getResponse(URL url, Expected returnType, String... formParams){
        try {
            Log.i(TAG, "building form entries");
            StringBuilder formData = new StringBuilder();
            for(int i=0; i<formParams.length; i++){
                if(i == 0) {
                    formData.append(formParams[i]);
                }
                else{
                    formData.append("&" + formParams[i]);
                }
            }

            byte[] formBytes = formData.toString().getBytes(DEFAULT_ENCODING);

            Log.i(TAG, "building HTTP request header");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", String.valueOf(formBytes.length));
            conn.setDoOutput(true); //allows for sending of request body info. ie: form data

            Log.i(TAG, "sending form request body");
            conn.getOutputStream().write(formBytes);

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), DEFAULT_ENCODING));

            return parse(reader, returnType);
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "encoding: " + DEFAULT_ENCODING + " is not supported", e);
            return "";
        } catch (IOException e){
            Log.e(TAG, "unable to open url connection at: " + url, e);
            return "";
        }
    }

    /**
     * goes to a url and gets a response. this method takes no form parameters
     * @param url the url to get a response from
     * @param returnType the type of expected response (HTML, JSON)
     * @return returns a string response from the url. if no response returns the empty string
     */
    public static String getResponse(URL url, Expected returnType) {
        try{
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), DEFAULT_ENCODING));

            return parse(reader, returnType);
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "encoding: " + DEFAULT_ENCODING + " is not supported", e);
            return "";
        } catch (IOException e){
            Log.e(TAG, "unable to open url connection at: " + url, e);
            return "";
        }
    }

    /**
     * parse a given buffered readers output based on the return type
     * @param reader source of data
     * @param returnType
     * @return the parsed response
     * @throws IOException
     */
    private static String parse(BufferedReader reader, Expected returnType) throws IOException{
        StringBuilder response = new StringBuilder();
        String line;

        Log.i(TAG, "parsing response");
        if(returnType == Expected.JSON){
            while ((line = reader.readLine()) != null){
                response.append(line);
            }
        }
        else{
            //the parsable html contains a lot of scripts and other elements that we do not
            //need. this will hopefully reduce the memory foot print by only copying what is
            //within table elements
            boolean tableOpen = false;
            while ((line = reader.readLine()) != null){
                if(line.contains("<table") && !line.contains("id=\"term-summary\"")){
                    tableOpen = true;
                }

                if(tableOpen){
                    response.append(line);
                }

                if(line.contains("</table>")){
                    tableOpen = false;
                }
            }
        }

        //strips whitespace between tagged elements for readability in logging
        Log.i(TAG, "returning response from server as string");
        return response.toString().replaceAll(">\\s*<", "><");
    }

    public enum Expected {
        JSON,
        HTML
    }
}
