package com.zc.es.utils.esconfig;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.apache.commons.io.IOUtils;
import org.elasticsearch.action.admin.indices.alias.Alias;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class EsUtils {

    @Resource
   private  RestHighLevelClient client;

    /**
     * 创建索引
     */
    public  String createIndex(String indexName) throws IOException {
        CreateIndexRequest createIndexRequest = new CreateIndexRequest(indexName);
        CreateIndexResponse createIndexResponse = client.indices().create(createIndexRequest, RequestOptions.DEFAULT);
        return createIndexResponse.index();
    }



    /**
     * 判断索引是否存在
     */

    public   boolean existIndex(String indexName) throws IOException {
        GetIndexRequest getIndexRequest = new GetIndexRequest(indexName);
        boolean exists = client.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
        return exists;
    }


    /**
     * 删除索引
     */

    public  boolean deleteIndex(String indexName) throws IOException {
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(indexName);
        AcknowledgedResponse delete = client.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
        return delete.isAcknowledged();
    }


    /***
     * 创建文档
     * @param indexName   索引名称
     * @param json        保存数据
     * @return            返回结果
     */
    public   String  createDocument(String indexName,String json) throws IOException {
        Map map = JSON.parseObject(json, Map.class);
        IndexRequest indexRequest = new IndexRequest(indexName);
        indexRequest.source(json, XContentType.JSON);
        indexRequest.id(map.get("id").toString());
        IndexResponse index = client.index(indexRequest, RequestOptions.DEFAULT);
        return index.toString();
    }

    /***
     * 批量添加文档
     * @param indexName   索引名称
     * @param json        保存数据
     * @return            返回结果
     * @throws IOException
     */
    public  <T> int  batchCreateDocument(String indexName, List<T> json) throws IOException {
        BulkRequest bulkRequest = new BulkRequest();
        json.forEach(data->{
            IndexRequest indexRequest = new IndexRequest(indexName);
            Map map = JSON.parseObject(JSON.toJSONString(data), Map.class);
            indexRequest.id(map.get("id").toString());
            indexRequest.source(JSON.toJSONString(map), XContentType.JSON);
            bulkRequest.add(indexRequest);
        });
        BulkResponse bulk = client.bulk(bulkRequest, RequestOptions.DEFAULT);
        return bulk.status().getStatus();
    }


    /**
     * 修改文档
     */
    public  String updateDocument(String indexName,String json) throws IOException {
        Map map = JSON.parseObject(json, Map.class);
        UpdateRequest updateRequest = new UpdateRequest(indexName,map.get("id").toString());
        updateRequest.doc(json,XContentType.JSON);
        UpdateResponse update = client.update(updateRequest, RequestOptions.DEFAULT);
        return update.toString();
    }

    /**
     * 删除文档
     */
    public   int deleteDocument(String indexName,String id) throws IOException {
        DeleteRequest deleteRequest=new DeleteRequest(indexName,id);
        DeleteResponse delete = client.delete(deleteRequest, RequestOptions.DEFAULT);
        return delete.status().getStatus();
    }


    /***
     *
     * 批量删除
     */
    public  int batchDeleteDocument(String indexName,List<String> ids) throws IOException {
        AtomicInteger count= new AtomicInteger(1);
        ids.forEach(id->{
            DeleteRequest deleteRequest=new DeleteRequest(indexName,id);
            DeleteResponse delete = null;
            try {
                delete = client.delete(deleteRequest, RequestOptions.DEFAULT);
            } catch (IOException e) {
                e.printStackTrace();
            }
            int status = delete.status().getStatus();
            if (status==200){
                count.getAndIncrement();
            }
        });
        return count.get();
    }

    /***
     * 查询文档
     * @param searchFiled  查询字段
     * @param keyword      查询条件
     * @param indexName    查询索引
     * @param sortFiled    排序字段
     * @param isdesc       升序还是降序
     * @param pageNumber   当前页码
     * @param pageSize      查询条数
     * @return
     * @throws IOException
     */
    public  List<String> search(String searchFiled,String keyword,String indexName,String sortFiled,String isdesc,Integer pageNumber ,Integer pageSize ) throws IOException {
        //设置高亮字段
        HighlightBuilder highlightBuilder = new HighlightBuilder(); //创建一个新的HighlightBuilder。
        //自定义高亮标签
        highlightBuilder.preTags("<span style=\"color:red\">");
        highlightBuilder.postTags("</span>");
        HighlightBuilder.Field highlightTitle = new HighlightBuilder.Field("name");  //为title字段创建字段高光色。
        highlightTitle.highlighterType("unified"); // 设置字段高光色类型。
        highlightBuilder.field(highlightTitle);   //将字段高光色添加到高亮构建器。
       /* HighlightBuilder.Field highlightFiled = new HighlightBuilder.Field("keywords");
        highlightBuilder.field(highlightFiled);*/
        //下面这两项,如果你要高亮如文字内容等有很多字的字段,必须配置,不然会导致高亮不全,文章内容缺失等
        highlightBuilder.fragmentSize(800000); //最大高亮分片数
        highlightBuilder.numOfFragments(0); //从第一个分片获取高亮片段
        //构建条件执行器
        BoolQueryBuilder boolBuilder = QueryBuilders.boolQuery();
        if (StringUtils.isNotBlank(searchFiled) && StringUtils.isNotBlank(keyword)){
            boolBuilder.must(QueryBuilders.matchQuery(searchFiled, keyword)); // 这里可以根据字段进行搜索，must表示符合条件的，相反的mustnot表示不符合条件的
        }
        // boolBuilder.must(QueryBuilders.matchQuery("id", tests.getId().toString()));
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.sort(sortFiled, "DESC".equals(isdesc)?SortOrder.DESC:SortOrder.ASC);
        searchSourceBuilder.query(boolBuilder);
        searchSourceBuilder.from(pageNumber);
        searchSourceBuilder.size(pageSize); // 获取记录数，默认10

        searchSourceBuilder.highlighter(highlightBuilder);
        //sourceBuilder.fetchSource(new String[] { "user", "title","desc" }, new String[] {}); // 第一个是获取字段，第二个是过滤的字段，默认获取全部
        SearchRequest searchRequest = new SearchRequest(indexName);
        //searchRequest.types(type);
        searchRequest.source(searchSourceBuilder);
        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
        SearchHit[] hits = response.getHits().getHits();
        ArrayList<String> list = new ArrayList<String>();
        for (SearchHit hit : hits) {
            String json = hit.getSourceAsString();
            System.out.println("查询结果-------->"+json);
            list.add(json);
        }
        return list;
    }



    /**
     * 创建索引   根据json文件 创建es索引文档   还有基于注解的加载json  @setting  与  @mapping  注解来加载json文件
     * @param index
     * @throws IOException
     */
    public  boolean createIndexTwo(String index) throws IOException {
        //如果存在就不创建了
        if(existIndex(index)) {
            System.out.println(index+"索引库已经存在!");
            return false;
        }
        // 开始创建库
        CreateIndexRequest request = new CreateIndexRequest(index);
        //配置文件
        ClassPathResource seResource = new ClassPathResource("settings.json");
        InputStream seInputStream = seResource.getInputStream();
        String seJson = String.join("\n", IOUtils.readLines(seInputStream,"UTF-8"));
        seInputStream.close();
        //映射文件
        ClassPathResource mpResource = new ClassPathResource("esjson/"+index+"-mapping.json");
        InputStream mpInputStream = mpResource.getInputStream();
        String mpJson = String.join("\n",IOUtils.readLines(mpInputStream,"UTF-8"));
        mpInputStream.close();

        request.settings(seJson, XContentType.JSON);
        request.mapping(mpJson, XContentType.JSON);

        //设置别名
        request.alias(new Alias(index+"_alias"));
        CreateIndexResponse createIndexResponse = client.indices().create(request, RequestOptions.DEFAULT);
        boolean falg = createIndexResponse.isAcknowledged();
        if(falg){
            System.out.println("创建索引库:"+index+"成功！" );
        }
        return falg;
    }
}
