package com.moneyflow.entity.enuns;

public enum UserStatus {
    ACTIVE("active"),
    INACTIVE("inactive");

    private String status;

    UserStatus(String status){
        this.status = status;
    }

    public String getStatus(){
        return status;
    }
}
