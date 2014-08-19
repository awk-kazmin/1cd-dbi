package ru.spb.awk.onec.core.tables;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.sun.org.apache.bcel.internal.generic.NEWARRAY;

import ru.spb.awk.onec.dbi.Field;
import ru.spb.awk.onec.dbi.Index;
import ru.spb.awk.onec.dbi.Table;


public class TableImpl implements Table {

	private int startRecordPage;
	private int startIndexPage;
	private int startBlobPage;
	private boolean recordLock;
	private Map<String, Field> fields = new HashMap<>();
	private List<Field> fOrder = new ArrayList<>();
	private boolean mRV;


	private String name;
	private List<Index> indexes = new ArrayList<>();
	private Map<String, Index> indexesMap = new HashMap<>();



	public String getName() {
		return name;
	}


	public List<Field> getFields() {
		return new ArrayList<Field>(fields.values());
	}


	public int getRecordLenght() {
		int sum = (!mRV&&recordLock)?8:0;
		sum +=1;
		for(Field f : fields.values()) {
			sum += f.getSize();
		}
		return sum;
	}


	@Override
	public Iterator<Field> iterator() {
		return fOrder.iterator();
	}


	@Override
	public int getRecordPage() {
		return startRecordPage;
	}

	@Override
	public int getIndexPage() {
		return startIndexPage;
	}

	@Override
	public int getBlobPage() {
		return startBlobPage;
	}


	@Override
	public boolean haveVersionField() {
		return mRV;
	}


	@Override
	public boolean haveRecordLock() {
		return recordLock;
	}


	public void setName(String pS) {
		name = pS;
	}


	public void addField(Field pField) {
		fields.put(pField.getName(), pField);
		if(pField.isVersionField()) {
			mRV = true;
		}
		fOrder.add(pField);		
	}


	public void setRecordLock(boolean pEquals) {
		recordLock = pEquals;		
	}


	public void setRecordPage(int pParseInt) {
		startRecordPage = pParseInt;
	}


	public void setBlobPage(int pParseInt) {
		startBlobPage = pParseInt;
	}


	public void setIndexPage(int pParseInt) {
		startIndexPage = pParseInt;
	}


	public Field getField(String pName) {
		return fields.get(pName);
	}


	public void addIndex(Index pIndex) {
		indexesMap.put(pIndex.getName(), pIndex);
		indexes.add(pIndex);
	}


	@Override
	public Collection<Index> indexes() {
		return indexes;
	}


	@Override
	public Index getIndex(String pString) {
		return indexesMap.get(pString);
	}

}
