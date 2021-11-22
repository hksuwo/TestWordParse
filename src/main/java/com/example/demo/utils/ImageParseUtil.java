package com.example.demo.utils;

import org.apache.poi.util.IOUtils;

import java.io.*;
import java.util.UUID;

/**
 * 获取word文档中图片的工具类
 * @author jljy
 */
public class ImageParseUtil {
    private String targetDir;
    private String baseUrl;

    public ImageParseUtil(String targetDir, String baseUrl) {
        super();
        this.targetDir = targetDir;
        this.baseUrl = baseUrl;
    }


    public String parse(byte[] data, String extName) {
        return parse(new ByteArrayInputStream(data), extName);
    }


    public String parse(InputStream in, String extName) {
        if (extName.lastIndexOf(".") > -1) {
            extName = extName.substring(extName.lastIndexOf(".") + 1);
        }
        String filename = UUID.randomUUID() + "." + extName;
        File target = new File(targetDir);
        if (!target.exists()) {
            target.mkdirs();
        }
//        try {
//            IOUtils.copy(in, new FileOutputStream(new File(target, filename)));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return baseUrl + filename;
    }
}
