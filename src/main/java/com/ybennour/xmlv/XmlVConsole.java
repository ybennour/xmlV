package com.ybennour.xmlv;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

/**
 * Created by Youssef BENNOUR
 */
public class XmlVConsole {
    final static Logger logger = LoggerFactory.getLogger(XmlVConsole.class);

    public static void main(String[] args) {
        // input validation
        if (!validateArgs(args)) {
            return;
        }

        String xsdFile = args[0];
        String xmlFile = args[1];

        if (validateXMLSchema(xsdFile, xmlFile)) {
            logger.info("Result : XML is valid :)");
        } else {
            logger.info("Result : XML is not valid :(");
        }
    }

    private static boolean validateXMLSchema(String xsdPath, String xmlPath) {
        Schema schema = null;

        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            schema = factory.newSchema(new File(xsdPath));
        } catch (SAXException e) {
            logger.error("SAXException on opening xsd: " + e.getMessage());
            return false;
        }

        try {
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new File(xmlPath)));
        } catch (SAXException e) {
            logger.error("Error: " + e.getMessage());
            return false;
        } catch (IOException e) {
            logger.error("Error: " + e.getMessage());
            return false;
        }
        return true;
    }

    private static boolean validateArgs(String[] args) {
        // validate number of arguments
        if (args.length < 2) {
            logger.info("Usage: xmlv.bat [primary xsd file] [xml file]");
            return false;
        }

        // validate files exist
        if (!fileExists(args[0])) {
            logger.error("xsd file not exists");
            return false;
        }
        if (!fileExists(args[1])) {
            logger.error("xml file not exists");
            return false;
        }

        return true;
    }

    private static boolean fileExists(String path) {
        return Files.exists(Paths.get(path));
    }
}
