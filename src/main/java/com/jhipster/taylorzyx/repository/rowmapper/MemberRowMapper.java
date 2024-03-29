package com.jhipster.taylorzyx.repository.rowmapper;

import com.jhipster.taylorzyx.domain.Member;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Member}, with proper type conversions.
 */
@Service
public class MemberRowMapper implements BiFunction<Row, String, Member> {

    private final ColumnConverter converter;

    public MemberRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Member} stored in the database.
     */
    @Override
    public Member apply(Row row, String prefix) {
        Member entity = new Member();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setNickName(converter.fromRow(row, prefix + "_nick_name", String.class));
        return entity;
    }
}
