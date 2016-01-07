package me.utils.log;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This is a Log tool，with this you can the following
 * <ol>
 * <li>use KLog.i(),you could print whether the method execute,and the default tag is current class's name</li>
 * <li>use KLog.i(msg),you could print log as before,and you could location the method with a click in Android Studio Logcat</li>
 * <li>use KLog.json(),you could print json string with well format automatic</li>
 * </ol>
 *
 * @author zhaokaiqiang
 * @Modifier wangrenxing
 */
public class KLog {

    public static final String DEFAULT_MESSAGE = "execute";
    public static final String LINE_SEPARATOR = System.getProperty("line.separator");
    public static final String NULL_TIPS = "Log with null object";
    public static final String PARAM = "Param";
    public static final String NULL = "null";

    public static final int JSON_INDENT = 4;

    public static final int V = 0x1;
    public static final int D = 0x2;
    public static final int I = 0x3;
    public static final int W = 0x4;
    public static final int E = 0x5;
    public static final int A = 0x6;
    public static final int JSON = 0x7;
    public static final int XML = 0x8;

    private static boolean IS_SHOW_LOG = true;

    public static void init(boolean isShowLog) {
        IS_SHOW_LOG = isShowLog;
    }

    public static void v() {
        printLog(V, null, DEFAULT_MESSAGE);
    }

    public static void v(Object msg) {
        printLog(V, null, msg);
    }

    public static void v(String tag, Object... objects) {
        printLog(V, tag, objects);
    }

    public static void d() {
        printLog(D, null, DEFAULT_MESSAGE);
    }

    public static void d(Object msg) {
        printLog(D, null, msg);
    }

    public static void d(String tag, Object... objects) {
        printLog(D, tag, objects);
    }

    public static void i() {
        printLog(I, null, DEFAULT_MESSAGE);
    }

    public static void i(Object msg) {
        printLog(I, null, msg);
    }

    public static void i(String tag, Object... objects) {
        printLog(I, tag, objects);
    }

    public static void w() {
        printLog(W, null, DEFAULT_MESSAGE);
    }

    public static void w(Object msg) {
        printLog(W, null, msg);
    }

    public static void w(String tag, Object... objects) {
        printLog(W, tag, objects);
    }

    public static void e() {
        printLog(E, null, DEFAULT_MESSAGE);
    }

    public static void e(Object msg) {
        printLog(E, null, msg);
    }

    public static void e(String tag, Object... objects) {
        printLog(E, tag, objects);
    }

    public static void a() {
        printLog(A, null, DEFAULT_MESSAGE);
    }

    public static void a(Object msg) {
        printLog(A, null, msg);
    }

    public static void a(String tag, Object... objects) {
        printLog(A, tag, objects);
    }

    public static void json(String jsonFormat) {
        printLog(JSON, null, jsonFormat);
    }

    public static void json(String tag, String jsonFormat) {
        printLog(JSON, tag, jsonFormat);
    }

    public static void xml(String xml) {
        printLog(XML, null, xml);
    }

    public static void xml(String tag, String xml) {
        printLog(XML, tag, xml);
    }

    public static void file(File targetDirectory, Object msg) {
        printFile(null, targetDirectory, null, msg);
    }

    public static void file(String tag, File targetDirectory, Object msg) {
        printFile(tag, targetDirectory, null, msg);
    }

    public static void file(String tag, File targetDirectory, String fileName, Object msg) {
        printFile(tag, targetDirectory, fileName, msg);
    }

    private static void printLog(int type, String tagStr, Object... objects) {
        if (!IS_SHOW_LOG) {
            return;
        }

        String[] contents = wrapperContent(tagStr, objects);
        String tag = contents[0];
        String msg = contents[1];
        String headString = contents[2];

        switch (type) {
            case V:
            case D:
            case I:
            case W:
            case E:
            case A:
                BaseLog.printDefault(type, tag, headString + msg);
                break;
            case JSON:
                JsonLog.printJson(tag, msg, headString);
                break;
            case XML:
                XmlLog.printXml(tag, msg, headString);
                break;
        }
    }


    private static void printFile(String tagStr, File targetDirectory, String fileName, Object objectMsg) {

        if (!IS_SHOW_LOG) {
            return;
        }

        String[] contents = wrapperContent(tagStr, objectMsg);
        String tag = contents[0];
        String msg = contents[1];
        String headString = contents[2];

        FileLog.printFile(tag, targetDirectory, fileName, headString, msg);
    }

