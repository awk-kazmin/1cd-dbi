package ru.spb.awk.onec.core.scanners;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.zip.DataFormatException;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;
import java.util.zip.ZipInputStream;

public class IndexTree {

	private boolean root;
	private boolean leaf;
	private int prev_page;
	private int next_page;
	private short count;
	private byte[] packed_index_data = new byte[4084];
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
	private Object start_child_page;
	private byte[] index_data; 

	public static IndexTree createTree(ByteBuffer indPage, int index_length) {
		IndexTree tree = new IndexTree();
		int flags = indPage.getShort();
		tree.count = indPage.getShort();
		tree.prev_page = indPage.getInt();
		tree.next_page = indPage.getInt();
		if(flags==1||flags==3) tree.root = true;
		if(flags == 3 || flags == 2) {
			tree.leaf=true; 
	        
	        indPage.get(tree.packed_index_data);// offset 30 (4066 = 0x1000 - 30)
	        

	        tree.index_data = new byte[index_length*tree.count];
	        Inflater inflater = new Inflater(true);
	        try (InflaterInputStream infl = new InflaterInputStream(new ByteArrayInputStream(tree.packed_index_data),inflater)) {
				infl.read(tree.index_data);
			} catch (IOException except) {
				except.printStackTrace();
			}
		} else {
			indPage.order(ByteOrder.BIG_ENDIAN);
			for(int i=0;i<tree.count;i++) {
				tree.end_index = new byte[index_length];
				tree.end_table_record_index = indPage.getInt(); // reverse byte order!
				tree.start_child_page = indPage.getInt(); // reverse byte order!
			}
		}
		return tree;
	}

}
