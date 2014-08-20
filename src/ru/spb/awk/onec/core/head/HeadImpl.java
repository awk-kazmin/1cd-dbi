package ru.spb.awk.onec.core.head;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ru.spb.awk.onec.core.PageManager;
import ru.spb.awk.onec.core.Version;


public class HeadImpl extends FirstPage {
	

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
		inst.mRecordsLength = bb.getInt();
		inst.mVer = bb.getInt();
		return inst;
	}
	
	public static Head createSecondHead(PageManager pPageManager, ByteBuffer pByteBuffer) {
		HeadImpl inst = new HeadImpl();
		inst.mManager = pPageManager;
		ByteBuffer bb = pByteBuffer;
		bb.order(ByteOrder.LITTLE_ENDIAN);
		bb.position(0);
		inst.mSign = readSign(bb, 8);
		inst.mRecordsLength = bb.getInt();
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
	private long mCountRecords;

	private HeadImpl() {

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

	public static Head createSecondHead(PageManager pPageManager, ByteBuffer pPage, int pRecordSize) {
		HeadImpl h = (HeadImpl) createSecondHead(pPageManager, pPage); 
		h.setRecordSize(pRecordSize);
		return h;
	}

	private void setRecordSize(int pRecordSize) {
		mRecordSize     = pRecordSize;
		mRecordsOnPage  = 0x1000/mRecordSize;
		mPagesOnRecord  = mRecordSize/0x1000;
		mMaxBlocks		= super.getMaxBlocks() * 0x1000 / mRecordSize;
		mCountRecords	= mRecordsLength / pRecordSize;
	}

	@Override
	public int getMaxBlocks() {
		return mMaxBlocks;
	}

	@Override
	public ByteBuffer readBlock(int pIndx) {
		int i1 = pIndx / (1023 * mRecordsOnPage);
		int i2 = (pIndx - i1) / mRecordsOnPage;
		int i3 = (pIndx - i1 - i2) * mRecordSize;
		try {
			return getBlock(i1, i2, i3);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private ByteBuffer getBlock(int i1, int i2, int i3)
			throws FileNotFoundException, IOException {
		int indx = getIndx(i1);
		ByteBuffer buff = mManager.getPage(indx);
		buff.position(i2*4+4);
		buff.order(ByteOrder.LITTLE_ENDIAN);
		buff = mManager.getPage(buff.getInt());
		buff.position(i3);
		buff.order(ByteOrder.LITTLE_ENDIAN);
		return buff;
	}

	@Override
	public Iterator<ByteBuffer> iterator() {
		DataScanner scaner = new DataScanner(mManager, this);
		return scaner;
	}
}