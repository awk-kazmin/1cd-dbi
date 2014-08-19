package ru.spb.awk.onec.core.tables.strategy;

import ru.spb.awk.onec.dbi.BlobAddr;

public class AddrImpl implements BlobAddr {
	int indx;
	/* (non-Javadoc)
	 * @see ru.spb.awk.onec.core.tables.strategy.BlobAddr#getIndx()
	 */
	@Override
	public int getIndx() {
		return indx;
	}
	/* (non-Javadoc)
	 * @see ru.spb.awk.onec.core.tables.strategy.BlobAddr#getLen()
	 */
	@Override
	public int getLen() {
		return len;
	}
	int len;
	public void setLen(int pInt) {
		len = pInt;
	}
	public void setIndx(int pInt) {
		indx = pInt;
	}
}