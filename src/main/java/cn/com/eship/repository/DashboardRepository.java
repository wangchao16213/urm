package cn.com.eship.repository;
import cn.com.eship.models.Dashboard;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface DashboardRepository extends CrudRepository<Dashboard, String>, JpaSpecificationExecutor<Dashboard> {
}
