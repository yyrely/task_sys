package com.chuncongcong.task.utils;

import com.chuncongcong.task.model.constants.ReadType;
import com.chuncongcong.task.model.vo.MessageBoardVO;

import java.util.List;

/**
 * @author Hu
 * @date 2018/8/21 16:24
 */


public class MessageBoardUtils {

    public static List<MessageBoardVO> isReadComment(List<MessageBoardVO> messageList, String username) {
        for (MessageBoardVO messageBoardVO : messageList) {
            if (messageBoardVO.getIsRead().ordinal() == 1) {
                messageBoardVO.setComments(null);
                continue;
            }
            for (MessageBoardVO comment : messageBoardVO.getComments()) {
                if (comment.getIsRead().ordinal() == 1 && !comment.getFromUserName().equals(username)) {
                    messageBoardVO.setIsRead(ReadType.UNREAD);
                    messageBoardVO.setUpdateTime(comment.getCreateTime());
                }
            }
            messageBoardVO.setComments(null);
        }
        return messageList;
    }
}
