package cz.cuni.mff.mansuroa.sudoku;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Loader nacita data z XML souboru.
 * Format souboru je:
 * &lt;sudoku&gt;
 *   &lt;entry row="cisloRadku" col="cisloSloupce" value="hodnota" /&gt;
 * &lt;/sudoku&gt;
 * @author Alexandr Mansurov <alexander.mansurov@gmail.com>
 */
class Loader {
    
    /**
     * Nacti sudoku ze souboru.
     * @param file soubor s daty
     * @return nactene sudoku
     * @throws LoadException doslo k chybe
     */
    public static Sudoku load(File file) throws LoadException {
        try{
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);

            NodeList entries = doc.getElementsByTagName("entry");
            Sudoku sudoku = parseEntries(entries);

            return sudoku;
        } catch(ParserConfigurationException | SAXException | IOException e) {
            throw new LoadException(e);
        }
        
        //<sudoku>
            //<entry row="" col="" value="" />
        //</sudoku>
    }

    /**
     * Zpracuj jeden XML element "entry".
     * Nacti cislo radku, sloupce a hodnotu a nastav je v modelu.
     * @param sudoku Data
     * @param entry XML element k naparsovani
     */
    private static void parseNode(Sudoku sudoku, Element entry) {
        int row = Integer.parseInt(entry.getAttribute("row"));
        int col = Integer.parseInt(entry.getAttribute("col"));
        int value = Integer.parseInt(entry.getAttribute("value"));
               
        sudoku.setValue(row, col, value);
    }

    /**
     * Zpracuj vsechny elementy "entry" nalezene ve vstupnim souboru.
     * @param entries XML elementy
     * @return nactene Sudoku
     */
    private static Sudoku parseEntries(NodeList entries) {
        Sudoku sudoku = new Sudoku(9);
        for (int entryNo = 0; entryNo < entries.getLength(); entryNo++) {
            Node entryNode = entries.item(entryNo);
            if (entryNode.getNodeType() == Node.ELEMENT_NODE) {
                parseNode(sudoku, (Element) entryNode);
            }
        }
        return sudoku;
    }
}
