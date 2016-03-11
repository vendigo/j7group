![j7Group](/logo.png?raw=true "j7Group")
## Description
J7Group is a small open source Java library which provide convenient, DSL like, and type safe way for manipulation with collections.
It allows to perform such operations as **collect**, **groupBy**, **filter** and **map** without lyambda expressions (e.g. in Java 7 and below.)
For detailed documentation check Javadoc of [J7Group.java](https://github.com/vendigo/j7group/blob/master/src/main/java/com/github/vendigo/j7group/J7Group.java). 
To see all possible use cases check [J7GroupTest.java](https://github.com/vendigo/j7group/blob/master/src/test/java/com/github/vendigo/j7group/J7GroupTest.java).


## Credits
Inspired by [Mockito](http://mockito.org/) 
 
## Usage examples
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
        
        public boolean isAdult() {
            return age >= 18;
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
    
    List<Person> characters = listOf(stan, kyle, eric, kenny, randy);
```
 
Now we want to collect all names:
 
```java

    Set<String> names = collectToSetFrom(characters, field(Person.class).getName());
    // Got ["Stan", "Kyle", "Eric", "Kenny", "Randy"]
```

Group characters by name:

```java

    Map<String, Person> personByName = group(characters, by(Person.class).getName());
    // Got {"Stan" -> stan, "Kyle" -> kyle, "Eric" -> eric, "Kenny" -> kenny, "Randy" -> randy}
```

Group characters by age. However there are equal ages, so we want to get map of lists:

```java

    Map<String, List<Person>> personByAge = groupToLists(characters, by(Person.class).getAge());
    // Got {8 -> [stan, kyle, kenny], 9 -> [eric], 35 -> [randy]}
```

Build mapping from name to surname:

```java

    Map<String, String> nameToSurname = map(characters, from(Person.class).getName(), to(Person.class).getSurname());
    // Got {"Stan" -> "Marsh", "Kyle" -> "Broflovski", "Eric" -> "Cartman", "Kenny" -> "McCormick", "Randy" -> "Marsh"}
```

Collect all adults:

```java
    List<Person> adults = collect(characters, whenFalse(Person.class).isAdult());
    // Got [randy]
```

Remove all adults from the collection:

```java
    removeFrom(characters, whenTrue(Person.class).isAdult());
    // Got in characters [stan, kyle, eric, kenny]
```

## Restrictions

Since library uses cglib there are some restrictions on the target entity class:
* It should not be final
* It should have default constructor
* Fields should have getters

## Analogs

```java
    
     // Collect field
        // j7Group
        collectToListFrom(characters, field(Person.class).getName());     
        // Java 8   
        characters.stream().map(Person::getName).collect(Collectors.toList());
        // Groovy
        characters.collect { it.name }
        
    // Collect with predicate
        // j7Group
        collect(characters, whenTrue(Person.class).isAdult());
        // Java 8
        characters.stream().filter(Person::isAdult).collect(Collectors.toList());
        // Groovy
        characters.findAll { it.isAdult()}
        
     // GroupBy
        // j7Group
        group(characters, by(Person.class).getName());
        // Java 8
        characters.stream().collect(Collectors.toMap(Person::getName, Function.<Person>identity()));
        // Groovy
        characters.collectEntries{[it.name, it]}
     
     // GroupBy to Lists   
        // j7Group
        groupToLists(characters, by(Person.class).getAge());       
        // Java 8
        characters.stream().collect(Collectors.groupingBy(Person::getAge));
        // Groovy
        characters.groupBy { it.name }
        
     // Map
        // j7Group     
        map(characters, from(Person.class).getName(), to(Person.class).getSurname());
        // Java 8
        characters.stream().collect(Collectors.toMap(Person::getName, Person::getSurname));
        // Groovy
        characters.collectEntries{[it.name, it.surname]}
        
```
