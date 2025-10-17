package com.apiPages;

public class Payload {

    public static String request(){
        String body="{\n" +
                "                    \"name\": \"morpheus\",\n" +
                "                    \"job\": \"leader\"\n" +
                "                }";
        return body;
    }
}
