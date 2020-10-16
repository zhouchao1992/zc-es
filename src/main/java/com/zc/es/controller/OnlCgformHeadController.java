package com.zc.es.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zc.es.entity.OnlCgformHead;
import com.zc.es.service.OnlCgformHeadService;
import com.zc.es.utils.esconfig.EsUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zc
 * @since 2020-10-14
 */
@RestController
@RequestMapping("/onl-cgform-head/api/")
@Api(tags = "表信息控制器")
public class OnlCgformHeadController {

    @Resource
    OnlCgformHeadService onlCgformHeadService;

    @Resource
    EsUtils esUtils;

    @GetMapping("list")
    @ApiOperation("获取表信息")
    public  Object list(Long pageSize,Long current){
        Page page = new Page();
        page.setSize(pageSize);
        page.setCurrent(current);
        return onlCgformHeadService.page(page);
    }

    @ApiOperation("导入数据")
    @GetMapping("importdata")
    public  Object importdata(String indexName) throws Exception {
        List<OnlCgformHead> list = onlCgformHeadService.list();
        int index = esUtils.batchCreateDocument(indexName,list);
        return index;
    }

    @ApiOperation("es查询数据")
    @GetMapping("essearch")
    public  Object essearch(String searchFiled,String keyword,String indexName,String sortFiled,String isdesc,Integer pageNumber ,Integer pageSize) throws Exception {
        List<String> index = esUtils.search(searchFiled,keyword,indexName,sortFiled,isdesc,pageNumber,pageSize);
        return index;
    }
}
