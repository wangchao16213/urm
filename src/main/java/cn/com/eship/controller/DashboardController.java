package cn.com.eship.controller;

import cn.com.eship.models.Dashboard;
import cn.com.eship.models.DashboardPanel;
import cn.com.eship.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("dashboard")
public class DashboardController {
    @Autowired
    private DashboardService dashboardService;

    @RequestMapping("dashboardPage")
    public String dashboardPage(String dashboardId, Model model) {
        Dashboard dashboard = dashboardService.findDashboardById(dashboardId);
        DashboardPanel dashboardPanel = new DashboardPanel();
        dashboardPanel.setDashboardId(dashboard.getId());
        List<DashboardPanel> dashboardPanelList = dashboardService.findDashboardPanelList(dashboardPanel);
        model.addAttribute("dashboard", dashboard);
        model.addAttribute("dashboardPanelList", dashboardPanelList);
        return "dashboard/dashboard";
    }


    @RequestMapping("getDashboardList")
    @ResponseBody
    public List<Dashboard> getDashboardList(Dashboard dashboard) {
        return dashboardService.findDashboardList(dashboard);
    }

    @RequestMapping("saveDashboardPanel")
    @ResponseBody
    public Map<String, Object> saveDashboardPanel(DashboardPanel dashboardPanel) {
        dashboardService.saveDashboardPanel(dashboardPanel);
        Map<String, Object> result = new HashMap<>();
        result.put("code", "0");
        result.put("msg", "保存成功");
        return result;
    }

    @RequestMapping("getDashboardPanelList")
    @ResponseBody
    public List<DashboardPanel> getDashboardPanelList(DashboardPanel dashboardPanel) {
        return dashboardService.findDashboardPanelList(dashboardPanel);
    }


    @RequestMapping("deleteDashboardPanel")
    @ResponseBody
    public String deleteDashboardPanel(String panelId) {
        dashboardService.deleteDashboardPanel(panelId);
        return "1";

    }
}
