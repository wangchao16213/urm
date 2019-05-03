package cn.com.eship.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DataWarehouseRepository {
    @Autowired
    @Qualifier("dwJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    public List<List<Object>> commonData(String sql) {
        List<List<Object>> data = new ArrayList<>();
        jdbcTemplate.query(sql, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                long count = rs.getMetaData().getColumnCount();
                List<Object> row = new ArrayList<>();
                for (int i = 1; i <= count; i++) {
                    row.add(rs.getObject(i));
                }
                data.add(row);
            }
        });
        return data;
    }


    public void executeSql(String sql) {
        this.jdbcTemplate.execute(sql);
    }


    public void deleteTestTable() {
        String sql = "drop table test.wangchao";
        this.jdbcTemplate.execute(sql);
    }
}
