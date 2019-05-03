package cn.com.eship.configurations;

import org.hibernate.dialect.MySQL5Dialect;

public class MySQL5DialectUTF extends MySQL5Dialect {
    @Override
    public String getTableTypeString() {
        return " character set utf8";
    }
}