package com.chuncongcong.task.config.interceptor;

import com.chuncongcong.task.context.RequestContext;
import com.chuncongcong.task.exception.BaseErrorCode;
import com.chuncongcong.task.exception.ServiceException;
import com.chuncongcong.task.model.constants.Contants;
import com.chuncongcong.task.model.entity.Users;
import com.chuncongcong.task.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

@Component
public class LoginInterceptor implements HandlerInterceptor {


    private static final Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);
    private final StringRedisTemplate stringRedisTemplate;
    @Value("${token.timeout}")
    private Integer tokenTimeout;

    public LoginInterceptor(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        RequestContext.setRequestTime(System.currentTimeMillis());
        if (request.getMethod().equalsIgnoreCase(HttpMethod.OPTIONS.name())) {
            return true;
        }
        logger.debug("===> method: {},path: {}", request.getMethod(), request.getRequestURI());
        String token = request.getHeader("token");
        String userJson = stringRedisTemplate.opsForValue().get(Contants.ONLINE_TOKEN + token);

        if (StringUtils.isBlank(userJson)) {
            logger.error("===> user not login,token: {}", token);
            throw new ServiceException(BaseErrorCode.NOT_LOGIN);
        }

        if (userJson.equals("other login")) {
            logger.error("===> user other login,token: {}", token);
            throw new ServiceException(BaseErrorCode.OTHER_LOGIN);
        }

        Users users = JsonUtils.toObject(userJson, Users.class);
        stringRedisTemplate.expire(Contants.ONLINE_USER + users.getUsername(), tokenTimeout, TimeUnit.SECONDS);
        stringRedisTemplate.expire(Contants.ONLINE_TOKEN + token, tokenTimeout, TimeUnit.SECONDS);

        RequestContext.setUserInfo(users);
        logger.debug("===> check token,cost {}ms", System.currentTimeMillis() - RequestContext.getRequestTime());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        logger.debug("<=== api cost {} ms.", System.currentTimeMillis() - RequestContext.getRequestTime());
    }
}
