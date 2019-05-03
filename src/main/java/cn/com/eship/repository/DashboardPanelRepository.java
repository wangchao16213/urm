package cn.com.eship.repository;

import cn.com.eship.models.DashboardPanel;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface DashboardPanelRepository extends CrudRepository<DashboardPanel, String>, JpaSpecificationExecutor<DashboardPanel> {
    @Modifying
    @Query("delete from DashboardPanel dashboardPanel where dashboardPanel.dashboard.id = ?1")
    public void deleteByDashboardId(String dashboardId);
}
