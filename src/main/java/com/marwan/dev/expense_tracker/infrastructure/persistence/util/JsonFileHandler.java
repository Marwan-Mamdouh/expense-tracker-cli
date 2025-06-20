package com.marwan.dev.expense_tracker.infrastructure.persistence.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * Utility class providing static helper methods for Reading and writing to JSON files.
 * <p>
 * This class is final and cannot be instantiated. It provides two method to read and write.
 */
@Component
public final class JsonFileHandler implements JsonFileHandlerI {

  private final ObjectMapper mapper;

  public JsonFileHandler(ObjectMapper mapper) {
    this.mapper = mapper;
  }

  /**
   * Utility: Read the list of budgets from the JSON file.
   *
   * @return list of budgets
   */
  @Override
  public <T> List<T> read(String filePath, Class<T> tClass) {
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
  @Override
  public <T> void write(String filePath, List<T> items) {
    try {
      final var file = new File(filePath);
      file.getParentFile().mkdirs();
      mapper.writerWithDefaultPrettyPrinter().writeValue(file, items);
    } catch (IOException e) {
      throw new RuntimeException("Error writing to file: ", e);
    }
  }
}