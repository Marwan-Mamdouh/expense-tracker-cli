package com.marwan.dev.expense_tracker.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class providing static helper methods for Reading and writing to JSON files.
 * <p>
 * This class is final and cannot be instantiated. It provides two method to read and write.
 */
public final class ReadAndWriteUtil {

  /**
   * Private constructor to prevent instantiation.
   */
  private ReadAndWriteUtil() {
    // prevent instantiation
  }

  /**
   * Utility: Read the list of budgets from the JSON file.
   *
   * @return list of budgets
   */
  public static <T> List<T> readFromFile(String filePath, ObjectMapper mapper, Class<T> tClass) {
    try {
      final var file = new File(filePath);
      if (!file.exists()) {
        return new ArrayList<>();
      }
      final var collectionType = mapper.getTypeFactory()
          .constructCollectionType(ArrayList.class, tClass);
      return mapper.readValue(file, collectionType);
    } catch (IOException e) {
      throw new RuntimeException("Error reading from file", e);
    }
  }

  /**
   * Utility: Write the list of T to the JSON file.
   *
   * @param items list to write
   */
  public static <T> void writeToFile(String filePath, ObjectMapper mapper, List<T> items) {
    try {
      final var file = new File(filePath);
      file.getParentFile().mkdirs();
      mapper.writerWithDefaultPrettyPrinter().writeValue(file, items);
    } catch (IOException e) {
      throw new RuntimeException("Error writing to file: ", e);
    }
  }
}