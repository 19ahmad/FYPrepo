package com.example.myapplication.externalService;

import java.util.HashMap;

public class Upload_chat {
    HashMap<String,String> msg;

    public Upload_chat(HashMap<String, String> msg)
    {
        this.msg = msg;
    }

    public Upload_chat() { }

    public HashMap<String, String> getMsg() {
        return msg;
    }

    public void setMsg(HashMap<String, String> msg) {
        this.msg = msg;
    }

    //public void getMessage()


}
