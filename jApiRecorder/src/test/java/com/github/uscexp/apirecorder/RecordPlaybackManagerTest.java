/*
 * Copyright (C) 2014 by haui - all rights reserved
 */
package com.github.uscexp.apirecorder;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import com.github.uscexp.apirecorder.attributereplacement.ReplacementConfiguration;
import com.github.uscexp.apirecorder.attributereplacement.replacementvalues.ReplacementValue;
import com.github.uscexp.apirecorder.attributereplacement.replacementvalues.ReplacementValueFactory;
import com.github.uscexp.apirecorder.contenttypestrategy.ContentTypeStrategy;
import com.github.uscexp.apirecorder.contenttypestrategy.XStreamContentTypeStrategy;
import com.github.uscexp.apirecorder.readwritestrategy.H2ReadWriteStrategy;
import com.github.uscexp.apirecorder.readwritestrategy.ReadWriteStrategy;

/**
 * @author haui
 *
 */
public class RecordPlaybackManagerTest extends JUnitBase {

    @Test
	public void testPBOnlineConstructorNoArgs()
		throws Exception {
		ContentTypeStrategy contentTypeStrategy = new XStreamContentTypeStrategy();
		ReadWriteStrategy readWriteStrategy = new H2ReadWriteStrategy();
		RecordPlaybackConfiguration recordPlaybackConfiguration = new RecordPlaybackConfiguration();
		recordPlaybackConfiguration.addArgumentIndices4PrimaryKey("simpleMethod", 0, 1);
		TestClass testClass = (TestClass) RecordPlaybackManager.newInstance(TestClass.class, RecordPlaybackMode.PB_ONLINE,
				contentTypeStrategy, readWriteStrategy, recordPlaybackConfiguration);

		Date date = new Date();
		String result = testClass.simpleMethod(2, "text2", date);

		Assert.assertEquals("" + 2 + "text2" + date.toString() + "intern", result);
	}

	@Test
	public void testPBOnlineConstructorWithArgs()
		throws Exception {
		ContentTypeStrategy contentTypeStrategy = new XStreamContentTypeStrategy();
		ReadWriteStrategy readWriteStrategy = new H2ReadWriteStrategy();
		RecordPlaybackConfiguration recordPlaybackConfiguration = new RecordPlaybackConfiguration();
		recordPlaybackConfiguration.addArgumentIndices4PrimaryKey("simpleMethod", 0, 1);
		Class<? extends Object>[] parameterTypes = new Class[1];
		parameterTypes[0] = String.class;
		Object[] args = { "extern" };
		TestClass testClass = (TestClass) RecordPlaybackManager.newInstance(TestClass.class, parameterTypes, args,
				RecordPlaybackMode.PB_ONLINE, contentTypeStrategy, readWriteStrategy, recordPlaybackConfiguration);

		Date date = new Date();
		String result = testClass.simpleMethod(1, "text1", date);

		Assert.assertEquals("" + 1 + "text1" + date.toString() + args[0], result);
	}

	@Test
	public void testPBOffline()
		throws Exception {
		ContentTypeStrategy contentTypeStrategy = new XStreamContentTypeStrategy();
		ReadWriteStrategy readWriteStrategy = new H2ReadWriteStrategy();
		RecordPlaybackConfiguration recordPlaybackConfiguration = new RecordPlaybackConfiguration();
		recordPlaybackConfiguration.addArgumentIndices4PrimaryKey("simpleMethod", 0, 1);
		TestClass testClass = (TestClass) RecordPlaybackManager.newInstance(TestClass.class, RecordPlaybackMode.PB_OFFLINE,
				contentTypeStrategy, readWriteStrategy, recordPlaybackConfiguration);

		Date date = new Date();
		String result = testClass.simpleMethod(4, "text4", date);

		Assert.assertNull(result);
	}

	@Test
	public void testRecordPBOffline()
		throws Exception {
		ContentTypeStrategy contentTypeStrategy = new XStreamContentTypeStrategy();
		ReadWriteStrategy readWriteStrategy = new H2ReadWriteStrategy();
		RecordPlaybackConfiguration recordPlaybackConfiguration = new RecordPlaybackConfiguration();
		recordPlaybackConfiguration.addArgumentIndices4PrimaryKey("simpleMethod", 0, 1);
		TestClass testClass = (TestClass) RecordPlaybackManager.newInstance(TestClass.class, RecordPlaybackMode.RECORD, contentTypeStrategy,
				readWriteStrategy, recordPlaybackConfiguration);

		Date date = new Date();
		String result = testClass.simpleMethod(3, "text3", date);

		Assert.assertEquals("" + 3 + "text3" + date.toString() + "intern", result);

		testClass = (TestClass) RecordPlaybackManager.newInstance(TestClass.class, RecordPlaybackMode.PB_OFFLINE, contentTypeStrategy,
				readWriteStrategy, recordPlaybackConfiguration);

		Date date2 = new Date();
		result = testClass.simpleMethod(3, "text3", date2);

		Assert.assertEquals("" + 3 + "text3" + date.toString() + "intern", result);
	}

