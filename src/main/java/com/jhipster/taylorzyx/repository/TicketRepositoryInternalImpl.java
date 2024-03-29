package com.jhipster.taylorzyx.repository;

import com.jhipster.taylorzyx.domain.Label;
import com.jhipster.taylorzyx.domain.Ticket;
import com.jhipster.taylorzyx.repository.rowmapper.ProjectRowMapper;
import com.jhipster.taylorzyx.repository.rowmapper.TicketRowMapper;
import com.jhipster.taylorzyx.repository.rowmapper.UserRowMapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.support.SimpleR2dbcRepository;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Comparison;
import org.springframework.data.relational.core.sql.Condition;
import org.springframework.data.relational.core.sql.Conditions;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoinCondition;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC custom repository implementation for the Ticket entity.
 */
@SuppressWarnings("unused")
class TicketRepositoryInternalImpl extends SimpleR2dbcRepository<Ticket, Long> implements TicketRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final ProjectRowMapper projectMapper;
    private final UserRowMapper userMapper;
    private final TicketRowMapper ticketMapper;

    private static final Table entityTable = Table.aliased("ticket", EntityManager.ENTITY_ALIAS);
    private static final Table projectTable = Table.aliased("project", "project");
    private static final Table assignedToTable = Table.aliased("jhi_user", "assignedTo");

    private static final EntityManager.LinkTable labelLink = new EntityManager.LinkTable("rel_ticket__label", "ticket_id", "label_id");

    public TicketRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        ProjectRowMapper projectMapper,
        UserRowMapper userMapper,
        TicketRowMapper ticketMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Ticket.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.projectMapper = projectMapper;
        this.userMapper = userMapper;
        this.ticketMapper = ticketMapper;
    }

    @Override
    public Flux<Ticket> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Ticket> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = TicketSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(ProjectSqlHelper.getColumns(projectTable, "project"));
        columns.addAll(UserSqlHelper.getColumns(assignedToTable, "assignedTo"));
        SelectFromAndJoinCondition selectFrom = Select.builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(projectTable)
            .on(Column.create("project_id", entityTable))
            .equals(Column.create("id", projectTable))
            .leftOuterJoin(assignedToTable)
            .on(Column.create("assigned_to_id", entityTable))
            .equals(Column.create("id", assignedToTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Ticket.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Ticket> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Ticket> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<Ticket> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<Ticket> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<Ticket> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private Ticket process(Row row, RowMetadata metadata) {
        Ticket entity = ticketMapper.apply(row, "e");
        entity.setProject(projectMapper.apply(row, "project"));
        entity.setAssignedTo(userMapper.apply(row, "assignedTo"));
        return entity;
    }

    @Override
    public <S extends Ticket> Mono<S> save(S entity) {
        return super.save(entity).flatMap((S e) -> updateRelations(e));
    }

    protected <S extends Ticket> Mono<S> updateRelations(S entity) {
        Mono<Void> result = entityManager.updateLinkTable(labelLink, entity.getId(), entity.getLabels().stream().map(Label::getId)).then();
        return result.thenReturn(entity);
    }

    @Override
    public Mono<Void> deleteById(Long entityId) {
        return deleteRelations(entityId).then(super.deleteById(entityId));
    }

    protected Mono<Void> deleteRelations(Long entityId) {
        return entityManager.deleteFromLinkTable(labelLink, entityId);
    }
}
