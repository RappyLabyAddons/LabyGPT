package com.rappytv.labygpt.api;

import java.util.ArrayList;

public class ResponseBody {

    public ArrayList<Choice> choices;
    public Error error;

    public static class Choice {
        public ChatMessage message;
    }
    public static class Error {
        public String message;
    }
}
