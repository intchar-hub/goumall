package com.stack.dogcat.gomall.config;

import java.io.FileWriter;
import java.io.IOException;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *修改日期：2017-04-05
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */

public class AlipayConfig {
	
//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

	// 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
	public static String app_id = "2021000117688671";
	
	// 商户私钥，您的PKCS8格式RSA2私钥
    public static String merchant_private_key = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDd45ZJXn2C8yhy1diFfALBtiOuT9m3Epa0+apOGRRyw9bhaiGswHAWHfeRthMRjk/NS5VovSUSYSOnvIwt6eqYSL8vB3IWBGDwGQysbuJf73F8rY4AqP2TSDKBQcKQkBkJ9ZpYVkhb8xqhLghAvc38mSNtRDlXWkefh/j1BsqmHbBgQ3aH08QxuNg6WDUioS8No/KA0CJvumFf56nQtgRCCKNvm7UDk/SVKw2OBQDV+d8UPePSV6eF8u9Khiw8d7aPoYJROytF297uJV497wnhJPW26d/x846rgvruKG7/ZzHCaD9u3nnxRWPqDdYP9sa/DvL1khkH8Mr2TtZNESlnAgMBAAECggEAIXKIGgEzXET5KO5RWDXMWJTzJfiFchJMQ3WLFYC0U2RwBMSx1M+hVcQoqH9QuonAP8sP95zjdLTN4iAK5XToVxJIN3eg2+7HCgpMncWNPDbk3Y1v5WuT5dqxOkjGYnJGQ/kQzhYbjDjGH9vHhLLX1Sbkbzkkh6VlPIgLoCvo6kcxe055emqm/siG+LdrftrHPx7R2mcdj+BEALneExPk9IxT+i4p7Vf6mVYJyWvPzTecV7svjEK6Nk+4owE+i9Sj6euXV7K6CaqOhL9jfCGM9CrnqnAWIKLIQsruTXhgFn4ZShIMiFDHJMLRLMwTZIcnNlY84hV/rnfd95GVsXalIQKBgQD5xPhP3Uzo2WhjzhJ65mla7AsQzXeag7kjiU/HZCQ2OYeqAnRc12zIfLJnpzW3YlGmXgUCtURmXGRleLCZsjQixH+Nqc1ov8Ur76RkfDhy/D0EGin8A1+EjvirKgfr0REextpXTkKPg2VyDcXiyxxUVdEh/PQlOdscTRzFebs+0QKBgQDjbJKTChez8fEz8ZZkEMelcAkqbyRkzyrjRUYAt+1OAkBVEcdiPVyvatV+PfEFRcDnE0OZrysvuLR6Jij5T7HLVm5KSnL5rv2i0PM/lIuE1vfb6e0y0euUAi5kzCsl/9EKJEgbHaWYc16WAcV/G9gLL0l/Yw9bLM94nq+wFseitwKBgQC62/eBmGifk3cbo+IDfDm5wfoNWgpu2Lk7stCytWK6BpRORoPTqX9DBrdaAKiZR78aHiDVg9NmWRzrJT3J3p5OnH9o9m4sPbw/QIvWeXCp5beoSAK8/bEKNQQo2csPS1vpkI9s3anCQGU2zMMNBzNsjL9nFB17dCtOowoxWv8Q4QKBgGyqKfbb8atnRQq+uk8hvWMM/zQpWshYPHPPW71oJJkLdlWvYwdLroeaklv5mLoeNCe1Cq3aBtaMeJ+MsDM/owI3UAdrPziSJlqvYCrW/TsBw/ht84o+x0M3L5e1/j/v1Z/06gcayU7ogR9HxWgQk8/0TjfxyxqTdKZ7nBKVL9pNAoGAOY1p2R6lBNE29K/g4gPFc2Am6n8T0Rla5J0yBHCvnYTbTLTOAbzXgv9x9TbNm5kAHsA+6JmIqlQxDgjWm8aPY4YvY+iQH16d+W40weB0n8Qb92z/z6MKArNd7BdQ79Sh2nmV5KE4OmitM3E03eKJKdI+6HBGKPgtWcqlHgp1GS0=";
	
	// 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqxdZ8bI29+oWJLXYauNvr8ipQm1cJNBgfV8F8bXxn1WwNcJbGPwB0dMd+WrPTWReXgqWSW7vXF3nnDL9wnO5IogM8wbDyS14T3zs81fXRrl9Talwa8w3Vwr17dWoXe9mOkdf3w4eiq+ECbmVdU9ZQRqUleL1pmX5PNmObMBoSC1hePtkO6dTcJCY5hrfzi1lTdonnpl0bo19UOBQeH/F6G+iJ2oNBdrFJzXVINceJ1hQ42RMyMMLq+lLRFsvlI5k3N96i2ptGZlOhRPv36AK+jmrDcoIkG84sOAEjD2RzvHQbrtuRteId+eCuQBpNHEA8B7CbkbRxiNcV/f9rfRT4wIDAQAB";

//	// 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
//	public static String notify_url = "http://工程公网访问地址/alipay.trade.page.pay-JAVA-UTF-8/notify_url.jsp";
//
//	// 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
//	public static String return_url = "http://工程公网访问地址/alipay.trade.page.pay-JAVA-UTF-8/return_url.jsp";

	// 签名方式
	public static String sign_type = "RSA2";

	// 仅支持json格式
    public static String format = "json";
	
	// 字符编码格式
	public static String charset = "utf-8";
	
	// 支付宝网关
	public static String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";
	
	// 支付宝网关
	public static String log_path = "D:\\";


//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    /** 
     * 写日志，方便测试（看网站需求，也可以改成把记录存入数据库）
     * @param sWord 要写入日志里的文本内容
     */
    public static void logResult(String sWord) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(log_path + "alipay_log_" + System.currentTimeMillis()+".txt");
            writer.write(sWord);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

