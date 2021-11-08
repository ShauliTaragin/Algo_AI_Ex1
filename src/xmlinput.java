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

public class xmlinput {
    static getinput a = new getinput("input.txt");
    private static  String FILENAME = "alarm_net.xml";

    public static void main(String[] args) {
        a.readfromfile();
        FILENAME= a.xml_path;
        try {
//creating a constructor of file class and parsing an XML file
            File file = new File(FILENAME);
//an instance of factory that gives a document builder
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//an instance of builder to parse the specified xml file
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();
            System.out.println("Root element: " + doc.getDocumentElement().getNodeName());
            NodeList nodeList = doc.getElementsByTagName("VARIABLE");
// nodeList is not iterable, so we are using for loop
            for (int itr = 0; itr < nodeList.getLength(); itr++) {
                Node node = nodeList.item(itr);
                System.out.println("\nNode Name :" + node.getNodeName());
                if (node.getNodeType() == Node.ELEMENT_NODE)
                {
                    Element eElement = (Element) node;
                    System.out.println("variable: "+ eElement.getElementsByTagName("NAME").item(0).getTextContent());
                    int iterator = 0;
                    while(eElement.getElementsByTagName("OUTCOME").item(iterator)!=null){
                        System.out.println("outcome "+ eElement.getElementsByTagName("OUTCOME").item(iterator).getTextContent());
                        iterator++;
                    }
                }
            }
            //we reach the nodes again this time the definition
            NodeList nodeList2 = doc.getElementsByTagName("DEFINITION");
            for (int itr = 0; itr < nodeList2.getLength(); itr++) {
                Node node = nodeList2.item(itr);
                System.out.println("\nNode Name :" + node.getNodeName());
                if (node.getNodeType() == Node.ELEMENT_NODE)
                {
                    Element eElement = (Element) node;
                    System.out.println("variable: "+ eElement.getElementsByTagName("FOR").item(0).getTextContent());
                    int iterator = 0;
                    while(eElement.getElementsByTagName("TABLE").item(iterator)!=null){
                        System.out.println("table "+ eElement.getElementsByTagName("TABLE").item(iterator).getTextContent());
                        iterator++;
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

}
