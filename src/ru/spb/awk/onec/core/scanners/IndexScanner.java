package ru.spb.awk.onec.core.scanners;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Iterator;

import ru.spb.awk.onec.core.PageManager;
import ru.spb.awk.onec.core.head.Head;
import ru.spb.awk.onec.core.head.PageHead;
import ru.spb.awk.onec.dbi.Index;
import ru.spb.awk.onec.dbi.Table;

public class IndexScanner implements Iterator<ByteBuffer> {

	private PageManager mPageManager;
	private Table mTable;
	private Index mIndex;
	private ByteBuffer rootPage;
	private Head head;
	private ByteBuffer declRootPage;
	private int firstFreePage;

	public IndexScanner(PageManager pPageManager, Table pTable, Index pIndx) {
		mPageManager = pPageManager;
		mTable = pTable;
		mIndex = pIndx;
		try {
			head = PageHead.createSecondHead(mPageManager, mPageManager.getPage(mTable.getIndexPage()));
			Iterator<ByteBuffer> iterator = head.iterator();
			ByteBuffer root = iterator.next();
			root.position(0);
			root.order(ByteOrder.LITTLE_ENDIAN);
			int free = root.getInt();
			int ind = root.getInt()/0x1000;
			ArrayList<Integer> indxs = new ArrayList<>();
			while(ind!=0){
				indxs.add(ind);
				ind = root.getInt()/0x1000;
			}
			ByteBuffer indPageDescr = head.readBlock(indxs.get(mIndex.getPosition())); // Страница индексов
			int root_offset = indPageDescr.getInt()/0x1000;
			int index_length = indPageDescr.getShort();
			ByteBuffer indPage = head.readBlock(root_offset); // Страница индексов
			IndexTree tree = IndexTree.createTree(indPage, index_length);
		} catch (IOException except) {
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
