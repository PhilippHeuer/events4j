package com.github.philippheuer.events4j.simple;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.BiConsumer;

class ClassUtil {

    private static final BiConsumer<Class<?>, Set<Class<?>>> ADD_INTERFACES = (c, set) -> {
        final Class<?>[] interfaces = c.getInterfaces();
        for (Class<?> anInterface : interfaces) {
            if (set.add(anInterface)) {
                ClassUtil.ADD_INTERFACES.accept(anInterface, set);
            }
        }
    };

    static Collection<Class<?>> getInheritanceTree(final Class<?> clazz) {
        final Set<Class<?>> set = new LinkedHashSet<>();

        Class<?> c = clazz;
        while (c != null) {
            set.add(c);
            ADD_INTERFACES.accept(c, set);
            c = c.getSuperclass();
        }

        return Collections.unmodifiableSet(set);
    }

}
