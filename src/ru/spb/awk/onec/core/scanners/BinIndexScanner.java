package ru.spb.awk.onec.core.scanners;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Iterator;

import ru.spb.awk.onec.core.PageManager;
import ru.spb.awk.onec.core.head.Head;
import ru.spb.awk.onec.core.head.PageHead;
import ru.spb.awk.onec.dbi.Index;
import ru.spb.awk.onec.dbi.Table;

public class BinIndexScanner implements Iterator<ByteBuffer> {

	private PageManager mPageManager;
	private Table mTable;
	private Index mIndex;
	private ByteBuffer rootPage;
	private Head head;
	private ByteBuffer declRootPage;

	public BinIndexScanner(PageManager pPageManager, Table pTable, Index pIndx) {
		mPageManager = pPageManager;
		mTable = pTable;
		mIndex = pIndx;
		try {
			head = PageHead.createSecondHead(mPageManager, mPageManager.getPage(mTable.getIndexPage()));
			DataScanner ds = new DataScanner(mPageManager, head);
			ByteBuffer indPage = ds.next();
			indPage.order(ByteOrder.LITTLE_ENDIAN);
			indPage.position(4 + 4 * mIndex.getPosition());
			int declPageAddr = indPage.getInt();
			declRootPage = mPageManager.getPage(declPageAddr/0x1000);
			declRootPage.order(ByteOrder.LITTLE_ENDIAN);
			declRootPage.position(0);
			int root_offset = declRootPage.getInt();
			short index_length = declRootPage.getShort();
			int unk = declRootPage.getChar();
		} catch (IOException except) {
			// TODO Auto-generated catch block
			except.printStackTrace();
		}
	}

	public ByteBuffer next() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasNext() {
		return false;
	}

}
