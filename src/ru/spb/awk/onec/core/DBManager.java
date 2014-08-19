package ru.spb.awk.onec.core;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import ru.spb.awk.onec.dbi.DB;

public class DBManager {
	private Map<String, DB> bases = new HashMap<>();
	
	public DB open(File f) throws IOException {
		String path = f.getCanonicalPath();
		DB result = bases.get(path);
		if(result == null) {
			result = new DBImpl(this, f);
			bases.put(path, result);
		}
		return result;
	}

	public void close(File pF) throws IOException {
		String path = pF.getCanonicalPath();
		bases.remove(path);
	}
}
