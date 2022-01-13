package com.github.philippheuer.events4j.simple.util;

import com.github.philippheuer.events4j.api.domain.IEvent;
import com.github.philippheuer.events4j.core.domain.Event;
import com.github.philippheuer.events4j.simple.domain.TestEvent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class ClassUtilTest {

    @Test
    public void testInheritanceTree() throws Exception {
        ArrayList<Class<?>> inheritanceTree = new ArrayList<>(ClassUtil.getInheritanceTree(TestEvent.class));

        Assertions.assertEquals(TestEvent.class, inheritanceTree.get(0));
        Assertions.assertEquals(Event.class, inheritanceTree.get(1));
        Assertions.assertEquals(IEvent.class, inheritanceTree.get(2));
        Assertions.assertEquals(Object.class, inheritanceTree.get(3));
        Assertions.assertEquals(4, inheritanceTree.size());
    }

}
