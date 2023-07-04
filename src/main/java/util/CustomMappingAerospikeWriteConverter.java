package util;

import com.aerospike.client.Key;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.data.aerospike.convert.AerospikeWriteData;
import org.springframework.data.aerospike.convert.MappingAerospikeWriteConverter;
import org.springframework.data.aerospike.mapping.AerospikeMappingContext;
import org.springframework.data.aerospike.mapping.AerospikePersistentEntity;
import org.springframework.data.aerospike.mapping.AerospikePersistentProperty;
import org.springframework.data.convert.CustomConversions;
import org.springframework.data.convert.EntityWriter;
import org.springframework.data.convert.TypeMapper;
import org.springframework.data.mapping.model.ConvertingPropertyAccessor;
import org.springframework.data.util.TypeInformation;
import org.springframework.util.Assert;

import java.util.Map;


public class CustomMappingAerospikeWriteConverter extends MappingAerospikeWriteConverter {

    private final GenericConversionService customConversionService;
    private final AerospikeMappingContext customMappingContext;
    private final CustomConversions customConversions;

    public CustomMappingAerospikeWriteConverter(TypeMapper<Map<String, Object>> typeMapper, AerospikeMappingContext mappingContext, CustomConversions conversions, GenericConversionService conversionService) {
        super(typeMapper, mappingContext, conversions, conversionService);
        this.customConversionService = conversionService;
        this.customMappingContext = mappingContext;
        this.customConversions = conversions;
    }

    @Override
    public void write(Object source, AerospikeWriteData data) {
        if (source != null) {
            boolean hasCustomConverter = this.customConversions.hasCustomWriteTarget(source.getClass(), AerospikeWriteData.class);
            if (hasCustomConverter) {
                super.write(source, data);
            } else {
                AerospikePersistentEntity<?> entity = this.customMappingContext.getPersistentEntity(source.getClass());
                ConvertingPropertyAccessor<?> accessor = new ConvertingPropertyAccessor(entity.getPropertyAccessor(source), this.customConversionService);
                AerospikePersistentProperty idProperty = entity.getIdProperty();
                ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
                try {
                    String json = ow.writeValueAsString(source);
                    data.addBin("cachedValue", json);
                } catch (JsonProcessingException e) {
                    System.out.println("Not pushed to cache");
                    return;
                }
                if (idProperty != null) {
                    String id = (String)accessor.getProperty(idProperty, String.class);
                    Assert.notNull(id, "Id must not be null!");
                    data.setKey(new Key(entity.getNamespace(), entity.getSetName(), id));
                    data.addBin("@user_key", id);
                }

                AerospikePersistentProperty versionProperty = (AerospikePersistentProperty)entity.getVersionProperty();
                if (versionProperty != null) {
                    Integer version = (Integer)accessor.getProperty(versionProperty, Integer.class);
                    data.setVersion(version);
                }


            }
        }
    }

}
