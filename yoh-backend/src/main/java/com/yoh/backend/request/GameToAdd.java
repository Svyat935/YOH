package com.yoh.backend.request;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

public class GameToAdd {
    @NotNull
    private String name;

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }


    @NotNull
    private String description;

    public String getDescription(){
        return this.description;
    }

    public void setDescription(String description){
        this.description = description;
    }
}
