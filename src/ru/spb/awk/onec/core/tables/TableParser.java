package ru.spb.awk.onec.core.tables;

import ru.spb.awk.onec.dbi.Field;
import ru.spb.awk.onec.text.OneCHandler;

public class TableParser implements OneCHandler {
	
	public class IndexHandler implements OneCHandler {
		int state = 0;
		int count = 0;
		int pos   = 0;
		private IndexImpl index;
		private Field field;

		@Override
		public void start() {
			if(state==1) {
				index = new IndexImpl(pos);
				pos++;
			}
			state ++;
		}

		@Override
		public void end() {
			state --;
			if(state==0)
				current = null;
			else if(state==1) {
				mTableImpl.addIndex(index);
				/*index = new IndexImpl(pos);
				pos++;*/
				count=0;
			}
		}

		@Override
		public void br() {
		}

		@Override
		public void value(String pS) {
			switch (state) {
			case 2:
				if(count==0) {
					index.setName(pS);
					count = 1;
				} else {
					index.setPrimaryKey(pS.equals("1"));
					count = 0;
				}
				break;
			case 3:
				if(count==0) {
					field = mTableImpl.getField(pS);
					count = 1;
				} else {
					index.addField(field, Integer.parseInt(pS));
					count = 0;
				}
				break;
			}

		}

	}

	public class FildsHandler implements OneCHandler {
		int state = 0;
		int count = 0;
		private FieldImpl field;
		@Override
		public void start() {
			if(state==1)
				field = new FieldImpl();
			state ++;
		}

		@Override
		public void end() {
			state --;
			if(state==0)
				current = new IndexHandler();
		}

		@Override
		public void br() {
		}

		@Override
		public void value(String pS) {
			if(state == 2)
			switch (count) {
			case 0:
				field.setName(pS);
				count++;
				break;
			case 1:
				field.setType(FieldType.get(pS));
				count++;
				break;
			case 2:
				field.setNullable(pS.equals("1"));
				count++;
				break;
			case 3:
				field.setLength(Integer.parseInt(pS));
				count++;
				break;
			case 4:
				field.setPrecision(Integer.parseInt(pS));
				count++;
				break;
			case 5:
				field.setIgnoreCase(pS.equals("CI"));
				mTableImpl.addField(field);
				count = 0;
				break;
			}
		}

	}

	/**
	 * 
	 */
	private final TableImpl mTableImpl;

	/**
	 * @param pTableImpl
	 */
	public TableParser(TableImpl pTableImpl) {
		mTableImpl = pTableImpl;
	}

	private int state = 0;
	private OneCHandler current;
	private boolean readrl;
	private boolean readfiles;
	private int count;
	private int substate;

	@Override
	public void start() {
		state ++;
		if(current!=null) {
			current.start();
		}
	}

	@Override
	public void end() {
		if(current!=null) {
			current.end();
		}
		state --;
	}

	@Override
	public void br() {
		if(current!=null) {
			current.br();
		}
	}

	@Override
	public void value(String s) {
		if(current!=null) {
			current.value(s);
			return;
		} 
		if(mTableImpl.getName() == null) {
			mTableImpl.setName(s);
		} else if (state == 1 && substate == 0) {
			current = new FildsHandler();
			substate ++;
		} else {
			if(s.equalsIgnoreCase("Recordlock")) {
				readrl = true;
			} else if(s.equalsIgnoreCase("Files")) {
				readfiles = true;
				count = 0;
			} else if (readrl) {
				mTableImpl.setRecordLock(s.equals("1"));
				readrl = false;
			} else if (readfiles) {
				switch (count) {
				case 0:
					mTableImpl.setRecordPage(Integer.parseInt(s));
					break;
				case 1:
					mTableImpl.setBlobPage(Integer.parseInt(s));	
					break;
				case 2:
					mTableImpl.setIndexPage(Integer.parseInt(s));
					break;
				}
				count++;
			}
		}
	}

}