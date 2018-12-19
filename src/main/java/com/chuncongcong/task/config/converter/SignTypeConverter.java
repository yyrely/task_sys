package com.chuncongcong.task.config.converter;

import com.chuncongcong.task.model.constants.SignType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * @author Hu
 * @date 2018/8/16 10:46
 */

@Component
public class SignTypeConverter implements Converter<String, SignType> {

    @Override
    public SignType convert(String value) {
        return SignType.create(value);
    }
}
