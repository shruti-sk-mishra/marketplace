package com.shr.marketplace.config;

/**
 * @author shruti.mishra
 */
import com.mongodb.WriteConcern;

import java.time.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.MongoActionOperation;
import org.springframework.data.mongodb.core.WriteConcernResolver;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.lang.NonNull;

@Configuration
@EnableMongoAuditing
@EnableMongoRepositories(
        basePackages = {"com.shr"}
)
public class MongoConfig {
    private static final Set<MongoActionOperation> OperationsForAcknowledgedWriteConcern;

    public MongoConfig() {
    }

    @Bean
    public WriteConcernResolver writeConcernResolver() {
        return (action) -> {
            LoggerFactory.getLogger(MongoConfig.class).info("Trying to resolve the write concern...");
            return OperationsForAcknowledgedWriteConcern.contains(action.getMongoActionOperation()) ? WriteConcern.ACKNOWLEDGED : action.getDefaultWriteConcern();
        };
    }

    static {
        OperationsForAcknowledgedWriteConcern = Set.of(MongoActionOperation.UPDATE, MongoActionOperation.SAVE, MongoActionOperation.BULK);
    }

    @Bean
    public MongoCustomConversions mongoCustomConversions() {
        final var defaultZoneId = ZoneId.systemDefault();
        return new MongoCustomConversions(List.of(

                new Converter<LocalDateTime, Date>() {
                    @Override
                    public Date convert(@NonNull LocalDateTime source) {
                        return Date.from(source.atZone(defaultZoneId).toInstant());
                    }
                },
                new Converter<Date, LocalDateTime>() {
                    @Override
                    public LocalDateTime convert(@NonNull Date source) {
                        return LocalDateTime.ofInstant(source.toInstant(), defaultZoneId);
                    }
                }
        ));
    }
}