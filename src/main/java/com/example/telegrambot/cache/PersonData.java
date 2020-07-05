package com.example.telegrambot.cache;

import com.example.telegrambot.Entity.Person;
import com.example.telegrambot.cache.Status.SaveStatus;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;


@Data
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PersonData {

  Set<Person> people = new HashSet<>();

  public SaveStatus savePerson(Person person) {
    if (people.contains(person)) {
      return SaveStatus.ALREADY_EXIST;
    } else {
      people.add(person);
      return SaveStatus.SAVED;
    }
  }

  public List<Person> getAllPersons() {
    return new ArrayList<>(people);
  }
}
