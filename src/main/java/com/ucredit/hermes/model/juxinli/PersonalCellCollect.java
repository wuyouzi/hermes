package com.ucredit.hermes.model.juxinli;

import java.util.List;

public class PersonalCellCollect {
    private String begin_date;
    private String end_date;
    private List<String> shared_with;
    private List<String> from_datasource;
    private String phone_num;
    private double day_used;

    public String getBegin_date() {
        return this.begin_date;
    }

    public void setBegin_date(String begin_date) {
        this.begin_date = begin_date;
    }

    public String getEnd_date() {
        return this.end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public List<String> getShared_with() {
        return this.shared_with;
    }

    public void setShared_with(List<String> shared_with) {
        this.shared_with = shared_with;
    }

    public List<String> getFrom_datasource() {
        return this.from_datasource;
    }

    public void setFrom_datasource(List<String> from_datasource) {
        this.from_datasource = from_datasource;
    }

    public String getPhone_num() {
        return this.phone_num;
    }

    public void setPhone_num(String phone_num) {
        this.phone_num = phone_num;
    }

    public double getDay_used() {
        return this.day_used;
    }

    public void setDay_used(double day_used) {
        this.day_used = day_used;
    }

}
