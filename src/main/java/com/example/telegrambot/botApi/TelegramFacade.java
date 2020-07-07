package com.example.telegrambot.botApi;

import com.example.telegrambot.Service.BotService;
import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class TelegramFacade {

  final BotService botService;
  private static boolean addingReceipt = false;
  private int sum = 0, amount = 0, counter = 0, amountOthers = 0;
  Map<String, Integer> payers = new HashMap<>();
  List<String> rest = new ArrayList<>();
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
        case OTHERS -> getOthers(update);
        case INSERT_OTHERS -> insertOthers(update);
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
    sum = Integer.parseInt(update.getMessage().getText());
    step = Steps.PAYERS;
    return "Введите количество плательщиков.";
  }

  private String getPayers(Update update) {
    System.out.println(update.getMessage().getText());
    amount = Integer.parseInt(update.getMessage().getText());
    step = Steps.INSERT_PAYERS;
    return "Введите имена плательщиков и сумму через пробел.";
  }

  private String insertPayers(Update update) {
//    if (counter < amount - 1) {
//      String stringToParse = update.getMessage().getText();
//      String name = stringToParse.split(" ")[0];
//      int money = Integer.parseInt(stringToParse.split(" ")[1]);
//      payers.put(name, money);
//      System.out.println(name + " " + money);
//      counter++;
//      return "Слудующий плательщик";
//    }
    while (counter < amount) {
      System.out.println(counter + " " + amount);
      String stringToParse = update.getMessage().getText();
      String name = stringToParse.split(" ")[0];
      int money = Integer.parseInt(stringToParse.split(" ")[1]);
      payers.put(name, money);
      System.out.println(name + " " + money);
      counter++;
      if (counter == amount) {
        continue;
      }
      return "Слудующий плательщик";
    }
    counter = 0;
    step = Steps.OTHERS;
    return "Введите количество людей, кто не платил.";
  }

  private String getOthers(Update update) {
    amountOthers = Integer.parseInt(update.getMessage().getText());
    System.out.println(amountOthers);
    step = Steps.INSERT_OTHERS;
    return "Введите имена тех, кто не платил.";
  }

  private String insertOthers(Update update) {
    while (counter < amountOthers) {
      String name = update.getMessage().getText();
      rest.add(name);
      System.out.println(name);
      counter++;
      if (counter == amountOthers) {
        continue;
      }
      return "Слудующий";
    }
    counter = 0;
    Map<String, Double> buf = getResult();
    addingReceipt = false;
    return "Итог: \n" + toNewString(buf);
  }

  private Map<String, Double> getResult() {
    Map<String, Double> buf = new HashMap<>();
    int totalPeople = amount + amountOthers;
    double avg = (double) sum / totalPeople;
    for (String name :payers.keySet()) {
      if (payers.get(name) > avg) {
        buf.put(name + " долен(а) получить", Math.abs(payers.get(name) - avg));
      } else if (payers.get(name) == avg) {
        buf.put(name + " ничего ему не должны", 0d);
      } else if (payers.get(name) < avg) {
        buf.put(name + " должен(а) отдать", Math.abs(payers.get(name) - avg));
      }
    }
    for (String name : rest) {
      buf.put(name + " должен(а ) отдать", avg);
    }
    return buf;
  }

  private String toNewString(Map<String, Double> buf) {
    StringBuilder stringBuilder = new StringBuilder();
    for (String s : buf.keySet()) {
      stringBuilder.append(s + " " + buf.get(s)).append("\n");
    }
    return new String(stringBuilder);
  }

  private enum Steps {
    AMOUNT, PAYERS, INSERT_PAYERS, OTHERS, INSERT_OTHERS
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
}
