package ru.spb.awk.onec.core.scanners;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Iterator;

import ru.spb.awk.onec.core.PageManager;
import ru.spb.awk.onec.core.head.Head;


public class IndexScaner implements Iterator<ByteBuffer> {

	private Head mHead;
	private int mCursor;
	private PageManager mManager;

	public IndexScaner(Head head, PageManager pageManager) {
		mHead = head;
		mCursor = -1;
		mManager = pageManager;
	}

	public boolean hasNext() {
		return mCursor<mHead.getIndexesSize()-1;
	}

	public ByteBuffer next() {
		mCursor++;
		try {
			return mManager.getPage(mHead.getIndx(mCursor));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	
}
