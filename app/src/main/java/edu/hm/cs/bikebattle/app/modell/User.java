package edu.hm.cs.bikebattle.app.modell;

/**
 * Created by Nils Bernhardt on 30.03.2016.
 * This class represents a user and stores his values as well as his routes and tracks.
 *
 * @author Nils Bernhardt
 * @version 1.0
 */
public class User {

  /**
   * Name of the user.
   */
  private String name;
  /**
   * Weight of the user.
   */
  private float weightInKg;
  /**
   * Size of the user.
   */
  private int sizeInCm;

  /**
   * Initializes the user.
   *
   * @param name       name of the user
   * @param weightInKg weight of the user in kg
   * @param sizeInCm   size of the user in cm
   */
  public User(final String name, final float weightInKg, final int sizeInCm) {
    setName(name);
    setWeightInKg(weightInKg);
    setSizeInCm(sizeInCm);
  }

  /**
   * Returns the size of the user.
   *
   * @return size
   */
  public int getSizeInCm() {
    return sizeInCm;
  }

  /**
   * Returns the name of the user.
   *
   * @return name
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the weight of the user.
   *
   * @return weight
   */
  public float getWeightInKg() {
    return weightInKg;
  }

  /**
   * Changes the name of the user.
   *
   * @param name new name
   */
  public void setName(String name) {
    if (name == null) {
      throw new NullPointerException("Name can not be null.");
    }
    if (name.length() <= 0) {
      throw new IllegalArgumentException("Name must contain at least one character.");
    }
    this.name = name;
  }

  /**
   * Changes the weight of the user.
   *
   * @param weightInKg new weight in kg.
   */
  public void setWeightInKg(float weightInKg) {
    this.weightInKg = weightInKg;
  }

  /**
   * Changes the size of the user.
   *
   * @param sizeInCm new size in cm
   */
  public void setSizeInCm(int sizeInCm) {
    this.sizeInCm = sizeInCm;
  }
}
