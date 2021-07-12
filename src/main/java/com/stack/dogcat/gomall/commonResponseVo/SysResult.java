package com.stack.dogcat.gomall.commonResponseVo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @author 徐汝明
 * @version 1.0
 */


@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysResult {
   //表示状态码的数字
   private Integer status;
   //携带详细信息的字符串
   private String msg;
   //携带的各种数据
   private Object data;

   /**
    * 返回状态
    * @param status 200成功，其他失败
    * @param msg ok成功，其他失败
    * @param data 数据
    * @return vo
    */
   public static SysResult build(Integer status, String msg, Object data) {
      return new SysResult(status, msg, data);
   }

   /**
    * @return 成功
    */
   public static SysResult success() {
      return new SysResult(200, "操作成功", null);
   }

   /**
    * 状态
    * @param msg 要返回的信息
    * @return 成功
    */
   public static SysResult success(String msg){
      return new SysResult(200,msg,null);
   }

   /**
    * 状态
    *
    * @param data vo要封装的数据
    * @return vo
    */
   public static SysResult success(Object data) {
      return new SysResult(200, "操作成功", data);
   }

   /**
    * 状态
    *
    * @return 失败
    */
   public static SysResult error(String msg) {
      return new SysResult(400, msg, null);
   }
   /**
    * 状态
    *
    * @return 失败
    */
   public static SysResult error(String msg,Object data) {
      return new SysResult(400, msg, data);
   }
}
