package com.testing.demo.configuration;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.policy.ClientPolicy;
import org.luaj.vm2.ast.Str;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.aerospike.cache.AerospikeCacheConfiguration;
import org.springframework.data.aerospike.cache.AerospikeCacheManager;
import org.springframework.data.aerospike.convert.AerospikeCustomConversions;
import org.springframework.data.aerospike.convert.AerospikeTypeAliasAccessor;
import org.springframework.data.aerospike.convert.MappingAerospikeConverter;
import org.springframework.data.aerospike.mapping.AerospikeMappingContext;
import org.springframework.data.mapping.model.SimpleTypeHolder;
import org.springframework.web.bind.annotation.Mapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableConfigurationProperties(AerospikeConfigurationProperties.class)
@Import(value = {MappingAerospikeConverter.class, AerospikeMappingContext.class, AerospikeTypeAliasAccessor.class, AerospikeCustomConversions.class, SimpleTypeHolder.class})
public class AerospikeConfiguration {

    private static ArrayList<String> cacheNames;
    static  {
        cacheNames = new ArrayList<>();
        cacheNames.add("ruleUseCase1");
        cacheNames.add("ruleUseCase2");
        cacheNames.add("ruleUseCase3");
    }

    @Autowired
    private MappingAerospikeConverter mappingAerospikeConverter;

    @Autowired
    private AerospikeConfigurationProperties aerospikeConfigurationProperties;

    @Bean(destroyMethod = "close")
    public AerospikeClient aerospikeClient() {
        ClientPolicy clientPolicy = new ClientPolicy();
        clientPolicy.failIfNotConnected = true;
        return new AerospikeClient(clientPolicy, "127.0.0.1", 3000);
    }

    @Bean
    public AerospikeCacheManager cacheManager(AerospikeClient aerospikeClient) {
        AerospikeCacheConfiguration defaultConfiguration = new AerospikeCacheConfiguration("test", "set1");
        Map<String, AerospikeCacheConfiguration> cacheConfigurationMap = new HashMap<>();
        for(String cacheName : cacheNames) {
            cacheConfigurationMap.put(cacheName, new AerospikeCacheConfiguration("test", cacheName));
        }

        return new AerospikeCacheManager(aerospikeClient, mappingAerospikeConverter, defaultConfiguration, cacheConfigurationMap);
    }
}