    private static String[] wrapperContent(String tagStr, Object... objects) {

        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        int index = 5;
        String className = stackTrace[index].getFileName();
        String methodName = stackTrace[index].getMethodName();
        int lineNumber = stackTrace[index].getLineNumber();
        String methodNameShort = methodName.substring(0, 1).toUpperCase() + methodName.substring(1);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[ (").append(className).append(":").append(lineNumber).append(")#").append(methodNameShort).append(" ] ");

        String tag = (tagStr == null ? className : tagStr);
        String msg = (objects == null) ? NULL_TIPS : getObjectsString(objects);
        String headString = stringBuilder.toString();

        return new String[]{tag, msg, headString};
    }

    private static String getObjectsString(Object... objects) {

        if (objects.length > 1) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("\n");
            for (int i = 0; i < objects.length; i++) {
                Object object = objects[i];
                if (object == null) {
                    stringBuilder.append(PARAM).append("[").append(i).append("]").append(" = ").append(NULL).append("\n");
                } else {
                    stringBuilder.append(PARAM).append("[").append(i).append("]").append(" = ").append(object.toString()).append("\n");
                }
            }
            return stringBuilder.toString();
        } else {
            Object object = objects[0];
            return object == null ? NULL : object.toString();
        }
    }
}

class BaseLog {
    public static void printDefault(int type, String tag, String msg) {
        int index = 0;
        int maxLength = 4000;
        int countOfSub = msg.length() / maxLength;
        if (countOfSub > 0) {
            for (int i = 0; i < countOfSub; i++) {
                String sub = msg.substring(index, index + maxLength);
                printSub(type, tag, sub);
                index += maxLength;
            }
            printSub(type, tag, msg.substring(index, msg.length()));
        } else {
            printSub(type, tag, msg);
        }
    }
    private static void printSub(int type, String tag, String sub) {
        switch (type) {
            case KLog.V:
                Log.v(tag, sub);
                break;
            case KLog.D:
                Log.i(tag, sub);
                break;
            case KLog.I:
                Log.i(tag, sub);
                break;
            case KLog.W:
                Log.w(tag, sub);
                break;
            case KLog.E:
                Log.e(tag, sub);
                break;
            case KLog.A:
                Log.wtf(tag, sub);
                break;
        }
    }
}

class JsonLog {
    public static void printJson(String tag, String msg, String headString) {
        String message;
        try {
            if (msg.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(msg);
                message = jsonObject.toString(KLog.JSON_INDENT);
            } else if (msg.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(msg);
                message = jsonArray.toString(KLog.JSON_INDENT);
            } else {
                message = msg;
            }
        } catch (JSONException e) {
            message = msg;
        }
        Util.printLine(tag, true);
        message = headString + KLog.LINE_SEPARATOR + message;
        String[] lines = message.split(KLog.LINE_SEPARATOR);
        for (String line : lines) {
            Log.i(tag, "║ " + line);
        }
        Util.printLine(tag, false);
    }
}

class XmlLog{
    public static void printXml(String tag, String xml,String headString) {
        if (xml != null) {
            xml = XmlLog.formatXML(xml);
            xml = headString + "\n" + xml;
        } else {
            xml = headString + KLog.NULL_TIPS;
        }

        Util.printLine(tag, true);
        String[] lines = xml.split(KLog.LINE_SEPARATOR);
        for (String line : lines) {
            if (!Util.isEmpty(line)) {
                Log.i(tag, "║ " + line);
            }
        }
        Util.printLine(tag, false);
    }

