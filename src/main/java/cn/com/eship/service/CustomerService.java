package cn.com.eship.service;

import cn.com.eship.models.Channel;
import cn.com.eship.models.CustomerField;
import cn.com.eship.models.CustomerGroup;
import cn.com.eship.models.CustomerTemplate;

import java.util.List;

public interface CustomerService {
    public List<CustomerField> findCustomerFields(CustomerField customerField);

    public List<CustomerField> findCustomerFieldsByChannelId(Integer channelId);

    public List<Channel> findChannelList(Channel channel);

    public List<CustomerGroup> findCustomerGroupList(CustomerGroup customerGroup);

    public void saveCustomerGroup(CustomerGroup customerGroup) throws Exception;
    public void updateCustomerGroup(CustomerGroup customerGroup);

    public CustomerGroup findCustomerGroupById(String customerGroupId);

    public void deleteCustomerGroupById(String customerGroupId);

    public List<CustomerField> findCustomerFieldListByChannelId(Integer channelId);

    List<List<Object>> getCustomerList(Integer channelId, String customerGroupId, String filter,String orderFieldId);
    public Long getCustomerCount(Integer channelId, String customerGroupId, String filter);
}
