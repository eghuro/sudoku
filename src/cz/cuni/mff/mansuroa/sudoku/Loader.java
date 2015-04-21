/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
 *
 * @author Alexander Mansurov <alexander.mansurov@gmail.com>
 */
class Loader {
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

    private static void parseNode(Sudoku sudoku, Element entry) {
        int row = Integer.parseInt(entry.getAttribute("row"));
        int col = Integer.parseInt(entry.getAttribute("col"));
        int value = Integer.parseInt(entry.getAttribute("value"));
               
        sudoku.setValue(row, col, value);
    }

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