    private static InputStream getStringStream(String sInputString){
        if (sInputString != null && !sInputString.trim().equals("")){
            try{
                ByteArrayInputStream tInputStringStream = new ByteArrayInputStream(sInputString.getBytes());
                return tInputStringStream;
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
        return null;
    }

    private static String getMultiIndent(int n, String indent) {
        StringBuilder sb = new StringBuilder();
        for( int i=0; i<n; i++ ) {
            sb.append(indent);
        }
        return sb.toString();
    }
    private static String removeSpaces(String s) {
        s = s.trim();
        int len = s.length();
        int k = 0;
        for( int i=0; i<len; i++ ) {
            if( s.charAt(k) == '\t' || s.charAt(k) == '\n' || s.charAt(k) == ' ' ) {
                k++;
            }
        }
        if( k > 0 ) {
            s = s.substring(k);
        }
        return s;
    }
    public static String formatXML(String inputXML) {
        try {
            inputXML = removeSpaces(inputXML);
            List<String> nameSpaceList = new ArrayList<String>();
            final String indent = "    ";
            XmlPullParser xmlParser = Xml.newPullParser();
            xmlParser.setInput(getStringStream(inputXML), "utf-8");
            StringBuilder stringBuilder = new StringBuilder();
            if( inputXML.startsWith("<?") ) {
                final int index = inputXML.indexOf("?>");
                if( index > 1 ) {
                    stringBuilder.append(inputXML.substring(0, index+2)).append("\n");
                    inputXML = inputXML.substring(index+2);
                }
            }
            int evtType=xmlParser.getEventType();
            while( evtType!=XmlPullParser.END_DOCUMENT ){
                switch( evtType ){
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.COMMENT:
                        stringBuilder
                                .append("\n")
                                .append(getMultiIndent(xmlParser.getDepth(), indent))
                                .append("<!-- ")
                                .append(xmlParser.getText())
                                .append(" -->")
                                .append("\n");
                        break;
                    case XmlPullParser.START_TAG:
                        final int depth = xmlParser.getDepth();
                        stringBuilder.append("\n")
                                .append(getMultiIndent(depth-1, indent))
                                .append("<")
                                .append(xmlParser.getName());

                        // 命名空间
                        final int count = xmlParser.getNamespaceCount(depth);
                        for( int i=0; i<count; i++ ) {
                            String prefix = xmlParser.getNamespacePrefix(i);
                            String ns = xmlParser.getNamespace(prefix);
                            if( !nameSpaceList.contains(ns) ) {
                                stringBuilder
                                        .append("\n")
                                        .append(getMultiIndent(depth, indent))
                                        .append("xmlns:")
                                        .append(prefix)
                                        .append("=")
                                        .append(ns);
                                nameSpaceList.add(ns);
                            }
                        }
                        // 属性
                        int size = xmlParser.getAttributeCount();
                        for( int i=0; i<size; i++ ) {
                            final String prefix = xmlParser.getAttributePrefix(i);
                            stringBuilder
                                    .append("\n")
                                    .append(getMultiIndent(depth, indent));
                            if( !TextUtils.isEmpty(prefix) ) {
                                stringBuilder.append(prefix)
                                        .append(":");
                            }
                            stringBuilder.append(xmlParser.getAttributeName(i))
                                    .append("=\"")
                                    .append(xmlParser.getAttributeValue(i))
                                    .append("\"");

                        }
                        stringBuilder.append(">");
                        break;
                    case XmlPullParser.TEXT:
                        stringBuilder
                                .append("\n")
                                .append(getMultiIndent(xmlParser.getDepth(), indent))
                                .append(xmlParser.getText());
                        break;
                    case XmlPullParser.END_TAG:
                        stringBuilder
                                .append("\n")
                                .append(getMultiIndent(xmlParser.getDepth()-1, indent))
                                .append("</")
                                .append(xmlParser.getName()).append(">\n");
                        break;
                    default:
                        break;
                }
                evtType=xmlParser.nextToken();
            }
            return stringBuilder.toString();
        } catch (XmlPullParserException e) {
            return inputXML;
        } catch (IOException e1) {
            return inputXML;
        } catch (Exception e) {
            return inputXML;
        }
    }
    /*
    public static String formatXML(String inputXML) {
        XMLWriter writer = null;
        String requestXML = null;
        try {
            SAXReader reader = new SAXReader();
            Document document = reader.read(new StringReader(inputXML));
            StringWriter stringWriter = new StringWriter();
            OutputFormat format = new OutputFormat(" ", true);
            writer = new XMLWriter(stringWriter, format);
            writer.write(document);
            writer.flush();
            requestXML = stringWriter.getBuffer().toString();
        } catch (IOException e) {
            return inputXML;
        } catch (DocumentException e) {
            return inputXML;
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    return inputXML;
                }
            }
        }
        return requestXML;
    }*/

}

class FileLog{
    public static void printFile(String tag, File targetDirectory, String fileName, String headString, String msg) {
        fileName = (fileName == null) ? getFileName() : fileName;
        if (save(targetDirectory, fileName, msg)) {
            Log.i(tag, headString + " save log success ! location is >>>" + targetDirectory.getAbsolutePath() + "/" + fileName);
        } else {
            Log.e(tag, headString + "save log fails !");
        }
    }

    private static boolean save(File dic, String fileName, String msg) {
        if( !dic.exists() ) {
            dic.mkdir();
        }
        File file = new File(dic, fileName);
        try {
            if( !file.exists() ) {
                file.createNewFile();
            }
            OutputStream outputStream = new FileOutputStream(file);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
            outputStreamWriter.write(msg);
            outputStreamWriter.flush();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private static String getFileName() {
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder("KLog_");
        stringBuilder.append(Long.toString(System.currentTimeMillis() + random.nextInt(10000)).substring(4));
        stringBuilder.append(".txt");
        return stringBuilder.toString();
    }

}

class Util {
    public static boolean isEmpty(String line) {
        return TextUtils.isEmpty(line) || line.equals("\n") || line.equals("\t") || TextUtils.isEmpty(line.trim());
    }
    public static void printLine(String tag, boolean isTop) {
        if (isTop) {
            Log.i(tag, "╔═══════════════════════════════════════════════════════════════════════════════════════");
        } else {
            Log.i(tag, "╚═══════════════════════════════════════════════════════════════════════════════════════");
        }
    }
}