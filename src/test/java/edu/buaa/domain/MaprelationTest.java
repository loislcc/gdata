package edu.buaa.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import edu.buaa.web.rest.TestUtil;

public class MaprelationTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Maprelation.class);
        Maprelation maprelation1 = new Maprelation();
        maprelation1.setId(1L);
        Maprelation maprelation2 = new Maprelation();
        maprelation2.setId(maprelation1.getId());
        assertThat(maprelation1).isEqualTo(maprelation2);
        maprelation2.setId(2L);
        assertThat(maprelation1).isNotEqualTo(maprelation2);
        maprelation1.setId(null);
        assertThat(maprelation1).isNotEqualTo(maprelation2);
    }
}
