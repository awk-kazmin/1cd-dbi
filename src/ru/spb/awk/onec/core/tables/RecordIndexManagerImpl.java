package ru.spb.awk.onec.core.tables;

import java.util.Iterator;
import java.util.Map;

import ru.spb.awk.onec.core.PageManager;
import ru.spb.awk.onec.core.scanners.IndexScanner;
import ru.spb.awk.onec.core.scanners.IndexTree.Record;
import ru.spb.awk.onec.dbi.Field;
import ru.spb.awk.onec.dbi.Index;
import ru.spb.awk.onec.dbi.Table;

public class RecordIndexManagerImpl extends RecordManagerImpl {

	private Index index;
	private class RecordIterator implements Iterator<Map<String, Object>> {
		RecordIterator() {
			indexScanner = new IndexScanner(mPageManager, mTable, index);
			cursor = nextCursor();
		}

		private Map<String, Object> cursor;
		private IndexScanner indexScanner;

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

		private IndexHashMap<String, Object> nextCursor() {
			if(indexScanner.hasNext()) {
				Record nextKey = indexScanner.next();
				IndexHashMap<String, Object> map = new IndexHashMap<>();
				map.setIndex(nextKey.getIndex());
				for(Field f : index) {
					String name = f.getName();
					Object val = f.getValue(nextKey.getRecord());
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
