package fecaf.eduardo.gerenciador.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import jakarta.annotation.PostConstruct;

@Configuration
public class MongoConfig {

    private final MappingMongoConverter mappingMongoConverter;

    public MongoConfig(MappingMongoConverter mappingMongoConverter) {
        this.mappingMongoConverter = mappingMongoConverter;
    }

    @PostConstruct
    public void removeClassField() {
        mappingMongoConverter.setTypeMapper(new DefaultMongoTypeMapper(null));
    }
}