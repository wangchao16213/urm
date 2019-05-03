package cn.com.eship.repository;

import cn.com.eship.models.Channel;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface ChannelRepository extends CrudRepository<Channel, String>, JpaSpecificationExecutor<Channel> {
}
