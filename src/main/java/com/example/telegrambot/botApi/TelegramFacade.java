package com.example.telegrambot.botApi;

import com.example.telegrambot.Service.BotService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class TelegramFacade {

  final BotService botService;

  public TelegramFacade(BotService botService) {
    this.botService = botService;
  }

  public SendMessage handleUpdate(Update update) {
    SendMessage replyMessage = null;
    Message message = update.getMessage();
    if (message != null && message.hasText()) {
      replyMessage = handleInputMessage(update);
    }
    return replyMessage;
  }

  private SendMessage handleInputMessage(Update update) {
    SendMessage replyMessage = new SendMessage();
    replyMessage.setChatId(update.getMessage().getChatId());
    String result = switch (update.getMessage().getText()) {
      case "/start" -> "Welcome";
      case "/registry" -> registry(update);
      case "/getAllPersons" -> getAllPersons();
      default -> update.getMessage().getText();
    };
    replyMessage.setText(result);
    return replyMessage;
  }

  private String registry(Update update) {
    return botService.registry(update.getMessage().getFrom());
  }

  private String getAllPersons() {
    return botService.getAllPersons();
  }
}
