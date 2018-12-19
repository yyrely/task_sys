package com.chuncongcong.task.controller;

import com.chuncongcong.task.model.constants.MessageType;
import com.chuncongcong.task.model.constants.ReadType;
import com.chuncongcong.task.model.constants.SignType;
import com.chuncongcong.task.model.dto.MessageBoardDTO;
import com.chuncongcong.task.model.dto.MessageDriftDTO;
import com.chuncongcong.task.model.entity.MessageBoard;
import com.chuncongcong.task.service.MessageService;
import org.springframework.web.bind.annotation.*;

/**
 * @author Hu
 * @date 2018/8/10 10:20
 */

@RestController
@RequestMapping("/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    /**
     * 添加留言
     *
     * @param messageBoardDTO
     * @return
     */
    @PostMapping
    public Object addMessage(
            @RequestBody MessageBoardDTO messageBoardDTO) throws Exception {
        return messageService.addMessage(messageBoardDTO);
    }

    /**
     * 获取留言列表
     * @param messageType
     * @param read
     * @return
     */
    @GetMapping
    public Object getMessage(
            @RequestParam(value = "messageType", defaultValue = "RECEIVE") MessageType messageType,
            @RequestParam(value = "isRead", defaultValue = "UNKNOWN") ReadType read,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        return messageService.getMessageList(messageType, read, page, pageSize);
    }

    /**
     * 获取留言详情
     * @param messageId
     * @return
     */
    @GetMapping("/{messageId}")
    public Object getMessage(
            @PathVariable("messageId") Long messageId) {
        return messageService.getMessage(messageId);
    }

    /**
     * 更新留言收藏状态
     *
     * @param messageId
     * @param sign
     * @return
     */
    @PutMapping("/{messageId}/signs")
    public Object updateMessageSign(
            @PathVariable("messageId") Long messageId,
            @RequestParam(value = "sign", defaultValue = "UNKNOWN") SignType sign) {
        return messageService.updateMessageSign(messageId, sign);
    }

    /**
     * 删除留言
     *
     * @param messageId
     * @return
     * @throws Exception
     */
    @DeleteMapping("/{messageId}")
    public Object removeMessage(
            @PathVariable("messageId") Long messageId) {
        return messageService.removeMessage(messageId);
    }

    /**
     * 添加评论
     *
     * @param messageId
     * @param comment
     * @return
     */
    @PostMapping("/{messageId}/comments")
    public Object addComment(
            @PathVariable("messageId") Long messageId,
            @RequestBody MessageBoard comment) {
        return messageService.addComment(messageId, comment);
    }

    /**
     * 删除评论
     * @param messageId
     * @param commentId
     * @return
     */
    @DeleteMapping("/{messageId}/comments/{commentId}")
    public Object removeComment(
            @PathVariable("messageId") Long messageId,
            @PathVariable("commentId") Long commentId) {
        return messageService.removeComment(messageId, commentId);
    }

    /**
     * 保存为草稿
     *
     * @param messageDriftDTO
     * @return
     */
    @PostMapping("/drifts")
    public Object addDrift(
            @RequestBody MessageDriftDTO messageDriftDTO) {
        return messageService.addDrift(messageDriftDTO);
    }

    /**
     * 获取草稿列表
     *
     * @return
     */
    @GetMapping("/drifts")
    public Object getDriftMessageList(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        return messageService.getDriftMessageList(page, pageSize);
    }

    /**
     * 获取草稿详情
     *
     * @return
     */
    @GetMapping("/drifts/{driftId}")
    public Object getDriftMessage(
            @PathVariable("driftId") Long driftId) {
        return messageService.getDriftMessage(driftId);
    }

    /**
     * 更新草稿详情
     *
     * @return
     */
    @PutMapping("/drifts/{driftId}")
    public Object updateDriftMessage(
            @PathVariable("driftId") Long driftId,
            @RequestBody MessageDriftDTO messageDriftDTO) {
        return messageService.updateDriftMessage(driftId, messageDriftDTO);
    }

    /**
     * 删除草稿
     * @param driftId
     * @return
     */
    @DeleteMapping("/drifts/{driftId}")
    public Object removeDriftMessage(
            @PathVariable("driftId") Long driftId) {
        return messageService.removeDriftMessage(driftId);
    }

}
