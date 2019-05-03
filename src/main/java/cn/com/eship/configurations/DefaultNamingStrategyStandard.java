package cn.com.eship.configurations;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

public class DefaultNamingStrategyStandard extends PhysicalNamingStrategyStandardImpl {
    private static final long serialVersionUID = 1L;

    @Override
    public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment context) {
        String tableName = name.getText().toLowerCase();
        return name.toIdentifier(tableName);
    }
}
