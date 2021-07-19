package com.stack.dogcat.gomall.product.controller;


import com.stack.dogcat.gomall.commonResponseVo.SysResult;
import com.stack.dogcat.gomall.product.service.IAttributeValueService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 属性值 前端控制器
 * </p>
 *
 * @author xrm
 * @since 2021-07-08
 */
@RestController
@CrossOrigin
@RequestMapping("/product/attribute-value")
public class AttributeValueController {


    @Autowired
    IAttributeValueService attributeValueService;

    @PostMapping("/saveAttributeValue")
    public SysResult saveAttributeValue(Integer attributeNameId,String value){
        try{
            attributeValueService.saveAttributeValue(attributeNameId,value);
            return SysResult.success();
        }
        catch (Exception e){
            e.printStackTrace();
            return SysResult.error(e.getMessage());
        }
    }

    @DeleteMapping("/deleteAttributeValueById")
    public SysResult deleteAttributeValueById(Integer attributeValueId){
        try{
            attributeValueService.deleteAttributeValueById(attributeValueId);
            return SysResult.success();
        }
        catch (Exception e){
            e.printStackTrace();
            return SysResult.error(e.getMessage());
        }
    }

}
