package ru.spb.awk.onec.core.head;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Iterator;

import ru.spb.awk.onec.core.PageManager;


class DataScanner implements Iterator<ByteBuffer> {
	
	
	class ArrayScaner implements Iterator<ByteBuffer> {

		private int[] mData;
		private int mCursor;

		public ArrayScaner(ByteBuffer data) {
			data.order(ByteOrder.LITTLE_ENDIAN);
			data.rewind();
			int len = data.getInt();
			mData = new int[len];
			for(int i = 0; i<len; i++){
				mData[i] = data.getInt();
			}
			mCursor = -1;
		}

		public boolean hasNext() {
			return mCursor<mData.length-1;
		}

		public ByteBuffer next() {
			mCursor++;
			try {
				return mPageManager.getPage(mData[mCursor]);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}

	}
	
	class IndexScaner implements Iterator<ByteBuffer> {

		private int mCursor;

		public IndexScaner() {
			mCursor = -1;
		}

		public boolean hasNext() {
			return mCursor<mHead.getIndexesSize()-1;
		}

		public ByteBuffer next() {
			mCursor++;
			try {
				return mPageManager.getPage(mHead.getIndx(mCursor));
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}

		
	}
	
	
	private PageManager mPageManager;
	private Head mHead;
	private IndexScaner mIndexScaner;
	private ArrayScaner mData;

	public DataScanner(PageManager pPageManager, Head pTOCHead) {
		mHead = pTOCHead;
		mPageManager = pPageManager;
		mIndexScaner = new IndexScaner();
		mData = new ArrayScaner(mIndexScaner.next());
	}

	public boolean hasNext() {
		return mData.hasNext() || mIndexScaner.hasNext();
	}

	public ByteBuffer next() {
		if(!mData.hasNext()) {
			if(mIndexScaner.hasNext()) {
				mData = new ArrayScaner(mIndexScaner.next());
			} else return null;
		}
		return mData.next();
	}

}
