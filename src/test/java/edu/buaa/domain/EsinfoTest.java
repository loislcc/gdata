package edu.buaa.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import edu.buaa.web.rest.TestUtil;

public class EsinfoTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Esinfo.class);
        Esinfo esinfo1 = new Esinfo();
        esinfo1.setId(1L);
        Esinfo esinfo2 = new Esinfo();
        esinfo2.setId(esinfo1.getId());
        assertThat(esinfo1).isEqualTo(esinfo2);
        esinfo2.setId(2L);
        assertThat(esinfo1).isNotEqualTo(esinfo2);
        esinfo1.setId(null);
        assertThat(esinfo1).isNotEqualTo(esinfo2);
    }
}
