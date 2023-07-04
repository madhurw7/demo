package util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.data.aerospike.convert.AerospikeReadData;
import org.springframework.data.aerospike.convert.MappingAerospikeReadConverter;
import org.springframework.data.aerospike.mapping.AerospikeMappingContext;
import org.springframework.data.convert.CustomConversions;
import org.springframework.data.convert.TypeAliasAccessor;
import org.springframework.data.convert.TypeMapper;
import org.springframework.data.mapping.model.EntityInstantiators;
import org.springframework.data.util.TypeInformation;

import java.util.Map;

public class CustomAerospikeReadConverter extends MappingAerospikeReadConverter {

    private final TypeMapper<Map<String, Object>> customTypeMapper;

    public CustomAerospikeReadConverter(EntityInstantiators entityInstantiators, TypeAliasAccessor<Map<String, Object>> typeAliasAccessor, TypeMapper<Map<String, Object>> typeMapper, AerospikeMappingContext mappingContext, CustomConversions conversions, GenericConversionService conversionService) {
        super(entityInstantiators, typeAliasAccessor, typeMapper, mappingContext, conversions, conversionService);
        this.customTypeMapper = typeMapper;
    }

    @Override
    public <R> R read(Class<R> targetClass, AerospikeReadData data) {
        Map<String, Object> record = data.getRecord();
        TypeInformation<? extends  R> typeToUse = this.customTypeMapper.readType(record, TypeInformation.of(targetClass));
        Class<? extends R> rawType = typeToUse.getType();
        ObjectMapper objectMapper = new ObjectMapper();
        R returnVal;
        try {
            returnVal = objectMapper.readValue((String)(record.get("cachedValue")), rawType);
        } catch (JsonProcessingException e) {
            returnVal = null;
        }
        return returnVal;
    }
}
