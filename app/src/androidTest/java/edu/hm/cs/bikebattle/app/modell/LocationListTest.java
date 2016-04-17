package edu.hm.cs.bikebattle.app.modell;

import android.location.Location;
import junit.framework.TestCase;

/**
 * Created by Nils on 05.04.2016.
 */
public class LocationListTest extends TestCase {

  public void testCalculateDistance() throws Exception {
    LocationList list = new LocationList();
    Location add, last = null;
    float distance = 0;
    for (int i = 0; i < 5; i++) {
      add = new TestLocation();
      add.setLongitude(100*Math.random());
      add.setLatitude(100*Math.random());
      if (last != null)
        distance += last.distanceTo(add);
      list.add(add);
      last = add;
      assertEquals(distance, LocationList.calculateDistance(list));
    }

    list.clear();
    assertEquals(0.0f, LocationList.calculateDistance(list));
    list.add(new TestLocation());
    assertEquals(0.0f, LocationList.calculateDistance(list));
  }

  public void testAdd() throws Exception {
    LocationList list = new LocationList();
    Location add, last = null;
    //no element
    add = new TestLocation();
    add.setLatitude(10);
    add.setLongitude(15);
    list.add(add);
    assertEquals(1, list.size());
    assertEquals(0, list.indexOf(add));
    assertEquals(LocationList.calculateDistance(list), list.getDistanceInM());

    //one element
    add = new TestLocation();
    add.setLatitude(10);
    add.setLongitude(20);
    list.add(add);
    assertEquals(2, list.size());
    assertEquals(1, list.indexOf(add));
    assertEquals(LocationList.calculateDistance(list), list.getDistanceInM());


    //n elements
    add = new TestLocation();
    add.setLatitude(10);
    add.setLongitude(25);
    list.add(add);
    assertEquals(3, list.size());
    assertEquals(2, list.indexOf(add));
    assertEquals(LocationList.calculateDistance(list), list.getDistanceInM());
  }

  public void testAdd1() throws Exception {
    LocationList list = new LocationList();
    Location add, last = null;
    //no element (end)
    add = new TestLocation();
    add.setLatitude(10);
    add.setLongitude(15);
    list.add(0, add);
    assertEquals(1, list.size());
    assertEquals(0, list.indexOf(add));
    assertEquals(LocationList.calculateDistance(list), list.getDistanceInM());

    //one element (begin)
    add = new TestLocation();
    add.setLatitude(10);
    add.setLongitude(20);
    list.add(0,add);
    assertEquals(2, list.size());
    assertEquals(0, list.indexOf(add));
    assertEquals(LocationList.calculateDistance(list), list.getDistanceInM());


    //n elements (middle)
    add = new TestLocation();
    add.setLatitude(10);
    add.setLongitude(25);
    list.add(1, add);
    assertEquals(3, list.size());
    assertEquals(1, list.indexOf(add));
    assertEquals(LocationList.calculateDistance(list), list.getDistanceInM());
  }

  public void testAddAll() throws Exception {
    LocationList list = new LocationList(), list2;
    Location add, last = null;

    add = new TestLocation();
    add.setLatitude(10);
    add.setLongitude(15);
    list.add(0, add);

    add = new TestLocation();
    add.setLatitude(10);
    add.setLongitude(20);
    list.add(0, add);

    add = new TestLocation();
    add.setLatitude(10);
    add.setLongitude(25);
    list.add(1, add);

    //empty
    list2 = new LocationList(list);
    assertEquals(LocationList.calculateDistance(list2), list2.getDistanceInM());
    list2.addAll(list);
    assertEquals(LocationList.calculateDistance(list2), list2.getDistanceInM());
  }

  public void testAddAll1() throws Exception {
    LocationList list = new LocationList(), list2;
    Location add, last = null;

    add = new TestLocation();
    add.setLatitude(10);
    add.setLongitude(15);
    list.add(0, add);

    add = new TestLocation();
    add.setLatitude(10);
    add.setLongitude(20);
    list.add(0, add);

    add = new TestLocation();
    add.setLatitude(10);
    add.setLongitude(25);
    list.add(1, add);

    //empty
    list2 = new LocationList(list);
    assertEquals(LocationList.calculateDistance(list2), list2.getDistanceInM());
    list2.addAll(0, list);
    assertEquals(LocationList.calculateDistance(list2), list2.getDistanceInM());
    list2.addAll(0, list);
    assertEquals(LocationList.calculateDistance(list2), list2.getDistanceInM());
    list2.addAll(5, list);
    assertEquals(LocationList.calculateDistance(list2), list2.getDistanceInM());
  }

  public void testClear() throws Exception {
    LocationList list = new LocationList();
    Location add, last = null;

    add = new TestLocation();
    add.setLatitude(10);
    add.setLongitude(15);
    list.add(0, add);

    add = new TestLocation();
    add.setLatitude(10);
    add.setLongitude(20);
    list.add(0, add);

    add = new TestLocation();
    add.setLatitude(10);
    add.setLongitude(25);
    list.add(1, add);

    list.clear();
    assertEquals(0.0f, list.getDistanceInM());
  }

