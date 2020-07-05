package com.example.telegrambot;

import com.example.telegrambot.botApi.TelegramFacade;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class TelegramBot extends TelegramWebhookBot {

  private String botUserName;
  private String botToken;
  private String botPath;

  private final TelegramFacade telegramFacade;

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
    return botUserName;
  }

  @Override
  public String getBotToken() {
    return botToken;
  }

  @Override
  public String getBotPath() {
    return botPath;
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
