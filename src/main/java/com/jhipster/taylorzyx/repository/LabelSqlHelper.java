package com.jhipster.taylorzyx.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class LabelSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("label", table, columnPrefix + "_label"));
        columns.add(Column.aliased("jhi_desc", table, columnPrefix + "_jhi_desc"));
        columns.add(Column.aliased("fake_number", table, columnPrefix + "_fake_number"));
        columns.add(Column.aliased("some_faker", table, columnPrefix + "_some_faker"));

        return columns;
    }
}
