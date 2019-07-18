package cn.com.eship.service;

import cn.com.eship.models.Channel;
import cn.com.eship.models.CustomerField;
import cn.com.eship.models.CustomerGroup;
import cn.com.eship.models.CustomerTemplate;

import java.util.List;
import java.util.Map;

public interface CustomerService {
    public List<CustomerField> findCustomerFields(CustomerField customerField);

    public List<CustomerField> findCustomerFieldsByChannelId(String channelId);

    public List<Channel> findChannelList(Channel channel);

    public List<CustomerGroup> findCustomerGroupList(CustomerGroup customerGroup);

    public void saveCustomerGroup(CustomerGroup customerGroup) throws Exception;
    public void updateCustomerGroup(CustomerGroup customerGroup);

    public CustomerGroup findCustomerGroupById(String customerGroupId);

    public void deleteCustomerGroupById(String customerGroupId);

    public List<CustomerField> findCustomerFieldListByChannelId(String channelId);

    List<List<Object>> getCustomerList(String channelId, String customerGroupId, String filter,String orderFieldId);
    public Long getCustomerCount(String channelId, String customerGroupId, String filter);

    public List<Map<String, Object>> getCustomerById(String customerId,String channelId);

    public List<Map<String,Object>> getEventLogList(String customerId,String channelId);
    
    public List<String> getCustomerInGroupList(String customerId,String channelId);
}
