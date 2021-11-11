import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class xmlinput {
    static getinput a = new getinput("input.txt");
    private String FILENAME;
    ArrayList<Pnode> variables; // we hold an array list which will hold all the nodes we received from the xml

    public xmlinput(String FILENAME){
        this.FILENAME = FILENAME;
        ArrayList<Pnode> variables  = new ArrayList<Pnode>();
    }
    public ArrayList<Pnode> createNet(){
        a.readfromfile();
        FILENAME= a.xml_path;
        ArrayList<String> variables_names  = new ArrayList<String>();
        try {
            File file = new File(FILENAME);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();
            System.out.println("Root element: " + doc.getDocumentElement().getNodeName());
            NodeList nodeList = doc.getElementsByTagName("VARIABLE");
            // nodeList is not iterable, so we are using for loop
            // the main goal in this iteration is to init all the nodes
            for (int itr = 0; itr < nodeList.getLength(); itr++) {
                Node node = nodeList.item(itr);
                Pnode event = null;
                if (node.getNodeType() == Node.ELEMENT_NODE)
                {
                    Element eElement = (Element) node;
                    event = new Pnode(eElement.getElementsByTagName("NAME").item(0).getTextContent());//creating the pnode by name
                    int iterator = 0;
                    while(eElement.getElementsByTagName("OUTCOME").item(iterator)!=null){
                        event.addvalues(eElement.getElementsByTagName("OUTCOME").item(iterator).getTextContent());//adding outcomes to our pnode
                        iterator++;
                    }
                }
                variables.add(event);
                variables_names.add(event.getName());
            }
            //we reach the nodes again this time the definition
            NodeList nodeList2 = doc.getElementsByTagName("DEFINITION");
            for (int itr = 0; itr < nodeList2.getLength(); itr++) {
                /*we will now create some helpful event objects
                event2 -> the node we are working on
                str    -> holds the name of the given node in each iteration
                par    -> the node parent of event we found and want to add event as his child
                */
                Pnode event2 = null;
                String str = "";
                Pnode par = null;
                Node node = nodeList2.item(itr);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) node;
                    event2 = variables.get(variables_names.indexOf(eElement.getElementsByTagName("FOR").item(0).getTextContent()));
                    //System.out.println("the node is :  "  + event2);
                    //code for adding given
                    int iterator = 0;
                    while(eElement.getElementsByTagName("GIVEN").item(iterator)!=null){
                        str=eElement.getElementsByTagName("GIVEN").item(iterator).getTextContent();
                        par=variables.get(variables_names.indexOf(str));
                        event2.addparent(par);
                        par.addchild(event2);
                        iterator++;
                    }
                    iterator = 0;
                    while(eElement.getElementsByTagName("TABLE").item(iterator)!=null){
                        event2.addcpt(eElement.getElementsByTagName("TABLE").item(iterator).getTextContent());
                        iterator++;
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        for (int i = 0; i <variables.size() ; i++) {
            System.out.println(variables.get(i).toString());
        }
        return variables;
    }

}
