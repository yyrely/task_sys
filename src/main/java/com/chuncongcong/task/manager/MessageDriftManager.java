package com.chuncongcong.task.manager;

import com.chuncongcong.task.dao.mapper.ext.MessageDriftExtMapper;
import com.chuncongcong.task.model.entity.MessageDrift;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Hu
 * @date 2018/8/15 16:44
 */

@Component
public class MessageDriftManager {

    private final MessageDriftExtMapper messageDriftExtMapper;

    public MessageDriftManager(MessageDriftExtMapper messageDriftExtMapper) {
        this.messageDriftExtMapper = messageDriftExtMapper;
    }

    public void addDriftMessage(MessageDrift messageDrift) {
        messageDriftExtMapper.insertSelective(messageDrift);
    }

    public List<MessageDrift> getDriftMessageList(Long fromUserId) {
        return messageDriftExtMapper.getDriftMessageList(fromUserId);
    }

    public MessageDrift getDriftMessage(Long driftId) {
        return messageDriftExtMapper.getDriftMessage(driftId);
    }

    public void updateDriftMessage(MessageDrift messageDrift) {
        messageDriftExtMapper.updateByPrimaryKeySelective(messageDrift);
    }

    public void removeDriftManager(Long driftId) {
        messageDriftExtMapper.removeDriftManager(driftId);
    }
}
