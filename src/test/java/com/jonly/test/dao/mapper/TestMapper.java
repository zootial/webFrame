package com.jonly.test.dao.mapper;

import com.jonly.frame.dao.IDao;
import com.jonly.frame.util.pagination.Page;
import com.jonly.test.dto.TestExample;
import com.jonly.test.model.Test;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TestMapper extends IDao<Test> {

    long countByExample(TestExample example);

    int deleteByExample(TestExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Test record);

    int insertSelective(Test record);

    List<Test> selectByExample(@Param("example") TestExample example, @Param("page") Page page);

    Test selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Test record, @Param("example") TestExample example);

    int updateByExample(@Param("record") Test record, @Param("example") TestExample example);

    int updateByPrimaryKeySelective(Test record);

    int updateByPrimaryKey(Test record);
}