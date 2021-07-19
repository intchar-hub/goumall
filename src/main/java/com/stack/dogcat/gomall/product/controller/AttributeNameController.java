package com.stack.dogcat.gomall.product.controller;


import com.stack.dogcat.gomall.commonResponseVo.PageResponseVo;
import com.stack.dogcat.gomall.commonResponseVo.SysResult;
import com.stack.dogcat.gomall.product.entity.AttributeName;
import com.stack.dogcat.gomall.product.requestVo.AddAttributeRequestVo;
import com.stack.dogcat.gomall.product.responseVo.AttributeNameVo;
import com.stack.dogcat.gomall.product.service.IAttributeNameService;
import com.stack.dogcat.gomall.product.service.impl.AttributeNameServiceImpl;
import com.stack.dogcat.gomall.user.service.IAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;


/**
 * <p>
 * 属性名 前端控制器
 * </p>
 *
 * @author xrm
 * @since 2021-07-08
 */
@RestController
@CrossOrigin
@RequestMapping("/product/attribute-name")
public class AttributeNameController {

    @Autowired
    IAttributeNameService attributeNameService;

    /**
     * 商店添加属性
     * @param addAttributeRequestVo
     * @return
     */
    @PostMapping("/addAttribute")
    @ResponseBody
    public SysResult addAttribute(@Valid @RequestBody AddAttributeRequestVo addAttributeRequestVo){

        try{
            AttributeName attributeName =new AttributeName();
            attributeName.setName(addAttributeRequestVo.getAttributeName());
            attributeName.setAttributeCollectionId(addAttributeRequestVo.getCollectionId());
            attributeName.setHandAdd(1);
            attributeName.setInputType(addAttributeRequestVo.getInputType());
            attributeName.setGmtCreate(LocalDateTime.now());
            if(addAttributeRequestVo.getAttributeValuesString()!=null&&addAttributeRequestVo.getInputType()==1){
                //属性列表处理(属性值根据','或'，'分割)
                String valueArrayStr=addAttributeRequestVo.getAttributeValuesString();
                ArrayList<String> valueArray=new ArrayList<>();
                int indexTemp=0;
                for(int i=0;i<valueArrayStr.length();i++){
                    char ch = valueArrayStr.charAt(i);
                    if(ch==','||ch=='，'){
                        String valueStr=valueArrayStr.substring(indexTemp,i);
                        valueArray.add(valueStr);
                        indexTemp=i+1;
                        //System.out.println(valueStr);
                        //System.out.println(indexTemp);
                    }
                    if(i==valueArrayStr.length()-1){
                        String valueStr=valueArrayStr.substring(indexTemp);
                        valueArray.add(valueStr);
                        //System.out.println(valueStr);
                    }
                }
                attributeNameService.insertAttributeNameAndValueList(attributeName,valueArray);
            }
            else if(addAttributeRequestVo.getInputType()==0){
                attributeNameService.insertAttributeNameManualInput(attributeName);
            }
            SysResult result=SysResult.success();
            return result;

        }catch(Exception e){
            SysResult result = SysResult.error(e.getMessage());
            return result;
        }
    }


    /**
     * 商家按属性集合查看所有商品属性
     * @param collectionId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/listAttributeByCollection")
    @ResponseBody
    public SysResult listAttributeByCollection(Integer collectionId,Integer pageNum,Integer pageSize){

        PageResponseVo<AttributeNameVo> responseVos;
        try{
            responseVos = attributeNameService.listAttributeByCollection(collectionId,pageNum,pageSize);

        }catch (Exception e){
            SysResult result = SysResult.error(e.getMessage());
            return result;
        }
        return SysResult.success(responseVos);
    }


    /**
     * 商家按属性名id查看所有商品属性
     * @param  attributeNameId
     * @return
     */
    @GetMapping("/listAttributeValueByName")
    @ResponseBody
    public SysResult listAttributeValueByName(Integer attributeNameId){

        AttributeNameVo responseVos;
        try {
            responseVos = attributeNameService.listAttributeValueByName(attributeNameId);

        }catch (Exception e){
            SysResult result = SysResult.error(e.getMessage());
            return result;
        }
        return SysResult.success(responseVos);
    }


    /**
     * 商家删除属性
     * @param  attributeNameId
     * @return
     */
    @DeleteMapping("/deleteAttributeNameById")
    @ResponseBody
    public SysResult deleteAttributeNameById(Integer attributeNameId){

        try{
            attributeNameService.deleteAttributeNameById(attributeNameId);
        }catch (Exception e){
            SysResult result = SysResult.error(e.getMessage());
            return result;
        }
        return SysResult.success();
    }


}
