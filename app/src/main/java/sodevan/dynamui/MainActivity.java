package sodevan.dynamui;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;


public class MainActivity extends FragmentActivity{

    String TAG = "Main Activity" ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Context c =getApplicationContext();

        // TODO: 12/04/17 Add Multiple View feature

        DynamuiObjectParams[] apra = {new DynamuiObjectParams("android.content.Context", "this" , "android.content.Context" ,"android.content.Context" ) , new DynamuiObjectParams("java.lang.CharSequence","Im Pro" , "java.lang.String","String") , new DynamuiObjectParams("int" ,"1" , "java.lang.Integer","parseInt" ) } ;
        DynamuiObjectProperty[] dp2   = {new DynamuiObjectProperty("makeText", apra )} ;
        DynamuiTask[] t = {new DynamuiTask("android.widget.Toast" , dp2 , "new")  };


        DynamuiListener lis = new DynamuiListener("click" , t , 1 ) ;

        DynamuiObjectParams[] para1 ={ new DynamuiObjectParams("java.lang.CharSequence","My Name Is Pro" , "java.lang.String","String" )} ;
        DynamuiObjectParams[] para2= {new DynamuiObjectParams("float" ,"30" , "java.lang.Float","parseFloat" )} ;
        DynamuiObjectParams[] para3= {new DynamuiObjectParams("int" ,"130" , "java.lang.Integer","parseInt" ) , new DynamuiObjectParams("int" ,"130" , "java.lang.Integer","parseInt" ) , new DynamuiObjectParams("int" ,"130" , "java.lang.Integer","parseInt" ) , new DynamuiObjectParams("int" ,"130" , "java.lang.Integer","parseInt" )} ;
        DynamuiObjectProperty[] dp = {new DynamuiObjectProperty("setText" , para1) , new DynamuiObjectProperty("setTextSize", para2) , new DynamuiObjectProperty("setPadding" ,para3)}  ;


        DynamuiObject n1 = new DynamuiObject("android.widget.TextView" ,dp , "new" , lis) ;
         Dynamui dynamui = new Dynamui("Main Activity", R.id.container , this , n1) ;
        dynamui.initializefragment();


    }


}
