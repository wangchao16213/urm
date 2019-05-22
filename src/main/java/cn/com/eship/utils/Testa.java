package cn.com.eship.utils;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.tomcat.util.log.SystemLogHandler;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Testa {
    public static void main(String[] args) {
        String str = "1557127919577";
        Date startDate = (DateUtils.addDays(new Date(),  7));

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println(simpleDateFormat.format(startDate));

    }
}
