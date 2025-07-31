package cn.easttrans.reaiot.agentic.dao.sql;

import cn.easttrans.reaiot.agentic.domain.persistence.sql.FileMeta;
import io.r2dbc.spi.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.mapping.event.BeforeConvertCallback;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import static io.r2dbc.spi.ConnectionFactories.get;


@Configuration
@Slf4j
public class H2Config extends AbstractR2dbcConfiguration {

    private static final String SCHEMA = "schema.sql";

    @Value("${spring.r2dbc.url}")
    private String r2dbcUrl;

    @Override
    public ConnectionFactory connectionFactory() {
        return get(r2dbcUrl);
    }

    @Bean
    ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {
        ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
        initializer.setConnectionFactory(connectionFactory);
        initializer.setDatabasePopulator(new ResourceDatabasePopulator(new ClassPathResource(SCHEMA)));
        return initializer;
    }

    @Bean
    public BeforeConvertCallback<FileMeta> fileIdGenerationCallback() {
        return (entity, _) -> {
            if (entity.pk() == null) {
                var newId = new FileMeta.PK(UUID.randomUUID(), System.currentTimeMillis());
                log.trace("New FileMeta.PK was generated: ({}, {})", newId.fileId(), newId.ts());
                return Mono.just(new FileMeta(newId, entity.userId(), entity.fileName(), entity.vectorizable(), entity.vectorized(), entity.mimeType(), entity.uri()));
            }
            return Mono.just(entity);
        };
    }

    @Override
    public List<Object> getCustomConverters() {
        return List.of(
                new URIToStringConverter(), // (Converter<URI, String>) URI::toString,
                new StringToURIConverter() // (Converter<String, URI>) URI::create
        );
    }

    @ReadingConverter
    private static class StringToURIConverter implements Converter<String, URI> {
        @Override
        public URI convert(String source) {
            return URI.create(source);
        }
    }

    @WritingConverter
    private static class URIToStringConverter implements Converter<URI, String> {
        @Override
        public String convert(URI source) {
            return source.toString();
        }
    }
}
