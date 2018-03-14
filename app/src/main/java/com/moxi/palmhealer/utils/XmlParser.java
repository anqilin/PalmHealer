package com.moxi.palmhealer.utils;

import android.content.Context;

import com.moxi.palmhealer.beans.*;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


/**
 * Created by yinlu on 2016/4/15.
 */
public class XmlParser {

    public static List<BodyPoint> pointList = new ArrayList<>();
    public static Map<String, Disease> diseaseMap = new LinkedHashMap<>();
    public static Map<String, BodyPoint> pointMap = new LinkedHashMap<>();
    private static XmlParser xmlParse;

    public static XmlParser getInstance() {
        if (xmlParse == null) {
            xmlParse = new XmlParser();
        }
        return xmlParse;
    }

    public void parseDisease(Context context) throws Exception {
        Document document = getDocument(context.getResources().getAssets().open("disease.xml"));

        NodeList list = document.getElementsByTagName("disease");
        for (int i = 0; i < list.getLength(); i++) {
            Disease disease = new Disease();
            Element element = (Element) list.item(i);
            disease.name = element.getAttribute("name");
            disease.point = element.getElementsByTagName("point").item(0).getFirstChild().getNodeValue();
            disease.care = element.getElementsByTagName("care").item(0).getFirstChild().getNodeValue();

            NodeList picsList = element.getElementsByTagName("pics");
            Element picsElement = (Element) picsList.item(0);
            disease.picNum = picsElement.getAttribute("num");
            NodeList picList = picsElement.getElementsByTagName("pic");
            List<Disease.PicRele> picReleList = new ArrayList<>();
            for (int j = 0; j < picList.getLength(); j++) {
                Disease.PicRele picRele = new Disease.PicRele();
                Element picElement = (Element) picList.item(j);
                picRele.picName = picElement.getAttribute("picName");
                picRele.rele = picElement.getAttribute("rele");
                picReleList.add(picRele);
            }
            disease.list = (ArrayList<Disease.PicRele>) picReleList;
            diseaseMap.put(disease.name, disease);
        }
        LogUtils.debug("-----------diseaseMap=" + diseaseMap);
    }


    public void parsePoint(Context context) throws Exception {
        Document document = getDocument(context.getResources().getAssets().open("point.xml"));
        NodeList list = document.getElementsByTagName("point");
        for (int i = 0; i < list.getLength(); i++) {
            BodyPoint point = new BodyPoint();
            Element element = (Element) list.item(i);
            point.pointName = element.getAttribute("name");
            point.picName = element.getElementsByTagName("point_pic").item(0).getFirstChild().getNodeValue();
            point.location = element.getElementsByTagName("location").item(0).getFirstChild().getNodeValue();
            point.features = element.getElementsByTagName("features").item(0).getFirstChild().getNodeValue();
            pointList.add(point);
            pointMap.put(point.pointName, point);
        }
        LogUtils.debug("-----------pointList=" + pointMap);
    }

    private static Document getDocument(InputStream in) throws Exception {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder();
        return builder.parse(in);
    }


}
