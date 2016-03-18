package com.ucredit.hermes.model.juxinli;

public class ReportBackInfo {
    private String note;
    private JsonInfoTwo report_data;
    private String success;

    public String getNote() {
        return this.note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public JsonInfoTwo getReport_data() {
        return this.report_data;
    }

    public void setReport_data(JsonInfoTwo report_data) {
        this.report_data = report_data;
    }

    public String getSuccess() {
        return this.success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

}
