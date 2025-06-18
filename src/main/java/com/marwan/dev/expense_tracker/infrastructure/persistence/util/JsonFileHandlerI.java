package com.marwan.dev.expense_tracker.infrastructure.persistence.util;

import java.util.List;

public interface JsonFileHandlerI {

  <T> List<T> read(String filePath, Class<T> clazz);

  <T> void write(String filePath, List<T> data);
}