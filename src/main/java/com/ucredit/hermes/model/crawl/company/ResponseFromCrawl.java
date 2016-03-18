package com.ucredit.hermes.model.crawl.company;

public class ResponseFromCrawl {
    private CompanyInfos json;
    private String status;

    public CompanyInfos getJson() {
        return this.json;
    }

    public void setJson(CompanyInfos json) {
        this.json = json;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
