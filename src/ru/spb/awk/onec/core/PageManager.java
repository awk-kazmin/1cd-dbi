package ru.spb.awk.onec.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.LinkedHashMap;
import java.util.Map;

import ru.spb.awk.onec.core.head.Head;
import ru.spb.awk.onec.core.head.HeadImpl;
import ru.spb.awk.onec.core.tables.TableManager;


public class PageManager {

	public final int PAGE_SIZE 		 = 0x1000;
	public final int PAGES_ON_BUFFER = 0x1000;
	public final int FREE_PAGE_INDEX = 1;
	public final int TOC_PAGE_INDEX  = 2;
	
	private Map<Integer, ByteBuffer> mPagesBuffer = new LinkedHashMap<Integer, ByteBuffer>(PAGES_ON_BUFFER+1, 0.75f, true) {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		protected boolean removeEldestEntry(Map.Entry<Integer,ByteBuffer> eldest) {
			return size()>PAGES_ON_BUFFER;
		};
	};
	
	private final Head mDBHead;
	private final FreePageManager mFPM;
	private final TableManager mTableManager;
	private File mFile;
	public PageManager(File pFile) throws FileNotFoundException, IOException {
		mFile = pFile;
		try (InputStream is = new FileInputStream(pFile)) {
			byte[] buf = new byte[PAGE_SIZE];
			is.read(buf);
			mDBHead = HeadImpl.createFirstHead(ByteBuffer.wrap(buf));
			is.read(buf);
			mFPM = new FreePageManager(this, ByteBuffer.wrap(buf));
			is.read(buf);
			mTableManager = new TableManager(this, ByteBuffer.wrap(buf));
		}
		
	}


	public Version getVersionDB() {
		return mDBHead.getVerDB();
	}

	public String getSignDB() {
		return mDBHead.getSign();
	}

	public FreePageManager getFreePageManager() {
		return mFPM;
	}


	public long getSizeDB() {
		return mDBHead.getPages();
	}


	public ByteBuffer getPage(int pIndx) throws FileNotFoundException, IOException {
		if(mPagesBuffer.containsKey(pIndx)) {
			return mPagesBuffer.get(pIndx);
		}
		return load(pIndx);
	}


	private ByteBuffer load(int pIndx) throws FileNotFoundException, IOException {
		try (InputStream is = new FileInputStream(mFile)) {
			is.skip(pIndx*PAGE_SIZE);
			byte[] buf = new byte[PAGE_SIZE];
			is.read(buf);
			ByteBuffer byteBuffer = ByteBuffer.allocate(PAGE_SIZE);
			byteBuffer.put(buf);
			mPagesBuffer.put(pIndx, byteBuffer);
			return byteBuffer;
		}
	}


	public TableManager getTableManager() {
		return mTableManager;
	}


}
