package com.jhipster.taylorzyx.repository.rowmapper;

import com.jhipster.taylorzyx.domain.Team;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Team}, with proper type conversions.
 */
@Service
public class TeamRowMapper implements BiFunction<Row, String, Team> {

    private final ColumnConverter converter;

    public TeamRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Team} stored in the database.
     */
    @Override
    public Team apply(Row row, String prefix) {
        Team entity = new Team();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        return entity;
    }
}
