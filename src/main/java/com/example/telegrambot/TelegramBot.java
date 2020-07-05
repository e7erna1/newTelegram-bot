package com.example.telegrambot;

import com.example.telegrambot.botApi.TelegramFacade;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class TelegramBot extends TelegramWebhookBot {

  String botUserName;
  String botToken;
  String botPath;

  final TelegramFacade telegramFacade;

  public TelegramBot(DefaultBotOptions options, TelegramFacade telegramFacade) {
    super(options);
    this.telegramFacade = telegramFacade;
  }

  @Override
  public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
    return telegramFacade.handleUpdate(update);
  }

  @Override
  public String getBotUsername() {
    return this.botUserName;
  }

  @Override
  public String getBotToken() {
    return this.botToken;
  }

  @Override
  public String getBotPath() {
    return this.botPath;
  }

  public void setBotUserName(String botUserName) {
    this.botUserName = botUserName;
  }

  public void setBotToken(String botToken) {
    this.botToken = botToken;
  }

  public void setBotPath(String botPath) {
    this.botPath = botPath;
  }
}
