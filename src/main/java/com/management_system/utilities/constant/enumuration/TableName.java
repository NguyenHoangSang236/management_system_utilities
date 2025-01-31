package com.management_system.utilities.constant.enumuration;

public enum TableName {
    INGREDIENT,
    ACCOUNT,
    IMPORTATION,
    CATEGORY,
    FACILITY,
    DISCOUNT,
    TASK,
    NOTIFICATION,
    SUPPLIER,
    RECIPE,
    MENU;

    @Override
    public String toString() {
        return name();
    }
}
