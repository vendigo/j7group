package com.github.vendigo.j7group;

import org.junit.Test;

import java.util.*;

import static com.github.vendigo.j7group.J7Group.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.emptyCollectionOf;
import static org.hamcrest.collection.IsMapContaining.hasEntry;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.IsCollectionContaining.hasItems;

public class J7GroupTest {
    private final Person petro = new Person("Petro", "Pomagai", 17);
    private final Person stan = new Person("Stan", "Marsh", 8);
    private final Person kyle = new Person("Kyle", "Broflovski", 8);
    private final Person boris = new Person("Boris", "Britva", 47);
    private final Person vinsent = new Person("Vinsent", "Vega", 50);

    @Test
    public void testCollectNamesToList() throws Exception {
        List<String> result = collectToListFrom(Arrays.asList(petro, boris, vinsent), field(Person.class).getName());

        assertThat(result, hasSize(3));
        assertThat(result, hasItems("Petro", "Boris", "Vinsent"));
    }

    @Test
    public void testCollectAgesToList() throws Exception {
        List<Integer> result = collectToListFrom(Arrays.asList(petro, boris, vinsent, kyle, stan),
                field(Person.class).getAge());

        assertThat(result, hasSize(5));
        assertThat(result, hasItems(17, 47, 50, 8, 8));
    }

    @Test
    public void testCollectAgesToSet() throws Exception {
        Set<Integer> result = collectToSetFrom(Arrays.asList(petro, boris, vinsent, kyle, stan),
                field(Person.class).getAge());

        assertThat(result, hasSize(4));
        assertThat(result, hasItems(17, 47, 50, 8));
    }

    @Test
    public void testCollectFromEmptyList() throws Exception {
        List<String> result = collectToListFrom(Collections.emptyList(), field(Person.class).getName());

        assertThat(result, emptyCollectionOf(String.class));
    }

    @Test
    public void testGroupBySurname() throws Exception {
        Map<String, Person> result = group(Arrays.asList(petro, boris, vinsent), by(Person.class).getSurname());

        assertThat(result.keySet(), hasSize(3));
        assertThat(result,
                allOf(
                        hasEntry("Pomagai", petro),
                        hasEntry("Britva", boris),
                        hasEntry("Vega", vinsent)
                ));
    }

    @Test
    public void testGroupByAge() throws Exception {
        Map<Integer, Person> result = group(Arrays.asList(petro, boris, vinsent), by(Person.class).getAge());

        assertThat(result, allOf(
                        hasEntry(17, petro),
                        hasEntry(47, boris),
                        hasEntry(50, vinsent)
                ));
    }

    @Test
    public void testGroupByAgeKeepLast() throws Exception {
        Map<Integer, Person> result = group(Arrays.asList(petro, boris, kyle, vinsent, stan), by(Person.class).getAge());

        assertThat(result, allOf(
                        hasEntry(17, petro),
                        hasEntry(47, boris),
                        hasEntry(50, vinsent),
                        hasEntry(8, stan)
                ));
    }

    @Test
    public void testGroupByAgeKeepFirst() throws Exception {
        Map<Integer, Person> result = group(Arrays.asList(petro, boris, kyle, vinsent, stan), by(Person.class).getAge(),
                KeyAmbiguityPolicy.KEEP_FIRST);

        assertThat(result, allOf(
                hasEntry(17, petro),
                hasEntry(47, boris),
                hasEntry(50, vinsent),
                hasEntry(8, kyle)
        ));
    }

    @Test(expected = KeyAmbiguityException.class)
    public void testGroupByAgeFailFast() throws Exception {
        group(Arrays.asList(petro, boris, kyle, vinsent, stan), by(Person.class).getAge(),
                KeyAmbiguityPolicy.FAIL_FAST);
    }

    @Test
    public void testGroupToListsByAge() throws Exception {
        Map<Integer, List<Person>> result = groupToLists(Arrays.asList(petro, boris, stan, stan, vinsent, kyle),
                by(Person.class).getAge());

        assertThat(result, allOf(
                        hasEntry(8, Arrays.asList(stan, stan, kyle)),
                        hasEntry(17, Collections.singletonList(petro)),
                        hasEntry(47, Collections.singletonList(boris)),
                        hasEntry(50, Collections.singletonList(vinsent))
                ));
    }

