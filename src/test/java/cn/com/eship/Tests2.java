package cn.com.eship;

import org.junit.Test;

import cn.com.eship.models.enums.BehaviorType;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Tests2 {
    public void test2() throws Exception {
        String URL = "jdbc:mysql://cdb-gjulj29s.bj.tencentcdb.com:10052?useUnicode=true&amp;characterEncoding=utf-8";
        String USER = "root";
        String PASSWORD = "Root!@#123";
        //1.加载驱动程序
        Class.forName("com.mysql.jdbc.Driver");
        //2.获得数据库链接
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        //3.通过数据库的连接操作数据库，实现增删改查（使用Statement类）
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("select * from urmdb.behavior_field");
        //4.处理数据库的返回结果(使用ResultSet类)
        while (rs.next()) {
            System.out.println(rs.getString(3));
        }
        //关闭资源
        rs.close();
        st.close();
        conn.close();
    }


    @Test
    public void test() {
        System.out.println(BehaviorType.EVENT_LAYER.getValue());
    }
}
