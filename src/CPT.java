import java.util.ArrayList;
import java.util.HashMap;

public class CPT {

    public ArrayList<HashMap<String,String>> table;//this will hold are values according to what is given to us.
    public int size_of_rows;

    /**
    A simple constructor for cpt.
     our CPT is represented by an Array list of hash maps. each index in the array list presenting a row of our cpt.
     The reason we are using this Data Structure for both our cpt and factor is in order to get easy and quick O(1)
        access to each event , what its outcome and whats the probability of combination of events we have in this row in the cpt.
     */
    public CPT() {
        table = new ArrayList<HashMap<String,String>>();
        this.size_of_rows = 0;
    }

    /**
     *
     A copy constructor for cpt receiving another cpt
   */
    public CPT(CPT other) {
        this.size_of_rows = other.size_of_rows;
        table = new ArrayList<HashMap<String,String>>();
        for (int i = 0; i < other.size_of_rows; i++) {
            table.add(other.table.get(i));
        }
    }

    @Override
    public String toString() {
        String ret = "";
        for (int i = 0; i < this.table.size(); i++) {
            ret += table.get(i).toString();
            ret += "\n";
        }
        ret += "The number of rows we have is : " + this.size_of_rows;
        return ret;
    }
}
