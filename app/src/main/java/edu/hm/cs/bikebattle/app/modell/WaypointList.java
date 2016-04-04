package edu.hm.cs.bikebattle.app.modell;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Nils on 01.04.2016.
 */
public class WaypointList extends LinkedList<Waypoint> {
  /**
   * distance of the the track.
   */
  private float distanceInM;

  public WaypointList(float distanceInM) {
    this.distanceInM = distanceInM;
  }

  public WaypointList(Collection<? extends Waypoint> collection, float distanceInM) {
    super(collection);
    this.distanceInM = distanceInM;
  }

  public WaypointList() {
    this(0);
  }

  public WaypointList(Collection<? extends Waypoint> collection) {
    super(collection);
    distanceInM = calculateDistance(this);
  }

  public float getDistanceInM() {
    return distanceInM;
  }

  /**
   * Calculates the distance between all waypoints in the list.
   *
   * @param waypoints list of waypoints
   * @return distance
   */
  public static float calculateDistance(List<? extends Waypoint> waypoints) {
    if (waypoints == null) {
      throw new NullPointerException("Waypoints can not be null!");
    }
    if (waypoints.size() < 2) {
      return 0;
    }
    float distance = 0;
    for (int index = 0; index < waypoints.size() - 1; index++) {
      distance += waypoints.get(index).distanceTo(waypoints.get(index + 1));
    }
    return distance;
  }

  @Override
  public void add(int index, Waypoint waypoint) {
    //update distance
    if (size() < 1) {
      distanceInM = 0;
    } else if (index == 0) {
      //first element
      distanceInM += waypoint.distanceTo(getFirst());
    } else if (index == size()) {
      //last element
      distanceInM += waypoint.distanceTo(getLast());
    } else {
      //in the middle
      //subtract previous distance
      distanceInM -= get(index - 1).distanceTo(get(index));
      //add new distances
      distanceInM += waypoint.distanceTo(get(index - 1)) + waypoint.distanceTo(get(index));
    }
    super.add(index, waypoint);
  }

  @Override
  public boolean add(Waypoint waypoint) {
    add(this.size(), waypoint);
    return true;
  }

  @Override
  public boolean addAll(int index, Collection<? extends Waypoint> collection) {
    if (collection.isEmpty()) {
      return false;
    }
    Iterator<? extends Waypoint> iterator = collection.iterator();
    while (iterator.hasNext()) {
      add(index, iterator.next());
      index++;
    }
    return true;
  }

  @Override
  public boolean addAll(Collection<? extends Waypoint> collection) {
    return addAll(size(), collection);
  }

  @Override
  public void clear() {
    super.clear();
    distanceInM = 0;
  }

  @Override
  public Waypoint remove(int index) {
    //update distance

    if (size() <= 2) {
      distanceInM = 0;
    } else if (index == 0) {
      //first element
      distanceInM -= getFirst().distanceTo(get(1));
    } else if (index == size() - 1) {
      //last element
      distanceInM -= getLast().distanceTo(get(size() - 2));
    } else {
      //in the middle
      //subtract previous length
      distanceInM -= (get(index).distanceTo(get(index + 1))
          + get(index).distanceTo(get(index - 1)));
      //add new distance
      distanceInM += get(index - 1).distanceTo(get(index + 1));
    }
    return super.remove(index);
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
    Collection<Waypoint> toRemove = new LinkedList<Waypoint>();
    for (Waypoint point : this) {
      if (!collection.contains(point)) {
        toRemove.add(point);
      }
    }
    return removeAll(toRemove);
  }

  @Override
  public Waypoint set(int index, Waypoint waypoint) {
    add(index, waypoint);
    return remove(index + 1);
  }
}
