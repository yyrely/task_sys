package com.chuncongcong.task.config.converter;

import com.chuncongcong.task.model.constants.ReadType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * @author Hu
 * @date 2018/8/16 10:46
 */

@Component
public class ReadTypeConverter implements Converter<String, ReadType> {

    @Override
    public ReadType convert(String value) {
        return ReadType.create(value);
    }
}
