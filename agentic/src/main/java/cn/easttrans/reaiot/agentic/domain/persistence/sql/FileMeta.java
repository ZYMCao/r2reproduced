package cn.easttrans.reaiot.agentic.domain.persistence.sql;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.net.URI;
import java.util.UUID;

@Table("FILE_META")
public record FileMeta(
        @Id PK pk,
        @Column("USER_ID") String userId,
        @Column("FILE_NAME") String fileName,
        @Column("VECTORIZABLE") boolean vectorizable,
        @Column("VECTORIZED") boolean vectorized,
        @Column("MIMETYPE") String mimeType,
        @Column("URI") URI uri
) {
    public record PK(@Column("FILE_ID") UUID fileId, @Column("TS") long ts) {
    }
}
