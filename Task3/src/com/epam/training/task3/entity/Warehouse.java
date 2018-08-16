package com.epam.training.task3.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Warehouse {

	private List<Container> containerList;
	private int size;
	private Lock lock = new ReentrantLock();

	public Warehouse(int size) {
		containerList = new ArrayList<Container>(size);
		this.size = size;
	}

	public boolean addContainer(Container container) {
		return containerList.add(container);
	}

	public boolean addContainer(List<Container> containers) {
		boolean result = false;
		if (containerList.size() + containers.size() <= size) {
			result = containerList.addAll(containers);
		}
		return result;
	}

	public List<Container> getContainer(int amount) {
		lock.lock();
		try {
			if (containerList.size() >= amount && !containerList.isEmpty()) {
				List<Container> cargo = new ArrayList<Container>(containerList.subList(0, amount));
				containerList.removeAll(cargo);
				return cargo;
			}
		} finally {
			lock.unlock();
		}
		return null;
	}

	public int getSize() {
		return size;
	}

	public int getCurrentSize() {
		return containerList.size();
	}

	public int getFreeSize() {
		return size - containerList.size();
	}
}