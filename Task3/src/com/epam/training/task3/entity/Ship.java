package com.epam.training.task3.entity;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class Ship implements Runnable {

  private final static Logger LOGGER = LogManager.getRootLogger();
  AtomicBoolean stopThread = new AtomicBoolean(false);

  enum ShipAction {
    LOAD_TO_PORT, LOAD_FROM_PORT
  }

  private String name;
  private Port port;
  private Warehouse shipWarehouse;

  public Ship(String name, Port port, int shipWarehouseSize) {
    this.name = name;
    this.port = port;
    shipWarehouse = new Warehouse(shipWarehouseSize);
  }

  public void stopThread() {
    stopThread.set(true);
  }

  public void run() {
    try {
      while (!stopThread.get()) {
        atSea();
        inPort();
      }
    } catch (InterruptedException e) {
      LOGGER.error("С кораблем случилась неприятность и он уничтожен.", e);
    }
  }

  private void atSea() throws InterruptedException {
    Thread.sleep(1000);
  }

  private void inPort() throws InterruptedException {

    boolean isLockedBerth = false;
    Berth berth = null;
    try {
      isLockedBerth = port.lockBerth(this);
      if (isLockedBerth) {
        berth = port.getBerth(this);
        LOGGER.debug("Корабль " + name + " пришвартовался к причалу " + berth.getId());
        ShipAction action = getNextAction();
        executeAction(action, berth);
      } else {
        LOGGER.debug("Кораблю " + name + " отказано в швартовке к причалу ");
      }
    } finally {
      if (isLockedBerth) {
        port.unlockBerth(this);
        LOGGER.debug("Корабль " + name + " отошел от причала " + berth.getId());
      }
    }

  }

  private void executeAction(ShipAction action, Berth berth) throws InterruptedException {
    switch (action) {
    case LOAD_TO_PORT:
      loadToPort(berth);
      break;
    case LOAD_FROM_PORT:
      loadFromPort(berth);
      break;
    }
  }

  private boolean loadToPort(Berth berth) throws InterruptedException {

    Random random = new Random();
    int containersNumberToMove = random.nextInt(shipWarehouse.getCurrentSize());
    boolean result = false;

    LOGGER.debug("Корабль " + name + " хочет загрузить " + containersNumberToMove
        + " контейнеров на склад порта.");

    result = berth.add(shipWarehouse, containersNumberToMove);

    if (!result) {
      LOGGER.debug("Недостаточно места на складе порта для выгрузки кораблем " + name + " "
          + containersNumberToMove + " контейнеров.");
    } else {
      LOGGER.debug(
          "Корабль " + name + " выгрузил " + containersNumberToMove + " контейнеров в порт.");

    }
    return result;
  }

  private boolean loadFromPort(Berth berth) throws InterruptedException {

    Random random = new Random();
    int containersNumberToMove = random.nextInt(shipWarehouse.getFreeSize());

    boolean result = false;

    LOGGER.debug("Корабль " + name + " хочет загрузить " + containersNumberToMove
        + " контейнеров со склада порта.");

    result = berth.get(shipWarehouse, containersNumberToMove);

    if (result) {
      LOGGER.debug(
          "Корабль " + name + " загрузил " + containersNumberToMove + " контейнеров из порта.");
    } else {
      LOGGER.debug("Недостаточно места на на корабле " + name + " для погрузки "
          + containersNumberToMove + " контейнеров из порта.");
    }

    return result;
  }

  private ShipAction getNextAction() {
    Random random = new Random();
    int value = random.nextInt(2);
    if (value == 0)
      return ShipAction.LOAD_TO_PORT;
    else
      return ShipAction.LOAD_FROM_PORT;
  }

  public void setContainersToWarehouse(List<Container> containerList) {
    shipWarehouse.addContainer(containerList);
  }

  public String getName() {
    return name;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (null == obj) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }

    Ship ship = (Ship) obj;
    if ((null == name) ? (null != ship.name) : (!name.equals(ship.name))) {
      return false;
    }
    if ((null == port) ? (null != ship.port) : (!port.equals(ship.port))) {
      return false;
    }
    if ((null == shipWarehouse) ? (null != ship.shipWarehouse)
        : (!shipWarehouse.equals(ship.shipWarehouse))) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int hash = 13;
    hash = 31 * hash + (name != null ? name.hashCode() : 0);
    hash = 47 * hash + (port != null ? port.hashCode() : 0);
    hash = 17 * hash + (shipWarehouse != null ? shipWarehouse.hashCode() : 0);
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder result = new StringBuilder();

    result.append(this.getClass().getSimpleName() + " Object {");
    result.append("Name: " + name);
    result.append(", port: " + port);
    result.append(", shipWarehouse: " + shipWarehouse);
    result.append("}");

    return result.toString();
  }

}