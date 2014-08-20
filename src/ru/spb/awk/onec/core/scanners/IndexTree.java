package ru.spb.awk.onec.core.scanners;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

import ru.spb.awk.onec.core.head.Head;

public class IndexTree {

	public static class Record {

		private int len;
		private ByteBuffer record;
		private int numrec;
		private int left;
		private int right;

		private Record(int pIndex_length) {
			len= pIndex_length;
		}

		public ByteBuffer getRecord() {
			return record;
		}
		
		public int getIndex() {
			return numrec;
		}
	}


	private Head head;

	public static IndexTree createTree(ByteBuffer indPage, int index_length, Head pHead) {
		
		IndexTree tree = new IndexTree(index_length);
		tree.head = pHead;
		int flags = indPage.getShort();
		tree.number_indexes = indPage.getShort();
		tree.prev_page = indPage.getInt();
		tree.next_page = indPage.getInt();
		if(flags==1||flags==3) tree.root = true;
		if(flags == 3 || flags == 2) {
			tree.leaf=true; 
	        
			tree.freebytes = indPage.getShort(); // offset 12
			tree.numrecmask = indPage.getShort() ; // offset 14
			tree.leftmask = indPage.getInt(); // offset 18
			tree.rightmask = indPage.getShort(); // offset 20
			tree.numrecbits = indPage.getShort(); // offset 22
			tree.leftbits = indPage.getShort(); // offset 24
			tree.rightbits = indPage.getShort(); // offset 26
			tree.recbytes = indPage.getShort(); // offset 28
			byte[] buffer = new byte[tree.recbytes];
			tree.numrecs = new Record[tree.number_indexes];
			byte[] record_buffer = new byte[index_length];
			int readed = 0;
			for(int i=0;i<tree.number_indexes;i++) {
				indPage.get(buffer);
				Record record = new Record(index_length);
				int indrec = 0;
				for(int j=0, m = 0;j<tree.recbytes;j++,m++) {
					indrec |= buffer[j] << (8 * m);
				}
				record.numrec = indrec & tree.numrecmask;
				indrec >>= tree.numrecbits;
				record.left = Math.min(indrec & tree.leftmask, record.len);
				indrec >>= tree.leftbits;
				record.right = Math.min(indrec & tree.rightmask, record.len-record.left);
				long len = record.len - record.left - record.right;
				if(len>0) {
					int pos = indPage.position();
					long newPositon = indPage.limit() - len - readed;
					indPage.position((int) newPositon);
					readed += len;
					indPage.get(record_buffer, (int)record.left, (int)len);
					indPage.position(pos);
					if(record.right>0)
						Arrays.fill(record_buffer, (int)(index_length-1-record.right), (int)index_length, (byte)0);
				}
				record.record = ByteBuffer.wrap(Arrays.copyOf(record_buffer, index_length));
				tree.numrecs[i] = record;
			}
	        //indPage.get(tree.packed_index_data);
		} else {
			indPage.order(ByteOrder.BIG_ENDIAN);
			for(int i=0;i<tree.number_indexes;i++) {
				tree.end_index = new byte[index_length];
				tree.end_table_record_index = indPage.getInt(); // reverse byte order!
				tree.start_child_page = indPage.getInt(); // reverse byte order!
			}
		}
		return tree;
	}
	
	
	private static short abs(byte pB) {
		if(pB<0) {
			return (short) (256 + pB);
		}
		return pB;
	}


	private boolean root;
	private boolean leaf;
	private int prev_page;
	private int next_page;
	private short number_indexes;
	private short freebytes;
	private short numrecmask;
	private int leftmask;
	private short rightmask;
	private short numrecbits;
	private short leftbits;
	private short rightbits;
	private short recbytes;
	private byte[] end_index;
	private Object end_table_record_index;
	private int start_child_page;
	private Record[] numrecs;
	private int cursor;
	private int records;
	private IndexTree mNextNode;
	private int index_length;
	private boolean childRead;
	
	private IndexTree(int pIndex_length) {
		index_length = pIndex_length;
		cursor = 0;
	}
	
	
	public boolean hasNext() {
		if(leaf)
			if(cursor<number_indexes)
				return true;
			else if(next_page!=-1) {
				if(mNextNode==null || mNextNode.hasNext() == false) {
					getNextNode();
				}
				if(mNextNode == null) return false;
				return mNextNode.hasNext();
			}
		if(mNextNode==null || mNextNode.hasNext() == false) {
			getNextNode();
		}
		if(mNextNode == null) return false;
		return mNextNode.hasNext();
	}


	private void getNextNode() {
		if(leaf) {
			if(next_page!=-1) {
				ByteBuffer indPage = head.readBlock(next_page);
				mNextNode = createTree(indPage, index_length, head);
			}
		} else {
			if(childRead) {
				ByteBuffer indPage = head.readBlock(next_page);
				mNextNode = createTree(indPage, index_length, head);
			} else {
				ByteBuffer indPage = head.readBlock(start_child_page);
				mNextNode = createTree(indPage, index_length, head);
				childRead=true;
			}
		}
	}


	public Record next() {
		if(leaf){
			if(cursor<number_indexes) { 
				Record item = numrecs[cursor];
				cursor++;
				return item;
			}
		}
		if(mNextNode==null || mNextNode.hasNext() == false) {
			getNextNode();
		}
		return mNextNode.next();
	}

}
