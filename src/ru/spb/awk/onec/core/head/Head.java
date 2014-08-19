package ru.spb.awk.onec.core.head;

import java.nio.ByteBuffer;

import ru.spb.awk.onec.core.Version;

public interface Head {

	public abstract long getPages();

	public abstract void setPages(long pPages);

	public abstract long getVer();

	public abstract void setVer(long pVer);

	public abstract String getSign();

	public abstract Version getVerDB();

	public abstract int getIndexesSize();

	public abstract int getIndx(int pX);

	public abstract int getMaxBlocks();

	public abstract ByteBuffer readBlock(int pIndx);

}