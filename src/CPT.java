import com.sun.jdi.DoubleValue;

import java.util.ArrayList;
import java.util.HashMap;

public class CPT {

    public ArrayList<HashMap> table;//this will hold are values according to what is given to us.
    public int size_of_rows;

    /*
    A simple constructor for cpt
     */
    public CPT(){
        table= new ArrayList<HashMap>();
        this.size_of_rows=0;
    }
      /*
    A copy constructor for cpt recieveing another cpt
     */
      public CPT(CPT other){
          this.size_of_rows=other.size_of_rows;
          table= new ArrayList<HashMap>();
          for (int i = 0; i <other.size_of_rows ; i++) {
              table.add(other.table.get(i));
          }
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
