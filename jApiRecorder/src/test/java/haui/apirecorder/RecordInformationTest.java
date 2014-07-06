package haui.apirecorder;

import org.junit.Assert;
import org.junit.Test;

public class RecordInformationTest {

    @Test
    public void testRecordInformation() throws Exception {
        Object[] args = { "a", "b" };
        int[] argIdx4Pk = { 0 };
        RecordInformation recordInformation = new RecordInformation("test", args, argIdx4Pk);
        
        Assert.assertEquals(args, recordInformation.getArgs());
        Assert.assertEquals("test", recordInformation.getMethodName());
        Assert.assertEquals(args[0].hashCode(), recordInformation.getReturnValueId());
    }

    @Test
    public void testGetReturnValue() throws Exception {
        Object[] args = { "a", "b" };
        int[] argIdx4Pk = { 0 };
        RecordInformation recordInformation = new RecordInformation("test", args, argIdx4Pk);

        recordInformation.setReturnValue("test");
        
        Object result = recordInformation.getReturnValue();
        
        Assert.assertEquals("test", result);
    }

    @Test
    public void testGetReturnValueId() throws Exception {
        Object[] args = { "a", "b" };
        int[] argIdx4Pk = {};
        RecordInformation recordInformation = new RecordInformation("test", args, argIdx4Pk);

        recordInformation.setReturnValue("test");
        
        long result = recordInformation.getReturnValueId();
       
        long current = 0;
        for (int i = 0; i < args.length; i++) {
            current += args[i].hashCode();
        }
        Assert.assertEquals(current, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParameterValidationMethod() {
        Object[] args = { "a", "b" };
        int[] argIdx4Pk = { 0 };
        new RecordInformation(null, args, argIdx4Pk);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParameterValidationArgs() {
        Object[] args = null;
        int[] argIdx4Pk = { 0 };
        new RecordInformation("test", args, argIdx4Pk);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParameterValidationArgs2() {
        Object[] args = new Object[0];
        int[] argIdx4Pk = { 0 };
        new RecordInformation("test", args, argIdx4Pk);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParameterValidationArgs3() {
        Object[] args = { "a" };
        int[] argIdx4Pk = { 0, 0 };
        new RecordInformation("test", args, argIdx4Pk);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParameterValidationArgIdx4Pk3() {
        Object[] args = { "a", "b" };
        int[] argIdx4Pk = { -1 };
        new RecordInformation("test", args, argIdx4Pk);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParameterValidationArgIdx4Pk4() {
        Object[] args = { "a", "b" };
        int[] argIdx4Pk = { 3 };
        new RecordInformation("test", args, argIdx4Pk);
    }
}
