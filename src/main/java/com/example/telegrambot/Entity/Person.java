package com.example.telegrambot.Entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Person {

  long id;
  String name;
  String surName;
  String userName;

  @Override
  public String toString() {
    return "Имя: " + this.name + "\n"+
        "Фамилия: " + this.surName;
  }
}
