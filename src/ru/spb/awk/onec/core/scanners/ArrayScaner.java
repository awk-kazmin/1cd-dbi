package ru.spb.awk.onec.core.scanners;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ru.spb.awk.onec.core.PageManager;


public class ArrayScaner implements Iterator<ByteBuffer> {

	private PageManager mPageManager;
	private List<Integer> mData;
	private int mCursor;

	public ArrayScaner(ByteBuffer data, PageManager pageManager) {
		mPageManager = pageManager;
		data.order(ByteOrder.LITTLE_ENDIAN);
		data.position(0);
		int len = data.getInt();
		mData = new ArrayList<>(len);
		for(int i = 0; i<len; i++){
			mData.add(data.getInt());
		}
		mCursor = -1;
	}

	public boolean hasNext() {
		return mCursor<mData.size()-1;
	}

	public ByteBuffer next() {
		mCursor++;
		try {
			return mPageManager.getPage(mData.get(mCursor));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}
