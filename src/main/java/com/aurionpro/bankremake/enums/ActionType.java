package com.aurionpro.bankremake.enums;

public enum ActionType {
    APPROVE_ACCOUNT("approve_account"),
    REJECT_ACCOUNT("reject_account"),
    APPROVE_TRANSACTION("approve_transaction"),
    REJECT_TRANSACTION("reject_transaction"),
    REVIEW_DEPOSIT("review_deposit"),
    UPDATE_USER_INFO("update_user_info"),
    FREEZE_ACCOUNT("freeze_account"),
    UNFREEZE_ACCOUNT("unfreeze_account");
    
    private final String value;
    
    ActionType(String value) {
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }
}