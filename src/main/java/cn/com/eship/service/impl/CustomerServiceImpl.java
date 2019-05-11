package cn.com.eship.service.impl;

import cn.com.eship.context.ContextSetting;
import cn.com.eship.models.Channel;
import cn.com.eship.models.CustomerField;
import cn.com.eship.models.CustomerGroup;
import cn.com.eship.models.CustomerTemplate;
import cn.com.eship.repository.ChannelRepository;
import cn.com.eship.repository.CustomerFieldRepository;
import cn.com.eship.repository.CustomerGroupRepository;
import cn.com.eship.repository.DataWarehouseRepository;
import cn.com.eship.repository.specifications.ChannelSpecification;
import cn.com.eship.repository.specifications.CustomerFieldSpecification;
import cn.com.eship.repository.specifications.CustomerGroupSpecification;
import cn.com.eship.service.CustomerService;
import cn.com.eship.tasks.CustomerGroupTask;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    @Override
    public List<CustomerField> findCustomerFields(CustomerField customerField) {
        CustomerFieldSpecification customerFieldSpecification = new CustomerFieldSpecification(customerField);
        return customerFieldRepository.findAll(customerFieldSpecification);
    }

    @Override
    public List<CustomerField> findCustomerFieldsByChannelId(String channelId) {
        return customerFieldRepository.findByChannelId(channelId);
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
        return customerFieldRepository.findByChannelId(channelId);
    }

    @Override
    public List<List<Object>> getCustomerList(String channelId, String customerGroupId, String filter, String orderFieldId) {
        List<CustomerField> customerFieldList = customerFieldRepository.findByChannelId(channelId);
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
        List<CustomerField> customerFieldList = customerFieldRepository.findByChannelId(channelId);
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


}
