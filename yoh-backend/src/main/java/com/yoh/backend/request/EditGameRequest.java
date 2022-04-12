package com.yoh.backend.request;

import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

public class EditGameRequest {
    @NotNull
    private String game_id;

    public String getGame_id() { return game_id;}

    public void setGame_id(String game_id) { this.game_id = game_id;}

    @Nullable
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Nullable
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Nullable
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
