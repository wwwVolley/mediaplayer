package aiyzp.com.medialib;

import android.text.TextUtils;
import master.flame.danmaku.danmaku.model.AlphaValue;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.Duration;
import master.flame.danmaku.danmaku.model.IDisplayer;
import master.flame.danmaku.danmaku.model.android.DanmakuFactory;
import master.flame.danmaku.danmaku.model.android.Danmakus;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;
import master.flame.danmaku.danmaku.parser.android.AndroidFileSource;
import master.flame.danmaku.danmaku.util.DanmakuUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.IOException;
import java.util.Locale;

public class BiliDanmukuParser extends BaseDanmakuParser {
    private float mDispScaleX;
    private float mDispScaleY;

    public BiliDanmukuParser() {
    }

    public Danmakus parse() {
        if (this.mDataSource != null) {
            AndroidFileSource source = (AndroidFileSource)this.mDataSource;

            try {
                XMLReader xmlReader = XMLReaderFactory.createXMLReader();
                XmlContentHandler contentHandler = new XmlContentHandler();
                xmlReader.setContentHandler(contentHandler);
                xmlReader.parse(new InputSource(source.data()));
                return contentHandler.getResult();
            } catch (SAXException var4) {
                var4.printStackTrace();
            } catch (IOException var5) {
                var5.printStackTrace();
            }
        }

        return null;
    }

    private boolean isPercentageNumber(float number) {
        return number >= 0.0F && number <= 1.0F;
    }

    public BaseDanmakuParser setDisplayer(IDisplayer disp) {
        super.setDisplayer(disp);
        this.mDispScaleX = (float)this.mDispWidth / 682.0F;
        this.mDispScaleY = (float)this.mDispHeight / 438.0F;
        return this;
    }

    static {
        System.setProperty("org.xml.sax.driver", "org.xmlpull.v1.sax2.Driver");
    }

    public class XmlContentHandler extends DefaultHandler {
        private static final String TRUE_STRING = "true";
        public Danmakus result = null;
        public BaseDanmaku item = null;
        public boolean completed = false;
        public int index = 0;

        public XmlContentHandler() {
        }

        public Danmakus getResult() {
            return this.result;
        }

        public void startDocument() throws SAXException {
            this.result = new Danmakus();
        }

        public void endDocument() throws SAXException {
            this.completed = true;
        }

        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            String tagName = localName.length() != 0 ? localName : qName;
            tagName = tagName.toLowerCase(Locale.getDefault()).trim();
            if (tagName.equals("d")) {
                String pValue = attributes.getValue("p");
                String[] values = pValue.split(",");
                if (values.length > 0) {
                    long time = (long)(Float.parseFloat(values[0]) * 1000.0F);
                    int type = Integer.parseInt(values[1]);
                    float textSize = Float.parseFloat(values[2]);
                    int color = (int)((-16777216L | Long.parseLong(values[3])) & -1L);
                    this.item = BiliDanmukuParser.this.mContext.mDanmakuFactory.createDanmaku(type, BiliDanmukuParser.this.mContext);
                    if (this.item != null) {
                        this.item.setTime(time);
                        this.item.textSize = textSize * (BiliDanmukuParser.this.mDispDensity - 0.6F);
                        this.item.textColor = color;
                        this.item.textShadowColor = color <= -16777216 ? -1 : -16777216;
                    }
                }
            }

        }

        public void endElement(String uri, String localName, String qName) throws SAXException {
            if (this.item != null) {
                if (this.item.duration != null) {
                    String tagName = localName.length() != 0 ? localName : qName;
                    if (tagName.equalsIgnoreCase("d")) {
                        this.item.setTimer(BiliDanmukuParser.this.mTimer);
                        this.result.addItem(this.item);
                    }
                }

                this.item = null;
            }

        }

