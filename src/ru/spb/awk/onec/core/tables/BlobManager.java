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


	private Head head;
	private PageManager pageManager;

	public BlobManager(PageManager pPageManager, Table pT) throws FileNotFoundException, IOException {
		head = PageHead.createSecondHead(pPageManager.getPage(pT.getBlobPage()));
		pageManager = pPageManager;
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
		int i1;
		int i2;
		int i3;
		i1 = indx/16368;
		i2 = (indx - i1*16368) / 16 ;
		i3 = (indx - i1*16368 - i2*16)*256;
		ByteBuffer buff = pageManager.getPage(head.getIndx(i1));
		buff.position(i2*4+4);
		buff.order(ByteOrder.LITTLE_ENDIAN);
		buff = pageManager.getPage(buff.getInt());
		buff.position(i3);
		buff.order(ByteOrder.LITTLE_ENDIAN);
		int nextblock = buff.getInt();
		int clen = buff.getShort();
		byte[] bufb = new byte[250];
		buff.get(bufb);
		int length = Math.min(250, len); 
		if(clen!=0 && length>clen) length = clen;
		blob.put(bufb, 0, length);
		return nextblock;
	}



}
