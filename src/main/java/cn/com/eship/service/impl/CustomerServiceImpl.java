package cn.com.eship.service.impl;

import cn.com.eship.context.ContextSetting;
import cn.com.eship.models.*;
import cn.com.eship.repository.*;
import cn.com.eship.repository.specifications.ChannelSpecification;
import cn.com.eship.repository.specifications.CustomerFieldSpecification;
import cn.com.eship.repository.specifications.CustomerGroupSpecification;
import cn.com.eship.service.CustomerService;
import cn.com.eship.service.SceneService;
import cn.com.eship.tasks.CustomerGroupTask;

import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

@EnableTransactionManagement()
@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerFieldRepository customerFieldRepository;
    @Autowired
    private ChannelRepository channelRepository;
    @Autowired
    private CustomerGroupRepository customerGroupRepository;
    @Autowired
    private DataWarehouseRepository dataWarehouseRepository;
    @Autowired
    private CustomerGroupTask customerGroupTask;
    @Autowired
    private BehaviorFieldRepository behaviorFieldRepository;
    @Autowired
    private EventRepository eventRepository;

    @Override
    public List<CustomerField> findCustomerFields(CustomerField customerField) {
        CustomerFieldSpecification customerFieldSpecification = new CustomerFieldSpecification(customerField);
        return customerFieldRepository.findAll(customerFieldSpecification);
    }

    @Override
    public List<CustomerField> findCustomerFieldsByChannelId(String channelId) {
        Sort sort = new Sort("sortNum");
        return customerFieldRepository.findByChannelId(channelId,sort);
    }

    @Override
    public List<Channel> findChannelList(Channel channel) {
        ChannelSpecification channelSpecification = new ChannelSpecification(channel);
        return channelRepository.findAll(channelSpecification);
    }

    @Override
    public List<CustomerGroup> findCustomerGroupList(CustomerGroup customerGroup) {
        CustomerGroupSpecification customerGroupSpecification = new CustomerGroupSpecification(customerGroup);
        return customerGroupRepository.findAll(customerGroupSpecification);
    }

    @Override
    public void saveCustomerGroup(CustomerGroup customerGroup) throws Exception {
        customerGroup.setId(UUID.randomUUID().toString().replace("-", ""));
        customerGroup.setCreateDate(new Date());
        Channel channel = channelRepository.findById(customerGroup.getChannelId()).get();
        customerGroup.setChannel(channel);
        String createGroupSql = String.format(ContextSetting.CUSTOMERGROUP_CREATOR_SQL, channel.getSchema() + ".cg" + customerGroup.getId());
        dataWarehouseRepository.executeSql(createGroupSql);
        this.customerGroupRepository.save(customerGroup);
        customerGroupTask.executeCustomerGroupTask(customerGroup);
    }

    @Override
    public void updateCustomerGroup(CustomerGroup customerGroup) {
        CustomerGroup customerGroup1 = customerGroupRepository.findById(customerGroup.getId()).get();
        customerGroup.setChannel(channelRepository.findById(customerGroup.getChannelId()).get());
        BeanUtils.copyProperties(customerGroup, customerGroup1);
        customerGroup1.setCreateDate(new Date());
        this.customerGroupRepository.save(customerGroup1);
    }

    @Override
    public CustomerGroup findCustomerGroupById(String customerGroupId) {
        return customerGroupRepository.findById(customerGroupId).get();
    }

    @Transactional
    @Override
    public void deleteCustomerGroupById(String customerGroupId) {
        CustomerGroup customerGroup = customerGroupRepository.findById(customerGroupId).get();
        String sql = "DROP TABLE " + customerGroup.getChannel().getSchema() + ".cg" + customerGroup.getId();
        dataWarehouseRepository.executeSql(sql);
        customerGroupRepository.deleteById(customerGroupId);
    }

    @Override
    public List<CustomerField> findCustomerFieldListByChannelId(String channelId) {
        Sort sort = new Sort("sortNum");
        return customerFieldRepository.findByChannelId(channelId,sort);
    }

    @Override
    public List<List<Object>> getCustomerList(String channelId, String customerGroupId, String filter, String orderFieldId) {
        Sort sort = new Sort("sortNum");
        List<CustomerField> customerFieldList = customerFieldRepository.findByChannelId(channelId,sort);
        CustomerTemplate customerTemplate = customerFieldList.get(0).getCustomerTemplate();
        StringBuffer select = new StringBuffer(" SELECT ");
        StringBuffer join = new StringBuffer();
        StringBuffer from = new StringBuffer(" FROM " + customerTemplate.getFullTableName() + " " + customerTemplate.getAlias() + " ");
        StringBuffer where = new StringBuffer(" WHERE 1 = 1 ");
        if (StringUtils.isNotBlank(filter)) {
            where.append(" AND " + filter);
        }
        customerFieldList.forEach(customerField -> {
            select.append(customerField.getFullFieldName()).append(",");
        });
        if (StringUtils.isNotBlank(customerGroupId)) {
            Optional<CustomerGroup> customerGroupOptional = customerGroupRepository.findById(customerGroupId);
            if (customerGroupOptional.isPresent()) {
                CustomerGroup customerGroup = customerGroupOptional.get();
                CustomerField keyField = null;
                for (CustomerField customerField : customerFieldList) {
                    if (customerField.getIsKey() == 1) {
                        keyField = customerField;
                        break;
                    }
                }
                String joinPart = String.format(" JOIN %s.cg%s cg on %s = cg.uid ", customerGroup.getChannel().getSchema(), customerGroup.getId(), keyField.getFullFieldName());
                join.append(joinPart);
            }

        }
        String sql = select.deleteCharAt(select.length() - 1).append(from).append(join).append(where).append("limit 1000").toString();
        return dataWarehouseRepository.commonData(sql);
    }

    @Override
    public Long getCustomerCount(String channelId, String customerGroupId, String filter) {
        Sort sort = new Sort("sortNum");
        List<CustomerField> customerFieldList = customerFieldRepository.findByChannelId(channelId,sort);
        CustomerTemplate customerTemplate = customerFieldList.get(0).getCustomerTemplate();
        StringBuffer select = new StringBuffer(" SELECT count(1) ");
        StringBuffer join = new StringBuffer();
        StringBuffer from = new StringBuffer(" FROM " + customerTemplate.getFullTableName() + " " + customerTemplate.getAlias() + " ");
        StringBuffer where = new StringBuffer(" WHERE 1 = 1 ");
        if (StringUtils.isNotBlank(filter)) {
            where.append(" AND " + filter);
        }

        if (StringUtils.isNotBlank(customerGroupId)) {
            Optional<CustomerGroup> customerGroupOptional = customerGroupRepository.findById(customerGroupId);
            if (customerGroupOptional.isPresent()) {
                CustomerGroup customerGroup = customerGroupOptional.get();
                CustomerField keyField = null;
                for (CustomerField customerField : customerFieldList) {
                    if (customerField.getIsKey() == 1) {
                        keyField = customerField;
                        break;
                    }
                }
                String joinPart = String.format(" JOIN %s.cg%s cg on %s = cg.uid ", customerGroup.getChannel().getSchema(), customerGroup.getId(), keyField.getFullFieldName());
                join.append(joinPart);
            }

        }
        String sql = select.append(from).append(join).append(where).toString();
        return (Long) dataWarehouseRepository.commonData(sql).get(0).get(0);
    }

    @Override
    public List<Map<String, Object>> getCustomerById(String customerId,String channelId) {
        Sort sort = new Sort("sortNum");
        List<CustomerField> customerFieldList =  customerFieldRepository.findByChannelId(channelId,sort);
        List<Map<String,Object>> result = new ArrayList<>();
        if (customerFieldList != null && customerFieldList.size() > 0){
            StringBuffer selectPart = new StringBuffer("SELECT ");
            StringBuffer fromPart = new StringBuffer(" FROM " + customerFieldList.get(0).getCustomerTemplate().getFullTableName());
            StringBuffer wherePart = new StringBuffer(" WHERE " + customerFieldRepository.findPrimariField(channelId).getFullFieldName() + "= '" + customerId + "'");
            customerFieldList.forEach(customerField -> {
                selectPart.append(customerField.getFullFieldName()).append(",");
                Map<String,Object> item = new HashMap<>();
                item.put("lable",customerField.getFieldLable());
                item.put("value",null);
                result.add(item);
            });
            String sql = selectPart.deleteCharAt(selectPart.length() - 1).append(fromPart).append(wherePart).toString();
            List<Object> data = dataWarehouseRepository.commonData(sql).get(0);
            for (int i =0;i < result.size();i++){
                result.get(i).put("value",data.get(i));
            }
        }
        return result;
    }

    @Override
    public List<Map<String, Object>> getEventLogList(String customerId, String channelId) {
        Sort sort = new Sort("sortNum");
        List<BehaviorField> behaviorFieldList = behaviorFieldRepository.findDimensionByChannelIdAndBehaviorType(channelId,"2",sort);
        StringBuffer selectPart = new StringBuffer("SELECT ");
        StringBuffer fromPart = new StringBuffer(" FROM " + behaviorFieldList.get(0).getBehavior().getFullTableName());
        StringBuffer wherePart = new StringBuffer(" WHERE event1.event_id != 'PageView' and " + behaviorFieldRepository.findEventPrimariField(channelId).getFullFieldName() + "= '" + customerId + "'");

        behaviorFieldList.forEach(customerField -> {
            selectPart.append(customerField.getFullFieldName()).append(",");
        });
        List<Map<String,Object>> result = new ArrayList<>();
        String sql = selectPart.deleteCharAt(selectPart.length() - 1).append(fromPart).append(wherePart).append(" order by event1.create_timestamp desc limit 20").toString();
        List<List<Object>> data = dataWarehouseRepository.commonData(sql);
        Map<String,String> eventDict = getEventDict(channelId);
        //获取事件所处的位置
        int eventPosition = findSpecialField(behaviorFieldList,"1");
        int datetimePosition = findSpecialField(behaviorFieldList,"0");
        for (int i =0;i < data.size();i++){
            String event_id = (String) data.get(i).get(eventPosition);
            String eventLable = eventDict.get(event_id);
            String dateTime = (String) data.get(i).get(datetimePosition);
            Map<String,Object> item = new HashMap<>();
            item.put("event",eventLable);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long lt = new Long(dateTime);
            Date date = new Date(lt);
            item.put("datetime",simpleDateFormat.format(date));
            item.put("data",convertData(behaviorFieldList,data.get(i)));
            result.add(item);
        }
        return result;
    }

    /**
     * 查找特殊的维度的索引  例如时间戳维度 0 和 事件维度 1
     * @return
     */
    private int findSpecialField( List<BehaviorField> behaviorFieldList,String type){
        String fieldName = type.equals("0")?"create_timestamp":"event_id";
        int result = -1;
        for(int i =0;i < behaviorFieldList.size();i++){
            if (behaviorFieldList.get(i).getFieldName().equals(fieldName)){
                result = i;
                break;
            }
        }
        return result;
    }

    private Map<String,String> getEventDict(String channelId){
        List<Event> events = eventRepository.findAllByChannelId(channelId);
        Map<String, String> dict = new HashMap<>();
        events.forEach(event -> {
            dict.put(event.getEventName(),event.getEventLable());
        });
        return dict;
    }

    private List<Map<String,Object>> convertData(List<BehaviorField> behaviorFieldList,List<Object> data){
        List<Map<String,Object>> result = new ArrayList<>();
        for (int i =0;i < behaviorFieldList.size();i++){
            Map<String,Object> item = new HashMap<>();
            item.put("lable",behaviorFieldList.get(i).getFieldLable());
            item.put("value",data.get(i));
            result.add(item);
        }
        return result;
    }

	@Override
	public List<String> getCustomerInGroupList(String customerId, String channelId) {
		CustomerGroup customerGroup = new CustomerGroup();
		customerGroup.setChannelId(channelId);
		CustomerGroupSpecification customerGroupSpecification = new CustomerGroupSpecification(customerGroup);
		List<CustomerGroup> customerGroups = customerGroupRepository.findAll(customerGroupSpecification);
		List<String> exitGroupIdList = new ArrayList<String>();
		List<String> naming = new ArrayList<String>();
		customerGroups.stream().forEach(cg ->{
			String groupid = cg.getId();
			String sql = String.format("SELECT COUNT(1) FROM %s.cg%s where uid = '%s'",cg.getChannel().getSchema(),groupid,customerId);
			Long count = (Long)dataWarehouseRepository.commonData(sql).get(0).get(0);
			if(count > 0) {
				exitGroupIdList.add(groupid);
			}
		});
		Iterator<CustomerGroup> it = customerGroupRepository.findAllById(exitGroupIdList).iterator();
		while(it.hasNext()) {
			naming.add(it.next().getGroupName());
		}
		return naming;
	}


}
