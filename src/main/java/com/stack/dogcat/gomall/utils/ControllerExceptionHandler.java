package com.stack.dogcat.gomall.utils;

/**
 * @Author Yang Jie
 * @Date 2021/6/9 22:16
 * @Descrition 统一异常处理、数据预处理等
 */
import com.stack.dogcat.gomall.commonResponseVo.SysResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


@ControllerAdvice
public class ControllerExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    /**
     * 校验异常统一处理
     * @param e
     * @return
     */
    @ExceptionHandler(value = BindException.class)
    @ResponseBody
    public SysResult validExceptionHandler(BindException e) {
        LOG.warn("参数校验失败：{}", e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        return SysResult.error("参数格式错误或缺少参数");
    }

    /**
     * 校验异常统一处理
     * @param e
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public SysResult validExceptionHandler(Exception e) {
        LOG.error("系统异常：", e);
        return SysResult.error("出现未知错误");
    }
}