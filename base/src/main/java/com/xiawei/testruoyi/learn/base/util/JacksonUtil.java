package com.xiawei.testruoyi.learn.base.util;

import cn.hutool.core.util.ObjectUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.xiawei.testruoyi.learn.base.exceptions.SystemException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Jackson工具类 如果想要Jackson对应的注解生效，那么需要使用该工具类
 */
@Slf4j
public class JacksonUtil {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        //对象的所有字段全部列入
        MAPPER.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        //取消默认转换timestamps形式
        MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        //忽略空Bean转json的错误
        MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        //忽略 在json字符串中存在，但是在java对象中不存在对应属性的情况。防止错误
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        MAPPER.configure(DeserializationFeature.ACCEPT_FLOAT_AS_INT, true);
        SimpleModule module = new SimpleModule();
        module.addSerializer(Long.class, ToStringSerializer.instance);
        module.addSerializer(Long.TYPE, ToStringSerializer.instance);
        MAPPER.registerModule(module);
    }

    /**
     * 对象转化为json字符串
     *
     * @param obj 要转换的对象
     * @return 转化之后的json字符串
     */
    public static String toJsonStr(Object obj) {
        if (ObjectUtil.isNull(obj)) {
            return null;
        }
        try {
            return MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("Jackson对象转字符串失败", e);
            throw new SystemException(e);
        }
    }


    /**
     * 将JSON字符串转换为指定类型的Java对象
     *
     * @param json 待转换的JSON字符串
     * @param cls  目标对象的Class类型
     * @return 转换后的Java对象，如果json为空则返回null
     */
    public static <T> T toBean(String json, Class<T> cls) {
        // 检查输入的JSON字符串是否为空
        if (ObjectUtil.isEmpty(json)) {
            return null;
        }
        try {
            // 使用Jackson ObjectMapper将JSON字符串转换为指定类型的对象
            return MAPPER.readValue(json, cls);
        } catch (IOException e) {
            // 记录转换异常并抛出系统异常
            log.error("Json字符串转化为对象失败", e);
            throw new SystemException(e);
        }
    }

    /**
     * 将JSON字符串转换为指定类型的对象
     *
     * @param json          JSON字符串
     * @param typeReference 目标类型引用
     * @return 转换后的对象
     * @throws SystemException 当转换失败或参数无效时抛出
     */
    private static <T> T toObject(String json, TypeReference<T> typeReference) {
        try {
            // 检查JSON字符串是否为空
            if (ObjectUtil.isEmpty(json)) {
                return null;
            }
            // 检查类型引用是否为空
            if (ObjectUtil.isNull(typeReference)) {
                throw new SystemException("转换数据类型必传");
            }
            // 使用MAPPER进行JSON反序列化转换
            return MAPPER.readValue(json, typeReference);
        } catch (Exception e) {
            throw new SystemException("json数据转化为对象失败", e);
        }
    }

    /**
     * 将JSON字符串转换为List对象
     *
     * @param <T>  泛型类型参数，表示List中元素的类型
     * @param json 待转换的JSON字符串
     * @return 转换后的List对象，如果输入为空则返回null
     */
    public static <T> List<T> toList(String json) {
        // 检查输入的JSON字符串是否为空
        if (ObjectUtil.isEmpty(json)) {
            return null;
        }
        // 调用toObject方法将JSON字符串转换为List对象
        return toObject(json, new TypeReference<>() {
        });
    }

    /**
     * 将JSON字符串转换为Map对象
     *
     * @param <K>  Map的键类型
     * @param <V>  Map的值类型
     * @param json 待转换的JSON字符串
     * @return 转换后的Map对象，如果输入为空则返回null
     */
    public static <K, V> Map<K, V> toMap(String json) {
        // 检查输入的JSON字符串是否为空
        if (ObjectUtil.isEmpty(json)) {
            return null;
        }
        // 调用toObject方法将JSON字符串转换为Map对象
        return toObject(json, new TypeReference<>() {
        });
    }

    /**
     * 将JSON字符串转换为指定类型的List对象
     *
     * @param <T>   泛型类型参数，表示List中元素的类型
     * @param json  待转换的JSON字符串
     * @param clazz 目标List中元素的Class类型
     * @return 转换后的List对象，如果输入为空则返回null
     */
    public static <T> List<T> toList(String json, Class<T> clazz) {
        try {
            if (ObjectUtil.isEmpty(json)) {
                return null;
            }
            if (ObjectUtil.isNull(clazz)) {
                throw new SystemException("转换数据类型必传");
            }
            return MAPPER.readValue(json, MAPPER.getTypeFactory().constructParametricType(List.class, clazz));
        } catch (Exception e) {
            throw new SystemException("json数据转化为对象失败", e);
        }
    }

    /**
     * 获取ObjectMapper实例
     *
     * @return
     */
    public static ObjectMapper getMapper() {
        return MAPPER;
    }
}
