package cn.com.eship.controller;

import cn.com.eship.models.BehaviorField;
import cn.com.eship.models.Channel;
import cn.com.eship.models.CustomerGroup;
import cn.com.eship.models.Funnel;
import cn.com.eship.models.vo.EventAnalyticsVo;
import cn.com.eship.models.vo.FunnelAnalyticsVo;
import cn.com.eship.models.vo.HitAnalyticsVo;
import cn.com.eship.service.CustomerService;
import cn.com.eship.service.InsightService;
import cn.com.eship.service.SceneService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("insight")
public class InsightController {
    @Autowired
    private SceneService sceneService;
    @Autowired
    private InsightService insightService;
    @Autowired
    private CustomerService customerService;
    private ObjectMapper objectMapper = new ObjectMapper();

    @RequestMapping("eventAnalyticsPage")
    public String eventAnalyticsPage(Model model) {
        List<Channel> channelList = sceneService.findAllChannel();
        model.addAttribute("channelList", channelList);
        return "insight/eventAnalytics";
    }


    /**
     * 事件分析
     *
     * @param channelId
     * @param eventId
     * @param dimension  该维度是传过来对应数仓表的列名
     * @param measureId
     * @param chartType
     * @param dateranger
     * @param filter
     * @return
     */
    @RequestMapping("eventAnalytics")
    @ResponseBody
    public EventAnalyticsVo eventAnalytics(String channelId, String eventId, String dimension, String measureId, String chartType, String dateranger, String filter, String customerGroupId) {
        return insightService.eventAnalytics(channelId, eventId, dimension, measureId, chartType, dateranger, filter, customerGroupId);
    }


    @RequestMapping("hitAnalyticsPage")
    public String hitAnalyticsPage(Model model) {
        List<Channel> channelList = sceneService.findAllChannel();
        model.addAttribute("channelList", channelList);
        return "insight/hitAnalytics";
    }


    @RequestMapping("hitAnalytics")
    @ResponseBody
    public HitAnalyticsVo hitAnalytics(String channelId, String dimension, String measureId, String chartType, String dateranger, String filter, String customerGroupId) {
        return insightService.hitAnalytics(channelId, dimension, measureId, chartType, dateranger, filter, customerGroupId);
    }

    @RequestMapping("funnelListPage")
    public String funnelListPage(Model model) {
        List<Channel> channelList = sceneService.findAllChannel();
        model.addAttribute("channelList", channelList);
        return "insight/funnelList";
    }


    @RequestMapping("getFunnelList")
    @ResponseBody
    public List<Funnel> getFunnelList(Funnel funnel) {
        return insightService.findFunnelList(funnel);
    }

    @RequestMapping("addFunnelPage")
    public String addFunnelPage(Model model) {
        List<Channel> channelList = sceneService.findAllChannel();
        model.addAttribute("channelList", channelList);
        return "insight/addFunnel";
    }

    @RequestMapping("saveFunnel")
    @ResponseBody
    public Map<String, String> saveFunnel(Funnel funnel, String funnelDetailJson) throws IOException {
        List<Map<String, Object>> funnelDetailList = objectMapper.readValue(funnelDetailJson, List.class);
        insightService.saveFunnel(funnel, funnelDetailList);
        Map<String, String> result = new HashMap<>();
        result.put("code", "0");
        result.put("mgs", "保存成功");
        return result;
    }


    @RequestMapping("deleteFunnel")
    public String deleteFunnel(String funnelId) {
        insightService.deleteFunnel(funnelId);
        return "redirect:/insight/funnelListPage";
    }


    @RequestMapping("funnelAnalyticsPage")
    public String funnelAnalyticsPage(String funnelId, Model model) {
        Funnel funnel = insightService.findFunnelById(funnelId);
        CustomerGroup customerGroup = new CustomerGroup();
        customerGroup.setChannelId(funnel.getChannel().getId());
        List<CustomerGroup> customerGroupList = customerService.findCustomerGroupList(customerGroup);
        List<BehaviorField> measures = sceneService.findMeasureForFunnel(funnel.getChannel().getId());
        model.addAttribute("customerGroupList", customerGroupList);
        model.addAttribute("funnel", funnel);
        model.addAttribute("measures", measures);
        return "insight/funnelAnalytics";

    }


    @RequestMapping("funnelAnalytics")
    @ResponseBody
    public FunnelAnalyticsVo funnelAnalytics(String funnelId, String measureId, String dateranger,String customerGroupId) {
        return insightService.funnelAnalytics(funnelId, measureId, dateranger,customerGroupId);
    }

    /**
     * 转化分析编辑
     *
     * @param funnelId
     * @return
     */
    @RequestMapping("editFunnelPage")
    public String editFunnelPage(String funnelId, Model model) {
        List<Channel> channelList = sceneService.findAllChannel();
        model.addAttribute("channelList", channelList);
        Funnel funnel = insightService.findFunnelById(funnelId);
        model.addAttribute("funnel", funnel);
        return "insight/editFunnel";
    }

}
