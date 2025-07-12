package com.rz.manuscript.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.URL;
import java.util.*;

@Slf4j
public class Html2WordUtil {
    public static String doConvert(String input, Integer projectId) {
        String fileName = "";
        try {
            Document doc = Jsoup.parse(input, "UTF-8");

            // 创建Word文档
            XWPFDocument docx = new XWPFDocument();

            // 遍历HTML中的所有元素
            Elements elements = doc.getAllElements();
            for (Element element : elements) {
                // 处理文本元素
                if (element.tagName().equals("p")) {
                    String text = element.text();
                    XWPFParagraph paragraph = docx.createParagraph();
                    XWPFRun run = paragraph.createRun();
                    run.setText(text);
                }
                // 处理图片元素
                else if (element.tagName().equals("img")) {
                    String imgUrl = element.attr("src");
                    String imageDate = "";
                    if (imgUrl.startsWith("http")) {
                        Map map = new HashMap();
                        String domain = new URL(imgUrl).getHost();
                        map.put("Referer", "http://" + domain);
                        imageDate = doGetHeader(imgUrl, map);
                        imageDate = "data:" + getImageToBase64MimeType(imgUrl) + ";base64," + imageDate;
                    } else {
                        imageDate = imgUrl;
                    }

                    String img = imageDate.split(",")[1];
                    String imgType = imageDate.split(",")[0].split(":")[1].replace(";", "");
                    int picType = XWPFDocument.PICTURE_TYPE_JPEG;
                    switch (imgType) {
                        case "image/png":
                            picType = XWPFDocument.PICTURE_TYPE_PNG;
                            break;
                        case "image/gif":
                            picType = XWPFDocument.PICTURE_TYPE_GIF;
                            break;
                        case "image/tiff":
                            picType = XWPFDocument.PICTURE_TYPE_TIFF;
                            break;
                        default:
                            picType = XWPFDocument.PICTURE_TYPE_JPEG;
                            break;
                    }
                    byte[] imgBytes = Base64.getDecoder().decode(img);
                    //  InputStream imgStream = new FileInputStream(new File(imgUrl));

                    // imgBytes = IOUtils.toByteArray(imgStream);

                    XWPFParagraph paragraph = docx.createParagraph();
                    XWPFRun run = paragraph.createRun();
                    run.addPicture(new ByteArrayInputStream(imgBytes), picType, imgUrl, Units.toEMU(400), Units.toEMU(400));
                }
            }
            fileName = UUID.randomUUID().toString() + ".docx";
            String filePath = "D:\\test" + "\\" + projectId;
            if (System.getProperty("os.name").toLowerCase().contains("linux")) {
                filePath = "/home/file/" + projectId;
            }
            File file = new File(filePath, fileName);
            // 输出Word文档
            FileOutputStream out = new FileOutputStream(file);
            docx.write(out);
            out.close();

        } catch (IOException | InvalidFormatException e) {
            throw new RuntimeException(e);
        }
        return fileName;
    }

    public static String getImageDataFromUrl(String imgUrl) {
        String imageDate = "";
        try {

            if (imgUrl != null && imgUrl.startsWith("http")) {
                Map map = new HashMap();
                String domain = new URL(imgUrl).getHost();
                map.put("Referer", "http://" + domain);
                imageDate = doGetHeader(imgUrl, map);
                imageDate = "data:" + getImageToBase64MimeType(imgUrl) + ";base64," + imageDate;
            }
        } catch (Exception e) {
            log.error("获取网络图片失败,"+imgUrl, e);
        }
        return imageDate;
    }

    /**
     * @param url 地址
     * @param
     * @Description HTTP header GET请求图片地址 返回base64
     */
    public static String doGetHeader(String url, Map<String, String> headers) {

        CloseableHttpResponse response = null;
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(url);
            Iterator<Map.Entry<String, String>> iterator = headers.entrySet().iterator();
            //设置header信息
            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                httpGet.setHeader(entry.getKey(), entry.getValue());
            }
            RequestConfig config = RequestConfig.custom().setConnectTimeout(60000)
                    .setConnectionRequestTimeout(60000)
                    .setSocketTimeout(60000)
                    .build();
            httpGet.setConfig(config);
            response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            entity.writeTo(out);
            // ByteArrayOutputStream编码成base64字符串
            String result = new String(Base64.getEncoder().encode(out.toByteArray()));
            return result;
        } catch (Exception e) {
            System.out.println("httpClient请求图片url报错 " + e.getMessage());
            ;
            return null;
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (Exception e) {
                System.out.println("关闭响应流报错：" + e.getMessage());
            }
        }
    }

    public static String getImageToBase64MimeType(String imagePath) {
        String mimeType = "";
        String extension = imagePath.substring(imagePath.lastIndexOf(".") + 1);
        extension = extension.split("@")[0];
        switch (extension.toLowerCase()) {
            case "jpg":
            case "jpeg":
                mimeType = "image/jpeg";
                break;
            case "png":
                mimeType = "image/png";
                break;
            case "gif":
                mimeType = "image/gif";
                break;
            case "webp":
                mimeType = "image/webp";
                break;
            case "tiff":
                mimeType = "image/tiff";
                break;
            default:
                throw new IllegalArgumentException("Unsupported image format: " + extension);
        }
        return mimeType;
    }

    private static String convertImageToBase64(String imagePath, String base64String) {
        String extension = imagePath.substring(imagePath.lastIndexOf(".") + 1);
        String mimeType = "";
        switch (extension.toLowerCase()) {
            case "jpg":
            case "jpeg":
                mimeType = "image/jpeg";
                break;
            case "png":
                mimeType = "image/png";
                break;
            case "gif":
                mimeType = "image/gif";
                break;
            case "webp":
                mimeType = "image/webp";
                break;
            default:
                throw new IllegalArgumentException("Unsupported image format: " + extension);
        }
        return "data:" + mimeType + ";base64," + base64String;
    }
}
