package ru.spb.awk.onec.core.tables;

import java.util.HashMap;

public class IndexHashMap<K, V> extends HashMap<K, V> {

	private int indx;

	public void setIndex(int index) {
		indx = index;
	}

	public int getIndex() {
		return indx;
	}

}
