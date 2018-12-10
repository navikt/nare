package no.nav.nare.core.evaluation;

import org.junit.Test;

import static no.nav.nare.core.evaluation.Result.*;
import static org.assertj.core.api.Assertions.*;

public class ResultTest {

    @Test
    public void allPermutations() {
        // and
        assertThat(YES.and(YES)).isEqualTo(YES);
        assertThat(YES.and(NO)).isEqualTo(NO);
        assertThat(NO.and(YES)).isEqualTo(NO);
        assertThat(NO.and(NO)).isEqualTo(NO);

        assertThat(YES.and(MAYBE)).isEqualTo(MAYBE);
        assertThat(NO.and(MAYBE)).isEqualTo(NO);
        assertThat(MAYBE.and(MAYBE)).isEqualTo(MAYBE);
        assertThat(MAYBE.and(YES)).isEqualTo(MAYBE);
        assertThat(MAYBE.and(NO)).isEqualTo(NO);

        // or
        assertThat(YES.or(YES)).isEqualTo(YES);
        assertThat(YES.or(NO)).isEqualTo(YES);
        assertThat(NO.or(YES)).isEqualTo(YES);
        assertThat(NO.or(NO)).isEqualTo(NO);

        assertThat(YES.or(MAYBE)).isEqualTo(YES);
        assertThat(NO.or(MAYBE)).isEqualTo(MAYBE);
        assertThat(MAYBE.or(MAYBE)).isEqualTo(MAYBE);
        assertThat(MAYBE.or(YES)).isEqualTo(YES);
        assertThat(MAYBE.or(NO)).isEqualTo(MAYBE);

        // not
        assertThat(YES.not()).isEqualTo(NO);
        assertThat(NO.not()).isEqualTo(YES);
        assertThat(MAYBE.not()).isEqualTo(MAYBE);
    }
}
