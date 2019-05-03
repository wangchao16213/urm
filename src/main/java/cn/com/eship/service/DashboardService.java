package cn.com.eship.service;

import cn.com.eship.models.Dashboard;
import cn.com.eship.models.DashboardPanel;

import java.util.List;

public interface DashboardService {
    public List<Dashboard> findAllDashboard();
    public Dashboard findDashboardById(String dashboardId);
    public List<DashboardPanel> findDashboardPanelList(DashboardPanel dashboardPanel);
    public List<Dashboard> findDashboardList(Dashboard dashboard);
    public void saveDashboardPanel(DashboardPanel dashboardPanel);
    public void deleteDashboardPanel(String panelId);
    public void saveDashboard(Dashboard dashboard);
    public void deleteDashboard(String dashboardId);
    public void editDashboard(Dashboard dashboard);
}
