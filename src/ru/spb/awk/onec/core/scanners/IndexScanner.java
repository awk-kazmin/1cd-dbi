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
			int flags = indPage.getShort();
			int count = indPage.getShort();
			int prev_page = indPage.getInt();
			int next_page = indPage.getInt();
			if(flags == 3 || flags == 2) {
				int freebytes = indPage.getShort(); // offset 12
		        int numrecmask = indPage.getShort(); // offset 14
		        int leftmask = indPage.getInt(); // offset 18
		        int rightmask = indPage.getShort(); // offset 20
		        int numrecbits = indPage.getShort(); // offset 22
		        int leftbits = indPage.getShort(); // offset 24
		        int rightbits = indPage.getShort(); // offset 26
		        int recbytes = indPage.getShort(); // offset 28
		        byte[] packed_index_data = new byte[4066]; // offset 30 (4066 = 0x1000 - 30)
		        indPage.get(packed_index_data);
			} else {
				for(int i=0;i<count;i++) {
					byte[] end_index = new byte[index_length];
			        int end_table_record_index; // reverse byte order!
			        int start_child_page; // reverse byte order!
				}
			}
		} catch (IOException except) {
			except.printStackTrace();
		}
	}

	private void skip(ByteBuffer pIndPage, int pIndxPos) {
		pIndPage.position(pIndPage.position() + pIndxPos);
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
