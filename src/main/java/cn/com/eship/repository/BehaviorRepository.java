package cn.com.eship.repository;

import cn.com.eship.models.Behavior;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface BehaviorRepository extends CrudRepository<Behavior, String>, JpaSpecificationExecutor<Behavior> {
    @Query("select behavior from Behavior behavior where behavior.channel.id = ?1 and behavior.behaviorType = '2'")
    public Behavior findEventBehaviorByChannelId(String channelId);


    @Query("select behavior from Behavior behavior where behavior.channel.id = ?1 and behavior.behaviorType = '0'")
    public Behavior findHitBehaviorByChannelId(String channelId);
}
