package cn.com.eship.service;

import cn.com.eship.models.Channel;
import cn.com.eship.models.SysDict;
import cn.com.eship.models.User;
import cn.com.eship.models.vo.RespModel;

import java.util.List;

public interface SystemService {
    public User validatLogin(User user);
    public List<SysDict> findSysDictList(SysDict sysDict);
    public SysDict findSysDictById(String dictId);
    public void saveSysDict(SysDict sysDict);
    public void deleteSysDict(String dictId);
    public void editSysDict(SysDict sysDict);
    public List<Channel> findChannelList(Channel channel);
    public void saveChannel(Channel channel);
    public Channel findChannelById(String channelId);
    public void editChannel(Channel channel);
    public RespModel deleteChannel(String channelId);
}
