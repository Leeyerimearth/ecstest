package com.example.ecstest;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SampleTest {

    @Test
    void testSample() {
        String coffee = "coffee";
        String tea = "tea";

        assertThat(coffee).isNotEqualTo(tea);
    }
}
