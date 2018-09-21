package com.jonly.frame.util.pagination.mybatis.parser;

import java.util.Map;

import com.jonly.frame.util.pagination.Page;

public class MysqlParser extends AbstractParser {
    @Override
    public String getPageSql(String sql) {
        StringBuilder sqlBuilder = new StringBuilder(sql.length() + 14);
        sqlBuilder.append(sql);
        sqlBuilder.append(" limit ?, ?");
        return sqlBuilder.toString();
    }

    @Override
    public Map<String, Object> buildPageParameter(Map<String, Object> paramMap, Page page) {
        paramMap.put(PAGEPARAMETER_FIRST, page.getPageNo() > 0 ? (page.getPageNo() - 1) * page.getPageSize() : 0);
        paramMap.put(PAGEPARAMETER_SECOND, page.getPageSize());
        return paramMap;
    }
}