package ru.spb.awk.onec.core;
import java.nio.ByteBuffer;

import ru.spb.awk.onec.core.head.Head;
import ru.spb.awk.onec.core.head.PageHead;


public class FreePageManager {


	//private long mSize;
	private Head mHead;

	public FreePageManager(ByteBuffer pBuf) {
		mHead = PageHead.createSecondHead(pBuf);
	}

	public long getSize() {
		return mHead.getPages();
	}
}
