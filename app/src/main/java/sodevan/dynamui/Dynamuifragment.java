package sodevan.dynamui;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.gson.Gson;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by ravipiyush on 06/04/17.
 */


// Dynamui Fragment Class , A Dynamic Fragment is build here  
    
    
public class Dynamuifragment extends Fragment {

    private String TAG = "DynamuiFragment";
    private ViewGroup container ;
    private RelativeLayout r ;
    private String PACKAGE_NAME ;
    private int rid ;




    // constructor type Function for creating new Instances/ Dynamic Fragements
    public static Dynamuifragment newInstance(DynamuiObject dynamuiObject ) {

        Bundle args = new Bundle();
        String  gson  = new Gson().toJson(dynamuiObject) ;
        args.putString("stuff", gson);
        Dynamuifragment fragment = new Dynamuifragment();
        fragment.setArguments(args);
        return fragment;

    }

    
    
    // Building Components and adding them to our Fragement .Currently for single View/Object 
    // TODO: 22/04/17 make Multi Object loop 
    
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dynamui_fragment , container , false) ;

        this.container = container ;


        PACKAGE_NAME = getContext().getPackageName() ;


        r = (RelativeLayout)v.findViewById(R.id.mag) ;
        rid = R.id.mag;



        String stuff =  getArguments().get("stuff").toString() ;

        DynamuiObject  dynamuiObject = new Gson().fromJson( stuff , DynamuiObject.class) ;

        DynamuiListener listeners = dynamuiObject.getDynamuiListener() ;


        Log.i("dynamuiobject : "  , dynamuiObject.getProperties()[0].getParameters()[0].getParamtype()+"") ;

        View view= buildView(dynamuiObject)  ;


        // Dimensions and positioning of a particular view object


        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams( dynamuiObject.getObjwidth(), dynamuiObject.getObjheight()) ;

        //if position does not depend on any other view

        if (dynamuiObject.getPosi().getRlobjid()==-1){
            params.addRule(dynamuiObject.getPosi().getRlposiparam());
        }


        //if position depends  on any other view

        else {
            params.addRule(dynamuiObject.getPosi().getRlposiparam() , dynamuiObject.getPosi().getRlobjid()) ;
        }


        view.setLayoutParams(params);
        r.addView(view);
        setListeners(view , listeners);


        return v ;
    }





    
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public View buildView(DynamuiObject object) {

        Class classname = getClassfromname(object.getClassname()) ;
        Object viewObject = buildViewObject(classname)  ;
        DynamuiObjectProperty[] properties = object.getProperties() ;
        int n = properties.length  ;

        for (int h=0 ; h<n ; h++) {

            DynamuiObjectParams[]  params = properties[h].getParameters() ;

            int n1 = params.length ;
            Class<?>[] allparams = new Class[n1] ;
            Object[] finalparams = new Object[n1] ;


            for (int k=0 ; k<n1 ; k++) {
                allparams[k] =getClassfromname( params[k].getParamtype() ) ;
                Object o12 = parsevalue(params[k].getParamvalue()  , params[k].getParserClass() , params[k].getParsermethod()) ;
                finalparams[k] = o12  ;

            }

            try {

                Method method = viewObject.getClass().getMethod(properties[h].getMethodname() , allparams ) ;
                Log.i( TAG, method+"");
                method.invoke(viewObject , finalparams) ;

            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }


        View view=(View) viewObject ;

        view.setId(object.getId());

        return view ;
    }




    public Class<?> getClassfromname(String Classname) {

        Class<?> c=null ;

        if (Classname.equals("int")||Classname.equals("float")||Classname.equals("double")||Classname.equals("long")||Classname.equals("char")||Classname.equals("boolean")) {

            c=getprimitiveclass(Classname) ;

        }

        else {
            try {
                c = Class.forName(Classname);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }

        return c ;

    }


    public Object buildViewObject(Class<?> classname){

        Object o =null ;


        try {

            Constructor<?> constructor = classname.getConstructor(Context.class) ;
            o = constructor.newInstance(getContext()) ;
            Log.i(TAG , "Object :"+o+"") ;


        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return o ;
    }


    public Object parsevalue(String stringvalue , String  paramtype , String parser) {

        Object parsedvalue = null ;

        Log.i(TAG, "parsevalue: "+stringvalue + " " + paramtype + " " + parser);


        if (parser.equals("String")){
            Log.i("parsed Object" ,  "<3") ;
            return stringvalue ;
        }

        else if (parser.equals("android.content.Context")){
            return getContext();
        }

        else if(paramtype.equals("findview")) {

            if (parser.equals("stringid")){

                parsedvalue  =viewfinder(intIDGenerator(stringvalue)) ;
                Log.i("Viewparamstest" , parsedvalue+"") ;
            }

            else if (parser.equals("intid")){

                parsedvalue  =viewfinder(Integer.parseInt(stringvalue)) ;
                Log.i("Viewparamstest" , parsedvalue+"") ;


            }

            return parsedvalue ;

        }

        else {


            try {
                Method method = getClassfromname(paramtype).getMethod(parser, String.class);
                try {
                    parsedvalue = method.invoke(null ,stringvalue ) ;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }

            Log.i("parsed Object" , parsedvalue+"") ;
            return parsedvalue;

        }




    }



    public Class<?> getprimitiveclass(String classname){
        
        // We Cant Create Primitive Class Objects from Java Reflection so Creating using getprimitiveclass function
        

        Class<?> prim=null ;


        switch(classname) {

            case "int" : prim= int.class ;
                break;
            case "float" : prim = float.class ;
                break ;
            case "double" : prim = double.class ;
                break;
            case "long" : prim=long.class ;
                break ;
            case "char" : prim=char.class ;
                break ;
            case "boolean" : prim=boolean.class ;
        }
        return prim ;
    }



    public void setListeners(View v , final DynamuiListener listeneer) {


        if (listeneer!= null) {


            String lname = listeneer.getListenername();


            DynamuiTask[] tasks = listeneer.getObjoperations() ;
            int totaloper = listeneer.getTotaloper() ;

            final DynamuiCompactTask[] compactTasks = buildCompactTasks(tasks);







            //  Onlcick Listener
            if (lname.equals("click")) {

                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {



                        for ( DynamuiCompactTask y : compactTasks) {

                            Object obj = y.getFinalTaskObject() ;
                            Object callbackObj = null ;
                            HashMap<Method , Object[]> methods = y.getFinalTaskMethod() ;
                            Log.i("methodno" , methods.size()+"");

                            for( Method method : methods.keySet()){

                                Log.i("method name : " , method+"") ;

                                Object[] finalparams = methods.get(method) ;
                                if (finalparams.length >0)

                                {
                                    try {


                                        Log.i("tttobj" , obj+"");
                                        Log.i("tttfp" , finalparams+"");

                                        Object o =  method.invoke(obj, finalparams);

                                        if (o!=null) {
                                            if (o.getClass() == y.getaClass()) {
                                                callbackObj = o;
                                            }
                                        }
                                        Log.i("callback obj" , callbackObj+"") ;
                                    } catch (IllegalAccessException e) {
                                        e.printStackTrace();
                                    } catch (InvocationTargetException e) {
                                        e.printStackTrace();
                                    }

                                }

                                else {

                                    try {
                                        method.invoke(callbackObj);
                                    } catch (IllegalAccessException e) {
                                        e.printStackTrace();
                                    } catch (InvocationTargetException e) {
                                        e.printStackTrace();
                                    }


                                }
                            }

                        }

                    }
                });
            }





        }

    }



    public DynamuiCompactTask[] buildCompactTasks(DynamuiTask[] tasks){
        DynamuiCompactTask[] compactTasks = null  ;

        if(tasks!=null){

            int n = tasks.length ;
            compactTasks  = new DynamuiCompactTask[n] ;

            int num =0 ;

            for (int i=0 ; i<n ; i++ ){


                Class classname = getClassfromname(tasks[i].getClassname()) ;

                LinkedHashMap<Method , Object[]> finalTasksMethod  = new LinkedHashMap<>() ;


                Log.i("tasks :" , classname+ "") ;


                Object viewObject =null ;

                if(tasks[i].getStatus().equals("new")) {

                    viewObject = buildViewObject(classname)  ;


                }



                else {

                    int id ;

                    if (android.text.TextUtils.isDigitsOnly(tasks[i].getStatus())){

                         id = Integer.parseInt(tasks[i].getStatus()) ;


                    }

                    else {
                        id = intIDGenerator(tasks[i].getStatus()) ;
                    }


                    viewObject =viewfinder(id);

                }


                Log.i("tasks :" , viewObject+ "") ; ;



                Log.i("tasks :" , viewObject+ "") ; ;




                DynamuiObjectProperty[] properties = tasks[i].getProperties() ;

                int n3 = properties.length  ;

                for (int h=0 ; h<n3 ; h++) {
                    Method method = null ;


                    DynamuiObjectParams[]  params = properties[h].getParameters() ;

                    int n1 = params.length ;
                    Class<?>[] allparams = new Class[n1] ;
                    Object[] finalparams = new Object[n1] ;


                    for (int k=0 ; k<n1 ; k++) {
                        allparams[k] =getClassfromname( params[k].getParamtype() ) ;
                        Object o12 = parsevalue(params[k].getParamvalue()  ,  params[k].getParserClass() , params[k].getParsermethod()) ;
                        finalparams[k] = o12  ;

                    }

                    try {

                         method = classname.getMethod(properties[h].getMethodname() , allparams ) ;
                        Log.i( "tasks", method+"");

                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }


                    finalTasksMethod.put(method  , finalparams ) ;



                }


                compactTasks[i] = new DynamuiCompactTask(viewObject , finalTasksMethod , classname) ;


            }

        }

        return  compactTasks;
    }



    public View viewfinder(int viewid) {


        if (viewid==rid){

            return r ;
        }

        Log.i("Viewfinder" , viewid+"");
        View v = r.findViewById(viewid);
        Log.i("Viewfinder" , v+"");
        return v ;

    }



    public int intIDGenerator(String stringid) {

        int intID = getResources().getIdentifier(stringid, "id", PACKAGE_NAME);
        return intID ;
    }




}








/*  Class[] classes = new Class[n] ;

        for (int k=0 ; k<n ; k++) {

            classes[k] = params[k].getParamtype() ;
        }*/
