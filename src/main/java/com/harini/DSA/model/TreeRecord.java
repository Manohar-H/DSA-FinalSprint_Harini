package com.harini.DSA.model;

import jakarta.persistence.*;

@Entity
public class TreeRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String input;

    @Lob
    private String treeJson;

    public TreeRecord() {}

    public TreeRecord(String input, String treeJson) {
        this.input = input;
        this.treeJson = treeJson;
    }

    public String getInput() {
        return input;
    }

    public String getTreeJson() {
        return treeJson;
    }

    public Long getId() {
        return id;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public void setTreeJson(String treeJson) {
        this.treeJson = treeJson;
    }

    public void setId(Long id) {
        this.id = id;
    }
}