package com.borido.prctica_mymapas.modelos;

import android.content.Context;

import com.borido.prctica_mymapas.Clases.Localizacion;
import com.borido.prctica_mymapas.Intefaces.Persistencia;
import com.borido.prctica_mymapas.R;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class GestionLocalidadesXML implements Persistencia {

    private Context context;

    public GestionLocalidadesXML(Context context) {
        this.context = context;
    }

    public List<Localizacion> leerLocalizaciones() {
        List<Localizacion> localizaciones = new ArrayList<Localizacion>();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(this.context.getResources().openRawResource(R.raw.localizaciones));
            Element raiz = doc.getDocumentElement();
            NodeList items = raiz.getElementsByTagName("localizacion");

            for (int i = 0; i < items.getLength(); i++) {
                Node nodoLocalizacion = items.item(i);
                Localizacion localizacion = new Localizacion();

                for (int j = 0; j < nodoLocalizacion.getChildNodes().getLength() - 1; j++) {
                    Node nodoActual = nodoLocalizacion.getChildNodes().item(j);
                    if (nodoActual.getNodeType() == Node.ELEMENT_NODE) {
                        if (nodoActual.getNodeName().equalsIgnoreCase("titulo")) {
                            localizacion.setTitulo(nodoActual.getChildNodes().item(0).getNodeValue());
                        } else if (nodoActual.getNodeName().equalsIgnoreCase("fragmento")) {
                            localizacion.setFragmento(nodoActual.getChildNodes().item(0).getNodeValue());
                        } else if (nodoActual.getNodeName().equalsIgnoreCase("etiqueta")) {
                            localizacion.setEtiqueta(nodoActual.getChildNodes().item(0).getNodeValue());
                        } else if (nodoActual.getNodeName().equalsIgnoreCase("latitud")) {
                            String latitud = nodoActual.getChildNodes().item(0).getNodeValue();
                            localizacion.setLatitud(Double.parseDouble(latitud));
                        } else if (nodoActual.getNodeName().equalsIgnoreCase("longitud")) {
                            String longitud = nodoActual.getChildNodes().item(0).getNodeValue();
                            localizacion.setLongitud(Double.parseDouble(longitud));
                        } else if (nodoActual.getNodeName().equalsIgnoreCase("id")) {
                            String id = nodoActual.getChildNodes().item(0).getNodeValue();
                            localizacion.setId(Integer.parseInt(id));
                        }
                    }
                }
                localizaciones.add(localizacion);
            }

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return localizaciones;
    }
}
