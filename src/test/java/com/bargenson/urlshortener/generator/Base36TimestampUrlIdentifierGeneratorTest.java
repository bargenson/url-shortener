package com.bargenson.urlshortener.generator;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by bargenson on 2015-05-14.
 */
public class Base36TimestampUrlIdentifierGeneratorTest {

    @Test
    public void should_generateBase36Timestamp() throws Exception {
        // Given
        Base36TimestampUrlIdentifierGenerator generator = new Base36TimestampUrlIdentifierGenerator();
        long timestamp = System.currentTimeMillis();

        // When
        final String result = generator.generate();

        // Then
        assertThat(result).isNotNull().isNotEmpty();
        assertThat(Long.valueOf(result, 36)).isBetween(timestamp, System.currentTimeMillis());
    }
}