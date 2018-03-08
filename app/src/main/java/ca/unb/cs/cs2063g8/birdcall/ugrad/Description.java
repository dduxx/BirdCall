package ca.unb.cs.cs2063g8.birdcall.ugrad;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import ca.unb.cs.cs2063g8.birdcall.web.UNBAccess;

/**
 * Created by nmagee on 07/02/18.
 */

public class Description {
    private static final String TAG = "Description";
    private URL descriptionUrl;
    private String description;
    private String prereqs;
    private String response;

    public Description(String url) throws MalformedURLException{
        this(new URL(url));
    }

    public Description(URL url){
        if(url == null){
            response = "No Information Available";
            description = "No Description Available";
            prereqs = "No Prerequisite Information Available";
            descriptionUrl = url;
        }
        else{
            this.descriptionUrl = url;
        }
    }

    public void setDescription(){
        if(response == null) {
            setResponse();
        }

        Document doc = Jsoup.parse(response);
        Element description = doc.getElementsByTag("course_description").first();
        Log.i(TAG, "desc: " + description.text());
        this.description = description.text();
    }

    public void setPrereqs(){
        if(response == null) {
            setResponse();
        }
        Document doc = Jsoup.parse(response);
        Elements prereqTag = doc.getElementsByTag("course_prereq");
        if(prereqTag.isEmpty()){
            this.prereqs = "[NO PREREQS]";
        }
        else{
            this.prereqs = prereqTag.first().text();
        }
        Log.i(TAG, "prereqs: " + prereqTag.text());
    }

    public String getDescription(){
        if(description == null){
            setDescription();
        }
        return this.description;
    }

    public String getPrereqs(){
        if(prereqs == null){
            setPrereqs();
        }
        return this.prereqs;
    }

    public URL getDescriptionUrl(){
        return this.descriptionUrl;
    }

    public void setDescriptionUrl(URL url){
        this.descriptionUrl = url;
    }

    public void setDescriptionUrl(String url) throws MalformedURLException {
        this.setDescriptionUrl(new URL(url));
    }

    public void setResponse(){
        Map<String, String> params = new HashMap<>();
        String response = UNBAccess.getResponse(descriptionUrl, UNBAccess.Expected.HTML);
        Log.i(TAG, "message: " + response);
        this.response = response;
    }

    public String getResponse(){
        if(response == null){
            setResponse();
        }

        return this.response;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Description that = (Description) o;

        return descriptionUrl.equals(that.descriptionUrl);
    }

    @Override
    public int hashCode() {
        return descriptionUrl.hashCode();
    }

    @Override
    public String toString() {
        return "Description{" +
                "descriptionUrl=" + descriptionUrl +
                ", description='" + description + '\'' +
                ", prereqs='" + prereqs + '\'' +
                ", response='" + response + '\'' +
                '}';
    }
}
