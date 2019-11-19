package com.alex.rta.producer.service;

import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.Charset;

public class ServletUtils {
    private ServletUtils() {
    }

    public static String getRequestBody(HttpServletRequest req){
        try {
            return IOUtils.toString(req.getInputStream(), Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
