package ru.spb.awk.onec.core.scanners;
import java.nio.ByteBuffer;
import java.util.Iterator;

import ru.spb.awk.onec.core.PageManager;
import ru.spb.awk.onec.core.head.Head;


public class DataScanner implements Iterator<ByteBuffer> {

	private PageManager mPageManager;
	private Head mHead;
	private IndexScaner mIndexScaner;
	private ArrayScaner mData;

	public DataScanner(PageManager pPageManager, Head pTOCHead) {
		mHead = pTOCHead;
		mPageManager = pPageManager;
		mIndexScaner = new IndexScaner(mHead, mPageManager);
		mData = new ArrayScaner(mIndexScaner.next(), mPageManager);
	}

	public boolean hasNext() {
		return mData.hasNext() || mIndexScaner.hasNext();
	}

	public ByteBuffer next() {
		if(!mData.hasNext()) {
			if(mIndexScaner.hasNext()) {
				mData = new ArrayScaner(mIndexScaner.next(), mPageManager);
			} else return null;
		}
		return mData.next();
	}

}
