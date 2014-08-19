package ru.spb.awk.onec.core;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

public class PageManagerTest {

	PageManager mPageManager;
	@Before
	public void setUp() throws Exception {
		mPageManager = new PageManager(new File("resourse\\1cv8ddb.1CD"));
	}

	@Test
	public void test() {
		assertEquals("1CDBMSV8", mPageManager.getSignDB());
		assertEquals("8.2.14.0", mPageManager.getVersionDB().toString());
		//assertEquals(75946, mPageManager.getSizeDB());
	}

}
