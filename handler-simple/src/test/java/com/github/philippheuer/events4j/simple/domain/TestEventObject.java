package com.github.philippheuer.events4j.simple.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A test-event to use when testing events4j.
 *
 * @author Philipp Heuer [https://github.com/PhilippHeuer]
 * @version %I%, %G%
 * @since 1.0
 */
@Data
@NoArgsConstructor
public class TestEventObject {

    private String id;

}
