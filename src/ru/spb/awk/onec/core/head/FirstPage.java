package ru.spb.awk.onec.core.head;

import java.nio.ByteBuffer;

import ru.spb.awk.onec.core.PageManager;
import ru.spb.awk.onec.core.Version;


public class FirstPage implements Head {
	
	String mSign;
	Version mVerDB;
	long mPages;
	long mVer;
	PageManager mManager;

	FirstPage() {

	}
	
	/* (non-Javadoc)
	 * @see ru.spb.awk.onec.core.Head#getPages()
	 */
	@Override
	public synchronized long getPages() {
		return mPages;
	}

	/* (non-Javadoc)
	 * @see ru.spb.awk.onec.core.Head#setPages(long)
	 */
	@Override
	public synchronized void setPages(long pPages) {
		mPages = pPages;
	}

	/* (non-Javadoc)
	 * @see ru.spb.awk.onec.core.Head#getVer()
	 */
	@Override
	public synchronized long getVer() {
		return mVer;
	}

	/* (non-Javadoc)
	 * @see ru.spb.awk.onec.core.Head#setVer(long)
	 */
	@Override
	public synchronized void setVer(long pVer) {
		mVer = pVer;
	}

	/* (non-Javadoc)
	 * @see ru.spb.awk.onec.core.Head#getSign()
	 */
	@Override
	public synchronized String getSign() {
		return mSign;
	}

	/* (non-Javadoc)
	 * @see ru.spb.awk.onec.core.Head#getVerDB()
	 */
	@Override
	public synchronized Version getVerDB() {
		return mVerDB;
	}


	/* (non-Javadoc)
	 * @see ru.spb.awk.onec.core.Head#getIndexesSize()
	 */
	@Override
	public int getIndexesSize() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see ru.spb.awk.onec.core.Head#getIndx(int)
	 */
	@Override
	public int getIndx(int pX) {
		return 0;
	}

	@Override
	public int getMaxBlocks() {
		return 1018*1023;
	}

	@Override
	public ByteBuffer readBlock(int pIndx) {
		return null;
	}


}