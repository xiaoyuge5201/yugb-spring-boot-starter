package com.yugb.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间工具类
 * @author yugb
 */
public class DateUtils {

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 格式化 y-m-d-h-m-s
     * @return
     */
    public static String formatToYmdhms() {
        return sdf.format(new Date());
    }
}
