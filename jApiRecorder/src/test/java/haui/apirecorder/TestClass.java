package haui.apirecorder;

import java.util.Date;

public class TestClass {
    
    private String text = "intern";
    
    public TestClass() {
        
    }
    
    public TestClass(String text) {
        this.text = text;
    }

    public String simpleMethod(int i, String text, Date date) {
        return "" + i + text + date.toString() + this.text;
    }
}
