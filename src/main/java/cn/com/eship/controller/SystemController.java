package cn.com.eship.controller;

import cn.com.eship.models.Channel;
import cn.com.eship.models.Dashboard;
import cn.com.eship.models.Event;
import cn.com.eship.models.SysDict;
import cn.com.eship.models.User;
import cn.com.eship.models.vo.RespModel;
import cn.com.eship.service.DashboardService;
import cn.com.eship.service.SceneService;
import cn.com.eship.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("system")
public class SystemController {
    @Autowired
    private SystemService systemService;
    @Autowired
    private DashboardService dashboardService;
    @Autowired
    private SceneService sceneService;

    @RequestMapping("loginPage")
    public String loginPage() {
        return "login";
    }

    @RequestMapping("login")
    public String login(User user, HttpSession session, Model model) {
        User user1 = systemService.validatLogin(user);
        if (user1 != null) {
            User userInfo = new User();
            userInfo.setId(user1.getId());
            userInfo.setName(user1.getName());
            userInfo.setUserName(user1.getUserName());
            session.setAttribute("userInfo", userInfo);
            List<Dashboard> dashbarods = dashboardService.findAllDashboard();
            session.setAttribute("dashbarodList", dashbarods);
            return "redirect:/dashboard/dashboardPage";
        }
        model.addAttribute("error", "请检查用户名和密码");
        return "login";
    }


    @RequestMapping("loginout")
    public String loginout(HttpSession session) {
        session.removeAttribute("userInfo");
        return "redirect:/system/loginPage";
    }

    @RequestMapping("systemDictListPage")
    public String systemDictListPage() {
        return "system/dictmg/systemDictList";
    }

    @RequestMapping("sysDictList")
    @ResponseBody
    public List<SysDict> sysDictListList(SysDict sysDict) {
        return systemService.findSysDictList(sysDict);
    }

    @RequestMapping("addSysDictPage")
    public String addSysDictPage() {
        return "system/dictmg/addSysDict";
    }


    @RequestMapping("saveSysDict")
    public String saveSysDict(SysDict sysDict) {
        systemService.saveSysDict(sysDict);
        return "redirect:/system/systemDictListPage";
    }

    @RequestMapping("deleteSysDictById")
    public String deleteSysDictById(String dictId) {
        systemService.deleteSysDict(dictId);
        return "redirect:/system/systemDictListPage";
    }

    @RequestMapping("editSysDictPage")
    public String editSysDictPage(String dictId, Model model) {
        SysDict sysDict = systemService.findSysDictById(dictId);
        model.addAttribute("sysDict", sysDict);
        return "system/dictmg/editSysDict";
    }

    @RequestMapping("editSysDict")
    public String editSysDict(SysDict sysDict) {
        systemService.editSysDict(sysDict);
        return "redirect:/system/systemDictListPage";
    }


    @RequestMapping("dashboardListPage")
    public String dashboardListPage(Dashboard dashboard, Model model) {
        List<Dashboard> dashboardList = dashboardService.findDashboardList(dashboard);
        model.addAttribute("dashboardList", dashboardList);
        List<Channel> channelList = sceneService.findAllChannel();
        model.addAttribute("channelList", channelList);

        model.addAttribute("dashboardForm", dashboard);
        return "system/dashboardmg/dashboardList";
    }

    @RequestMapping("addDashboardPage")
    public String addDashboardPage(Model model) {
        List<Channel> channelList = sceneService.findAllChannel();
        model.addAttribute("channelList", channelList);
        return "system/dashboardmg/addDashboard";
    }

    @RequestMapping("deleteDashboardById")
    public String deleteDashboardById(String dashboardId) {
        dashboardService.deleteDashboard(dashboardId);
        return "redirect:/system/dashboardListPage";
    }

    @RequestMapping("saveDashboard")
    public String saveDashboard(Dashboard dashboard) {
        dashboardService.saveDashboard(dashboard);
        return "redirect:/system/dashboardListPage";
    }

    @RequestMapping("editDashboardPage")
    public String editDashboardPage(String dashboardId, Model model) {
        Dashboard dashboard = dashboardService.findDashboardById(dashboardId);
        model.addAttribute("dashboard", dashboard);
        List<Channel> channelList = sceneService.findAllChannel();
        model.addAttribute("channelList", channelList);
        return "system/dashboardmg/editDashboard";
    }


    @RequestMapping("editDashboard")
    public String editDashboard(Dashboard dashboard) {
        dashboardService.editDashboard(dashboard);
        return "redirect:/system/dashboardListPage";
    }


    @RequestMapping("channelListPage")
    public String channelListPage(Channel channel, Model model) {
        List<Channel> channelList = systemService.findChannelList(channel);
        model.addAttribute("channelList", channelList);
        return "system/channelmg/channelList";
    }

    @RequestMapping("addChannel")
    public String addChannel(Channel channel) {
        systemService.saveChannel(channel);
        return "redirect:/system/channelListPage";
    }

    @RequestMapping("addChannelPage")
    public String addChannelPage() {
        return "system/channelmg/addChannel";
    }

    @RequestMapping("editChannelPage")
    public String editChannelPage(String channelId, Model model) {
        Channel channel = systemService.findChannelById(channelId);
        model.addAttribute("channel", channel);
        return "system/channelmg/editChannel";
    }

    @RequestMapping("editChannel")
    public String editChannel(Channel channel) {
        systemService.editChannel(channel);
        return "redirect:/system/channelListPage";
    }


    @RequestMapping("deleteChannel")
    public RespModel deleteChannel(String channelId){
        return null;
    }

    @RequestMapping("eventListPage")
    public String eventListPage(Event eventForm,Model model){
        List<Channel> channelList = systemService.findChannelList(null);
        model.addAttribute("channelList", channelList);
        List<Event> eventList =  sceneService.findEvents(eventForm);
        model.addAttribute("eventForm", eventForm);
        model.addAttribute("eventList", eventList);
        return "system/eventmg/eventList";
    } 
}
