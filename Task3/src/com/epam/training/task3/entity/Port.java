package com.epam.training.task3.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class Port {
  private static final Logger LOGGER = LogManager.getRootLogger();

  private List<Berth> berthList;
  private Warehouse portWarehouse;
  private Map<Ship, Berth> usedBerths;
  private static Lock lock = new ReentrantLock();
  private static Port instance;

  private Port(int berthCount, int warehouseCapacity) {
    portWarehouse = new Warehouse(warehouseCapacity);
    berthList = new ArrayList<Berth>(berthCount);
    for (int i = 0; i < berthCount; i++) {
      berthList.add(new Berth(i, portWarehouse));
    }
    usedBerths = new HashMap<Ship, Berth>();
    LOGGER.debug("Port has been created");
  }

  public static Port getInstance(int dock, int capacity) {
    if (instance == null) {
      lock.lock();
      try {
        if (instance == null)
          return new Port(dock, capacity);
      } finally {
        lock.unlock();
      }
    }
    return instance;
  }

  public boolean lockBerth(Ship ship) {
    boolean result = false;
    Berth berth = null;

    lock.lock();
    try {
      if (berthList.isEmpty()) {
        return false;
      } else {
        berth = berthList.remove(0);
      }

      if (berth != null) {
        result = true;
        usedBerths.put(ship, berth);
      }

    } finally {
      lock.unlock();
    }

    return result;
  }

  public boolean unlockBerth(Ship ship) {
    Berth berth = usedBerths.get(ship);
    if (berth == null)
      return false;
    lock.lock();
    try {
      berthList.add(berth);
      usedBerths.remove(ship);
    } finally {
      lock.unlock();
    }
    return true;
  }

  public Berth getBerth(Ship ship) {
    Berth berth = usedBerths.get(ship);
    return berth;
  }

  public void setContainersToWarehouse(List<Container> containerList) {
    portWarehouse.addContainer(containerList);
  }
}
