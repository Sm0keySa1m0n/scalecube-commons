package io.scalecube.runners;

import sun.misc.Signal;
import sun.misc.SignalHandler;

public final class Runners {

  private Runners() {
    // Do not instantiate
  }

  /**
   * Listens to jvm signas SIGTERM and SIGINT and applies shutdown lambda function.
   *
   * @param runnable shutdown task
   */
  public static void onShutdown(Runnable runnable) {
    SignalHandler handler = signal -> runnable.run();
    Signal.handle(new Signal("INT"), handler);
    Signal.handle(new Signal("TERM"), handler);
  }

  /**
   * Utility method to simply check if current thread is interrupted. If it is, then {@code
   * IllegalStateException} is being thrown.
   */
  public static void checkInterrupted() {
    if (Thread.currentThread().isInterrupted()) {
      throw new IllegalStateException("Unexpected interrupt");
    }
  }
}
