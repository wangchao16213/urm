package cn.com.eship.repository;

import cn.com.eship.models.BehaviorField;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BehaviorFieldRepository extends CrudRepository<BehaviorField, String>, JpaSpecificationExecutor<BehaviorField> {
    @Query("select behaviorField from BehaviorField behaviorField where behaviorField.aggFlag = 0 and behaviorField.behavior.channel.id = ?1 and behaviorField.behavior.behaviorType = ?2")
    public List<BehaviorField> findDimensionByChannelIdAndBehaviorType(String channelId, String behaviorType);

    @Query("select behaviorField from BehaviorField behaviorField where behaviorField.aggFlag = 0 and behaviorField.behavior.channel.id = ?1 and behaviorField.behavior.behaviorType = ?2")
    public List<BehaviorField> findDimensionByChannelIdAndBehaviorType(String channelId, String behaviorType, Sort sort);


    @Query("select behaviorField from BehaviorField behaviorField where behaviorField.aggFlag = 1 and behaviorField.behavior.channel.id = ?1 and behaviorField.behavior.behaviorType = ?2")
    public List<BehaviorField> findMeasureByChannelIdAndBehaviorType(String channelId, String behaviorType);

    /**
     * 根据渠道获取事件层的量度
     *
     * @param channelId
     * @return
     */
    @Query("select behaviorField from BehaviorField behaviorField where behaviorField.behavior.channel.id = ?1 and behaviorField.behavior.behaviorType = '2' and behaviorField.aggFlag = 1")
    public List<BehaviorField> findMeasureByChannelIdInEventLayer(String channelId);

    /**
     * 根据渠道获取点击层的量度
     *
     * @param channelId
     * @return
     */
    @Query("select behaviorField from BehaviorField behaviorField where behaviorField.behavior.channel.id = ?1 and behaviorField.behavior.behaviorType = '0' and behaviorField.aggFlag = 1")
    public List<BehaviorField> findMeasureInHitByChannelId(String channelId);


    /**
     * 根据渠道获取点击层的维度
     *
     * @param channelId
     * @return
     */
    @Query("select behaviorField from BehaviorField behaviorField where behaviorField.behavior.channel.id = ?1 and behaviorField.behavior.behaviorType = '0' and behaviorField.aggFlag = 0")
    public List<BehaviorField> findDimensionInHitByChannelId(String channelId);


    @Query("SELECT behaviorField from BehaviorField behaviorField where behaviorField.behavior.channel.id = ?1 and behaviorField.behavior.behaviorType = '2' and behaviorField.aggFlag = 1 and behaviorField.measureType = '0'")
    public List<BehaviorField> findMeasureForFunnel(String channelId);

    /**
     * 根据行为ID和行为类型获取行为表主键
     * @param behavioId
     * @param behaviorType
     * @return
     */
    @Query("SELECT behaviorField FROM BehaviorField behaviorField where behaviorField.isKey = 1 and behaviorField.behavior.id = ?1 and behaviorField.behavior.behaviorType = ?2")
    public BehaviorField findKeyFieldByBehavioIdAndBehaviorType(String behavioId,String behaviorType);

    @Query("SELECT behaviorField FROM BehaviorField behaviorField where behaviorField.isKey = 1 and behaviorField.behavior.id = ?1")
    public BehaviorField findKeyFieldByBehavioId(String behavioId);

    @Query("SELECT behaviorField FROM BehaviorField behaviorField where behaviorField.behavior.behaviorType = '2' and behaviorField.isKey = 1 and behaviorField.behavior.channel.id = ?1")
    public BehaviorField findEventPrimariField(String channelId);
}
