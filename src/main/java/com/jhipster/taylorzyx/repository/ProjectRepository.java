package com.jhipster.taylorzyx.repository;

import com.jhipster.taylorzyx.domain.Project;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Project entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProjectRepository extends ReactiveCrudRepository<Project, Long>, ProjectRepositoryInternal {
    @Query("SELECT * FROM project entity WHERE entity.owner_id = :id")
    Flux<Project> findByOwner(Long id);

    @Query("SELECT * FROM project entity WHERE entity.owner_id IS NULL")
    Flux<Project> findAllWhereOwnerIsNull();

    @Override
    <S extends Project> Mono<S> save(S entity);

    @Override
    Flux<Project> findAll();

    @Override
    Mono<Project> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface ProjectRepositoryInternal {
    <S extends Project> Mono<S> save(S entity);

    Flux<Project> findAllBy(Pageable pageable);

    Flux<Project> findAll();

    Mono<Project> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Project> findAllBy(Pageable pageable, Criteria criteria);
}
