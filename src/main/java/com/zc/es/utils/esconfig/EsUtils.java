package com.zc.es.utils.esconfig;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zc.es.entity.OnlCgformHead;
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
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class EsUtils<T> {

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



    /**  IK，拼音，短语分词分页并高亮关键词搜索
     * 该查询方法待封装完整，封装为公共的查询方法
     * @param searchVo
     * @return
     */
    public Page<OnlCgformHead> pageHigh(OnlCgformHead searchVo){
        Page<OnlCgformHead> page = new Page(1,10,0);
        // 页码
        try {
            //不指定索引，则搜索所有的索引
            SearchRequest searchRequest = new SearchRequest("onlcgformhead");
            // 构建查询
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            // 索引查询
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            //boost 设置权重
            //分词查询
            boolQueryBuilder.should(QueryBuilders.matchQuery("tableTxt", searchVo.getTableTxt()).boost(2f));
            //拼音查询
            boolQueryBuilder.should(QueryBuilders.matchPhraseQuery("tableTxt.pinyin", searchVo.getTableTxt()).boost(2f));
            //模糊查询，不区分大小写
//            boolQueryBuilder.should(QueryBuilders.wildcardQuery("keyword", "*"+searchVo.getKeyword().toLowerCase()+"*").boost(2f));
            //必须满足should其中一个条件
            boolQueryBuilder.minimumShouldMatch(1);
            //时间范围查询
//            boolQueryBuilder.must(QueryBuilders.rangeQuery("createTime")
//                    .from(DateKit.format(DateKit.getDayBegin(),"yyyy-MM-dd HH:mm:ss"))
//                    .to(DateKit.format(DateKit.getDayBegin(),"yyyy-MM-dd HH:mm:ss")));
            sourceBuilder.query(boolQueryBuilder);
            //设置返回的字段
           /* String[] includeFields = new String[] {"id","tableTxt"};
            sourceBuilder.fetchSource(includeFields,null);*/
            // 高亮设置
            List<String> highlightFieldList = new ArrayList<>();
            highlightFieldList.add("id");
            highlightFieldList.add("tableName");
            highlightFieldList.add("tableTxt");
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            for (int x = 0; x < highlightFieldList.size(); x++) {
                HighlightBuilder.Field field = new HighlightBuilder.Field(highlightFieldList.get(x)).preTags("<high>").postTags("</high>");
                highlightBuilder.field(field);
            }
            sourceBuilder.highlighter(highlightBuilder);
            // 分页设置
            sourceBuilder.from(1);
            sourceBuilder.size(10);
            //        sourceBuilder.sort("id", SortOrder.ASC); // 设置排序规则
            sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

            searchRequest.source(sourceBuilder);
            SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits searchHits = response.getHits();
            page.setTotal(searchHits.getTotalHits().value);
            List<OnlCgformHead> list = new ArrayList<>();
            Pattern pattern = Pattern.compile("(?i)"+searchVo.getTableTxt());
            for (SearchHit hit : searchHits.getHits()) {
                String sourceAsString = hit.getSourceAsString();
                OnlCgformHead vo = JSON.parseObject(sourceAsString, OnlCgformHead.class);
                //高亮字段（拼音不做高亮，拼音的高亮有问题，会将整个字符串高亮）
                HighlightField tableTxt = hit.getHighlightFields().get("tableTxt");
                if (tableTxt != null) {
                    Text[] text = hit.getHighlightFields().get("tableTxt").getFragments();
                    vo.setTableTxt(text[0].toString());
                }
                //ngram短语，模糊搜索高亮,不区分大小写直接字符串替换
                String keyword = vo.getTableTxt();
                if(!keyword.contains("<high>")){
                    Matcher matcher = pattern.matcher(keyword);
                    if(matcher.find()){
                        String s = matcher.group();
                        vo.setTableTxt(keyword.replace(s,"<high>"+s+"</high>"));
                    }
                }
                list.add(vo);
            }
            page.setRecords(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return page;
    }

    /***
     * 查询文档     该方法高亮显示有问题，参考上面一个查询方法
     * @param searchFiled  查询字段
     * @param keyword      查询条件
     * @param indexName    查询索引
     * @param sortFiled    排序字段
     * @param isdesc       升序还是降序
     * @param pageNumber   当前页码
     * @param pageSize     查询条数
     * @param clazz            查询需要返回的实体
     * @return
     * @throws IOException
     */
    public  List<T> search(String searchFiled,String keyword,String indexName,String sortFiled,String isdesc,Integer pageNumber ,Integer pageSize,T clazz ) throws IOException, IllegalAccessException, InstantiationException {
        //设置高亮字段
        HighlightBuilder highlightBuilder = new HighlightBuilder(); //创建一个新的HighlightBuilder。
        //自定义高亮标签
        highlightBuilder.preTags("<span style=\"color:red\">");
        highlightBuilder.postTags("</span>");
        HighlightBuilder.Field highlightTitle = new HighlightBuilder.Field(searchFiled);  //为title字段创建字段高光色。
        // highlightTitle.highlighterType("unified"); // 设置字段高光色类型。
        highlightBuilder.field(highlightTitle);   //将字段高光色添加到高亮构建器。
       /* HighlightBuilder.Field highlightFiled = new HighlightBuilder.Field("keywords");
        highlightBuilder.field(highlightFiled);*/
        //下面这两项,如果你要高亮如文字内容等有很多字的字段,必须配置,不然会导致高亮不全,文章内容缺失等
        highlightBuilder.fragmentSize(800000); //最大高亮分片数
        highlightBuilder.numOfFragments(0); //从第一个分片获取高亮片段
        //构建条件执行器
        BoolQueryBuilder boolBuilder = QueryBuilders.boolQuery();
        if (StringUtils.isNotBlank(searchFiled) && StringUtils.isNotBlank(keyword)){
            boolBuilder.must(QueryBuilders.matchAllQuery()); // 这里可以根据字段进行搜索，must表示符合条件的，相反的mustnot表示不符合条件的
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
        ArrayList<T> list = new ArrayList<>();
        for (SearchHit hit : hits) {
            String json = hit.getSourceAsString();
            T t = JSON.parseObject(json, (Type) ((Class) clazz).newInstance().getClass());
            System.out.println("查询结果-------->"+json);
            list.add(t);
        }
        return list;
    }
}
