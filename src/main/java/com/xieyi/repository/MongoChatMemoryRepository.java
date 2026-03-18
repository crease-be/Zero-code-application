package com.xieyi.repository;

import com.xieyi.model.MongoChatMemory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MongoChatMemoryRepository extends MongoRepository<MongoChatMemory, String> {
    // 继承 MongoRepository，Spring Data 会自动帮我们实现增删改查
}