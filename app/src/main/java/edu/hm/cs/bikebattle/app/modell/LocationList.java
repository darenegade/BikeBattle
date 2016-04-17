package edu.hm.cs.bikebattle.app.modell;

import android.location.Location;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import lombok.NonNull;

/**
 * Created by Nils on 01.04.2016.
 * <p/>
 * Class storing a list of locations and the distance in between.
 *
 * @author Nils Bernhardt
 */
public class LocationList implements List<Location> {
  /**
   * distance of the the track.
   */
  private float distanceInM;
  /**
   * Locations of the track.
   */
  private final LinkedList<Location> data;

  /**
   * Initializes the list with the given locations and the distance.
   *
   * @param collection  locations
   * @param distanceInM distance
   */
  public LocationList(@NonNull List<? extends Location> collection, float distanceInM) {
    data = new LinkedList<Location>(collection);
    this.distanceInM = distanceInM;
  }

  /**
   * Initializes an empty list.
   */
  public LocationList() {
    distanceInM = 0;
    data = new LinkedList<Location>();
  }

  /**
   * Initializes the list and calculates the distance.
   *
   * @param collection locations
   */
  public LocationList(@NonNull List<? extends Location> collection) {
    data = new LinkedList<Location>(collection);
    distanceInM = calculateDistance(this);
  }

  /**
   * Returns the distance in meter of the route.
   *
   * @return distance
   */
  public float getDistanceInM() {
    return distanceInM;
  }

  /**
   * Calculates the distance between all locations in the list.
   *
   * @param locations list of locations
   * @return distance
   */
  public static float calculateDistance(@NonNull List<? extends Location> locations) {
    if (locations.size() < 2) {
      return 0;
    }
    float distance = 0;
    for (int index = 0; index < locations.size() - 1; index++) {
      distance += locations.get(index).distanceTo(locations.get(index + 1));
    }
    return distance;
  }

  @Override
  public void add(int index, @NonNull Location location) {
    //update distance
    if (size() < 1) {
      distanceInM = 0;
    } else if (index == 0) {
      //first element
      distanceInM += location.distanceTo(data.getFirst());
    } else if (index == size()) {
      //last element
      distanceInM += location.distanceTo(data.getLast());
    } else {
      //in the middle
      //subtract previous distance
      distanceInM -= get(index - 1).distanceTo(get(index));
      //add new distances
      distanceInM += location.distanceTo(get(index - 1)) + location.distanceTo(get(index));
    }
    data.add(index, location);
  }

  @Override
  public boolean add(@NonNull Location location) {
    add(this.size(), location);
    return true;
  }


  @Override
  public boolean addAll(int index, @NonNull Collection<? extends Location> collection) {
    if (collection.isEmpty()) {
      return false;
    }
    for (Location location : collection) {
      add(index, location);
      index++;
    }
    return true;
  }

  @Override
  public boolean addAll(@NonNull Collection<? extends Location> collection) {
    return addAll(size(), collection);
  }

  @Override
  public void clear() {
    data.clear();
    distanceInM = 0;
  }

  @Override
  public boolean contains(Object object) {
    return data.contains(object);
  }

  @Override
  public boolean containsAll(Collection<?> collection) {
    return data.containsAll(collection);
  }

  @Override
  public Location get(int location) {
    return data.get(location);
  }

  @Override
  public int indexOf(Object object) {
    return data.indexOf(object);
  }

  @Override
  public boolean isEmpty() {
    return data.isEmpty();
  }

  @Override
  public Iterator<Location> iterator() {
    return data.iterator();
  }

  @Override
  public int lastIndexOf(Object object) {
    return data.lastIndexOf(object);
  }

  @Override
  public ListIterator<Location> listIterator() {
    return data.listIterator();
  }

  @Override
  public ListIterator<Location> listIterator(int location) {
    return data.listIterator(location);
  }

  @Override
  public Location remove(int index) {
    //update distance

    if (size() <= 2) {
      distanceInM = 0;
    } else if (index == 0) {
      //first element
      distanceInM -= data.getFirst().distanceTo(get(1));
    } else if (index == size() - 1) {
      //last element
      distanceInM -= data.getLast().distanceTo(get(size() - 2));
    } else {
      //in the middle
      //subtract previous length
      distanceInM -= (get(index).distanceTo(get(index + 1))
          + get(index).distanceTo(get(index - 1)));
      //add new distance
      distanceInM += get(index - 1).distanceTo(get(index + 1));
    }
    return data.remove(index);
  }

  @Override
  public boolean remove(Object object) {
    int index = indexOf(object);
    if (index == -1) {
      return false;
    }
    remove(index);
    return true;
  }

  @Override
  public boolean removeAll(Collection<?> collection) {
    boolean changed = false;
    for (Object o : collection) {
      changed |= remove(o);
    }
    return changed;
  }

  @Override
  public boolean retainAll(Collection<?> collection) {
    Collection<Location> toRemove = new LinkedList<Location>();
    for (Location point : this) {
      if (!collection.contains(point)) {
        toRemove.add(point);
      }
    }
    return removeAll(toRemove);
  }

  @Override
  public Location set(int index, Location location) {
    add(index, location);
    return remove(index + 1);
  }

  @Override
  public int size() {
    return data.size();
  }

  @Override
  public List<Location> subList(int start, int end) {
    return data.subList(start, end);
  }

  @Override
  public Object[] toArray() {
    return data.toArray();
  }

  @Override
  public <T> T[] toArray(T[] array) {
    return null;
  }

}
