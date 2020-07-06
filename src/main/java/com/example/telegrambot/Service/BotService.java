package com.example.telegrambot.Service;

import com.example.telegrambot.Entity.Person;
import com.example.telegrambot.cache.PersonData;
import com.example.telegrambot.cache.Status.SaveStatus;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;

@Service
public class BotService {

  final PersonData personData;

  public BotService(PersonData personData) {
    this.personData = personData;
  }

  public String registry(User from) {
    Person person = new Person();
    person.setId(from.getId());
    person.setName(from.getFirstName());
    person.setSurName(from.getLastName());
    person.setUserName(from.getUserName());
    if (personData.savePerson(person) == SaveStatus.SAVED) {
      return person.getName() + " успешно зарегистрирован(а).";
    } else {
      return person.getName() + " уже зарегистрирован(а).";
    }
  }

  public List<String> getAllPersons() {
    List<String> list = new ArrayList<>();
    if (personData.getAllPersons() == null || personData.getAllPersons().size() == 0) {
      return Collections.singletonList("Список Зарегистрированных пользователей пуст.");
    } else {
      for (Person person : personData.getAllPersons()) {
        list.add(person.toString());
      }
    }
    return list;
  }
}
