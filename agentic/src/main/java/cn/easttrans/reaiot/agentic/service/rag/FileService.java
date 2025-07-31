package cn.easttrans.reaiot.agentic.service.rag;

import cn.easttrans.reaiot.agentic.dao.sql.FileMetaRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Objects;

@Service
@Slf4j
public class FileService implements ApplicationListener<ApplicationReadyEvent> {
    private final ObjectMapper objectMapper;
    private final FileMetaRepository fileMetaRepository;

    public FileService(FileMetaRepository fileMetaRepository,
                       ObjectMapper objectMapper) {
        this.fileMetaRepository = fileMetaRepository;
        this.objectMapper = objectMapper;

        this.peekH2("814531").subscribe();
        this.peekH2("704029").subscribe();
    }

    Flux<String> peekH2(String userId) {
        String docx = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
        return Flux.defer(() -> this.fileMetaRepository.findByUserId(userId)
                .filter(x -> Objects.equals(x.mimeType(), docx))
                .map(fileMeta -> {
                    try {
                        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(fileMeta);
                    } catch (JsonProcessingException e) {
                        log.error("Failed to serialize FileMeta", e);
                        return "Serialization Error: " + e.getMessage();
                    }
                })
                .doOnNext(log::info));
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
    }
}
