package com.epam.training.task3.controller;

import java.util.ArrayList;
import java.util.List;

import com.epam.training.task3.entity.Container;
import com.epam.training.task3.entity.Port;
import com.epam.training.task3.entity.Ship;

public class Runner {

  static final int WAREHOUSE_PORT_SIZE = 15;
  static final int NUMBER_OF_BERTHS = 2;
  static final int PORT_WAREHEOUST_CAPACITY = 900;

  public static void main(String[] args) throws InterruptedException {

    List<Container> containerList = new ArrayList<Container>(WAREHOUSE_PORT_SIZE);
    for (int i = 0; i < WAREHOUSE_PORT_SIZE; i++) {
      containerList.add(new Container(i));
    }

    Port port = Port.getInstance(NUMBER_OF_BERTHS, PORT_WAREHEOUST_CAPACITY);
    port.setContainersToWarehouse(containerList);

    List<Ship> ships = new ArrayList<Ship>();
    for (int i = 0; i < 10; i++) {
      ships.add(new Ship("Ship" + i, port, 50));
      containerList = new ArrayList<Container>(WAREHOUSE_PORT_SIZE);
      for (int j = 0; j < WAREHOUSE_PORT_SIZE; j++) {
        containerList.add(new Container(i));
      }
      ships.get(i).setContainersToWarehouse(containerList);
    }
    for (int i = 0; i < ships.size(); i++) {
      new Thread(ships.get(i)).start();
    }

    Thread.sleep(3000);
  }

}
