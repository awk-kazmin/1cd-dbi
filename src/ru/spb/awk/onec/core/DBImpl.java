package ru.spb.awk.onec.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

import ru.spb.awk.onec.core.tables.RecordIndexManagerImpl;
import ru.spb.awk.onec.core.tables.RecordManagerImpl;
import ru.spb.awk.onec.dbi.DB;
import ru.spb.awk.onec.dbi.Index;
import ru.spb.awk.onec.dbi.Records;
import ru.spb.awk.onec.dbi.Table;

public class DBImpl implements DB {

	private File f;
	private PageManager pageManager;
	private DBManager manager;

	public DBImpl(DBManager manager, File pF) throws FileNotFoundException, IOException {
		f = pF; 
		pageManager = new PageManager(f);
		this.manager = manager;
	}

	@Override
	public void close() {
		try {
			manager.close(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Iterator<Table> iterator() {
		return pageManager.getTableManager().getTables().iterator();
	}

	@Override
	public Table getTable(String pString) {
		return pageManager.getTableManager().getTable(pString);
	}

	@Override
	public Records getTableScaner(Table pT) {
		return new RecordManagerImpl(pageManager, pT);
	}

	@Override
	public Records getTableScaner(Table pT, Index pI) {
		return new RecordIndexManagerImpl(pageManager, pT, pI);
	}


}
