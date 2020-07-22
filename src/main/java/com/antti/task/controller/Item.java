package com.antti.task.controller;

import javax.servlet.http.HttpServletRequest;

public abstract class Item {

    protected long getItemId(HttpServletRequest request){
        try {
            return request.getParameter("id") != null 
                    ? Long.valueOf(request.getParameter("id")) 
                    : 0;
        } catch (Exception nfe) {
            return 0;
        }
    }
}
