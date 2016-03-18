package com.ucredit.hermes.model.juxinli;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Lob;

public class EbusinessContact {

    @Column(length = 64)
    private String name;
    @Column(length = 32)
    private String end_date;
    private int total_count;
    @Lob
    private String explain;
    @Column(length = 32)
    private String begin_date;
    private float total_amount;
    private List<String> phone_num_list;
    private List<ContactDetails> contact_details;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEnd_date() {
        return this.end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public int getTotal_count() {
        return this.total_count;
    }

    public void setTotal_count(int total_count) {
        this.total_count = total_count;
    }

    public String getExplain() {
        return this.explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }

    public String getBegin_date() {
        return this.begin_date;
    }

    public void setBegin_date(String begin_date) {
        this.begin_date = begin_date;
    }

    public float getTotal_amount() {
        return this.total_amount;
    }

    public void setTotal_amount(float total_amount) {
        this.total_amount = total_amount;
    }

    public List<String> getPhone_num_list() {
        return this.phone_num_list;
    }

    public void setPhone_num_list(List<String> phone_num_list) {
        this.phone_num_list = phone_num_list;
    }

    public List<ContactDetails> getContact_details() {
        return this.contact_details;
    }

    public void setContact_details(List<ContactDetails> contact_details) {
        this.contact_details = contact_details;
    }

}
