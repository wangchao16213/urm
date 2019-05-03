package cn.com.eship.utils;

import cn.com.eship.context.ContextSetting;
import cn.com.eship.models.Behavior;
import cn.com.eship.models.BehaviorField;
import cn.com.eship.models.CustomerGroup;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SqlConstructor {
    private Behavior behavior;
    private StringBuffer select;
    private StringBuffer from;
    private StringBuffer where;
    private StringBuffer group;
    private StringBuffer order;
    private StringBuffer limit;
    private StringBuffer having;
    private StringBuffer join;
    private boolean aggFlag = false;
    private boolean orderFlag = false;
    private boolean havingFlag = false;


    public SqlConstructor(Behavior behavior) {
        this.behavior = behavior;
        this.select = new StringBuffer("SELECT ");
        this.from = new StringBuffer(" FROM " + behavior.getNamespace() + "." + behavior.getTbName() + " " + behavior.getAlias());
        this.where = new StringBuffer(" WHERE 1 = 1 ");
        this.group = new StringBuffer(" GROUP BY ");
        this.having = new StringBuffer(" HAVING ");
        this.order = new StringBuffer(" ORDER BY ");
        this.limit = new StringBuffer(" LIMIT 5000");
        this.join = new StringBuffer(" ");

    }


    public SqlConstructor agg(String dimension) {
        this.aggFlag = true;
        this.group.append(behavior.getAlias() + "." + dimension + ",");
        this.select.append(behavior.getAlias() + "." + dimension + ",");
        return this;
    }

    public SqlConstructor measure(String measure) {
        this.select.append(measure + ",");
        return this;
    }


    public SqlConstructor having(String measure, String opreation, String value) {
        if (StringUtils.isNotBlank(value)){
            havingFlag = true;
            if (!having.toString().equals(" HAVING ")) {
                having.append(" AND ");
            }
            having.append(measure + " ");

            if (opreation.equals("eq")) {
                having.append("= " + value);
            } else if (opreation.equals("gt")) {
                having.append("> " + value);
            } else if (opreation.equals("lt")) {
                having.append("< " + value);
            } else if (opreation.equals("gte")) {
                having.append(">= " + value);
            } else if (opreation.equals("lte")) {
                having.append("<= " + value);
            } else if (opreation.equals("neq")) {
                having.append("!= " + value);
            }
        }
        return this;
    }

    public SqlConstructor filter(String filter) {
        if (StringUtils.isNotBlank(filter)) {
            this.where.append(filter);
        }
        return this;
    }


    public SqlConstructor limit(String limit) {
        if (StringUtils.isNotBlank(limit)) {
            this.limit = new StringBuffer(" LIMIT " + limit);
        }
        return this;
    }

    public SqlConstructor order(String order, String type) {
        if (StringUtils.isNotBlank(limit)) {
            this.orderFlag = true;
            this.order.append(behavior.getAlias() + "." + order);
        }
        if (StringUtils.isNotBlank(type)) {
            this.order.append(" " + type);
        }
        return this;
    }


    public SqlConstructor filterDateranger(String dateranger, String dateDimension) {
        if (StringUtils.isNotBlank(dateranger)) {
            String[] daterangerArray = dateranger.split(",");
            this.where.append(String.format(" AND %s.%s >= date('%s') AND %s.%s <= date('%s')", behavior.getAlias(), dateDimension, daterangerArray[0], behavior.getAlias(), dateDimension, daterangerArray[1]));
        }
        return this;
    }

    public SqlConstructor filterDateranger(int day, String dateDimension) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date endDate = new Date();
        Date startDate = (DateUtils.addDays(new Date(), 0 - day));
        String startDateStr = simpleDateFormat.format(startDate);
        String endDateStr = simpleDateFormat.format(endDate);
        this.filterDateranger(String.format("%s,%s", startDateStr, endDateStr), dateDimension);
        return this;
    }

    public String getSql() {
        StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append(select.deleteCharAt(select.length() - 1)).append(from).append(join).append(where);
        if (aggFlag) {
            sqlBuffer.append(group.deleteCharAt(group.length() - 1));
        }
        if (havingFlag){
            sqlBuffer.append(having);
        }
        if (orderFlag) {
            sqlBuffer.append(order);
        }
        sqlBuffer.append(limit);
        return sqlBuffer.toString();
    }


    public String getSqlWithoutLimit() {
        StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append(select.deleteCharAt(select.length() - 1)).append(from).append(join).append(where);

        if (aggFlag) {
            sqlBuffer.append(group.deleteCharAt(group.length() - 1));
        }
        if (havingFlag){
            sqlBuffer.append(having);
        }
        if (orderFlag) {
            sqlBuffer.append(order);
        }
        return sqlBuffer.toString();
    }

    public SqlConstructor joinCustomerGroup(CustomerGroup customerGroup,BehaviorField customerField) {
        String join = String.format(" JOIN %s.cg%s cg ON cg.uid = %s.%s", customerGroup.getChannel().getSchema(),customerGroup.getId(),this.behavior.getAlias(),customerField.getFieldName());
        this.join.append(join);
        return this;
    }
}
