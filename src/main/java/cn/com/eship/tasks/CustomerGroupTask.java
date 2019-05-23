package cn.com.eship.tasks;

import cn.com.eship.context.ContextSetting;
import cn.com.eship.models.Behavior;
import cn.com.eship.models.CustomerGroup;
import cn.com.eship.models.Event;
import cn.com.eship.models.enums.GroupEngineType;
import cn.com.eship.repository.BehaviorFieldRepository;
import cn.com.eship.repository.BehaviorRepository;
import cn.com.eship.repository.DataWarehouseRepository;
import cn.com.eship.repository.EventRepository;
import cn.com.eship.utils.SqlConstructor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CustomerGroupTask {
    @Autowired
    private DataWarehouseRepository dataWarehouseRepository;
    @Autowired
    private BehaviorRepository behaviorRepository;
    @Autowired
    private BehaviorFieldRepository behaviorFieldRepository;
    @Autowired
    private EventRepository eventRepository;

    public void executeCustomerGroupTask(CustomerGroup customerGroup) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String truncateSql = String.format("DELETE FROM %s.cg%s", customerGroup.getChannel().getSchema(), customerGroup.getId());
        dataWarehouseRepository.executeSql(truncateSql);
        if (GroupEngineType.GROUP_ENGINE.equals(customerGroup.getGroupEngineType())) {
            Map<String, Object> dslMap = new ObjectMapper().readValue(customerGroup.getGroupDsl(), Map.class);
            Map<String, List<Map<String, Object>>> doEventConditonMap = getConditionMap((List<Map<String, Object>>) dslMap.get("doEvent"));
            Map<String, List<Map<String, Object>>> doHitConditonMap = getConditionMap((List<Map<String, Object>>) dslMap.get("doHit"));
            Map<String, List<Map<String, Object>>> doActionConditonMap = getConditionMap((List<Map<String, Object>>) dslMap.get("doAction"));


            Behavior event = behaviorRepository.findEventBehaviorByChannelId(customerGroup.getChannel().getId());
            Behavior hit = behaviorRepository.findHitBehaviorByChannelId(customerGroup.getChannel().getId());

            List<String> eventSqlList = new ArrayList<>();
            doEventConditonMap.forEach((k, v) -> {
                List<String> doEventInnerSqlList = new ArrayList<>();
                v.forEach(conditionItem -> {
                    SqlConstructor sqlConstructorForEvent = new SqlConstructor(event);
                    sqlConstructorForEvent.agg(behaviorFieldRepository.findKeyFieldByBehavioIdAndBehaviorType(event.getId(), "2").getFieldName());
                    String eventId = (String) conditionItem.get("eventId");
                    Event event1 = eventRepository.findById(eventId).get();
                    sqlConstructorForEvent.filter(String.format(" AND %s.%s = '%s'", event.getAlias(), ContextSetting.COMMON_EVENT_DIMENSION, event1.getEventName()));

                    if (conditionItem.get("filter") != null) {
                        sqlConstructorForEvent.filter(" AND " + ((String) conditionItem.get("filter")));
                    }
                    Map<String, String> measureOptionMap = (Map<String, String>) conditionItem.get("measureOption");
                    sqlConstructorForEvent.having(behaviorFieldRepository.findById(measureOptionMap.get("measureId")).get().getFieldName(), measureOptionMap.get("opreation"), measureOptionMap.get("value"));

                    Map<String, String> zqOptionMap = (Map<String, String>) conditionItem.get("zqOption");
                    String zqType = zqOptionMap.get("type");
                    if (zqType.equals("0")) {
                        //最近
                        sqlConstructorForEvent.filterDateranger(Integer.parseInt(zqOptionMap.get("value")), ContextSetting.COMMON_DATE_DIMENSION);
                    } else if (zqType.equals("1")) {
                        //固定时段
                        sqlConstructorForEvent.filterDateranger(zqOptionMap.get("value"), ContextSetting.COMMON_DATE_DIMENSION);
                    }
                    doEventInnerSqlList.add(sqlConstructorForEvent.getSqlWithoutLimit());

                });
                StringBuffer eventSql = new StringBuffer();
                doEventInnerSqlList.forEach(eventInnerSql -> {
                    eventSql.append("(" + eventInnerSql + ")").append(" intersect ");
                });
                eventSqlList.add(eventSql.delete(eventSql.length() - 11, eventSql.length()).toString());
            });


            List<String> hitSqlList = new ArrayList<>();
            doHitConditonMap.forEach((k, v) -> {
                List<String> doHitInnerSqlList = new ArrayList<>();
                v.forEach(conditionItem -> {
                    SqlConstructor sqlConstructorForHit = new SqlConstructor(hit);
                    sqlConstructorForHit.agg(behaviorFieldRepository.findKeyFieldByBehavioIdAndBehaviorType(hit.getId(), "0").getFieldName());

                    if (conditionItem.get("filter") != null) {
                        sqlConstructorForHit.filter(" AND " + ((String) conditionItem.get("filter")));
                    }
                    Map<String, String> measureOptionMap = (Map<String, String>) conditionItem.get("measureOption");
                    sqlConstructorForHit.having(behaviorFieldRepository.findById(measureOptionMap.get("measureId")).get().getFieldName(), measureOptionMap.get("opreation"), measureOptionMap.get("value"));

                    Map<String, String> zqOptionMap = (Map<String, String>) conditionItem.get("zqOption");
                    String zqType = zqOptionMap.get("type");
                    if (zqType.equals("0")) {
                        //最近
                        sqlConstructorForHit.filterDateranger(Integer.parseInt(zqOptionMap.get("value")), ContextSetting.COMMON_DATE_DIMENSION);
                    } else if (zqType.equals("1")) {
                        //固定时段
                        sqlConstructorForHit.filterDateranger(zqOptionMap.get("value"), ContextSetting.COMMON_DATE_DIMENSION);
                    }
                    doHitInnerSqlList.add(sqlConstructorForHit.getSqlWithoutLimit());

                });
                StringBuffer hitSql = new StringBuffer();
                doHitInnerSqlList.forEach(eventInnerSql -> {
                    hitSql.append("(" + eventInnerSql + ")").append(" intersect ");
                });
                hitSqlList.add(hitSql.delete(hitSql.length() - 11, hitSql.length()).toString());
            });


            List<String> actionSqlList = new ArrayList<>();
            doActionConditonMap.forEach((k, v) -> {
                List<String> doActionInnerSqlList = new ArrayList<>();
                v.forEach(conditionItem -> {
                    SqlConstructor sqlConstructorForHit = new SqlConstructor(hit);
                    sqlConstructorForHit.agg(behaviorFieldRepository.findKeyFieldByBehavioIdAndBehaviorType(hit.getId(), "0").getFieldName());

                    Map<String, String> zqOptionMap = (Map<String, String>) conditionItem.get("zqOption");
                    String zqType = zqOptionMap.get("type");
                    if (zqType.equals("0")) {
                        //最近
                        sqlConstructorForHit.filterDateranger(Integer.parseInt(zqOptionMap.get("value")), ContextSetting.COMMON_DATE_DIMENSION);
                    } else if (zqType.equals("1")) {
                        //固定时段
                        sqlConstructorForHit.filterDateranger(zqOptionMap.get("value"), ContextSetting.COMMON_DATE_DIMENSION);
                    }
                    doActionInnerSqlList.add(sqlConstructorForHit.getSqlWithoutLimit());

                });
                StringBuffer hitSql = new StringBuffer();
                doActionInnerSqlList.forEach(eventInnerSql -> {
                    hitSql.append("(" + eventInnerSql + ")").append(" intersect ");
                });
                actionSqlList.add(hitSql.delete(hitSql.length() - 11, hitSql.length()).toString());
            });

            StringBuffer sql = new StringBuffer();

            StringBuffer sqlBuffer1 = new StringBuffer();
            eventSqlList.forEach(eventSql -> {
                sqlBuffer1.append(" union ").append("(" + eventSql + ")");
            });

            if (sqlBuffer1.length() > 1) {
                sql.append(" intersect " + "(" + sqlBuffer1.delete(0, " union ".length()) + ")");
            }

            StringBuffer sqlBuffer2 = new StringBuffer();
            hitSqlList.forEach(hitSql -> {
                sqlBuffer2.append(" union ").append("(" + hitSql + ")");
            });

            if (sqlBuffer2.length() > 1) {
                sql.append(" intersect " + "(" + sqlBuffer2.delete(0, " union ".length()) + ")");
            }

            StringBuffer sqlBuffer3 = new StringBuffer();
            actionSqlList.forEach(actionSql -> {
                sqlBuffer2.append(" union ").append("(" + actionSql + ")");
            });

            if (sqlBuffer3.length() > 1) {
                sql.append(" intersect " + "(" + sqlBuffer3.delete(0, " union ".length()) + ")");
            }
            String sqllll = String.format("INSERT INTO %s.cg%s select * from (%s)", customerGroup.getChannel().getSchema(), customerGroup.getId(), sql.delete(0, " intersect ".length()));
            dataWarehouseRepository.executeSql(sqllll);
        } else if (GroupEngineType.JHCOMPUTING.equals(customerGroup.getGroupEngineType())) {
            Behavior event = behaviorRepository.findEventBehaviorByChannelId(customerGroup.getChannel().getId());
            String tabQz = event.getChannel().getSchema();
            customerGroup.getGroupDsl();
            Map<String, String> groupDsl = objectMapper.readValue(customerGroup.getGroupDsl(), Map.class);
            String groupId1 = groupDsl.get("groupId1");
            String groupOpt1 = groupDsl.get("groupOpt1");
            String groupId2 = groupDsl.get("groupId2");
            String groupOpt2 = groupDsl.get("groupOpt2");
            String groupId3 = groupDsl.get("groupId3");

            StringBuffer sqlPart = new StringBuffer();

            switch (groupOpt1) {
                case "+": {
                    sqlPart.append(String.format("select uid from ((select a.uid from %s.%s a) union (select b.uid from %s.%s b))", tabQz, "cg" + groupId1, tabQz, "cg" + groupId2));
                    break;
                }
                case "-": {
                    sqlPart.append(String.format("select a.uid from %s.%s a left outer join %s.%s b on a.uid = b.uid where b.uid is null", tabQz, "cg" + groupId1, tabQz, "cg" + groupId2));
                    break;
                }
                case "*": {
                    sqlPart.append(String.format("select uid from ((select a.uid from %s.%s a) intersect (select b.uid from %s.%s b))", tabQz, "cg" + groupId1, tabQz, "cg" + groupId2));
                    break;
                }
            }

            String sql = "";

            if (StringUtils.isNotBlank(groupId3)) {
                switch (groupOpt2) {
                    case "+": {
                        sql = String.format("select uid from ((%s) union (select d.uid from %s.%s d))", sqlPart.toString(), tabQz, "cg" + groupId3);
                        break;
                    }
                    case "-": {
                        sql = String.format("select a.uid from (%s) tmp left outer join %s.%s b on tmp.uid = b.uid where b.uid is null", sqlPart.toString(), tabQz, "cg" + groupId2);
                        break;
                    }
                    case "*": {
                        sql = String.format("select uid from ((%s) intersect (select d.uid from %s.%s d))", sqlPart.toString(), tabQz, "cg" + groupId3);
                        break;
                    }
                }

            } else {
                sql = sqlPart.toString();

            }

            //System.out.println(String.format("insert into %s.cg%s %s", tabQz, customerGroup.getId(),sql));
            dataWarehouseRepository.executeSql(String.format("insert into %s.cg%s %s", tabQz, customerGroup.getId(),sql));
        }

    }


    private Map<String, List<Map<String, Object>>> getConditionMap(List<Map<String, Object>> conditions) {
        Map<String, List<Map<String, Object>>> conditonMap = new HashMap<>();
        conditions.stream().forEach(condition -> {
            String conditionGroup = (String) condition.get("conditionGroup");
            if (conditonMap.get(conditionGroup) == null) {
                conditonMap.put(conditionGroup, new ArrayList<>());
            }
            conditonMap.get(conditionGroup).add(condition);
        });
        return conditonMap;
    }
}
