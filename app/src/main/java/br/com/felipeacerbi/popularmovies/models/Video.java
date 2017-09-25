package br.com.felipeacerbi.popularmovies.models;

@SuppressWarnings({"UnusedDeclaration"})
public class Video {

    private String key;
    private String name;
    private String site;
    private int size;
    private String type;

    public Video(String key, String name, String site, int size, String type) {
        this.key = key;
        this.name = name;
        this.site = site;
        this.size = size;
        this.type = type;
    }

    private String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVideoLink() {
        return String.format("https://www.youtube.com/watch?v=%s", getKey());
    }

    public String getCoverLink() {
        return String.format("https://img.youtube.com/vi/%s/0.jpg", getKey());
    }
}
