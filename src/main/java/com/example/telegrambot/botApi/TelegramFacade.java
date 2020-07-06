package com.example.telegrambot.botApi;

import com.example.telegrambot.Service.BotService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class TelegramFacade {

  final BotService botService;
  private static boolean addingReceipt = false;
  private int amount = 0, counter = 0;
  Steps step;

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
    String result;
    if (!addingReceipt) {
      result = switch (update.getMessage().getText()) {
        case "/start" -> "Welcome";
        case "/registration" -> registry(update);
        case "/getallpersons" -> getAllPersons();
        case "/newreceipt" -> addNewReceipt();
        default -> update.getMessage().getText();
      };
    } else {
      result = switch (step) {
        case AMOUNT -> getAmount(update);
        case PAYERS -> getPayers(update);
        case INSERT_PAYERS -> insertPayers(update);
        case NEXT_STEP -> next(update);
      };
    }



    replyMessage.setText(result);
    return replyMessage;
  }

  private String addNewReceipt() {
    addingReceipt = true;
    step = Steps.AMOUNT;
    return "Начнем. Введите сумму.";
  }

  private String getAmount(Update update) {
    System.out.println(update.getMessage().getText());
    step = Steps.PAYERS;
    return "Введите количество плательщиков";
  }

  private String getPayers(Update update) {
    System.out.println(update.getMessage().getText());
    amount = Integer.parseInt(update.getMessage().getText());
    System.out.println(amount);
    step = Steps.INSERT_PAYERS;
    return "Введите имена плательщиков";
  }

  /**
   * Имя |_| Сумма
   * @param update
   * @return
   */
  private String insertPayers(Update update) {
    System.out.println(counter + " " + amount);
    if (counter < amount - 1) {
      System.out.println(update.getMessage().getText());
      ++counter;
      return "Next payer";
    } else {
      counter = 0;
      step = Steps.NEXT_STEP;
      addingReceipt = false;
    }
    return "Next step";
  }

  private String next(Update update) {
    System.out.println("QQQ");
    return "QQQ";
  }

  private String registry(Update update) {
    return botService.registry(update.getMessage().getFrom());
  }

  private String getAllPersons() {
    StringBuilder result = new StringBuilder();
    for (String string : botService.getAllPersons()) {
      result.append(string).append("\n");
    }
    return result.toString();
  }

  private enum Steps{
    AMOUNT, PAYERS, INSERT_PAYERS, NEXT_STEP
  }
}
