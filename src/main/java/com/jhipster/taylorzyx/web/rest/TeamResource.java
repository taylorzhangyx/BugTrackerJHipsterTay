package com.jhipster.taylorzyx.web.rest;

import com.jhipster.taylorzyx.domain.Team;
import com.jhipster.taylorzyx.repository.TeamRepository;
import com.jhipster.taylorzyx.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.jhipster.taylorzyx.domain.Team}.
 */
@RestController
@RequestMapping("/api/teams")
@Transactional
public class TeamResource {

    private final Logger log = LoggerFactory.getLogger(TeamResource.class);

    private static final String ENTITY_NAME = "team";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TeamRepository teamRepository;

    public TeamResource(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    /**
     * {@code POST  /teams} : Create a new team.
     *
     * @param team the team to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new team, or with status {@code 400 (Bad Request)} if the team has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<Team>> createTeam(@RequestBody Team team) throws URISyntaxException {
        log.debug("REST request to save Team : {}", team);
        if (team.getId() != null) {
            throw new BadRequestAlertException("A new team cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return teamRepository
            .save(team)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/teams/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /teams/:id} : Updates an existing team.
     *
     * @param id the id of the team to save.
     * @param team the team to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated team,
     * or with status {@code 400 (Bad Request)} if the team is not valid,
     * or with status {@code 500 (Internal Server Error)} if the team couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<Team>> updateTeam(@PathVariable(value = "id", required = false) final Long id, @RequestBody Team team)
        throws URISyntaxException {
        log.debug("REST request to update Team : {}, {}", id, team);
        if (team.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, team.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return teamRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return teamRepository
                    .save(team)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(
                        result ->
                            ResponseEntity.ok()
                                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                                .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /teams/:id} : Partial updates given fields of an existing team, field will ignore if it is null
     *
     * @param id the id of the team to save.
     * @param team the team to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated team,
     * or with status {@code 400 (Bad Request)} if the team is not valid,
     * or with status {@code 404 (Not Found)} if the team is not found,
     * or with status {@code 500 (Internal Server Error)} if the team couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<Team>> partialUpdateTeam(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Team team
    ) throws URISyntaxException {
        log.debug("REST request to partial update Team partially : {}, {}", id, team);
        if (team.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, team.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return teamRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<Team> result = teamRepository
                    .findById(team.getId())
                    .map(existingTeam -> {
                        if (team.getName() != null) {
                            existingTeam.setName(team.getName());
                        }
                        if (team.getDescription() != null) {
                            existingTeam.setDescription(team.getDescription());
                        }

                        return existingTeam;
                    })
                    .flatMap(teamRepository::save);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(
                        res ->
                            ResponseEntity.ok()
                                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                                .body(res)
                    );
            });
    }

    /**
     * {@code GET  /teams} : get all the teams.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of teams in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<Team>> getAllTeams() {
        log.debug("REST request to get all Teams");
        return teamRepository.findAll().collectList();
    }

    /**
     * {@code GET  /teams} : get all the teams as a stream.
     * @return the {@link Flux} of teams.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<Team> getAllTeamsAsStream() {
        log.debug("REST request to get all Teams as a stream");
        return teamRepository.findAll();
    }

    /**
     * {@code GET  /teams/:id} : get the "id" team.
     *
     * @param id the id of the team to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the team, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<Team>> getTeam(@PathVariable("id") Long id) {
        log.debug("REST request to get Team : {}", id);
        Mono<Team> team = teamRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(team);
    }

    /**
     * {@code DELETE  /teams/:id} : delete the "id" team.
     *
     * @param id the id of the team to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteTeam(@PathVariable("id") Long id) {
        log.debug("REST request to delete Team : {}", id);
        return teamRepository
            .deleteById(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent()
                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                        .build()
                )
            );
    }
}
