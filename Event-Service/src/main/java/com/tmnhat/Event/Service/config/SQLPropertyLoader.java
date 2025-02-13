package com.tmnhat.Event.Service.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class SQLPropertyLoader {
    private static final Properties queries=new Properties();

    static {
        try (InputStream input = SQLPropertyLoader.class.getClassLoader().getResourceAsStream("sql/queries.properties")) {
            if (input == null) {
                throw new RuntimeException("Không tìm thấy file queries.properties");

            }
            queries.load(input);

        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi tải file queries.properties", e);
        }
    }
    public static String getQuery(String key){
        return queries.getProperty(key);
    }

}
