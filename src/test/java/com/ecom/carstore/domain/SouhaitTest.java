package com.ecom.carstore.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ecom.carstore.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SouhaitTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Souhait.class);
        Souhait souhait1 = new Souhait();
        souhait1.setId(1L);
        Souhait souhait2 = new Souhait();
        souhait2.setId(souhait1.getId());
        assertThat(souhait1).isEqualTo(souhait2);
        souhait2.setId(2L);
        assertThat(souhait1).isNotEqualTo(souhait2);
        souhait1.setId(null);
        assertThat(souhait1).isNotEqualTo(souhait2);
    }
}
