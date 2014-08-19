package ru.spb.awk.onec.core.tables;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import ru.spb.awk.onec.core.PageManager;
import ru.spb.awk.onec.core.head.Head;
import ru.spb.awk.onec.core.head.PageHead;
import ru.spb.awk.onec.dbi.BlobAddr;
import ru.spb.awk.onec.dbi.Table;

public class BlobManager {


	private static final int BLOCK_DATA_SIZE 	= 250;
	private static final int BLOB_BLOCK_SIZE 	= 256;
	private Head head;
	public BlobManager(PageManager pPageManager, Table pT) throws FileNotFoundException, IOException {
		head = PageHead.createSecondHead(pPageManager, pPageManager.getPage(pT.getBlobPage()), BLOB_BLOCK_SIZE);
	}

	public String getText(BlobAddr pAddr) throws FileNotFoundException, IOException {
		ByteBuffer blob = getBlob(pAddr);
		StringBuilder sb = new StringBuilder();
		while(blob.position() != blob.limit()) {
			sb.appendCodePoint(blob.getShort());
		}
		return sb.toString();
	}

	public ByteBuffer getBlob(BlobAddr pAddr) throws FileNotFoundException, IOException {
		int indx, len;
		len = pAddr.getLen();
		indx = pAddr.getIndx();
		ByteBuffer blob = ByteBuffer.allocate(len);
		int next = readBlock(indx, len, blob);
		while(next>0) {
			len-=250;
			next = readBlock(next, len, blob);
		}
		blob.position(0);
		blob.order(ByteOrder.LITTLE_ENDIAN);
		return blob;
	}

	private int readBlock(int indx, int len, ByteBuffer blob)
			throws FileNotFoundException, IOException {
		ByteBuffer buff = head.readBlock(indx);
		int nextblock = buff.getInt();
		int clen = buff.getShort();
		byte[] bufb = new byte[BLOCK_DATA_SIZE];
		buff.get(bufb);
		int length = Math.min(BLOCK_DATA_SIZE, len); 
		if(clen!=0 && length>clen) length = clen;
		blob.put(bufb, 0, length);
		return nextblock;
		
	}





}
