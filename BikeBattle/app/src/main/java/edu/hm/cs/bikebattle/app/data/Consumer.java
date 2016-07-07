package edu.hm.cs.bikebattle.app.data;

/**
 * Interface for consumer.
 * Created by Nils on 10.05.2016.
 * Interface for executing code after a specific tasks.
 * Consume should be called with the result on success.
 * Error should be called withe the reason on failure.
 *
 * @author Nils Bernhardt
 * @version 1.0
 */
public interface Consumer<T> {
  /**Failure code if an exception was thrown.*/
  public static final int EXCEPTION = -1;

  /**Method to execute on success.
   * @param input - result.
   */
  public void consume(T input);

  /**
   * Method to execute on failure.
   * @param error - code.
   * @param exception - thrown exception (may be null).
   */
  public void error(int error, Throwable exception);
}

