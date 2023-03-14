package com.example.a12.module_datas;


import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.ArrayList;
import java.util.List;

public class QuestionBean implements MultiItemEntity {

    private int id;
    public QuestionTypeBean type; //题型
    private String stem; //题干
    private ArrayList<String> answer; //正确答案
    private String analysis; //解析
    private ArrayList<String> metas; //选项
    private String difficulty;
    private QuestionBean parent;
    private ItemResultBean result; //结果
    private int itemType;

    private List<QuestionBean> items; //材料题子题

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public ItemResultBean getResult() {
        return result;
    }

    public void setResult(ItemResultBean result) {
        this.result = result;
    }

    public List<QuestionBean> getItems() {
        return items;
    }

    public void setItems(List<QuestionBean> items) {
        this.items = items;
    }

    public QuestionBean getParent() {
        return parent;
    }

    public void setParent(QuestionBean parent) {
        this.parent = parent;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public QuestionTypeBean getType() {
        return type;
    }

    public void setType(QuestionTypeBean type) {
        this.type = type;
    }

    public void setStem(String stem) {
        this.stem = stem;
    }

    public String getStem() {
        return this.stem;
    }

    public void setAnswer(ArrayList<String> answer) {
        this.answer = answer;
    }

    public ArrayList<String> getAnswer() {
        return this.answer;
    }

    public void setAnalysis(String analysis) {
        this.analysis = analysis;
    }

    public String getAnalysis() {
        return this.analysis;
    }

    public void setMetas(ArrayList<String> metas) {
        this.metas = metas;
    }

    public ArrayList<String> getMetas() {
        return this.metas;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getDifficulty() {
        return this.difficulty;
    }

    @Override
    public int getItemType() {
        return itemType;
    }



}
