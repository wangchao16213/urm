package cn.com.eship.service;

import cn.com.eship.models.Channel;

import java.util.List;

public interface CommonService {
    /**
     * 查询所有的渠道
     * @return
     */
    public List<Channel> findAllChannel();
}
