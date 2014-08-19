package ru.spb.awk.onec.core.tables;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import ru.spb.awk.onec.core.tables.strategy.BLOBStrategy;
import ru.spb.awk.onec.core.tables.strategy.BinaryStrategy;
import ru.spb.awk.onec.core.tables.strategy.BooleanStrategy;
import ru.spb.awk.onec.core.tables.strategy.CharStrategy;
import ru.spb.awk.onec.core.tables.strategy.DateStrategy;
import ru.spb.awk.onec.core.tables.strategy.DateTimeStrategy;
import ru.spb.awk.onec.core.tables.strategy.NumericStrategy;
import ru.spb.awk.onec.core.tables.strategy.TextStrategy;
import ru.spb.awk.onec.core.tables.strategy.VarCharStrategy;
import ru.spb.awk.onec.core.tables.strategy.VersionStrategy;
import ru.spb.awk.onec.dbi.Field;
import ru.spb.awk.onec.dbi.FieldStrategy;


public class FieldType {
	private static Map<String, FieldType> map = new HashMap<>();
	
	private FieldStrategy fs;
	private FieldType(FieldStrategy fs) {
		this.fs = fs;
	}
	
	public static FieldType get(String pFType) {
		FieldType t = map.get(pFType);
		if(t==null) {
			
			FieldStrategy s = null;
			if(pFType.equalsIgnoreCase("B")) {
				s = new BinaryStrategy();
			} else if(pFType.equalsIgnoreCase("L")) {
				s = new BooleanStrategy();
			} else if(pFType.equalsIgnoreCase("N")) {
				s = new NumericStrategy();
			} else if(pFType.equalsIgnoreCase("NC")) {
				s = new CharStrategy();
			} else if(pFType.equalsIgnoreCase("NVC")) {
				s = new VarCharStrategy();
			} else if(pFType.equalsIgnoreCase("RV")) {
				s = new VersionStrategy();
			} else if(pFType.equalsIgnoreCase("NT")) {
				s = new TextStrategy();
			} else if(pFType.equalsIgnoreCase("I")) {
				s = new BLOBStrategy();
			} else if(pFType.equalsIgnoreCase("D")) {
				s = new DateStrategy();
			} else if(pFType.equalsIgnoreCase("DT")) {
				s = new DateTimeStrategy();
			}			
			t=new FieldType(s);
			map.put(pFType, t);
		}
		return t;
	}
	public int getSize(Field f) {
		return fs.getSize(f);
	}
	public Object getValue(ByteBuffer pRecord, Field f) {
		return fs.getValue(pRecord, f);
	}
	
}
