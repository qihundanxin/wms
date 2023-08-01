package com.deer.wms.project.root.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.lang.ObjectUtils;
import org.krysalis.barcode4j.HumanReadablePlacement;
import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import sun.misc.BASE64Encoder;

public class BarCodeUtils {

    /**
     * 生成二维码base64
     * 
     * @param contents
     * @param width
     * @param height
     * @return
     */
    public static String encode(String contents, int width, int height) {
        Map<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
        // 设置二维码四周的留白
        hints.put(EncodeHintType.MARGIN, 2);
        // 指定纠错等级
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.Q);
        // 指定编码格式
        hints.put(EncodeHintType.CHARACTER_SET, "UTF8");
        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(contents, BarcodeFormat.QR_CODE, width, height, hints);
            // MatrixToImageWriter.writeToStream(bitMatrix, "png", new FileOutputStream(imgPath));

            ByteArrayOutputStream baos = new ByteArrayOutputStream();// io流
            ImageIO.write(MatrixToImageWriter.toBufferedImage(bitMatrix), "png", baos);// 写入流中
            byte[] bytes = baos.toByteArray();// 转换成字节
            BASE64Encoder encoder = new BASE64Encoder();
            String png_base64 = encoder.encodeBuffer(bytes).trim();// 转换成base64串
            png_base64 = "data:image/png;base64," + png_base64.replaceAll("\n", "").replaceAll("\r", "");// 删除 \r\n
            return png_base64;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static byte[] generateBarCode128(String message, Double height, Double width, boolean withQuietZone, boolean hideText) {
        Code128Bean bean = new Code128Bean();
        int dpi = 514;// 分辨率
        bean.doQuietZone(withQuietZone);// 设置两侧是否留白
        bean.setBarHeight((double) ObjectUtils.defaultIfNull(height, 9.0D));// 设置条形码高度和宽度
        if (width != null) {
            bean.setModuleWidth(width);// 设置文本位置(包括是否显示)
        }
        if (true) {
            bean.setMsgPosition(HumanReadablePlacement.HRP_NONE);
        }
        String format = "image/png";// 设置图片类型
        ByteArrayOutputStream ous = new ByteArrayOutputStream();
        BitmapCanvasProvider canvas = new BitmapCanvasProvider(ous, format, dpi, BufferedImage.TYPE_BYTE_BINARY, false, 0);
        bean.generateBarcode(canvas, message);// 生产条形码
        try {
            canvas.finish();
        } catch (IOException e) {}
        return ous.toByteArray();
    }

    /**
     * 生成code128条形码
     * 
     * @param content
     * @param height
     * @param width
     * @return
     */
    public static String barCode(String content, Double height, Double width) {
        byte[] bytes = generateBarCode128(content, height, width, true, false);
        BASE64Encoder encoder = new BASE64Encoder();
        String png_base64 = encoder.encodeBuffer(bytes).trim();// 转换成base64串
        png_base64 = "data:image/png;base64," + png_base64.replaceAll("\n", "").replaceAll("\r", "");// 删除 \r\n
        return png_base64;
    }
}
