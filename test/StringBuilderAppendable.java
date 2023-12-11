package test;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * This function is used to create a string builder that is appendable.
 */

public class StringBuilderAppendable implements Appendable {

  private StringBuilder stringBuilder;
  private final BlockingQueue<CharSequence> buffer;

  /**
   * This class is designed to test the string builder for the appendable out.
   * 
   * @param bufferSize is the side of the string builder.
   */

  public StringBuilderAppendable(int bufferSize) {
    this.stringBuilder = new StringBuilder();
    this.buffer = new ArrayBlockingQueue<>(bufferSize);
  }

  @Override
  public Appendable append(CharSequence csq) throws IOException {
    stringBuilder.append(csq);
    return this;
  }

  @Override
  public Appendable append(CharSequence csq, int start, int end) throws IOException {
    stringBuilder.append(csq, start, end);
    return this;
  }

  @Override
  public Appendable append(char c) throws IOException {
    stringBuilder.append(c);
    return this;
  }

  @Override
  public String toString() {
    return stringBuilder.toString();
  }

  /**
   * Constructor takes a readable in and appendable out to read the data for the game.
   * 
   * @param target takes in the data to place characters in the board
   */

  public void drainTo(StringBuilder target) {
    while (!buffer.isEmpty()) {
      try {
        CharSequence csq = buffer.take();
        target.append(csq);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    }
  }

  /**
   * This method flushes the string builder.
   */
  public void flush() {
    drainTo(stringBuilder);
  }

}
