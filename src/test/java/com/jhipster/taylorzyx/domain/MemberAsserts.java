package com.jhipster.taylorzyx.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class MemberAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertMemberAllPropertiesEquals(Member expected, Member actual) {
        assertMemberAutoGeneratedPropertiesEquals(expected, actual);
        assertMemberAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertMemberAllUpdatablePropertiesEquals(Member expected, Member actual) {
        assertMemberUpdatableFieldsEquals(expected, actual);
        assertMemberUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertMemberAutoGeneratedPropertiesEquals(Member expected, Member actual) {
        assertThat(expected)
            .as("Verify Member auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertMemberUpdatableFieldsEquals(Member expected, Member actual) {
        assertThat(expected)
            .as("Verify Member relevant properties")
            .satisfies(e -> assertThat(e.getNickName()).as("check nickName").isEqualTo(actual.getNickName()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertMemberUpdatableRelationshipsEquals(Member expected, Member actual) {}
}
