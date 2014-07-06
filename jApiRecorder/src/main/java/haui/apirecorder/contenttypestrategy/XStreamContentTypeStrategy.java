/*
 * (C) 2014 haui
 */
package haui.apirecorder.contenttypestrategy;

import haui.apirecorder.exception.ContentTypeStrategyException;

import com.thoughtworks.xstream.XStream;

/**
 * @author haui
 *
 */
public class XStreamContentTypeStrategy implements ContentTypeStrategy {

	@Override
	public String serialize(Object object)
		throws ContentTypeStrategyException {
		String result = null;
		try {
			XStream xStream = new XStream();

			result = xStream.toXML(object);
		} catch (Exception e) {
			throw new ContentTypeStrategyException(String.format("Error serializing object %s", object.toString()), e);
		}
		return result;
	}

	@Override
	public Object deserialize(String serializedObject)
		throws ContentTypeStrategyException {
		Object result = null;
		try {
			XStream xStream = new XStream();

			result = xStream.fromXML(serializedObject);
		} catch (Exception e) {
			throw new ContentTypeStrategyException("Error deserializing object", e);
		}
		return result;
	}
}
