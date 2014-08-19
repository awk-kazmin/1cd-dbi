package ru.spb.awk.onec.dbi;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import ru.spb.awk.onec.core.DBManager;


public class DBManagerTest {

	private File f;
	private DBManager manager;

	@Before
	public void setUp() throws Exception {
		f = new File("resourse\\1cv8ddb.1CD");
		manager = new DBManager();
	}

	@Test
	public void testOpenClose() throws IOException {
		DB db = manager.open(f);
		db.close();
	}

	@Test
	public void testReadTables() throws IOException {
		DB db = manager.open(f);
		for(Table t : db) {
			assertNotNull(t);
		}
		db.close();
	}

	@Test
	public void testReadTableFields() throws IOException {
		DB db = manager.open(f);
		Table t = db.getTable("USERS");
		assertNotNull(t);
		for(Field f : t) {
			assertNotNull(f);
		}
		db.close();
	}
	@Test
	public void testReadTableIndexes() throws IOException {
		DB db = manager.open(f);
		Table t = db.getTable("LASTESTVERSIONS");
		assertNotNull(t);
		for(Index f : t.indexes()) {
			assertNotNull(f);
		}
		db.close();
	}
	
	@Test
	public void testReadTableRecords() throws IOException {
		DB db = manager.open(f);
		Table t = db.getTable("USERS");
		assertNotNull(t);
		Records records = db.getTableScaner(t);
		for(Map<String, Object> r : records) {
			assertNotNull(r);
			System.out.println(r.toString());
		}

		t = db.getTable("VERSIONS");
		assertNotNull(t);
		records = db.getTableScaner(t);
		for(Map<String, Object> r : records) {
			assertNotNull(r);
			System.out.println(r.get("VERNUM") + ":" + r.get("VERDATE"));
			BlobAddr addr = (BlobAddr) r.get("COMMENT");
			System.out.println(records.getText(addr));
		}
		db.close();
	}

}
