package com.mystudy.lucenetest.utils;

import org.apache.lucene.search.Query;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class QueryUtil {

    public static long makeTimeStamp(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }
}
