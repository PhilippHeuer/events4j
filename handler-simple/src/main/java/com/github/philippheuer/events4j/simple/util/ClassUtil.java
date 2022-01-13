package com.github.philippheuer.events4j.simple.util;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.BiConsumer;

public class ClassUtil {

    private static final BiConsumer<Class<?>, Set<Class<?>>> ADD_INTERFACES = (c, set) -> {
        final Class<?>[] interfaces = c.getInterfaces();
        for (Class<?> anInterface : interfaces) {
            if (set.add(anInterface)) {
                ClassUtil.ADD_INTERFACES.accept(anInterface, set);
            }
        }
    };

    /**
     * get inheritance tree from a base class
     *
     * @param clazz the base class
     * @return an inheritance tree with all interfaces and superclasses, always includes the base class
     */
    public static Collection<Class<?>> getInheritanceTree(final Class<?> clazz) {
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
