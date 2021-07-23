package com.stack.dogcat.gomall.order.controller;


import com.stack.dogcat.gomall.annotation.CurrentUser;
import com.stack.dogcat.gomall.annotation.Token;
import com.stack.dogcat.gomall.commonResponseVo.SysResult;
import com.stack.dogcat.gomall.order.entity.CartItem;
import com.stack.dogcat.gomall.order.mapper.CartItemMapper;
import com.stack.dogcat.gomall.order.responseVo.CartItemResponseVo;
import com.stack.dogcat.gomall.order.service.ICartItemService;
import com.stack.dogcat.gomall.user.entity.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

/**
 * <p>
 * 购物车表 前端控制器
 * </p>
 *
 * @author xrm
 * @since 2021-07-08
 */
@RestController
@CrossOrigin
@RequestMapping("/order/cart-item")
public class CartItemController {

    private static Logger logger = LoggerFactory.getLogger(CartItemController.class);

    @Autowired
    ICartItemService cartItemService;

    /**
     * 加入商品到购物车
     * @param current_customer
     * @param productId
     * @param productAttribute
     * @param productNum
     * @return
     */
    @PostMapping("/saveCartItem")
    @ResponseBody
    @Token.UserLoginToken
    public SysResult saveCartItem(@CurrentUser Customer current_customer,Integer productId,String productAttribute,Integer productNum){

        try{
            Integer customerId = current_customer.getId();
            cartItemService.saveCartItem(customerId,productId,productAttribute,productNum);
            logger.info("save_cartItem->current_customer.id:{},current_customer.name:{},save_cartItem_pro.id:{},save_cartItem_pro.num:{}",
                    new Object[]{current_customer.getId().toString(), current_customer.getUserName(), productId.toString(), productNum.toString()});

        }catch (Exception e){
            SysResult result=SysResult.error(e.getMessage());
            return result;
        }
        return SysResult.success();
    }


    /**
     * 查看购物车
     * @param current_customer
     * @return
     */
    @GetMapping("/listCartItem")
    @ResponseBody
    @Token.UserLoginToken
    public SysResult listCartItem(@CurrentUser Customer current_customer){

        ArrayList<CartItemResponseVo> cartItemResponseVos;
        try{
            cartItemResponseVos=cartItemService.listCartItem(current_customer.getId());

        }catch (Exception e){
            SysResult result=SysResult.error(e.getMessage());
            return result;
        }
        return SysResult.success(cartItemResponseVos);
    }


    /**
     * 修改购物车项数量
     * @param cartItemId
     * @param productNum
     * @return
     */
    @PostMapping("/updateCartItemProductNum")
    @ResponseBody
    @Token.UserLoginToken
    public SysResult updateCartItemProductNum(Integer cartItemId,Integer productNum){

        try{
            cartItemService.updateCartItemProductNum(cartItemId,productNum);

        }catch (Exception e){
            SysResult result=SysResult.error(e.getMessage());
            return result;
        }
        return SysResult.success();
    }


    /**
     * 删除购物车项
     * @param cartItemIdList
     * @return
     */
    @DeleteMapping("/deleteCartItem")
    @ResponseBody
    @Token.UserLoginToken
    public SysResult deleteCartItem(String cartItemIdList){

        try{
            /**字符串处理**/
            ArrayList<Integer> cartItemIds=new ArrayList<>();
            int indexTemp=0;
            for(int i=0;i<cartItemIdList.length();i++){
                char ch = cartItemIdList.charAt(i);
                if(ch=='-'){
                    String str=cartItemIdList.substring(indexTemp,i);
                    cartItemIds.add(Integer.parseInt(str));
                    indexTemp=i+1;
                }
                if(i==cartItemIdList.length()-1){
                    String str=cartItemIdList.substring(indexTemp);
                    cartItemIds.add(Integer.parseInt(str));
                }
            }

            cartItemService.deleteCartItem(cartItemIds);

        }catch (Exception e){
            SysResult result=SysResult.error(e.getMessage());
            return result;
        }
        return SysResult.success();
    }



}
