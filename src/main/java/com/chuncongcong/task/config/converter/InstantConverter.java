package com.chuncongcong.task.config.converter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.Instant;

/**
 * @author yang
 * @date 2018/10/16
 */
@Component
public class InstantConverter implements Converter<String, Instant> {
    @Override
    public Instant convert(String s) {

        if (StringUtils.isEmpty(s)) {
            return null;
        }
        return Instant.ofEpochSecond(Long.valueOf(s));
    }
}
