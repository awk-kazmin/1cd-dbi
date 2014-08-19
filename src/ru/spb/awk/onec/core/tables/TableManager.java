package ru.spb.awk.onec.core.tables;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import ru.spb.awk.onec.core.PageManager;
import ru.spb.awk.onec.core.head.Head;
import ru.spb.awk.onec.core.head.PageHead;
import ru.spb.awk.onec.core.scanners.DataScanner;
import ru.spb.awk.onec.dbi.Table;
import ru.spb.awk.onec.text.OneCParser;




public class TableManager {

	private PageManager mPageManager;
	private String mEncoding;
	private int mTables;
	private Map<String, Table> mTablesMap = new HashMap<>();

	public TableManager(PageManager pPageManager, ByteBuffer pBuf) throws FileNotFoundException, IOException {
		mPageManager = pPageManager;
		setupPageManager(pBuf);
	}

	private void setupPageManager(ByteBuffer pBuf) throws FileNotFoundException, IOException {
		Head head = PageHead.createSecondHead(mPageManager, pBuf);
		DataScanner ds = new DataScanner(mPageManager, head);
		if(ds.hasNext()) {
			ByteBuffer bb = ds.next();
			bb.order(ByteOrder.LITTLE_ENDIAN);
			bb.position(0);
			byte[] cbuf = new byte[32];
			bb.get(cbuf);
			mEncoding = new String(cbuf).trim();
			mTables = bb.getInt();
			for(int i=0;i<mTables;i++) {
				if(bb.remaining()<4) {
					if(ds.hasNext()) {
						bb = ds.next();
						bb.order(ByteOrder.LITTLE_ENDIAN);
						bb.position(0);
					} else break;
				}
				createTable(bb.getInt());
			}
		}
	}

	public synchronized String getEncoding() {
		return mEncoding;
	}

	public synchronized void setEncoding(String pEncoding) {
		mEncoding = pEncoding;
	}

	public synchronized Collection<Table> getTables() {
		return mTablesMap.values();
	}

	public synchronized void setTables(int pTables) {
		mTables = pTables;
	}

	private void createTable(int pI) throws FileNotFoundException, IOException {
		Head head = PageHead.createSecondHead(mPageManager, mPageManager.getPage(pI));
		DataScanner ds = new DataScanner(mPageManager, head);
		StringBuilder sb = new StringBuilder();
		while(ds.hasNext()) {
			ByteBuffer bb = ds.next();
			bb.order(ByteOrder.LITTLE_ENDIAN);
			bb.position(0);
			while(bb.position() < bb.limit()-2) {
				sb.appendCodePoint(bb.getShort());
			}
		}
		String source = sb.toString().trim();
		TableImpl tbl = new TableImpl();
		Reader r = new StringReader(source);
		OneCParser p = new OneCParser(r , new TableParser(tbl));	
		p.read();
		mTablesMap.put(tbl.getName(), tbl);
	}

	public Table getTable(String pTblName) {
		return mTablesMap.get(pTblName);
	}

}
