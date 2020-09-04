package edu.buaa.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import edu.buaa.web.rest.TestUtil;

public class LoginfoTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Loginfo.class);
        Loginfo loginfo1 = new Loginfo();
        loginfo1.setId(1L);
        Loginfo loginfo2 = new Loginfo();
        loginfo2.setId(loginfo1.getId());
        assertThat(loginfo1).isEqualTo(loginfo2);
        loginfo2.setId(2L);
        assertThat(loginfo1).isNotEqualTo(loginfo2);
        loginfo1.setId(null);
        assertThat(loginfo1).isNotEqualTo(loginfo2);
    }
}
