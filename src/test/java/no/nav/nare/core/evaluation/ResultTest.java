package no.nav.nare.core.evaluation;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class ResultTest {

    @Test
    public void allPermutations() {
        // and
        Assertions.assertThat(Result.YES.and(Result.YES)).isEqualTo(Result.YES);
        Assertions.assertThat(Result.YES.and(Result.NO)).isEqualTo(Result.NO);
        Assertions.assertThat(Result.NO.and(Result.YES)).isEqualTo(Result.NO);
        Assertions.assertThat(Result.NO.and(Result.NO)).isEqualTo(Result.NO);

        Assertions.assertThat(Result.YES.and(Result.MAYBE)).isEqualTo(Result.MAYBE);
        Assertions.assertThat(Result.NO.and(Result.MAYBE)).isEqualTo(Result.NO);
        Assertions.assertThat(Result.MAYBE.and(Result.MAYBE)).isEqualTo(Result.MAYBE);
        Assertions.assertThat(Result.MAYBE.and(Result.YES)).isEqualTo(Result.MAYBE);
        Assertions.assertThat(Result.MAYBE.and(Result.NO)).isEqualTo(Result.NO);

        // or
        Assertions.assertThat(Result.YES.or(Result.YES)).isEqualTo(Result.YES);
        Assertions.assertThat(Result.YES.or(Result.NO)).isEqualTo(Result.YES);
        Assertions.assertThat(Result.NO.or(Result.YES)).isEqualTo(Result.YES);
        Assertions.assertThat(Result.NO.or(Result.NO)).isEqualTo(Result.NO);

        Assertions.assertThat(Result.YES.or(Result.MAYBE)).isEqualTo(Result.YES);
        Assertions.assertThat(Result.NO.or(Result.MAYBE)).isEqualTo(Result.MAYBE);
        Assertions.assertThat(Result.MAYBE.or(Result.MAYBE)).isEqualTo(Result.MAYBE);
        Assertions.assertThat(Result.MAYBE.or(Result.YES)).isEqualTo(Result.YES);
        Assertions.assertThat(Result.MAYBE.or(Result.NO)).isEqualTo(Result.MAYBE);

        // not
        Assertions.assertThat(Result.YES.not()).isEqualTo(Result.NO);
        Assertions.assertThat(Result.NO.not()).isEqualTo(Result.YES);

        Assertions.assertThat(Result.MAYBE.not()).isEqualTo(Result.MAYBE);
    }
}
