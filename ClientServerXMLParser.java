package pckg;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.io.StringWriter;

public class ClientServerXMLParser {
    public static <T> T jaxbXMLReader(InputStream is, Class<T> cls) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(cls);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            T obj = (T) unmarshaller.unmarshal(is);
            return obj;
        } catch (JAXBException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> String jaxbXMLWriter(T obj) {
        StringWriter sw = new StringWriter();
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(obj.getClass());
            Marshaller marshaller = jaxbContext.createMarshaller();
//            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(obj, sw);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return sw.toString();
    }
}
