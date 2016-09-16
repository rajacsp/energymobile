package org.qwan.energy.utils;

/**
 * Created by raja on 9/16/2016.
 */
public class CreditItem {

    private String category;
    private String content;
    private String clue;
    private String updatedDate;

    public CreditItem(String category, String content, String clue, String updatedDate) {
        this.category = category;
        this.content = content;
        this.clue = clue;
        this.updatedDate = updatedDate;
    }

    public CreditItem() {
    }

    public CreditItem(String category, String content, String clue) {
        this.category = category;
        this.content = content;
        this.clue = clue;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getClue() {
        return clue;
    }

    public void setClue(String clue) {
        this.clue = clue;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }
}


