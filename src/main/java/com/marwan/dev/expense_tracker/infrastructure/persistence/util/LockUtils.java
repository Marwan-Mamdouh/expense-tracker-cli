package com.marwan.dev.expense_tracker.infrastructure.persistence.util;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.function.Supplier;

/**
 * Utility class providing static helper methods for executing code blocks within read or write
 * locks using {@link ReadWriteLock}.
 * <p>
 * This class is final and cannot be instantiated. It provides multiple overloaded versions of read
 * and write locking methods to accommodate operations returning results or not.
 */
public final class LockUtils {

  /**
   * Private constructor to prevent instantiation.
   */
  private LockUtils() {
    // prevent instantiation
  }

  /**
   * Executes a read operation within a thread-safe read lock.
   *
   * @param lock the {@link ReadWriteLock} to be used
   * @param work the supplier representing the read operation
   * @param <R>  the result type of the operation
   * @return the result of the read operation
   */
  public static <R> R withReadLock(ReadWriteLock lock, Supplier<R> work) {
    lock.readLock().lock();
    try {
      return work.get();
    } finally {
      lock.readLock().unlock();
    }
  }

  /**
   * Executes a write operation within a thread-safe write lock.
   *
   * @param lock the {@link ReadWriteLock} to be used
   * @param work the runnable representing the write operation
   */
  public static void withWriteLock(ReadWriteLock lock, Runnable work) {
    lock.writeLock().lock();
    try {
      work.run();
    } finally {
      lock.writeLock().unlock();
    }
  }

  /**
   * Executes a write operation that returns a result within a thread-safe write lock.
   *
   * @param lock the {@link ReadWriteLock} to be used
   * @param work the supplier representing the write operation
   * @param <R>  the result type of the operation
   * @return the result of the write operation
   */
  public static <R> R withWriteLock(ReadWriteLock lock, Supplier<R> work) {
    lock.writeLock().lock();
    try {
      return work.get();
    } finally {
      lock.writeLock().unlock();
    }
  }
}