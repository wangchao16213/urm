package cn.com.eship.utils;

import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateUtilsHelper {
    /**
     *  ex:  2018-01-01,2018-01-04     2018-01-01,2018-01-02,2018-01-03,2018-01-04
     * @param dateranger
     * @return
     */
    public static List<String> getDateListByRangeer(String dateranger) {
        List<String> datelist = new ArrayList<>();
        String[] daterangerArray = dateranger.split(",");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = null;
        Date endDate = null;
        try {
            startDate = simpleDateFormat.parse(daterangerArray[0]);
            endDate = simpleDateFormat.parse(daterangerArray[1]);
            while (true) {
                if (DateUtils.isSameDay(startDate, endDate)) {
                    datelist.add(daterangerArray[1]);
                    break;
                } else {
                    datelist.add(simpleDateFormat.format(startDate));
                    startDate = DateUtils.addDays(startDate, 1);
                }
            }

        } catch (ParseException e) {
            //TODO 日志
            e.printStackTrace();
        }

        return datelist;

    }








    /**
     * 获取当前月份的时间范围
     *
     * @return
     */
    public static List<String> getThisMonth() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.set(Calendar.DAY_OF_MONTH, 1);
        setMinTime(startCalendar);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.set(Calendar.DAY_OF_MONTH, endCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        setMaxTime(endCalendar);
        List<String> dateranger = new ArrayList<>();
        dateranger.add(simpleDateFormat.format(startCalendar.getTime()));
        dateranger.add(simpleDateFormat.format(endCalendar.getTime()));
        return dateranger;
    }

    /**
     * 获取上个月的时间范围
     *
     * @return
     */
    public static  List<String> getLastMonth() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.add(Calendar.MONTH, -1);
        startCalendar.set(Calendar.DAY_OF_MONTH, 1);
        setMinTime(startCalendar);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.add(Calendar.MONTH, -1);
        endCalendar.set(Calendar.DAY_OF_MONTH, endCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        setMaxTime(endCalendar);
        List<String> dateranger = new ArrayList<>();
        dateranger.add(simpleDateFormat.format(startCalendar.getTime()));
        dateranger.add(simpleDateFormat.format(endCalendar.getTime()));
        return dateranger;
    }

    private static void setMinTime(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    private static void setMaxTime(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMaximum(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, calendar.getActualMaximum(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, calendar.getActualMaximum(Calendar.SECOND));
        calendar.set(Calendar.MILLISECOND, calendar.getActualMaximum(Calendar.MILLISECOND));
    }


    public final static String DEFAULT_PATTERN = "MM/dd/yyyy HH:mm:ss";

    public static String format(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_PATTERN);
        return sdf.format(date);
    }


    public static void main(String[] args) {
        System.out.println(getLastMonth());

    }
}
