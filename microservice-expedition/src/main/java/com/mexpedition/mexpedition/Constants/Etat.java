package com.mexpedition.mexpedition.Constants;

public enum Etat {

    EN_PREPARATION(0),
    EXPEDIEE(1),
    LIVREE(2);

    private Integer value;
    Etat(Integer value){
        this.value = value;
    }
    public Integer getValue(){
        return value;
    }
}
