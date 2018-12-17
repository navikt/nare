import org.junit.Test

import no.nav.knare.core.evaluations.Resultat.*
import org.assertj.core.api.Assertions.*

class ResultTest {

    @Test
    fun allPermutations() {
        // og
        assertThat(YES.and(YES)).isEqualTo(YES)
        assertThat(YES.and(NO)).isEqualTo(NO)
        assertThat(NO.and(YES)).isEqualTo(NO)
        assertThat(NO.and(NO)).isEqualTo(NO)

        assertThat(YES.and(MAYBE)).isEqualTo(MAYBE)
        assertThat(NO.and(MAYBE)).isEqualTo(NO)
        assertThat(MAYBE.and(MAYBE)).isEqualTo(MAYBE)
        assertThat(MAYBE.and(YES)).isEqualTo(MAYBE)
        assertThat(MAYBE.and(NO)).isEqualTo(NO)

        // eller
        assertThat(YES.or(YES)).isEqualTo(YES)
        assertThat(YES.or(NO)).isEqualTo(YES)
        assertThat(NO.or(YES)).isEqualTo(YES)
        assertThat(NO.or(NO)).isEqualTo(NO)

        assertThat(YES.or(MAYBE)).isEqualTo(YES)
        assertThat(NO.or(MAYBE)).isEqualTo(MAYBE)
        assertThat(MAYBE.or(MAYBE)).isEqualTo(MAYBE)
        assertThat(MAYBE.or(YES)).isEqualTo(YES)
        assertThat(MAYBE.or(NO)).isEqualTo(MAYBE)

        // ikke
        assertThat(YES.not()).isEqualTo(NO)
        assertThat(NO.not()).isEqualTo(YES)
        assertThat(MAYBE.not()).isEqualTo(MAYBE)
    }
}