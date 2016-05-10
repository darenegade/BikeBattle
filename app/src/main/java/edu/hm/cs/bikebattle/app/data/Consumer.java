package edu.hm.cs.bikebattle.app.data;

/**
 * Created by Nils on 10.05.2016.
 */
public interface Consumer<T> {

  public void consume(T input);

  public void error(int error);
}

