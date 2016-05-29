package com.example.venkatneehar.mainpage;

import android.os.Bundle;

/**
 * Created by venkatneehar on 5/29/2015.
 */
public class Node {
    private String data;
    private String sPhone;
    public Node(Bundle data){
        this.data =data.getString("m");
        this.sPhone=data.getString("sendPhone");
    }

    public String getData(){
        return data;
    }

    public String getsPhone(){
        return sPhone;
    }
}
