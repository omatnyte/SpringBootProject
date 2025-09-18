package com.springboot.SpringBootProject.entity;


public class JournalEntrySample {

    private String id;
    private String title;
    private String content;

    public JournalEntrySample(String id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public JournalEntrySample() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "JournalEntry [id=" + id + ", title=" + title + ", content=" + content + "]";
    }
}
