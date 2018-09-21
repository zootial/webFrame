package com.jonly.frame.util.pagination.mybatis.parser;

import java.util.Map;

import com.jonly.frame.util.pagination.Page;

public class OracleParser extends AbstractParser {
    @Override
    public String getPageSql(String sql) {
        StringBuilder sqlBuilder = new StringBuilder(sql.length() + 120);
        sqlBuilder.append("select * from ( select tmp_page.*, rownum row_id from ( ");
        sqlBuilder.append(sql);
        sqlBuilder.append(" ) tmp_page where rownum <= ? ) where row_id > ?");
        return sqlBuilder.toString();
    }

    @Override
    public Map<String, Object> buildPageParameter(Map<String, Object> paramMap, Page page) {
        int startRow = page.getPageNo() > 0 ? (page.getPageNo() - 1) * page.getPageSize() : 0;
        int endRow = startRow + page.getPageSize() * (page.getPageNo() > 0 ? 1 : 0);
        paramMap.put(PAGEPARAMETER_FIRST, endRow);
        paramMap.put(PAGEPARAMETER_SECOND, startRow);
        return paramMap;
    }
}