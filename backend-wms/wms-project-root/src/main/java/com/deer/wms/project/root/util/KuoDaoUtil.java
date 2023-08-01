package com.deer.wms.project.root.util;

import org.springframework.http.HttpMethod;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class KuoDaoUtil {

    public static String getSession() {
        // api url地址
        String url = "https://clb.kdware.cn:5000/v1.1/login";
        // post请求
        HttpMethod method = HttpMethod.POST;
        // 封装参数，千万不要替换为Map与HashMap，否则参数无法传递
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("data", "{\"name\":\"19025001\",\"passwd\":\"yyyx0000\"}");

        // 发送http请求并返回结果
        return DeerHttpClient.client(url, method, params);

    }

    public static void test(String[] args) {
        RestTemplate restTemplate = new RestTemplate();
//        User user = new User();
//        user.setName("19025001");
//        user.setPasswd("yyyx0000");
//        ResponseEntity<String> responseEntity = restTemplate.postForEntity("https://clb.kdware.cn:5000/v1.1/login", user, String.class);
//        String body = responseEntity.getBody();

//        JSONObject jsonObject = JSON.parseObject(body);
        // JSONObject jsonObject1 = JSONObject.parseObject(JSON_OBJ_STR); //因为JSONObject继承了JSON，所以这样也是可以的

//        System.out.println(jsonObject.getString("sessionid")+":"+jsonObject.getInteger("resultCode"));
//        String aaa = getSession();
        // System.out.println(body);
    }

}