  public void testRemove() throws Exception {
    LocationList list = new LocationList(), list2;
    Location add, last = null;

    add = new TestLocation();
    add.setLatitude(10);
    add.setLongitude(15);
    list.add(0, add);

    add = new TestLocation();
    add.setLatitude(10);
    add.setLongitude(20);
    list.add(0, add);

    add = new TestLocation();
    add.setLatitude(10);
    add.setLongitude(25);
    list.add(1, add);

    list.remove(add);
    assertEquals(list.getDistanceInM(), LocationList.calculateDistance(list));
  }

  public void testRemove1() throws Exception {
    LocationList list = new LocationList(), list2;
    Location add, last = null;

    add = new TestLocation();
    add.setLatitude(10);
    add.setLongitude(15);
    list.add(0, add);

    add = new TestLocation();
    add.setLatitude(10);
    add.setLongitude(20);
    list.add(0, add);

    add = new TestLocation();
    add.setLatitude(10);
    add.setLongitude(25);
    list.add(1, add);

    add = new TestLocation();
    add.setLatitude(10);
    add.setLongitude(40);
    list.add(1, add);

    //middle
    list.remove(2);
    assertEquals(LocationList.calculateDistance(list), list.getDistanceInM());
    //beginning
    list.remove(0);
    assertEquals(LocationList.calculateDistance(list), list.getDistanceInM());
    //end
    list.remove(list.size()-1);
    assertEquals(LocationList.calculateDistance(list), list.getDistanceInM());
  }

  public void testRemoveAll() throws Exception {
    LocationList list = new LocationList(), list2;
    Location add, last = null;

    add = new TestLocation();
    add.setLatitude(10);
    add.setLongitude(15);
    list.add(0, add);

    add = new TestLocation();
    add.setLatitude(10);
    add.setLongitude(20);
    list.add(0, add);

    add = new TestLocation();
    add.setLatitude(10);
    add.setLongitude(25);
    list.add(1, add);

    //empty
    list2 = new LocationList(list);
    add = new TestLocation();
    add.setLatitude(10);
    add.setLongitude(40);
    list2.add(0, add);
    add = new TestLocation();
    add.setLatitude(10);
    add.setLongitude(25);
    list2.add(add);
    list2.removeAll(list);
    assertEquals(LocationList.calculateDistance(list2), list2.getDistanceInM());
    assertEquals(2, list2.size());
  }

  public void testRetainAll() throws Exception {
    LocationList list = new LocationList(), list2;
    Location add, last = null;

    add = new TestLocation();
    add.setLatitude(10);
    add.setLongitude(15);
    list.add(0, add);

    add = new TestLocation();
    add.setLatitude(10);
    add.setLongitude(20);
    list.add(0, add);

    add = new TestLocation();
    add.setLatitude(10);
    add.setLongitude(25);
    list.add(1, add);

    //empty
    list2 = new LocationList(list);
    add = new TestLocation();
    add.setLatitude(10);
    add.setLongitude(40);
    list2.add(0, add);
    add = new TestLocation();
    add.setLatitude(10);
    add.setLongitude(25);
    list2.add(add);
    list2.retainAll(list);
    assertEquals(LocationList.calculateDistance(list2), list2.getDistanceInM());
    assertEquals(3, list2.size());
  }

  public void testSet() throws Exception {
    LocationList list = new LocationList();
    Location add, last = null;

    add = new TestLocation();
    add.setLatitude(10);
    add.setLongitude(15);
    list.add(0, add);

    add = new TestLocation();
    add.setLatitude(10);
    add.setLongitude(20);
    list.add(0, add);

    add = new TestLocation();
    add.setLatitude(10);
    add.setLongitude(25);
    list.add(1, add);

    add = new TestLocation();
    add.setLatitude(10);
    add.setLongitude(25);
    list.set(1, add);


    assertEquals(add, list.get(1));
    assertEquals(LocationList.calculateDistance(list), list.getDistanceInM());
    assertEquals(3, list.size());
  }

  private class TestLocation extends Location{

    public Creator CREATOR = null;

    private double longitude = 0, latitude = 0;

    public TestLocation(){
      super("");
    }
    @Override
    public void setLatitude(double latitude) {
      this.latitude = latitude;
    }

    @Override
    public double getLongitude() {
      return longitude;
    }

    @Override
    public void setLongitude(double longitude) {
      this.longitude = longitude;
    }

    @Override
    public double getLatitude() {
      return latitude;
    }

    @Override
    public float distanceTo(Location dest) {
      return (float) Math.hypot(latitude-dest.getLatitude(), longitude-dest.getLongitude());
    }
  }
}