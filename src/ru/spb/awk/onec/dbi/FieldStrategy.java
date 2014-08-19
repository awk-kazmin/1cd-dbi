package ru.spb.awk.onec.dbi;

import java.nio.ByteBuffer;

public interface FieldStrategy {

	int getSize(Field pF);

	Object getValue(ByteBuffer pRecord, Field pF);

}