    @Test
    public void testGroupToSetsByAge() throws Exception {
        Map<Integer, Set<Person>> result = groupToSets(Arrays.asList(petro, boris, stan, stan, vinsent, kyle),
                by(Person.class).getAge());

        assertThat(result, allOf(
                        hasEntry(8, TestCollections.setOf(stan, kyle)),
                        hasEntry(17, TestCollections.setOf(petro)),
                        hasEntry(47, TestCollections.setOf(boris)),
                        hasEntry(50, TestCollections.setOf(vinsent))
                ));
    }

    @Test
    public void testMapNameToSurname() throws Exception {
        Map<String, String> result = map(Arrays.asList(petro, vinsent, boris), from(Person.class).getName(),
                to(Person.class).getSurname());

        assertThat(result, allOf(
                        hasEntry("Petro", "Pomagai"),
                        hasEntry("Vinsent", "Vega"),
                        hasEntry("Boris", "Britva")
                ));
    }

    @Test
    public void testMapAgeToName() throws Exception {
        Map<Integer, String> result = map(Arrays.asList(petro, vinsent, boris), from(Person.class).getAge(),
                to(Person.class).getName());

        assertThat(result, allOf(
                        hasEntry(17, "Petro"),
                        hasEntry(50, "Vinsent"),
                        hasEntry(47, "Boris")
                ));
    }

    @Test
    public void testMapAgeToNameKeepLast() throws Exception {
        Map<Integer, String> result = map(Arrays.asList(petro, vinsent, stan, boris, kyle), from(Person.class).getAge(),
                to(Person.class).getName());

        assertThat(result, allOf(
                        hasEntry(17, "Petro"),
                        hasEntry(50, "Vinsent"),
                        hasEntry(47, "Boris"),
                        hasEntry(8, "Kyle")
                ));
    }

    @Test
    public void testMapAgeToNameKeepFirst() throws Exception {
        Map<Integer, String> result = map(Arrays.asList(petro, vinsent, stan, boris, kyle), from(Person.class).getAge(),
                to(Person.class).getName(), KeyAmbiguityPolicy.KEEP_FIRST);

        assertThat(result, allOf(
                hasEntry(17, "Petro"),
                hasEntry(50, "Vinsent"),
                hasEntry(47, "Boris"),
                hasEntry(8, "Stan")
        ));
    }

    @Test(expected = KeyAmbiguityException.class)
    public void testMapAgeToNameFailFast() throws Exception {
        map(Arrays.asList(petro, vinsent, stan, boris, kyle), from(Person.class).getAge(),
                to(Person.class).getName(), KeyAmbiguityPolicy.FAIL_FAST);
    }

    @Test
    public void testMapToListsAgeToName() throws Exception {
        Map<Integer, List<String>> result = mapToLists(Arrays.asList(petro, vinsent, stan, stan, boris, kyle), 
                from(Person.class).getAge(),
                to(Person.class).getName());

        assertThat(result, allOf(
                hasEntry(17, Collections.singletonList("Petro")),
                hasEntry(50, Collections.singletonList("Vinsent")),
                hasEntry(47, Collections.singletonList("Boris")),
                hasEntry(8, Arrays.asList("Stan", "Stan", "Kyle"))
        ));
    }

    @Test
    public void testMapToSetsAgeToName() throws Exception {
        Map<Integer, Set<String>> result = mapToSets(Arrays.asList(petro, vinsent, stan, stan, boris, kyle),
                from(Person.class).getAge(),
                to(Person.class).getName());

        assertThat(result, allOf(
                hasEntry(17, TestCollections.setOf("Petro")),
                hasEntry(50, TestCollections.setOf("Vinsent")),
                hasEntry(47, TestCollections.setOf("Boris")),
                hasEntry(8, TestCollections.setOf("Stan", "Kyle"))
        ));
    }
}
