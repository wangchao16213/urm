package cn.com.eship.service.impl;

import cn.com.eship.models.Channel;
import cn.com.eship.repository.ChannelRepository;
import cn.com.eship.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommonServiceImpl implements CommonService {
    @Autowired
    private ChannelRepository channelRepository;

    @Override
    public List<Channel> findAllChannel() {
        List<Channel> channelList = channelRepository.findAll(null);
        return channelList;
    }
}
