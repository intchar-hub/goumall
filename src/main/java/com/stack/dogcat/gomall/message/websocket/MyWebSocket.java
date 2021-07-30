package com.stack.dogcat.gomall.message.websocket;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stack.dogcat.gomall.config.WebSocketConfig;
import com.stack.dogcat.gomall.message.entity.ChatMessage;
import com.stack.dogcat.gomall.message.requestVo.MessageSaveRequestVo;
import com.stack.dogcat.gomall.message.responseVo.MessageResponseVo;
import com.stack.dogcat.gomall.message.service.IChatMessageService;
import io.swagger.models.auth.In;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;


/**
 * @Author kou xiaoyu
 * @Date 2021/7/17
 * @Descrition TODO
 */
@ServerEndpoint(value = "/mms/chat/websocket/{id}/{senderType}",configurator= WebSocketConfig.class)
@Component
public class MyWebSocket {

    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    private static CopyOnWriteArraySet<MyWebSocket> webSocketSet = new CopyOnWriteArraySet<MyWebSocket>();

    //消费者与session的映射
    private static Map<Integer,Session>  customerMap = new HashMap<>();

    //商家与session的映射
    private static Map<Integer,Session>  storeMap = new HashMap<>();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    //某个客户端的类型，0为消费者，1为商家
    private Integer senderType;

    //客户端id
    private Integer id;

    //获取全局容器
    private ApplicationContext applicationContext;

    private IChatMessageService chatMessageService;


    /**
     * 建立连接调用的方法
     */
    @OnOpen
    public void onOpen(Session session, EndpointConfig config, @PathParam("id")Integer id, @PathParam("senderType")Integer senderType) throws IOException {
        System.out.println("建立WebScoket连接 "+id+" "+senderType);

        try{
            HttpSession httpSession= (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
            WebApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(httpSession.getServletContext());

            this.applicationContext = applicationContext;
            this.session = session;
            this.senderType =senderType;
            this.id=id;
            if(senderType==0){
                customerMap.put(id,session);
            }
            else{
                storeMap.put(id,session);
            }
            this.chatMessageService = (IChatMessageService) applicationContext.getBean("chatMessageServiceImpl");
            webSocketSet.add(this);
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        //从set中删除
        webSocketSet.remove(this);
        System.out.println("WebScoket连接关闭");
    }

    /**
     * 收到客户端消息后调用的方法
     */
    @OnMessage
    public void onMessage(String message) {
        try {
            //从客户端传过来的数据是json数据，所以这里使用jackson进行转换为chatMsg对象，
            ObjectMapper objectMapper = new ObjectMapper();
            MessageSaveRequestVo messageVo = objectMapper.readValue(message, MessageSaveRequestVo.class);

            Integer toUserId;
            //customer方发送
            if(messageVo.getSenderType()==0){
                toUserId = chatMessageService.getToUser(messageVo.getChatUserLinkId(),messageVo.getSenderType());
                Session fromSession = customerMap.get(id);
                Session toSession = storeMap.get(toUserId);

                //声明一个map，封装直接发送信息数据返回前端
                Map<String, Object> resultMap = new HashMap<>();
                resultMap.put("chatUserLinkId",messageVo.getChatUserLinkId());
                resultMap.put("content",messageVo.getContent());
                resultMap.put("senderType",messageVo.getSenderType());
                JSONObject json = new JSONObject(resultMap);

                //发送回前端
                //fromSession.getAsyncRemote().sendText(json.toString());

                // 1.判断接收方的toSession是否为null
                // 2.判断在聊天页面 ==> 直接发送 其他都是存储在数据库中

                if (toSession != null && toSession.isOpen()) {
                    //发送给接收者
                    toSession.getAsyncRemote().sendText(json.toString());
                }

                //保存消息
                chatMessageService.saveMessage(messageVo);
            }
            //store方发送
            else if(messageVo.getSenderType()==1){
                toUserId = chatMessageService.getToUser(messageVo.getChatUserLinkId(),messageVo.getSenderType());
                Session fromSession = storeMap.get(id);
                Session toSession = customerMap.get(toUserId);

                //声明一个map，封装直接发送信息数据返回前端
                Map<String, Object> resultMap = new HashMap<>();
                resultMap.put("chatUserLinkId",messageVo.getChatUserLinkId());
                resultMap.put("content",messageVo.getContent());
                resultMap.put("senderType",messageVo.getSenderType());
                JSONObject json = new JSONObject(resultMap);

                //发送回前端
                //fromSession.getAsyncRemote().sendText(json.toString());

                // 1.判断接收方的toSession是否为null
                // 2.判断在聊天页面 ==> 直接发送 其他都是存储在数据库中

                if (toSession != null && toSession.isOpen()) {
                    //发送给接收者
                    toSession.getAsyncRemote().sendText(json.toString());
                }

                //保存消息
                chatMessageService.saveMessage(messageVo);
            }

        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @OnError
    public void onError(Throwable error){
        System.out.println("发生错误");
        error.printStackTrace();
    }

}