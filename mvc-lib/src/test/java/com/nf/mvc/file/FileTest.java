package com.nf.mvc.file;

import org.junit.Test;

import java.net.URLConnection;

public class FileTest {
    @Test
    public void testGetFilename(){
        String realPath2 = "d:/downloads/a.jpg";
        int lastSlash = realPath2.lastIndexOf("/");
        String filename = realPath2.substring(lastSlash+1);
        String mediaType = URLConnection.guessContentTypeFromName(filename);
        System.out.println("filename = " + filename);
        System.out.println("mediaType = " + mediaType);
    }
}
