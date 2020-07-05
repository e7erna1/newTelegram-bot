package com.example.telegrambot.botConfig;

import com.example.telegrambot.TelegramBot;
import com.example.telegrambot.botApi.TelegramFacade;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.ApiContext;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "telegram")
public class BotConfig {

  private String botUserName;
  private String webHookPath;
  private String botToken;

  private DefaultBotOptions.ProxyType proxyType;
  private String proxyHost;
  private int proxyPort;

  @Bean
  public TelegramBot telegramBot(TelegramFacade telegramFacade) {
    DefaultBotOptions options = ApiContext.getInstance(DefaultBotOptions.class);

    options.setProxyHost(proxyHost);
    options.setProxyPort(proxyPort);
    options.setProxyType(proxyType);

    TelegramBot telegramBot = new TelegramBot(options, telegramFacade);
    telegramBot.setBotUserName(botUserName);
    telegramBot.setBotToken(botToken);
    telegramBot.setWebHookPath(webHookPath);

    return telegramBot;
  }

  @Bean
  public MessageSource messageSource() {
    ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
    messageSource.setBasename("classpath:messages");
    messageSource.setDefaultEncoding("UTF-8");
    return messageSource;
  }

}
