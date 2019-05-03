package cn.com.eship.repository;

import cn.com.eship.models.Funnel;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FunnelRepository extends CrudRepository<Funnel, String>, JpaSpecificationExecutor<Funnel> {
}
