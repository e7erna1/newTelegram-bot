package com.example.telegrambot.Service;

import com.example.telegrambot.Entity.Person;
import com.example.telegrambot.Status.SaveStatus;
import com.example.telegrambot.cache.PersonData;
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


}
