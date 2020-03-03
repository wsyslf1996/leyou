package com.leyouxianggou.service;

import java.util.Map;

public interface StaticPageService {
    Map<String, Object> goodsDetailPage(Long spuId);

    void createHtml(Long spuId);

    void deleteHtml(long spuId);
}
