package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.system.SysDictionary;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * 查询书数据字典
 */
public interface SysDictionaryRepository extends MongoRepository<SysDictionary,String> {

                SysDictionary findSysDictionaryByDType(String dType);

}