    @Test
    public void testRecordPBOfflineWithAttributeReplacement()
        throws Exception {
        ContentTypeStrategy contentTypeStrategy = new XStreamContentTypeStrategy();
        ReadWriteStrategy readWriteStrategy = new H2ReadWriteStrategy();
        RecordPlaybackConfiguration recordPlaybackConfiguration = new RecordPlaybackConfiguration();
        recordPlaybackConfiguration.addArgumentIndices4PrimaryKey("simpleLevel2Method", 0, 1);
        TestClass testClass = (TestClass) RecordPlaybackManager.newInstance(TestClass.class, RecordPlaybackMode.RECORD, contentTypeStrategy,
                readWriteStrategy, recordPlaybackConfiguration);

        Date date = new Date();
        String text = "extern";
        TestClass result = testClass.simpleLevel2Method(3, text, date);

        Assert.assertEquals(text, result.getText());

        String value = "post_extern";
        ReplacementValue replacementValue = ReplacementValueFactory.createReplacementGivenValue(value);
        ReplacementConfiguration replacementConfiguration = new ReplacementConfiguration("text", replacementValue);
        recordPlaybackConfiguration.addReplacementConfiguration("simpleLevel2Method", replacementConfiguration );
        testClass = (TestClass) RecordPlaybackManager.newInstance(TestClass.class, RecordPlaybackMode.PB_OFFLINE, contentTypeStrategy,
                readWriteStrategy, recordPlaybackConfiguration);

        Date date2 = new Date();
        result = testClass.simpleLevel2Method(3, text, date2);

        Assert.assertEquals(value, result.getText());
    }

	@Test
	public void testForeward()
		throws Exception {
		ContentTypeStrategy contentTypeStrategy = new XStreamContentTypeStrategy();
		ReadWriteStrategy readWriteStrategy = new H2ReadWriteStrategy();
		RecordPlaybackConfiguration recordPlaybackConfiguration = new RecordPlaybackConfiguration();
		recordPlaybackConfiguration.addArgumentIndices4PrimaryKey("simpleMethod", 0, 1);
		TestClass testClass = (TestClass) RecordPlaybackManager.newInstance(TestClass.class, RecordPlaybackMode.FOREWARD,
				contentTypeStrategy, readWriteStrategy, recordPlaybackConfiguration);

		Date date = new Date();
		String result = testClass.simpleMethod(5, "text5", date);

		Assert.assertEquals("" + 5 + "text5" + date.toString() + "intern", result);

		testClass = (TestClass) RecordPlaybackManager.newInstance(TestClass.class, RecordPlaybackMode.PB_OFFLINE, contentTypeStrategy,
				readWriteStrategy, recordPlaybackConfiguration);

		Date date2 = new Date();
		result = testClass.simpleMethod(5, "text5", date2);

		Assert.assertNull(result);
	}

	@Test
	public void testRecordPBOfflineConfigNo()
		throws Exception {
		ContentTypeStrategy contentTypeStrategy = new XStreamContentTypeStrategy();
		ReadWriteStrategy readWriteStrategy = new H2ReadWriteStrategy();

		TestClass testClass = (TestClass) RecordPlaybackManager.newInstance(TestClass.class, RecordPlaybackMode.RECORD, contentTypeStrategy,
				readWriteStrategy, null);

		Date date = new Date();
		String result = testClass.simpleMethod(6, "text6", date);

		Assert.assertEquals("" + 6 + "text6" + date.toString() + "intern", result);

		testClass = (TestClass) RecordPlaybackManager.newInstance(TestClass.class, RecordPlaybackMode.PB_OFFLINE, contentTypeStrategy,
				readWriteStrategy, null);

		result = testClass.simpleMethod(6, "text6", date);

		Assert.assertEquals("" + 6 + "text6" + date.toString() + "intern", result);
	}
}
