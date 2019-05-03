package cn.com.eship.repository;

import cn.com.eship.models.Funnel;
import cn.com.eship.models.FunnelDetail;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FunnelDetailRepository extends CrudRepository<FunnelDetail, String>, JpaSpecificationExecutor<Funnel> {
    @Modifying
    @Query("delete from FunnelDetail where funnel.id = ?1")
    public void deleteAllByFunnelId(String funnelId);

    @Query("select funnelDetail from FunnelDetail funnelDetail where funnelDetail.funnel.id = ?1")
    public List<FunnelDetail> findByFunnelId(String funnelId, Sort sort);
}
