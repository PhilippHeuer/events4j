package com.github.philippheuer.events4j.simple.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.philippheuer.events4j.api.domain.IEvent;
import com.github.philippheuer.events4j.core.domain.Event;
import com.github.philippheuer.events4j.simple.domain.TestEvent;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class ClassUtilTest {

    @Test
    void inheritanceTree() throws Exception {
        ArrayList<Class<?>> inheritanceTree = new ArrayList<>(ClassUtil.getInheritanceTree(TestEvent.class));

        assertEquals(TestEvent.class, inheritanceTree.get(0));
        assertEquals(Event.class, inheritanceTree.get(1));
        assertEquals(IEvent.class, inheritanceTree.get(2));
        assertEquals(Object.class, inheritanceTree.get(3));
        assertEquals(4, inheritanceTree.size());
    }

}
