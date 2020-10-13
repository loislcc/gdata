package edu.buaa.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import edu.buaa.web.rest.TestUtil;

public class CycletaskTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cycletask.class);
        Cycletask cycletask1 = new Cycletask();
        cycletask1.setId(1L);
        Cycletask cycletask2 = new Cycletask();
        cycletask2.setId(cycletask1.getId());
        assertThat(cycletask1).isEqualTo(cycletask2);
        cycletask2.setId(2L);
        assertThat(cycletask1).isNotEqualTo(cycletask2);
        cycletask1.setId(null);
        assertThat(cycletask1).isNotEqualTo(cycletask2);
    }
}
