package com.jhipster.taylorzyx.domain;

import static com.jhipster.taylorzyx.domain.MemberTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.jhipster.taylorzyx.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MemberTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Member.class);
        Member member1 = getMemberSample1();
        Member member2 = new Member();
        assertThat(member1).isNotEqualTo(member2);

        member2.setId(member1.getId());
        assertThat(member1).isEqualTo(member2);

        member2 = getMemberSample2();
        assertThat(member1).isNotEqualTo(member2);
    }
}
