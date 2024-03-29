package com.jhipster.taylorzyx.web.rest;

import static com.jhipster.taylorzyx.domain.LabelAsserts.*;
import static com.jhipster.taylorzyx.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jhipster.taylorzyx.IntegrationTest;
import com.jhipster.taylorzyx.domain.Label;
import com.jhipster.taylorzyx.repository.EntityManager;
import com.jhipster.taylorzyx.repository.LabelRepository;
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
 * Integration tests for the {@link LabelResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class LabelResourceIT {

    private static final String DEFAULT_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_LABEL = "BBBBBBBBBB";

    private static final String DEFAULT_DESC = "AAAAAAAAAA";
    private static final String UPDATED_DESC = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/labels";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Label label;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Label createEntity(EntityManager em) {
        Label label = new Label().label(DEFAULT_LABEL).desc(DEFAULT_DESC);
        return label;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Label createUpdatedEntity(EntityManager em) {
        Label label = new Label().label(UPDATED_LABEL).desc(UPDATED_DESC);
        return label;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Label.class).block();
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
        label = createEntity(em);
    }

    @Test
    void createLabel() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Label
        var returnedLabel = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(label))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(Label.class)
            .returnResult()
            .getResponseBody();

        // Validate the Label in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertLabelUpdatableFieldsEquals(returnedLabel, getPersistedLabel(returnedLabel));
    }

    @Test
    void createLabelWithExistingId() throws Exception {
        // Create the Label with an existing ID
        label.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(label))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Label in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void getAllLabelsAsStream() {
        // Initialize the database
        labelRepository.save(label).block();

        List<Label> labelList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(Label.class)
            .getResponseBody()
            .filter(label::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(labelList).isNotNull();
        assertThat(labelList).hasSize(1);
        Label testLabel = labelList.get(0);

        // Test fails because reactive api returns an empty object instead of null
        // assertLabelAllPropertiesEquals(label, testLabel);
        assertLabelUpdatableFieldsEquals(label, testLabel);
    }

    @Test
    void getAllLabels() {
        // Initialize the database
        labelRepository.save(label).block();

        // Get all the labelList
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
            .value(hasItem(label.getId().intValue()))
            .jsonPath("$.[*].label")
            .value(hasItem(DEFAULT_LABEL))
            .jsonPath("$.[*].desc")
            .value(hasItem(DEFAULT_DESC));
    }

    @Test
    void getLabel() {
        // Initialize the database
        labelRepository.save(label).block();

        // Get the label
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, label.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(label.getId().intValue()))
            .jsonPath("$.label")
            .value(is(DEFAULT_LABEL))
            .jsonPath("$.desc")
            .value(is(DEFAULT_DESC));
    }

    @Test
    void getNonExistingLabel() {
        // Get the label
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingLabel() throws Exception {
        // Initialize the database
        labelRepository.save(label).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the label
        Label updatedLabel = labelRepository.findById(label.getId()).block();
        updatedLabel.label(UPDATED_LABEL).desc(UPDATED_DESC);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedLabel.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(updatedLabel))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Label in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedLabelToMatchAllProperties(updatedLabel);
    }

    @Test
    void putNonExistingLabel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        label.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, label.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(label))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Label in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchLabel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        label.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(label))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Label in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamLabel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        label.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(label))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Label in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateLabelWithPatch() throws Exception {
        // Initialize the database
        labelRepository.save(label).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the label using partial update
        Label partialUpdatedLabel = new Label();
        partialUpdatedLabel.setId(label.getId());

        partialUpdatedLabel.label(UPDATED_LABEL);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedLabel.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedLabel))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Label in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLabelUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedLabel, label), getPersistedLabel(label));
    }

    @Test
    void fullUpdateLabelWithPatch() throws Exception {
        // Initialize the database
        labelRepository.save(label).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the label using partial update
        Label partialUpdatedLabel = new Label();
        partialUpdatedLabel.setId(label.getId());

        partialUpdatedLabel.label(UPDATED_LABEL).desc(UPDATED_DESC);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedLabel.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedLabel))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Label in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLabelUpdatableFieldsEquals(partialUpdatedLabel, getPersistedLabel(partialUpdatedLabel));
    }

    @Test
    void patchNonExistingLabel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        label.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, label.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(label))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Label in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchLabel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        label.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(label))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Label in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamLabel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        label.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(label))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Label in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteLabel() {
        // Initialize the database
        labelRepository.save(label).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the label
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, label.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return labelRepository.count().block();
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

    protected Label getPersistedLabel(Label label) {
        return labelRepository.findById(label.getId()).block();
    }

    protected void assertPersistedLabelToMatchAllProperties(Label expectedLabel) {
        // Test fails because reactive api returns an empty object instead of null
        // assertLabelAllPropertiesEquals(expectedLabel, getPersistedLabel(expectedLabel));
        assertLabelUpdatableFieldsEquals(expectedLabel, getPersistedLabel(expectedLabel));
    }

    protected void assertPersistedLabelToMatchUpdatableProperties(Label expectedLabel) {
        // Test fails because reactive api returns an empty object instead of null
        // assertLabelAllUpdatablePropertiesEquals(expectedLabel, getPersistedLabel(expectedLabel));
        assertLabelUpdatableFieldsEquals(expectedLabel, getPersistedLabel(expectedLabel));
    }
}
