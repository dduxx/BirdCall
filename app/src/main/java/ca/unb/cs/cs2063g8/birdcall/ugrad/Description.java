package ca.unb.cs.cs2063g8.birdcall.ugrad;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
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
    private final String DESCRIPTION_SELECTOR =
            "#mcontent > table.ugradcal.large-only > tbody > tr > td > course_description > p";
    private final String PREREQ_SELECTOR = "#mcontent > table.ugradcal.large-only > tbody > tr > td > course_prereq > p";
    private URL descriptionUrl;
    private String description;
    private String prereqs;
    private String response;

    public Description(String url) throws MalformedURLException{
        this(new URL(url));
    }

    public Description(URL url){
        this.descriptionUrl = descriptionUrl;
    }

    public void setDescription(){
        if(response == null) {
            setResponse();
        }

        Document doc = Jsoup.parse(response);
        Elements elements = doc.select(DESCRIPTION_SELECTOR);
        this.description = elements.get(0).ownText();
    }

    public void setPrereqs(){
        if(response == null) {
            setResponse();
        }
        Document doc = Jsoup.parse(response);
        Elements elements = doc.select(PREREQ_SELECTOR);
        this.prereqs = elements.get(0).ownText();
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
        String response = UNBAccess.getResponse(params, descriptionUrl, UNBAccess.Expected.HTML);
        this.response = response;
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
