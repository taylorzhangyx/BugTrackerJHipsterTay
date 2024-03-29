package com.jhipster.taylorzyx.web.rest;

import static com.jhipster.taylorzyx.domain.TeamAsserts.*;
import static com.jhipster.taylorzyx.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jhipster.taylorzyx.IntegrationTest;
import com.jhipster.taylorzyx.domain.Team;
import com.jhipster.taylorzyx.repository.EntityManager;
import com.jhipster.taylorzyx.repository.TeamRepository;
import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link TeamResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class TeamResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/teams";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Team team;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Team createEntity(EntityManager em) {
        Team team = new Team().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
        return team;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Team createUpdatedEntity(EntityManager em) {
        Team team = new Team().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        return team;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Team.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        team = createEntity(em);
    }

    @Test
    void createTeam() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Team
        var returnedTeam = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(team))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(Team.class)
            .returnResult()
            .getResponseBody();

        // Validate the Team in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertTeamUpdatableFieldsEquals(returnedTeam, getPersistedTeam(returnedTeam));
    }

    @Test
    void createTeamWithExistingId() throws Exception {
        // Create the Team with an existing ID
        team.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(team))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Team in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void getAllTeamsAsStream() {
        // Initialize the database
        teamRepository.save(team).block();

        List<Team> teamList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(Team.class)
            .getResponseBody()
            .filter(team::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(teamList).isNotNull();
        assertThat(teamList).hasSize(1);
        Team testTeam = teamList.get(0);

        // Test fails because reactive api returns an empty object instead of null
        // assertTeamAllPropertiesEquals(team, testTeam);
        assertTeamUpdatableFieldsEquals(team, testTeam);
    }

    @Test
    void getAllTeams() {
        // Initialize the database
        teamRepository.save(team).block();

        // Get all the teamList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(team.getId().intValue()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION));
    }

    @Test
    void getTeam() {
        // Initialize the database
        teamRepository.save(team).block();

        // Get the team
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, team.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(team.getId().intValue()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION));
    }

    @Test
    void getNonExistingTeam() {
        // Get the team
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingTeam() throws Exception {
        // Initialize the database
        teamRepository.save(team).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the team
        Team updatedTeam = teamRepository.findById(team.getId()).block();
        updatedTeam.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedTeam.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(updatedTeam))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Team in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTeamToMatchAllProperties(updatedTeam);
    }

    @Test
    void putNonExistingTeam() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        team.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, team.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(team))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Team in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchTeam() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        team.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(team))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Team in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamTeam() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        team.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(team))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Team in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateTeamWithPatch() throws Exception {
        // Initialize the database
        teamRepository.save(team).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the team using partial update
        Team partialUpdatedTeam = new Team();
        partialUpdatedTeam.setId(team.getId());

        partialUpdatedTeam.name(UPDATED_NAME);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedTeam.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedTeam))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Team in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTeamUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedTeam, team), getPersistedTeam(team));
    }

    @Test
    void fullUpdateTeamWithPatch() throws Exception {
        // Initialize the database
        teamRepository.save(team).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the team using partial update
        Team partialUpdatedTeam = new Team();
        partialUpdatedTeam.setId(team.getId());

        partialUpdatedTeam.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedTeam.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedTeam))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Team in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTeamUpdatableFieldsEquals(partialUpdatedTeam, getPersistedTeam(partialUpdatedTeam));
    }

    @Test
    void patchNonExistingTeam() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        team.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, team.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(team))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Team in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchTeam() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        team.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(team))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Team in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamTeam() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        team.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(team))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Team in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteTeam() {
        // Initialize the database
        teamRepository.save(team).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the team
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, team.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return teamRepository.count().block();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Team getPersistedTeam(Team team) {
        return teamRepository.findById(team.getId()).block();
    }

    protected void assertPersistedTeamToMatchAllProperties(Team expectedTeam) {
        // Test fails because reactive api returns an empty object instead of null
        // assertTeamAllPropertiesEquals(expectedTeam, getPersistedTeam(expectedTeam));
        assertTeamUpdatableFieldsEquals(expectedTeam, getPersistedTeam(expectedTeam));
    }

    protected void assertPersistedTeamToMatchUpdatableProperties(Team expectedTeam) {
        // Test fails because reactive api returns an empty object instead of null
        // assertTeamAllUpdatablePropertiesEquals(expectedTeam, getPersistedTeam(expectedTeam));
        assertTeamUpdatableFieldsEquals(expectedTeam, getPersistedTeam(expectedTeam));
    }
}
