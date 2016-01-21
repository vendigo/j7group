# J7Group
## Description
J7Group is a small open source Java library which provide convenient, DSL like, and type safe way for manipulation with collections.
 It allows to perform such operations as **collect**, **groupBy** and **map** without lyambda expressions (e.g. in Java 7 and below.)
 
## Usage
Lets say, we have class Person:

```java

    public class Person {
        private String name;
        private String surname;
        private int age;
    
        public Person() {
        }
    
        public Person(String name, String surname, int age) {
            this.name = name;
            this.surname = surname;
            this.age = age;
        }
    
        public String getName() {
            return name;
        }
    
        public String getSurname() {
            return surname;
        }
    
        public int getAge() {
            return age;
        }
        }
```

And somewhere else:

```java

    Person stan = new Person("Stan", "Marsh", 8);
    Person kyle = new Person("Kyle", "Broflovski", 8);
    Person eric = new Person("Eric", "Cartman", 9);
    Person kenny = new Person("Kenny", "McCormick", 8);
    Person randy = new Person("Randy", "Marsh", 35);
    
    List<Person> characters = Arrays.asList(stan, kyle, eric, kenny, randy);
```
 
Now we want to collect all names:
 
```java

    Set<String> names = collectToSetFrom(characters, field(Person.class).getName());
    // Got ["Stan", "Kyle", "Eric", "Kenny", "Randy"]
```

Or to group characters by name:

```java

    Map<String, Person> personByName = group(characters, by(Person.class).getName());
    // Got {"Stan" -> stan, "Kyle" -> kyle, "Eric" -> eric, "Kenny" -> kenny, "Randy" -> randy}
```

Or to group characters by age. However there are equal ages, so we want to get map of lists:

```java

    Map<String, List<Person>> personByAge = groupToLists(characters, by(Person.class).getAge());
    // Got {8 -> [stan, kyle, kenny], 9 -> [eric], 35 -> [randy]}
```

And finally, we want to build mapping from name to surname:

```java

    Map<String, String> nameToSurname = map(characters, from(Person.class).getName(), to(Person.class).getSurname());
    // Got {"Stan" -> "Marsh", "Kyle" -> "Broflovski", "Eric" -> "Cartman", "Kenny" -> "McCormick", "Randy" -> "Marsh"}
```

## Restrictions

Since library uses cglib there are some restrictions on the target entity class:
* It should not be final
* It should have default constructor
* Fields should have getters

## Java 8 analogs

```java
    
     //Collect
        List<String> names = characters.stream().map(Person::getName).collect(Collectors.toList());
        System.out.println("names: "+ names);
        //Group
        Map<String, Person> personByName = characters.stream().collect(Collectors.toMap(Person::getName, Function.<Person>identity()));
        System.out.println("personByName: "+personByName);
        //GroupToList
        Map<String, List<Person>> personsByName = characters.stream().collect(Collectors.groupingBy(Person::getName));
        System.out.println("personsByName: " + personsByName);
        //Map
        Map<String, String> surnameByName = characters.stream().collect(Collectors.toMap(Person::getName, Person::getSurname));
        System.out.println("Surname by name: "+surnameByName);
```

## Groovy analogs

```groovyy
    
 //Collect
 List<String> names = characters.collect { it.name }
 println "names: $names"
 //Group
 Map<String, String> personByName = characters.collectEntries{[it.name, it]}
 println "personByName: $personByName"
 //GroupToList
 Map<String, List<Person>> personsByName = characters.groupBy { it.name }
 println "personsByName: $personsByName"
 //Map
 Map<String, String> surnameByName = characters.collectEntries{[it.name, it.surname]}
 println "surnameByName: $surnameByName"
```
