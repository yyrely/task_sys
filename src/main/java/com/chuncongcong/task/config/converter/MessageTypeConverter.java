package com.chuncongcong.task.config.converter;

import com.chuncongcong.task.model.constants.MessageType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * @author Hu
 * @date 2018/8/16 10:46
 */

@Component
public class MessageTypeConverter implements Converter<String, MessageType> {

    @Override
    public MessageType convert(String value) {
        return MessageType.create(value);
    }
}
