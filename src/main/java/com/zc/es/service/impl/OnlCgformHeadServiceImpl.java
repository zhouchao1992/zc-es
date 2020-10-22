package com.zc.es.service.impl;

import com.zc.es.entity.OnlCgformHead;
import com.zc.es.mapper.OnlCgformHeadMapper;
import com.zc.es.service.OnlCgformHeadService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zc
 * @since 2020-10-14
 */
@Service
public class OnlCgformHeadServiceImpl extends ServiceImpl<OnlCgformHeadMapper, OnlCgformHead> implements OnlCgformHeadService {

    @Override
    public void demo() {
        System.out.println("1111111111");
    }
}