        public void characters(char[] ch, int start, int length) {
            if (this.item != null) {
                DanmakuUtils.fillText(this.item, this.decodeXmlString(new String(ch, start, length)));
                this.item.index = this.index++;
                String text = String.valueOf(this.item.text).trim();
                if (this.item.getType() == 7 && text.startsWith("[") && text.endsWith("]")) {
                    String[] textArr = null;

                    try {
                        JSONArray jsonArray = new JSONArray(text);
                        textArr = new String[jsonArray.length()];

                        for(int ix = 0; ix < textArr.length; ++ix) {
                            textArr[ix] = jsonArray.getString(ix);
                        }
                    } catch (JSONException var26) {
                        var26.printStackTrace();
                    }

                    if (textArr == null || textArr.length < 5) {
                        this.item = null;
                        return;
                    }

                    this.item.text = textArr[4];
                    float beginX = Float.parseFloat(textArr[0]);
                    float beginY = Float.parseFloat(textArr[1]);
                    float endX = beginX;
                    float endY = beginY;
                    String[] alphaArr = textArr[2].split("-");
                    int beginAlpha = (int)((float)AlphaValue.MAX * Float.parseFloat(alphaArr[0]));
                    int endAlpha = beginAlpha;
                    if (alphaArr.length > 1) {
                        endAlpha = (int)((float)AlphaValue.MAX * Float.parseFloat(alphaArr[1]));
                    }

                    long alphaDuraion = (long)(Float.parseFloat(textArr[3]) * 1000.0F);
                    long translationDuration = alphaDuraion;
                    long translationStartDelay = 0L;
                    float rotateY = 0.0F;
                    float rotateZ = 0.0F;
                    if (textArr.length >= 7) {
                        rotateZ = Float.parseFloat(textArr[5]);
                        rotateY = Float.parseFloat(textArr[6]);
                    }

                    if (textArr.length >= 11) {
                        endX = Float.parseFloat(textArr[7]);
                        endY = Float.parseFloat(textArr[8]);
                        if (!"".equals(textArr[9])) {
                            translationDuration = (long)Integer.parseInt(textArr[9]);
                        }

                        if (!"".equals(textArr[10])) {
                            translationStartDelay = (long)Float.parseFloat(textArr[10]);
                        }
                    }

                    if (BiliDanmukuParser.this.isPercentageNumber(beginX)) {
                        beginX *= 682.0F;
                    }

                    if (BiliDanmukuParser.this.isPercentageNumber(beginY)) {
                        beginY *= 438.0F;
                    }

                    if (BiliDanmukuParser.this.isPercentageNumber(endX)) {
                        endX *= 682.0F;
                    }

                    if (BiliDanmukuParser.this.isPercentageNumber(endY)) {
                        endY *= 438.0F;
                    }

                    this.item.duration = new Duration(alphaDuraion);
                    this.item.rotationZ = rotateZ;
                    this.item.rotationY = rotateY;
                    BiliDanmukuParser.this.mContext.mDanmakuFactory.fillTranslationData(this.item, beginX, beginY, endX, endY, translationDuration, translationStartDelay, BiliDanmukuParser.this.mDispScaleX, BiliDanmukuParser.this.mDispScaleY);
                    BiliDanmukuParser.this.mContext.mDanmakuFactory.fillAlphaData(this.item, beginAlpha, endAlpha, alphaDuraion);
                    if (textArr.length >= 12 && !TextUtils.isEmpty(textArr[11]) && "true".equals(textArr[11])) {
                        this.item.textShadowColor = 0;
                    }

                    if (textArr.length >= 13) {
                        ;
                    }

                    if (textArr.length >= 14) {
                        ;
                    }

                    if (textArr.length >= 15 && !"".equals(textArr[14])) {
                        String motionPathString = textArr[14].substring(1);
                        String[] pointStrArray = motionPathString.split("L");
                        if (pointStrArray != null && pointStrArray.length > 0) {
                            float[][] points = new float[pointStrArray.length][2];

                            for(int i = 0; i < pointStrArray.length; ++i) {
                                String[] pointArray = pointStrArray[i].split(",");
                                points[i][0] = Float.parseFloat(pointArray[0]);
                                points[i][1] = Float.parseFloat(pointArray[1]);
                            }

                            DanmakuFactory var10000 = BiliDanmukuParser.this.mContext.mDanmakuFactory;
                            DanmakuFactory.fillLinePathData(this.item, points, BiliDanmukuParser.this.mDispScaleX, BiliDanmukuParser.this.mDispScaleY);
                        }
                    }
                }
            }

        }

        private String decodeXmlString(String title) {
            if (title.contains("&amp;")) {
                title = title.replace("&amp;", "&");
            }

            if (title.contains("&quot;")) {
                title = title.replace("&quot;", "\"");
            }

            if (title.contains("&gt;")) {
                title = title.replace("&gt;", ">");
            }

            if (title.contains("&lt;")) {
                title = title.replace("&lt;", "<");
            }

            return title;
        }
    }
}
