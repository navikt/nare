import org.junit.Test

import no.nav.knare.core.evaluations.Resultat.*
import org.assertj.core.api.Assertions.*

class ResultatTest {

    @Test
    fun allPermutations() {
        assertThat(JA and JA).isEqualTo(JA)
        assertThat(JA and NEI).isEqualTo(NEI)
        assertThat(NEI and JA).isEqualTo(NEI)
        assertThat(NEI and NEI).isEqualTo(NEI)

        assertThat(JA and KANSKJE).isEqualTo(KANSKJE)
        assertThat(NEI and KANSKJE).isEqualTo(NEI)
        assertThat(KANSKJE and KANSKJE).isEqualTo(KANSKJE)
        assertThat(KANSKJE and JA).isEqualTo(KANSKJE)
        assertThat(KANSKJE and NEI).isEqualTo(NEI)

        assertThat(JA or JA).isEqualTo(JA)
        assertThat(JA or NEI).isEqualTo(JA)
        assertThat(NEI or JA).isEqualTo(JA)
        assertThat(NEI or NEI).isEqualTo(NEI)

        assertThat(JA or KANSKJE).isEqualTo(JA)
        assertThat(NEI or KANSKJE).isEqualTo(KANSKJE)
        assertThat(KANSKJE or KANSKJE).isEqualTo(KANSKJE)
        assertThat(KANSKJE or JA).isEqualTo(JA)
        assertThat(KANSKJE or NEI).isEqualTo(KANSKJE)

        assertThat(JA.not()).isEqualTo(NEI)
        assertThat(NEI.not()).isEqualTo(JA)
        assertThat(KANSKJE.not()).isEqualTo(KANSKJE)
    }
}