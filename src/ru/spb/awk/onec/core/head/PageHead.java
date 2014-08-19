package ru.spb.awk.onec.core.head;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

import ru.spb.awk.onec.core.Version;


public class PageHead extends FirstPage {
	

	private static String readSign(ByteBuffer pBuffer, int len) {
		byte[] signBuf = new byte[len];
		pBuffer.get(signBuf);
		return new String(signBuf);
	}

	public static Head createFirstHead(ByteBuffer pByteBuffer) {
		FirstPage inst = new FirstPage();
		ByteBuffer bb = pByteBuffer;
		bb.order(ByteOrder.LITTLE_ENDIAN);
		bb.position(0);
		inst.mSign = readSign(bb, 8);
		inst.mVerDB = new Version(bb.get(),bb.get(),bb.get(),bb.get());
		inst.mPages = bb.getInt();
		inst.mVer = bb.getInt();
		return inst;
	}
	
	public static Head createSecondHead(ByteBuffer pByteBuffer) {
		PageHead inst = new PageHead();
		ByteBuffer bb = pByteBuffer;
		bb.order(ByteOrder.LITTLE_ENDIAN);
		bb.position(0);
		inst.mSign = readSign(bb, 8);
		inst.mPages = bb.getInt();
		inst.mVer = bb.getInt();
		inst.mVer2 = bb.getInt();
		inst.mVer = bb.getInt();
		for(int i=0;i<1018;i++) {
			int l = bb.getInt();
			if(l==0) break;
			inst.blocks.add(l);
		}
		return inst;
	}
	
	public long getVer1() {
		return mVer1;
	}

	public long getVer2() {
		return mVer2;
	}

	private long mVer1;
	private long mVer2;
	private List<Integer> blocks = new ArrayList<>();
	private int mRecordSize    = 0x1000;
	private int mRecordsOnPage = 1;
	private int mPagesOnRecord = 1;
	private int mMaxBlocks	   = 1018*1023;

	private PageHead() {

	}
	

	/* (non-Javadoc)
	 * @see ru.spb.awk.onec.core.Head#getIndexesSize()
	 */
	@Override
	public int getIndexesSize() {
		return blocks.size();
	}

	/* (non-Javadoc)
	 * @see ru.spb.awk.onec.core.Head#getIndx(int)
	 */
	@Override
	public int getIndx(int pX) {
		return blocks.get(pX);
	}

	public static Head createSecondHead(ByteBuffer pPage, int pRecordSize) {
		PageHead h = (PageHead) createSecondHead(pPage); 
		h.setRecordSize(pRecordSize);
		return h;
	}

	private void setRecordSize(int pRecordSize) {
		mRecordSize     = pRecordSize;
		mRecordsOnPage  = 0x1000/mRecordSize;
		mPagesOnRecord  = mRecordSize/0x1000;
		mMaxBlocks		= super.getMaxBlocks() * 0x1000 / mRecordSize;
	}

	@Override
	public int getMaxBlocks() {
		return mMaxBlocks;
	}

}