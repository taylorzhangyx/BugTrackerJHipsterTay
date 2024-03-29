package com.jhipster.taylorzyx.repository;

import com.jhipster.taylorzyx.domain.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Ticket entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TicketRepository extends ReactiveCrudRepository<Ticket, Long>, TicketRepositoryInternal {
    Flux<Ticket> findAllBy(Pageable pageable);

    @Override
    Mono<Ticket> findOneWithEagerRelationships(Long id);

    @Override
    Flux<Ticket> findAllWithEagerRelationships();

    @Override
    Flux<Ticket> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM ticket entity WHERE entity.project_id = :id")
    Flux<Ticket> findByProject(Long id);

    @Query("SELECT * FROM ticket entity WHERE entity.project_id IS NULL")
    Flux<Ticket> findAllWhereProjectIsNull();

    @Query("SELECT * FROM ticket entity WHERE entity.assigned_to_id = :id")
    Flux<Ticket> findByAssignedTo(Long id);

    @Query("SELECT * FROM ticket entity WHERE entity.assigned_to_id IS NULL")
    Flux<Ticket> findAllWhereAssignedToIsNull();

    @Query(
        "SELECT entity.* FROM ticket entity JOIN rel_ticket__label joinTable ON entity.id = joinTable.label_id WHERE joinTable.label_id = :id"
    )
    Flux<Ticket> findByLabel(Long id);

    Flux<Ticket> findAllByOrderByDueDateAsc(Pageable pageable);

    @Override
    <S extends Ticket> Mono<S> save(S entity);

    @Override
    Flux<Ticket> findAll();

    @Override
    Mono<Ticket> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface TicketRepositoryInternal {
    <S extends Ticket> Mono<S> save(S entity);

    Flux<Ticket> findAllBy(Pageable pageable);

    Flux<Ticket> findAll();

    Mono<Ticket> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Ticket> findAllBy(Pageable pageable, Criteria criteria);

    Mono<Ticket> findOneWithEagerRelationships(Long id);

    Flux<Ticket> findAllWithEagerRelationships();

    Flux<Ticket> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
