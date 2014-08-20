package ru.spb.awk.onec.core.tables;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.sun.org.apache.bcel.internal.classfile.PMGClass;

import ru.spb.awk.onec.core.PageManager;
import ru.spb.awk.onec.core.head.Head;
import ru.spb.awk.onec.core.head.HeadImpl;
import ru.spb.awk.onec.core.scanners.RecordScanner;
import ru.spb.awk.onec.dbi.BlobAddr;
import ru.spb.awk.onec.dbi.Field;
import ru.spb.awk.onec.dbi.Records;
import ru.spb.awk.onec.dbi.Table;


public class RecordManagerImpl implements  Records {

	public class RecordIterator implements Iterator<Map<String, Object>> {
		
		private RecordScanner rs;

		RecordIterator()  {
			try {
				rs = new RecordScanner(mPageManager, mTable);
				if(rs.hasNext())
					cursor = getNext();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		Map<String, Object> cursor;
		@Override
		public boolean hasNext() {
			return cursor!=null;
		}

		@Override
		public Map<String, Object> next() {
			Map<String, Object> oldCursor = cursor;
			cursor = getNext();
			return oldCursor;
		}

		private Map<String, Object> getNext() {
			Map<String, Object> c = null;
			if(!rs.hasNext()) return c;
			ByteBuffer record = rs.next();

			return createRecord(record);
		}

	}

	protected Table mTable;
	protected PageManager mPageManager;
	private BlobManager mMan;
	private Head head;


	public RecordManagerImpl(PageManager pPageManager, Table pT) {
		mPageManager = pPageManager;
		mTable = pT;
		try {
			mMan = new BlobManager(mPageManager, mTable);
			head = HeadImpl.createSecondHead(pPageManager, mPageManager.getPage(mTable.getRecordPage()), mTable.getRecordLenght());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Iterator<Map<String, Object>> iterator() {
		return new RecordIterator();
	}

	/* (non-Javadoc)
	 * @see ru.spb.awk.onec.core.tables.Records#getText(ru.spb.awk.onec.core.tables.strategy.Addr)
	 */
	@Override
	public String getText(BlobAddr pAddr) {
		if(pAddr==null) return null;
		try {
			return mMan.getText(pAddr);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}	
	}
	
	/* (non-Javadoc)
	 * @see ru.spb.awk.onec.core.tables.Records#getBlob(ru.spb.awk.onec.core.tables.strategy.Addr)
	 */
	@Override
	public ByteBuffer getBlob(BlobAddr pAddr) {
		if(pAddr==null) return null;
		try {
			return mMan.getBlob(pAddr);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}	
	}

	@Override
	public Map<String, Object> getRecord(int pIndex) {
		ByteBuffer readBlock = head.readBlock(pIndex);
		readBlock.get();
		return createRecord(readBlock);
	}

	private Map<String, Object> createRecord(ByteBuffer record) {
		Map<String, Object> c = new HashMap<>();
		if(mTable.haveVersionField()) {
			byte[] ver = new byte[16];
			record.get(ver);
		} else if(mTable.haveRecordLock()) {
			record.getLong();
		}
			
		for(Field f : mTable) {
			if(!f.isVersionField()) {
				Object val = null;
				if(f.isNullable()) {
					if(record.get() == 0) { 
						f.getValue(record);
					} else {
						val = f.getValue(record);
					}
				} else {
					val = f.getValue(record);
				}

				c.put(f.getName(), val);
			}
		}
		return c;
	}

}
