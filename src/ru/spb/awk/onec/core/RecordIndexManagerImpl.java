package ru.spb.awk.onec.core;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import ru.spb.awk.onec.core.scanners.BinIndexScanner;
import ru.spb.awk.onec.core.tables.RecordManagerImpl;
import ru.spb.awk.onec.dbi.Field;
import ru.spb.awk.onec.dbi.Index;
import ru.spb.awk.onec.dbi.Table;

public class RecordIndexManagerImpl extends RecordManagerImpl {

	private Index index;
	private class RecordIterator implements Iterator<Map<String, Object>> {
		RecordIterator() {
			cursor = nextCursor();
			indexScanner = new BinIndexScanner(mPageManager, mTable, index);
		}

		private Map<String, Object> cursor;
		private BinIndexScanner indexScanner;

		@Override
		public boolean hasNext() {
			return cursor != null;
		}

		@Override
		public Map<String, Object> next() {
			Map<String, Object> val = cursor;
			cursor = nextCursor();
			return val;
		}

		private Map<String, Object> nextCursor() {
			if(indexScanner.hasNext()) {
				ByteBuffer nextKey = indexScanner.next();
				Map<String, Object> map = new HashMap<>();
				for(Field f : index) {
					String name = f.getName();
					Object val = f.getValue(nextKey);
					map.put(name, val);
				}
				return map;
			}
			
			return null;
		}
		
	}

	public RecordIndexManagerImpl(PageManager pPageManager, Table pT, Index i) {
		super(pPageManager, pT);
		index = i;
	}

	@Override
	public Iterator<Map<String, Object>> iterator() {
		return new RecordIterator();
	}


}
