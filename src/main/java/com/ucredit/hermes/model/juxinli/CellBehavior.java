package com.ucredit.hermes.model.juxinli;

import java.util.List;

public class CellBehavior {
    private String phone_num;
    private List<Behavior> behavior;

    public String getPhone_num() {
        return this.phone_num;
    }

    public void setPhone_num(String phone_num) {
        this.phone_num = phone_num;
    }

    public List<Behavior> getBehavior() {
        return this.behavior;
    }

    public void setBehavior(List<Behavior> behavior) {
        this.behavior = behavior;
    }

}
