package com.deer.wms.intercept.dao;

import com.deer.wms.intercept.common.data.UserInfo2;
import com.deer.wms.project.root.core.mapper.Mapper;
import org.apache.ibatis.annotations.Param;

public interface User2Mapper extends Mapper<UserInfo2> {
    UserInfo2 findByToken(@Param("token") String token);
}
