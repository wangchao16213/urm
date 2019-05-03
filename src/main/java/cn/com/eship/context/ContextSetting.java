package cn.com.eship.context;

public class ContextSetting {
    public final static String COMMON_EVENT_DIMENSION = "event_id";
    public final static String COMMON_DATE_DIMENSION = "dt";




    //用户群组创建SQL
    public final static String CUSTOMERGROUP_CREATOR_SQL = "create table %s (uid varchar) with (format = 'ORC')";
}
