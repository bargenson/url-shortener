package com.bargenson.urlshortener.generator;

import org.assertj.core.api.Condition;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by bargenson on 2015-05-14.
 */
public class Base62TimestampUrlIdentifierGeneratorTest {

    @Test
    public void should_generateShorterThan10AlphanumericCharactersUniqueIds() throws Exception {
        // Given
        Base62TimestampUrlIdentifierGenerator generator = new Base62TimestampUrlIdentifierGenerator();
        Set<String> urlIds = new HashSet<>();
        int numberOfIds = 10_000;

        // When
        for (int i = 0; i < numberOfIds; i++) {
            urlIds.add(generator.generate());
        }

        // Then
        assertThat(urlIds).hasSize(numberOfIds);
        assertThat(urlIds).are(new Condition<String>(s -> s.length() <= 10, "shorter than 10 characters"));
        assertThat(urlIds).are(new Condition<String>((String s) -> {
            Pattern pattern = Pattern.compile("[a-zA-Z0-9]*");
            return pattern.matcher(s).matches();
        }, "only composed of alphanumeric characters"));
    }

}