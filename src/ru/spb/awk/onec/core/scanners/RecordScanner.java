package ru.spb.awk.onec.core.scanners;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Iterator;

import ru.spb.awk.onec.core.PageManager;
import ru.spb.awk.onec.core.head.Head;
import ru.spb.awk.onec.core.head.PageHead;
import ru.spb.awk.onec.dbi.Table;

public class RecordScanner implements Iterable<ByteBuffer>, Iterator<ByteBuffer> {

	private ByteBuffer cursor;
	private ByteBuffer buffer;
	private PageManager mPageManager;
	private Table mTable;
	private Head mTOCHead;
	private DataScanner ds;
	
	public RecordScanner(PageManager pPageManager, Table pTable) throws FileNotFoundException, IOException {
		mPageManager = pPageManager;
		mTable = pTable;
		ByteBuffer bb = mPageManager.getPage(mTable.getRecordPage());
		mTOCHead = PageHead.createSecondHead(pPageManager, bb);
		ds = new DataScanner(mPageManager, mTOCHead);
		if(ds.hasNext()) {
			buffer = ds.next();
			buffer.position(0);
			cursor = getNext();
		}

	}

	@Override
	public Iterator<ByteBuffer> iterator() {
		return this;
	}

	@Override
	public boolean hasNext() {
		return cursor != null;
	}

	@Override
	public ByteBuffer next() {
		ByteBuffer result = cursor;
		cursor = getNext();
		return result;
	}

	private ByteBuffer getNext() {
		int last = buffer.limit() - buffer.position();
		if(last == 0) {
			if(ds.hasNext()) {
				buffer = ds.next();
				buffer.position(0);
			} else {
				return null;
			}
		}
		byte[] b = new byte[mTable.getRecordLenght()];
		if (last < b.length) {
			if(ds.hasNext()) {
				buffer.get(b,0,last);
				buffer = ds.next();
				buffer.position(0);
				buffer.get(b,last,b.length-last);
			} else {
				return null;
			}
		} else {
			buffer.get(b);
		}
		int crc = 0;
		for(byte v : b) {
			crc+=v;
		}
		if(b[0] == 1) {
			return getNext();
		} else if (crc == 0 ) {
			return getNext();
		} else {
			ByteBuffer result = ByteBuffer.allocate(mTable.getRecordLenght());
			result.put(b);
			result.position(1);
			result.order(ByteOrder.LITTLE_ENDIAN);
			return result;
		}
		
	}

}
