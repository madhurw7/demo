package com.testing.demo.repository;

import com.testing.demo.objects.Input;
import com.testing.demo.objects.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.aerospike.cache.AerospikeCacheManager;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.Optional;
import java.util.Random;

@Repository
public class ResultRepository {

    @Autowired
    AerospikeCacheManager aerospikeCacheManager;

    @Cacheable(value = "test", key = "#ip.cacheKey")
    public Optional<Result> getResult(Input ip) {
        System.out.println(ip);
        Random random = new Random();
        Result result = new Result(String.valueOf(Math.abs(random.nextInt())), random.nextBoolean(), ip.getWhId(), "0");
        System.out.println(result);
        return Optional.of(result);
    }

    @CacheEvict(value = "ruleUseCase1", allEntries = true)
    public void evictAllCache1Values() {}

    @Cacheable(value = "ruleUseCase1", key = "#ip.cacheKey")
    public Optional<Result> getResultFromCache1(Input ip) {
        System.out.println(ip);
        Random random = new Random();
        Result result = new Result(String.valueOf(Math.abs(random.nextInt())), random.nextBoolean(), ip.getWhId(), "1");
        System.out.println(result);
        return Optional.of(result);
    }

    @CacheEvict(value = "ruleUseCase2", allEntries = true)
    public void evictAllCache2Values() {}

    @Cacheable(value = "ruleUseCase2", key = "#ip.cacheKey")
    public Optional<Result> getResultFromCache2(Input ip) {
        System.out.println(ip);
        Random random = new Random();
        Result result = new Result(String.valueOf(Math.abs(random.nextInt())), random.nextBoolean(), ip.getWhId(), "2");
        System.out.println(result);
        return Optional.of(result);
    }

    @CacheEvict(value = "ruleUseCase3", allEntries = true)
    public void evictAllCache3Values() {}

    @Cacheable(value = "ruleUseCase3", key = "#ip.cacheKey")
    public Optional<Result> getResultFromCache3(Input ip) {
        System.out.println(ip);
        Random random = new Random();
        Result result = new Result(String.valueOf(Math.abs(random.nextInt())), random.nextBoolean(), ip.getWhId(), "3");
        System.out.println(result);
        return Optional.of(result);
    }

    public Optional<Result> getResultFromCache3viaCacheManager(Input ip) {
        Cache requiredRuleCache = aerospikeCacheManager.getCache("ruleUseCase3");
        String cacheKey = ip.getCacheKey();
        System.out.println(ip);
        if(Objects.nonNull(requiredRuleCache.get(cacheKey, Result.class))) {
            System.out.println("Cache hit, returning directly");
            return Optional.of(requiredRuleCache.get(cacheKey, Result.class));
        }
        System.out.println("Cache Miss");
        Random random = new Random();
        Result result = new Result(String.valueOf(Math.abs(random.nextInt())), random.nextBoolean(), ip.getWhId(), "3");
        requiredRuleCache.put(cacheKey, result);
        System.out.println(result);
        return Optional.of(result);
    }
}
