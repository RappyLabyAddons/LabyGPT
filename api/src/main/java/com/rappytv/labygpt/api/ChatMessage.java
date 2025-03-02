package com.rappytv.labygpt.api;

import com.google.gson.annotations.SerializedName;

public class ChatMessage {
    public String content;
    public ChatRole role;
    public String name;

    public ChatMessage(String content, ChatRole role, String name) {
        this.content = content;
        this.role = role;
        this.name = name;
    }

    public enum ChatRole {
        @SerializedName("developer")
        DEVELOPER,
        @SerializedName("user")
        USER,
        @SerializedName("assistant")
        ASSISTANT
    }
}
