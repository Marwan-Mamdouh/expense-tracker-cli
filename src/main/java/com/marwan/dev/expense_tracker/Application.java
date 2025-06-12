package com.marwan.dev.expense_tracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.shell.command.annotation.CommandScan;

@CommandScan
@SpringBootApplication
public class Application {

  public static void main(String[] args) {
    final String[] arguments = noArgsHandler(args);
    SpringApplication.run(Application.class, arguments);
  }

  private static String[] noArgsHandler(String... args) {
    if (args.length == 0) {
      return new String[]{"help"};
    }
    return args;
  }
}