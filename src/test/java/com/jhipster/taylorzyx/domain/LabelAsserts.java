package com.jhipster.taylorzyx.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class LabelAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertLabelAllPropertiesEquals(Label expected, Label actual) {
        assertLabelAutoGeneratedPropertiesEquals(expected, actual);
        assertLabelAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertLabelAllUpdatablePropertiesEquals(Label expected, Label actual) {
        assertLabelUpdatableFieldsEquals(expected, actual);
        assertLabelUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertLabelAutoGeneratedPropertiesEquals(Label expected, Label actual) {
        assertThat(expected)
            .as("Verify Label auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertLabelUpdatableFieldsEquals(Label expected, Label actual) {
        assertThat(expected)
            .as("Verify Label relevant properties")
            .satisfies(e -> assertThat(e.getLabel()).as("check label").isEqualTo(actual.getLabel()))
            .satisfies(e -> assertThat(e.getDesc()).as("check desc").isEqualTo(actual.getDesc()))
            .satisfies(e -> assertThat(e.getFakeNumber()).as("check fakeNumber").isEqualTo(actual.getFakeNumber()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertLabelUpdatableRelationshipsEquals(Label expected, Label actual) {
        assertThat(expected)
            .as("Verify Label relationships")
            .satisfies(e -> assertThat(e.getTickets()).as("check tickets").isEqualTo(actual.getTickets()));
    }
}
