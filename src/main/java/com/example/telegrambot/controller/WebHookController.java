package com.example.telegrambot.controller;

import com.example.telegrambot.TelegramBot;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@RestController
public class WebHookController {

  private final TelegramBot telegramBot;

  public WebHookController(TelegramBot telegramBot) {
    this.telegramBot = telegramBot;
  }

  @RequestMapping(value = "/", method = RequestMethod.POST)
  public BotApiMethod<?> onUpdateReceived(@RequestBody Update update) {
    return telegramBot.onWebhookUpdateReceived(update);
  }
}
