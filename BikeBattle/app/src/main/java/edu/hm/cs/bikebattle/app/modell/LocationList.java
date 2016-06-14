package edu.hm.cs.bikebattle.app.modell;

import android.location.Location;

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
public class LocationList extends BaseEntity implements List<Location> {
  /**
   * distance of the the track.
   */
  private float distanceInM;
  /**
   * distance upward traveled in meter.
   */
  private float upwardInM;
  /**
   * distance downward traveled in meter.
   */
  private float downwardInM;
  /**
   * Locations of the track.
   */
  private final LinkedList<Location> data;

  /**
   * Initializes an empty list.
   */
  public LocationList() {
    distanceInM = 0;
    upwardInM = 0;
    downwardInM = 0;
    data = new LinkedList<Location>();
  }

  /**
   * Initializes the list and calculates the distance.
   *
   * @param collection locations
   */
  public LocationList(@NonNull List<? extends Location> collection) {
    data = new LinkedList<Location>(collection);
    calculateDistances();
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
   * Calculates the distance, distance upward and distance downward between all locations in the
   * list.
   */
  private void calculateDistances() {
    if (size() < 2) {
      distanceInM = 0;
      downwardInM = 0;
      upwardInM = 0;
    }
    for (int index = 0; index < size() - 1; index++) {
      distanceInM += get(index).distanceTo(get(index + 1));
      double heightDif = get(index).getAltitude() - get(index + 1).getAltitude();
      if (heightDif > 0) {
        downwardInM += heightDif;
      } else {
        upwardInM -= heightDif;
      }
    }
  }


  @Override
  public void add(int index, @NonNull Location location) {
    //update distance
    if (size() < 1) {
      distanceInM = 0;
      downwardInM = 0;
      upwardInM = 0;
    } else if (index == 0) {
      //first element
      distanceInM += location.distanceTo(data.getFirst());
      double heightDif = location.getAltitude() - data.getFirst().getAltitude();
      if (heightDif > 0) {
        downwardInM += heightDif;
      } else {
        upwardInM -= heightDif;
      }
    } else if (index == size()) {
      //last element
      distanceInM += location.distanceTo(data.getLast());
      double heightDif = data.getLast().getAltitude() - location.getAltitude();
      if (heightDif > 0) {
        downwardInM += heightDif;
      } else {
        upwardInM -= heightDif;
      }
    } else {
      //in the middle
      //subtract previous distance
      distanceInM -= get(index - 1).distanceTo(get(index));

      double heightDif = get(index - 1).getAltitude() - get(index).getAltitude();
      if (heightDif > 0) {
        downwardInM -= heightDif;
      } else {
        upwardInM += heightDif;
      }

      //add new distances
      distanceInM += location.distanceTo(get(index - 1)) + location.distanceTo(get(index));

      heightDif = get(index - 1).getAltitude() - location.getAltitude();
      if (heightDif > 0) {
        downwardInM += heightDif;
      } else {
        upwardInM -= heightDif;
      }

      heightDif = location.getAltitude() - get(index).getAltitude();
      if (heightDif > 0) {
        downwardInM += heightDif;
      } else {
        upwardInM -= heightDif;
      }
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
      downwardInM = 0;
      upwardInM = 0;
    } else if (index == 0) {
      //first element
      distanceInM -= data.getFirst().distanceTo(get(1));

      double heightDif = data.getFirst().getAltitude() - get(1).getAltitude();
      if (heightDif > 0) {
        downwardInM -= heightDif;
      } else {
        upwardInM += heightDif;
      }
    } else if (index == size() - 1) {
      //last element
      distanceInM -= data.getLast().distanceTo(get(size() - 2));

      double heightDif = get(size() - 2).getAltitude() - data.getLast().getAltitude();
      if (heightDif > 0) {
        downwardInM -= heightDif;
      } else {
        upwardInM += heightDif;
      }
    } else {
      //in the middle
      //subtract previous length
      distanceInM -= (get(index).distanceTo(get(index + 1))
          + get(index).distanceTo(get(index - 1)));

      double heightDif = get(index - 1).getAltitude() - get(index).getAltitude();
      if (heightDif > 0) {
        downwardInM -= heightDif;
      } else {
        upwardInM += heightDif;
      }

      heightDif = get(index).getAltitude() - get(index + 1).getAltitude();
      if (heightDif > 0) {
        downwardInM -= heightDif;
      } else {
        upwardInM += heightDif;
      }

      //add new distance
      distanceInM += get(index - 1).distanceTo(get(index + 1));

      heightDif = get(index - 1).getAltitude() - get(index + 1).getAltitude();
      if (heightDif > 0) {
        downwardInM += heightDif;
      } else {
        upwardInM -= heightDif;
      }
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

  /**
   * Returns the distance upward in meter.
   *
   * @return distance upward
   */
  public float getUpwardInM() {
    return upwardInM;
  }

  /**
   * Returns the distance downward in meter.
   *
   * @return distance downward
   */
  public float getDownwardInM() {
    return downwardInM;
  }

}
