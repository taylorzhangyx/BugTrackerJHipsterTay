package com.jhipster.taylorzyx.repository;

import com.jhipster.taylorzyx.domain.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Member entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MemberRepository extends ReactiveCrudRepository<Member, Long>, MemberRepositoryInternal {
    @Override
    <S extends Member> Mono<S> save(S entity);

    @Override
    Flux<Member> findAll();

    @Override
    Mono<Member> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface MemberRepositoryInternal {
    <S extends Member> Mono<S> save(S entity);

    Flux<Member> findAllBy(Pageable pageable);

    Flux<Member> findAll();

    Mono<Member> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Member> findAllBy(Pageable pageable, Criteria criteria);
}
