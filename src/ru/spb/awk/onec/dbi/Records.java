package ru.spb.awk.onec.dbi;

import java.nio.ByteBuffer;
import java.util.Map;

public interface Records extends Iterable<Map<String, Object>> {

	public abstract String getText(BlobAddr pAddr);

	public abstract ByteBuffer getBlob(BlobAddr pAddr);

	public abstract Map<String, Object> getRecord(int pIndex);

}