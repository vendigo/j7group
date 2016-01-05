package com.github.vendigo.j7group;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static com.github.vendigo.j7group.J7Group.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsEmptyCollection.emptyCollectionOf;
import static org.hamcrest.collection.IsMapContaining.hasEntry;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.IsCollectionContaining.hasItems;

public class TestJ7Group {
    private final Person petro = new Person("Petro", "Pomagai", 17);
    private final Person stan = new Person("Stan", "Marsh", 8);
    private final Person kyle = new Person("Kyle", "Broflovski", 8);
    private final Person boris = new Person("Boris", "Britva", 47);
    private final Person vinsent = new Person("Vinsent", "Vega", 50);

    @Test
    public void testCollectNames() throws Exception {
        assertThat(collectFrom(Arrays.asList(petro, boris, vinsent), field(Person.class).getName()),
                hasItems("Petro", "Boris", "Vinsent"));
    }

    @Test
    public void testCollectAges() throws Exception {
        assertThat(collectFrom(Arrays.asList(petro, boris, vinsent), field(Person.class).getAge()),
                hasItems(17, 47, 50));
    }

    @Test
    public void testCollectFromEmptyList() throws Exception {
        assertThat(collectFrom(Collections.emptyList(), field(Person.class).getName()),
                emptyCollectionOf(String.class));
    }

    @Test
    public void testGroupBySurname() throws Exception {
        assertThat(group(Arrays.asList(petro, boris, vinsent), by(Person.class).getSurname()),
                allOf(
                        hasEntry("Pomagai", petro),
                        hasEntry("Britva", boris),
                        hasEntry("Vega", vinsent)
                ));
    }

    @Test
    public void testGroupByAge() throws Exception {
        assertThat(group(Arrays.asList(petro, boris, vinsent), by(Person.class).getAge()),
                allOf(
                        hasEntry(17, petro),
                        hasEntry(47, boris),
                        hasEntry(50, vinsent)
                ));
    }

    @Test
    public void testGroupByAgeRemainsLast() throws Exception {
        assertThat(group(Arrays.asList(petro, boris, kyle, vinsent, stan), by(Person.class).getAge()),
                allOf(
                        hasEntry(17, petro),
                        hasEntry(47, boris),
                        hasEntry(50, vinsent),
                        hasEntry(8, stan)
                ));
    }


    @Test
    public void testGroupToListsByAge() throws Exception {
        assertThat(groupToLists(Arrays.asList(petro, boris, stan, stan, vinsent, kyle), by(Person.class).getAge()),
                allOf(
                        hasEntry(8, Arrays.asList(stan, stan, kyle)),
                        hasEntry(17, Collections.singletonList(petro)),
                        hasEntry(47, Collections.singletonList(boris)),
                        hasEntry(50, Collections.singletonList(vinsent))
                ));
    }

    @Test
    public void testGroupToSetsByAge() throws Exception {
        assertThat(groupToSets(Arrays.asList(petro, boris, stan, stan, vinsent, kyle), by(Person.class).getAge()),
                allOf(
                        hasEntry(8, TestCollections.setOf(stan, kyle)),
                        hasEntry(17, TestCollections.setOf(petro)),
                        hasEntry(47, TestCollections.setOf(boris)),
                        hasEntry(50, TestCollections.setOf(vinsent))
                ));
    }

    @Test
    public void testMapNameToSurname() throws Exception {
        assertThat(map(Arrays.asList(petro, vinsent, boris), from(Person.class).getName(), to(Person.class).getSurname()),
                allOf(
                        hasEntry("Petro", "Pomagai"),
                        hasEntry("Vinsent", "Vega"),
                        hasEntry("Boris", "Britva")
                ));
    }

    @Test
    public void testMapAgeToName() throws Exception {
        assertThat(map(Arrays.asList(petro, vinsent, boris), from(Person.class).getAge(), to(Person.class).getName()),
                allOf(
                        hasEntry(17, "Petro"),
                        hasEntry(50, "Vinsent"),
                        hasEntry(47, "Boris")
                ));
    }

    @Test
    public void testMapAgeToNameRemainsLast() throws Exception {
        assertThat(map(Arrays.asList(petro, vinsent, stan, boris, kyle), from(Person.class).getAge(), to(Person.class).getName()),
                allOf(
                        hasEntry(17, "Petro"),
                        hasEntry(50, "Vinsent"),
                        hasEntry(47, "Boris"),
                        hasEntry(8, "Kyle")
                ));
    }
}
