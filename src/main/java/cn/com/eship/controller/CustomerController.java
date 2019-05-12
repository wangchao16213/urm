package cn.com.eship.controller;

import cn.com.eship.models.Behavior;
import cn.com.eship.models.BehaviorField;
import cn.com.eship.models.Channel;
import cn.com.eship.models.CustomerField;
import cn.com.eship.models.CustomerGroup;
import cn.com.eship.models.enums.BehaviorFieldType;
import cn.com.eship.models.enums.BehaviorType;
import cn.com.eship.service.CustomerService;
import cn.com.eship.service.SceneService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("customer")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @Autowired
    private SceneService sceneService;

    @RequestMapping("customerFields")
    @ResponseBody
    public List<CustomerField> customerFields(CustomerField customerField) {
        return this.customerService.findCustomerFields(customerField);
    }


    @RequestMapping("getCustomerFieldsByChannelId")
    @ResponseBody
    public List<CustomerField> getCustomerFieldsByChannelId(String channelId) {
        return this.customerService.findCustomerFieldsByChannelId(channelId);
    }


    @RequestMapping("customerGroupListPage")
    public String customerGroupListPage(Model model) {
        List<Channel> channelList = customerService.findChannelList(null);
        model.addAttribute("channelList", channelList);
        return "customer/customerGroupList";
    }


    @RequestMapping("getCustomerGroupList")
    @ResponseBody
    public List<CustomerGroup> getCustomerGroupList(CustomerGroup customerGroup) {
        return customerService.findCustomerGroupList(customerGroup);
    }


    @RequestMapping("addCustomerGroup")
    public String addCustomerGroup(Model model) {
        List<Channel> channelList = customerService.findChannelList(null);
        model.addAttribute("channelList", channelList);
        return "customer/addCustomerGroup";
    }


    @RequestMapping("saveCustomerGroup")
    @ResponseBody
    public String saveCustomerGroup(CustomerGroup customerGroup) throws Exception {
        customerService.saveCustomerGroup(customerGroup);
        return "0";
    }

    @RequestMapping("updateCustomerGroup")
    @ResponseBody
    public String updateCustomerGroup(CustomerGroup customerGroup) {
        customerService.updateCustomerGroup(customerGroup);
        return "0";
    }


    @RequestMapping("editCustomerGroupPage")
    public String editCustomerGroupPage(String customerGroupId, Model model) {
        List<Channel> channelList = customerService.findChannelList(null);
        model.addAttribute("channelList", channelList);
        model.addAttribute("customerGroupId", customerGroupId);
        return "customer/editCustomerGroup";
    }

    @RequestMapping("getCustomerGroupById")
    @ResponseBody
    public CustomerGroup getCustomerGroupById(String customerGroupId) {
        return customerService.findCustomerGroupById(customerGroupId);
    }


    @RequestMapping("deleteCustomerGroupById")
    public String deleteCustomerGroupById(String customerGroupId) {
        customerService.deleteCustomerGroupById(customerGroupId);
        return "redirect:/customer/customerGroupListPage";
    }


    @RequestMapping("customerListPage")
    public String customerListPage(String customerGroupId, Model model) {
        //从群组页面进来的customerGroupId不为空
        if (StringUtils.isNotBlank(customerGroupId)) {
            CustomerGroup customerGroup = customerService.findCustomerGroupById(customerGroupId);
            model.addAttribute("customerGroupId", customerGroup.getId());
            model.addAttribute("channelId", customerGroup.getChannel().getId());
        }

        List<Channel> channelList = customerService.findChannelList(null);
        model.addAttribute("channelList", channelList);
        return "customer/customerList";
    }


    @RequestMapping("getCustomerFields")
    @ResponseBody
    public List<CustomerField> getCustomerFields(String channelId) {
        return customerService.findCustomerFieldsByChannelId(channelId);
    }


    @RequestMapping("getCustomerList")
    @ResponseBody
    public Map<String, Object> getCustomerList(String channelId, String customerGroupId, String filter, String orderFieldId) {
        Map<String, Object> map = new HashMap<>();
        map.put("data", customerService.getCustomerList(channelId, customerGroupId, filter, orderFieldId));
        map.put("customerCount", customerService.getCustomerCount(channelId, customerGroupId, filter));
        return map;
    }


    @RequestMapping("getCustomerCount")
    @ResponseBody
    public Long getCustomerCount(String channelId, String customerGroupId, String filter, String orderFieldId) {
        return customerService.getCustomerCount(channelId, customerGroupId, filter);
    }
    @RequestMapping("customerProfilePage")
    public String customerProfilePage(String customerId,String channelId,Model model){
        //查询事件下的所有维度
        List<BehaviorField> behaviorFieldList = sceneService.findDimensionByChannelIdAndBehaviorType(channelId,"2");
        model.addAttribute("customer",customerService.getCustomerById(customerId,channelId));
        model.addAttribute("behaviorFieldList",behaviorFieldList);
        model.addAttribute("customerId",customerId);
        model.addAttribute("channelId",channelId);
        return "customer/customerProfile";
    }


    @RequestMapping("behaviorFieldList")
    @ResponseBody
    public List<BehaviorField> behaviorFieldList(String channelId){
        List<BehaviorField> behaviorFieldList = sceneService.findDimensionByChannelIdAndBehaviorType(channelId,"2");
        return behaviorFieldList;
    }

    @RequestMapping("eventLogList")
    @ResponseBody
    public List<Map<String,Object>> eventLogList(String customerId,String channelId){
        return customerService.getEventLogList(customerId,channelId);
    }

}
