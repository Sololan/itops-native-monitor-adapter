package com.wejoyclass.itops.util;

import com.wejoyclass.core.util.CtrlUtil;
import com.wejoyclass.itops.bean.ToNativeEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ToNativeUtil {
    private static final Logger log = LoggerFactory.getLogger(CtrlUtil.class);

    public ToNativeUtil() {
    }

    public static <T> ToNativeEntity<T> exe(ToNativeUtil.ToNativeHandler<T> handler) {
        long start = System.currentTimeMillis();

        ToNativeEntity toNativeEntity = new ToNativeEntity();
        try {
            handler.process(toNativeEntity);
        } catch (Throwable var9) {
            log.error("Api exception:",var9);
            toNativeEntity.setCode(Status.FAIL.getCode());
            toNativeEntity.setMessage("接口调用出现异常:"+var9);
        }

        return toNativeEntity.end(System.currentTimeMillis() - start);
    }

    @FunctionalInterface
    public interface ToNativeHandler<T>{
        void process(ToNativeEntity<T> result) throws Exception;
    }
}
