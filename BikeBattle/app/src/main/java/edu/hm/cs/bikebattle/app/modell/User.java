package edu.hm.cs.bikebattle.app.modell;

/**
 * Created by Nils Bernhardt on 30.03.2016.
 * This class represents a user and stores his values as well as his routes and tracks.
 *
 * @author Nils Bernhardt
 * @version 1.0
 */
public class User extends BaseEntity {

  /**
   * Name of the user.
   */
  private String name;
  /**
   * Foto of user;
   */
  private String fotoUri;
  /**
   * Weight of the user.
   */
  private float weightInKg;
  /**
   * Size of the user.
   */
  private float sizeInMeter;


  /**
   * Email of the user.
   */
  private String email;

  /**
   * Initializes the user.
   *
   * @param name        name of the user
   * @param weightInKg  weight of the user in kg
   * @param sizeInMeter size of the user in cm
   */
  public User(final String name, String email, final float weightInKg, final float sizeInMeter) {
    setName(name);
    this.email = email;
    setWeightInKg(weightInKg);
    setSizeInMeter(sizeInMeter);
  }

  /**
   * Returns the size of the user.
   *
   * @return size
   */
  public float getSizeInMeter() {
    return sizeInMeter;
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
   * Returns the foto of the user.
   *
   * @return foto
   */
  public String getFotoUri() {
    return fotoUri;
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
   * Cahnges the foto of the user.
   *
   * @param fotoUri uri of foto
   */
  public void setFotoUri(String fotoUri) {
    this.fotoUri = fotoUri;
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
   * @param sizeInMeter new size in cm
   */
  public void setSizeInMeter(float sizeInMeter) {
    this.sizeInMeter = sizeInMeter;
  }

  /**
   * Returns the email.
   *
   * @return email
   */
  public String getEmail() {
    return email;
  }

  /**
   * Sets a new email.
   *
   * @param email email
   */
  public void setEmail(String email) {
    this.email = email;
  }
}
