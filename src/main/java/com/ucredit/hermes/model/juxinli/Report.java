package com.ucredit.hermes.model.juxinli;

public class Report {
    private String token;
    private Updt updt;
    private String id;
    private String version;
    private String uid;
    private String note;
    private String success;

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Updt getUpdt() {
        return this.updt;
    }

    public void setUpdt(Updt updt) {
        this.updt = updt;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUid() {
        return this.uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNote() {
        return this.note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getSuccess() {
        return this.success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

}
