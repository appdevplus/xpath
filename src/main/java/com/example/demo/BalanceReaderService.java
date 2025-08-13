package com.example.demo;

import org.springframework.stereotype.Service;
import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.*;
import org.w3c.dom.*;
import java.io.InputStream;
import java.util.*;

@Service
public class BalanceReaderService {

    public static record BalInfo(String type, String currency, String amount, String creditDebit, String date) {}

    private static class CamtNamespace implements NamespaceContext {
        @Override public String getNamespaceURI(String prefix) {
            if ("doc".equals(prefix)) return "urn:iso:std:iso:20022:tech:xsd:camt.053.001.08";
            return XMLConstants.NULL_NS_URI;
        }
        @Override public String getPrefix(String nsURI) { return null; }
        @Override public Iterator<String> getPrefixes(String nsURI) { return null; }
    }

    public List<BalInfo> readBalances(InputStream in) throws Exception {
        var dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        Document doc;
        try (InputStream input = in) {
            doc = dbf.newDocumentBuilder().parse(in);
        }

        XPathFactory xpf = XPathFactory.newInstance();
        XPath xp = xpf.newXPath();
        xp.setNamespaceContext(new CamtNamespace());

        NodeList balNodes = (NodeList) xp.evaluate("//doc:Document//doc:Stmt/doc:Bal",
                doc, XPathConstants.NODESET);

        List<BalInfo> result = new ArrayList<>();
        for (int i = 0; i < balNodes.getLength(); i++) {
            Node bal = balNodes.item(i);
            String typeCode = (String) xp.evaluate("./doc:Tp/doc:CdOrPrtry/doc:Cd/text()", bal, XPathConstants.STRING);
            String amt      = (String) xp.evaluate("./doc:Amt/text()", bal, XPathConstants.STRING);
            String ccy      = (String) xp.evaluate("./doc:Amt/@Ccy", bal, XPathConstants.STRING);
            String ind      = (String) xp.evaluate("./doc:CdtDbtInd/text()", bal, XPathConstants.STRING);
            String date     = (String) xp.evaluate("./doc:Dt/doc:Dt/text()", bal, XPathConstants.STRING);

            result.add(new BalInfo(typeCode, ccy, amt, ind, date));
        }
        return result;
    }
}
