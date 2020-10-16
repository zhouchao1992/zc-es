package com.zc.es.controller;

import com.zc.es.utils.esconfig.EsUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;

@RestController
@RequestMapping("/es/utils/")
@Api(tags = "ES工具控制器")
public class ESController {

    @Resource
    EsUtils esUtils;

    @ApiOperation("创建索引（简单）")
    @GetMapping("createIndex")
    public  Object createIndex(String indexName) throws IOException {
        String index = esUtils.createIndex(indexName);
        return index;
    }

    @ApiOperation("创建索引（基于自定义mapping）")
    @GetMapping("createIndexTwo")
    public  Object createIndexTwo(String indexName) throws Exception {
        boolean index = esUtils.createIndexTwo(indexName);
        return index;
    }




}
