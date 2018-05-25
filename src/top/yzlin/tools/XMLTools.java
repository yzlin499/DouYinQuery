package top.yzlin.tools;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;

public final class XMLTools {
    /**
     * 查找节点
     * @param express xml文档读写的时候用的，可无视
     * @param xmlDoc
     * @return 节点
     */
    public static Node selectNode(String express, Document xmlDoc) {
        try {
            return (Node) XPathFactory.newInstance().newXPath().evaluate(express,xmlDoc.getDocumentElement() , XPathConstants.NODE);
        } catch (XPathExpressionException e) {
            return null;
        }
    }

    /**
     * 保存文档
     * @param xmlDoc
     * @param xmlFile
     */
    public static void saveDocument(Document xmlDoc, File xmlFile){
        try {
            Transformer former= TransformerFactory.newInstance().newTransformer();
            former.transform(new DOMSource(xmlDoc), new StreamResult(xmlFile));
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

}
