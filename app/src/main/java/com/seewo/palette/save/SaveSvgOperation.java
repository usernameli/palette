package com.seewo.palette.save;

import android.os.Environment;

import com.seewo.palette.bean.Point;
import com.seewo.palette.bean.Shape;
import com.seewo.palette.ui.MyCanvas;
import com.seewo.palette.util.Constants;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Created by user on 2016/8/9.
 * 另存为svg格式
 */
public class SaveSvgOperation extends SaveOperation {

    List<Shape> shapeList;

    public SaveSvgOperation() {
        shapeList = new ArrayList<>();
    }

    public List<Shape> getShapeList() {
        return shapeList;
    }

    public void setShapeList(List<Shape> shapeList) {
        this.shapeList = shapeList;
    }

    @Override
    public void GetContent(MyCanvas myCanvas) {
        this.shapeList.addAll(myCanvas.getSaveShapeList());
    }

    @Override
    public String GetAbusoluteFileName() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String fileAbusoluteName = filepath + "/" + filename + ".svg";
            return fileAbusoluteName;
        } else {
            return null;
        }
    }


    @Override
    public void SavePainting() {
        try {
            // 创建一个DocumentBuilderFactory对象
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            //创建documentBuilder对象
            DocumentBuilder db = dbf.newDocumentBuilder();
            //创建document对象
            Document document = db.newDocument();
            // 创建根节点
            Element root = document.createElement("svg");
            root.setAttribute("width", "100%");
            root.setAttribute("height", "100%");
            root.setAttribute("version", "1.1");
            root.setAttribute("xmlns", "http://www.w3.org/2000/svg");
            //添加元素
            for (int i = 0; i < shapeList.size(); i++) {
                Element element = null;
                Shape shape = shapeList.get(i);
                switch (shape.GetKind()) {
                    case Constants.INK:
                        element = document.createElement("path");
                        element.setAttribute("d", ChangListToPathData(shape.getPointList()));
                        element.setAttribute("stroke-width", shape.getWidth() + "");
                        element.setAttribute("stroke", shape.getColor());
                        root.appendChild(element);
                        break;
                    case Constants.LINE:
                        element = document.createElement("line");
                        element.setAttribute("x1", shape.getPointList().get(0).getX() + "");
                        element.setAttribute("y1", shape.getPointList().get(0).getY() + "");
                        element.setAttribute("x2", shape.getPointList().get(1).getX() + "");
                        element.setAttribute("y2", shape.getPointList().get(1).getY() + "");
                        element.setAttribute("stroke-width", shape.getWidth() + "");
                        element.setAttribute("stroke", shape.getColor());
                        root.appendChild(element);
                        break;
                    case Constants.RECT:
                        element = document.createElement("rect");
                        element.setAttribute("x", shape.getPointList().get(0).getX() + "");
                        element.setAttribute("y", shape.getPointList().get(0).getY() + "");
                        element.setAttribute("width", Math.abs(shape.getPointList().get(1).getX() - shape.getPointList().get(0).getX()) + "");
                        element.setAttribute("height", Math.abs(shape.getPointList().get(1).getY() - shape.getPointList().get(0).getY()) + "");
                        element.setAttribute("stroke-width", shape.getWidth() + "");
                        element.setAttribute("stroke", shape.getColor());
                        root.appendChild(element);
                        break;
                    case Constants.CIRCLE:
                        element = document.createElement("circle");
                        element.setAttribute("cx", (shape.getPointList().get(1).getX() + shape.getPointList().get(0).getX()) / 2 + "");
                        element.setAttribute("cy", (shape.getPointList().get(1).getY() + shape.getPointList().get(0).getY()) / 2 + "");
                        element.setAttribute("r", (Math.abs(shape.getPointList().get(1).getY() - shape.getPointList().get(0).getY()) >= Math.abs(shape.getPointList().get(1).getX() - shape.getPointList().get(0).getX())
                                ? Math.abs(shape.getPointList().get(1).getX() - shape.getPointList().get(0).getX()) / 2
                                : Math.abs(shape.getPointList().get(1).getY() - shape.getPointList().get(0).getY()) / 2)
                                + "");
                        element.setAttribute("stroke-width", shape.getWidth() + "");
                        element.setAttribute("stroke", shape.getColor());
                        root.appendChild(element);
                        break;
                }
            }
            //将根节点添加到document对象中
            document.appendChild(root);
            //创建TransformerFactory对象
            TransformerFactory tff = TransformerFactory.newInstance();
            //创建Transformer对象
            Transformer transformer = tff.newTransformer();
            //设置文件换行
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            //设置文档编码
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            //设置standalone
            transformer.setOutputProperty(OutputKeys.STANDALONE, "no");
            //设置doctype
            transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "-//W3C//DTD SVG 1.1//EN");
            transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd");
            //创建一个文件对象
            File file = new File(GetAbusoluteFileName());
            //将内容通过输出流写入文件
            transformer.transform(new DOMSource(document), new StreamResult(file));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将之前保存的点集转换成path需要的数据形式
     *
     * @param pointList
     * @return
     */
    private static String ChangListToPathData(List<Point> pointList) {
        StringBuffer sb = new StringBuffer();
        sb.append("M" + pointList.get(0).getX() + " " + pointList.get(0).getY() + " ");//first
        for (int i = 1; i < pointList.size(); i++) {
            sb.append("Q" +
                    pointList.get(i - 1).getX() + " "
                    + pointList.get(i - 1).getY() + " "
                    + Math.floor((pointList.get(i - 1).getX() + pointList.get(i).getX()) / 2) + " "
                    + Math.floor((pointList.get(i - 1).getY() + pointList.get(i).getY()) / 2) + " "
                    + pointList.get(i).getX() + " "
                    + pointList.get(i).getY() + " ");//left
        }
        return sb.toString().trim();
    }
}
