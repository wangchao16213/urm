package cn.com.eship.service.impl;

import cn.com.eship.models.Channel;
import cn.com.eship.models.Dashboard;
import cn.com.eship.models.DashboardPanel;
import cn.com.eship.repository.ChannelRepository;
import cn.com.eship.repository.DashboardPanelRepository;
import cn.com.eship.repository.DashboardRepository;
import cn.com.eship.repository.specifications.DashboardPanelSpecification;
import cn.com.eship.repository.specifications.DashboardSpecification;
import cn.com.eship.service.DashboardService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@EnableTransactionManagement
@Service
public class DashboardServiceImpl implements DashboardService {
    @Autowired
    private DashboardRepository dashboardRepository;
    @Autowired
    private DashboardPanelRepository dashboardPanelRepository;
    @Autowired
    private ChannelRepository channelRepository;

    @Override
    public List<Dashboard> findAllDashboard() {
        List<Dashboard> dashbarodList = new ArrayList<>();
        dashboardRepository.findAll().forEach(dashbarod -> {
            dashbarodList.add(dashbarod);
        });
        return dashbarodList;
    }

    @Override
    public Dashboard findDashboardById(String dashboardId) {
        if (StringUtils.isBlank(dashboardId)) {
            return dashboardRepository.findAll().iterator().next();
        }
        return dashboardRepository.findById(dashboardId).get();
    }

    @Override
    public List<DashboardPanel> findDashboardPanelList(DashboardPanel dashboardPanel) {
        DashboardPanelSpecification dashboardPanelSpecification = new DashboardPanelSpecification(dashboardPanel);
        List<DashboardPanel> dashboardPanels = dashboardPanelRepository.findAll(dashboardPanelSpecification);
        return dashboardPanels;
    }

    @Override
    public List<Dashboard> findDashboardList(Dashboard dashboard) {
        return dashboardRepository.findAll(new DashboardSpecification(dashboard));
    }

    @Override
    public void saveDashboardPanel(DashboardPanel dashboardPanel) {
        Dashboard dashboard = dashboardRepository.findById(dashboardPanel.getDashboardId()).get();
        dashboardPanel.setId(UUID.randomUUID().toString().replace("-", ""));
        dashboardPanel.setDashboard(dashboard);
        dashboardPanelRepository.save(dashboardPanel);
    }


    @Transactional
    @Override
    public void deleteDashboardPanel(String panelId) {
        dashboardPanelRepository.deleteById(panelId);
    }

    @Override
    public void saveDashboard(Dashboard dashboard) {
        Channel channel = channelRepository.findById(dashboard.getChannelId()).get();
        dashboard.setId(UUID.randomUUID().toString().replace("-", ""));
        dashboard.setCreateDate(new Date());
        dashboard.setChannel(channel);
        dashboardRepository.save(dashboard);
    }

    @Transactional
    @Override
    public void deleteDashboard(String dashboardId) {
        dashboardPanelRepository.deleteByDashboardId(dashboardId);
        dashboardRepository.deleteById(dashboardId);
    }

    @Transactional
    @Override
    public void editDashboard(Dashboard dashboard) {
        Channel channel = channelRepository.findById(dashboard.getChannelId()).get();
        Dashboard dashboard1 = dashboardRepository.findById(dashboard.getId()).get();
        BeanUtils.copyProperties(dashboard,dashboard1);
        dashboard1.setChannel(channel);
        dashboard1.setCreateDate(new Date());
        dashboardRepository.save(dashboard1);
    }
}
