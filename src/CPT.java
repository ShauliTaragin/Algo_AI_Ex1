import com.sun.jdi.DoubleValue;

import java.util.ArrayList;
import java.util.HashMap;

public class CPT {

    public ArrayList<HashMap> table;//this will hold are values according to what is given to us.
    public HashMap<String , String> rows;
    public int size_of_rows;

    /*
    A simple constructor for cpt
     */
    public CPT(){
        table= new ArrayList<HashMap>();
        rows = new HashMap<String,String>();
        this.size_of_rows=0;
    }

//    public ArrayList<HashMap> getValues() {
//        return values;
//    }
//
//    public void setValues(ArrayList<HashMap> values) {
//        this.values = values;
//    }
//
//    public HashMap<String, String> getRows() {
//        return rows;
//    }
//
//
//    private void setRows(String key, String value) {
//        this.setRows(key,value);
//    }
//
//
//    public int getSize_of_rows() {
//        return size_of_rows;
//    }
//
//    public void setSize_of_rows(int size_of_rows) {
//        this.size_of_rows = size_of_rows;
//    }
}
