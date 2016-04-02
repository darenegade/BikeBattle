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
    private float distance_in_m;

    public WaypointList(float distance_in_m) {
        this.distance_in_m = distance_in_m;
    }

    public WaypointList(Collection<? extends Waypoint> collection, float distance_in_m) {
        super(collection);
        this.distance_in_m = distance_in_m;
    }

    public WaypointList() {
       this(0);
    }

    public WaypointList(Collection<? extends Waypoint> collection) {
        super(collection);
        distance_in_m = calculateDistance(this);
    }

    public float getDistance_in_m(){
        return distance_in_m;
    }

    /**
     * Calculates the distance between all waypoints in the list.
     *
     * @param waypoints list of waypoints
     * @return distance
     */
    public static float calculateDistance(List<? extends Waypoint> waypoints) {
        if (waypoints == null) throw new NullPointerException("Waypoints can not be null!");
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
    public void add(int i, Waypoint waypoint) {
        //update distance
        if (size() < 1)
            distance_in_m = 0;
        else if (i == 0) {
            //first element
            distance_in_m += waypoint.distanceTo(getFirst());
        } else if (i == size()) {
            //last element
            distance_in_m += waypoint.distanceTo(getLast());
        } else {
            //in the middle
            //subtract previous distance
            distance_in_m -= get(i - 1).distanceTo(get(i));
            //add new distances
            distance_in_m += waypoint.distanceTo(get(i - 1)) + waypoint.distanceTo(get(i));
        }
        super.add(i, waypoint);
    }

    @Override
    public boolean add(Waypoint waypoint) {
        add(this.size(), waypoint);
        return true;
    }

    @Override
    public boolean addAll(int i, Collection<? extends Waypoint> collection) {
        if (collection.isEmpty())
            return false;
        Iterator<? extends Waypoint> iterator = collection.iterator();
        while (iterator.hasNext()) {
            add(i, iterator.next());
            i++;
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
        distance_in_m = 0;
    }

    @Override
    public Waypoint remove(int i) {
        //update distance

        if (size() <= 2)
            distance_in_m = 0;
        if (i == 0) {
            //first element
            distance_in_m -= getFirst().distanceTo(get(1));
        } else if (i == size() - 1) {
            //last element
            distance_in_m -= getLast().distanceTo(get(size() - 2));
        } else {
            //in the middle
            //subtract previous length
            distance_in_m -= (get(i).distanceTo(get(i + 1)) + get(i).distanceTo(get(i - 1)));
            //add new distance
            distance_in_m += get(i-1).distanceTo(get(i+1));
        }
        return super.remove(i);
    }

    @Override
    public boolean remove(Object o) {
        int index = indexOf(o);
        if (index == -1)
            return false;
        remove(index);
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        boolean changed = false;
        for (Object o : collection)
            changed |= remove(o);
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        Collection<Waypoint> toRemove = new LinkedList<Waypoint>();
        for (Waypoint point : this)
            if (!collection.contains(point))
                toRemove.add(point);
        return removeAll(toRemove);
    }

    @Override
    public Waypoint set(int i, Waypoint waypoint) {
        add(i, waypoint);
        return remove(i + 1);
    }
}
