package sodevan.dynamui;

/**
 * Created by ravipiyush on 08/04/17.
 */

public class DynamuiObject {
    private String Classname ;
    private DynamuiObjectProperty[] Properties;
    private String Status ;
    private DynamuiListener dynamuiListener;

    public DynamuiObject(String classname, DynamuiObjectProperty[] properties, String status, DynamuiListener dynamuiListener) {
        Classname = classname;
        Properties = properties;
        Status = status;
        this.dynamuiListener = dynamuiListener;
    }


    public String getClassname() {
        return Classname;
    }

    public void setClassname(String classname) {
        Classname = classname;
    }

    public DynamuiObjectProperty[] getProperties() {
        return Properties;
    }

    public void setProperties(DynamuiObjectProperty[] properties) {
        Properties = properties;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public DynamuiListener getDynamuiListener() {
        return dynamuiListener;
    }

    public void setDynamuiListener(DynamuiListener dynamuiListener) {
        this.dynamuiListener = dynamuiListener;
    }


}
