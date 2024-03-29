package ru.spb.awk.onec.core;
import java.nio.ByteBuffer;

import ru.spb.awk.onec.core.head.Head;
import ru.spb.awk.onec.core.head.HeadImpl;


public class FreePageManager {


	//private long mSize;
	private Head mHead;

	public FreePageManager(PageManager pPageManager, ByteBuffer pBuf) {
		mHead = HeadImpl.createSecondHead(pPageManager, pBuf);
	}

	public long getSize() {
		return mHead.getPages();
	}
}
