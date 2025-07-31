package cn.easttrans.reaiot.agentic.dao.sql;

import cn.easttrans.reaiot.agentic.domain.persistence.sql.FileMeta;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface FileMetaRepository extends ReactiveCrudRepository<FileMeta, FileMeta.PK> {
    Flux<FileMeta> findByUserId(String userId);

    Flux<FileMeta> findByVectorizedAndVectorizable(boolean vectorized, boolean vectorizable);
    Mono<FileMeta> findFirstByFileName(String fileName);
}
