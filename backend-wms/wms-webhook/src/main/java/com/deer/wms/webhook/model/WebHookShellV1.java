package com.deer.wms.webhook.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import cn.hutool.crypto.SecureUtil;
import lombok.Data;

/**
 * WebHook外壳 V1 <br>
 * 
 * V1版本均为POST请求，body以json形式序列化发送本实体。不支持其他格式数据与Method
 * 
 * @author xuesinuo
 *
 */
@Data
public class WebHookShellV1 {
    public static final String TIME_PATTERN = "yyyyMMddHHmmss";

    /**
     * 推送数据
     */
    private Object data;
    /**
     * 版本，不同版本外壳格式、签名方式不同
     */
    private Integer version = 1;
    /**
     * 推送时间
     */
    private LocalDateTime time;
    /**
     * 本次推送的uuid
     */
    private String uuid;
    /**
     * 签名
     * 
     * <pre>
     * version=1时，签名方式如下：
     *   secretKey：配置WebHook时生成、记录并下发给了接收端
     *   sign=md5(time.as({@link WebHookShellV1}.TIME_PATTERN)+uuid+secretKey)，WMS端如此加密
     *   接收端重复此加密过程，得到sign，对比一致则验签成功
     *   接收端可以选择（安全性递增）：
     *     不验签：可以被随意仿造
     *     单次验签：不会被仿造，但签名可重复使用，如果签名被获取到，则后续可以重复使用
     *     验签并记录签名：一组签名只一次有效，不宜仿造，但记录历史UUID并验重复会越积越多
     *     验签并记录签名、定期失效：失效时长有接收方决定。如5分钟，则缓存5分钟内的UUID并判重，5分钟以上的UUID从缓存清除，time时间差大于5分钟的WebHook不再理会，或者返回500
     *   此签名方式不能防data篡改，WebHook被完全拦截并篡改时，无可奈何，需要新的验证方式，
     *   但data不参与验签预算，节省算力，节省代码（这是通用套壳，data内格式可能千差万别）。
     * ======
     * 其他版本未开放
     * </pre>
     */
    private String sign;

    /**
     * 制作签名
     * 
     * @param secretKey 秘钥
     */
    public void doSign(String secretKey) {
        if (this.getTime() == null || this.getUuid() == null || secretKey == null) {
            return;
        }
        DateTimeFormatter dfDate = DateTimeFormatter.ofPattern(WebHookShellV1.TIME_PATTERN);
        String timeString = dfDate.format(this.getTime());
        this.sign = SecureUtil.md5(timeString + this.getUuid() + secretKey);
    }
}
