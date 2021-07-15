package com.stack.dogcat.gomall;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class GomallApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void testSkuJSONParse() {
        String str = "{\"attrNameIdArray\":[1, 2],\"data\": [{\"stockNum\":1,\"price\":11,\"valueArray\":[3,4]},{\"stockNum\":1,\"price\":11,\"valueArray\":[3,4]}]}";
        JSONObject parse = JSON.parseObject(str);
        System.out.println(parse);
//        JSONArray attrNameIdArray = parse.getJSONArray("attrNameIdArray");
//        List<Integer> integers = attrNameIdArray.toJavaList(Integer.class);
        JSONArray data = parse.getJSONArray("data");
        System.out.println(data);
        System.out.println(data.get(0));
        JSONObject d = (JSONObject) data.get(0);
        System.out.println(d.get("price"));
        JSONArray valueArray = d.getJSONArray("valueArray");
        List<Integer> integers = valueArray.toJavaList(Integer.class);
        for (Integer integer : integers) {
            System.out.println(integer);
        }
//        List<Object> objects = data.toJavaList(Object.class);
//        for (Object object : objects) {
//            System.out.println(object);
//        }
    }

}
