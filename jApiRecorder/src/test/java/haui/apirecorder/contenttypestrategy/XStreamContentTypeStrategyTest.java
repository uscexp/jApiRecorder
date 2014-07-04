package haui.apirecorder.contenttypestrategy;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

public class XStreamContentTypeStrategyTest {

    private XStreamContentTypeStrategy xStreamContentTypeStrategySUT = new XStreamContentTypeStrategy();
    
    @Test
    public void testSerialize() throws Exception {
        Date date = new Date();
        
        String serializedObject = xStreamContentTypeStrategySUT.serialize(date);
        
        Assert.assertNotNull(serializedObject);
    }

    @Test
    public void testDeserialize() throws Exception {
        Date date = new Date();
        
        String serializedObject = xStreamContentTypeStrategySUT.serialize(date);
        Object object = xStreamContentTypeStrategySUT.deserialize(serializedObject);
        
        Assert.assertNotNull(object);
    }

}
