package com.traceability.util;

import com.google.zxing.*;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * 二维码生成工具类
 * 使用ZXing库生成二维码和条码
 */
public class QrCodeUtil {

    private static final int DEFAULT_WIDTH = 300;
    private static final int DEFAULT_HEIGHT = 300;
    private static final String DEFAULT_FORMAT = "PNG";

    /**
     * 生成二维码图片（Base64格式）
     * @param content 二维码内容
     * @return Base64编码的图片字符串
     */
    public static String generateQrCodeBase64(String content) {
        return generateQrCodeBase64(content, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    /**
     * 生成二维码图片（Base64格式）
     * @param content 二维码内容
     * @param width 宽度
     * @param height 高度
     * @return Base64编码的图片字符串
     */
    public static String generateQrCodeBase64(String content, int width, int height) {
        try {
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            hints.put(EncodeHintType.MARGIN, 1);

            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);

            BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, DEFAULT_FORMAT, outputStream);

            byte[] imageBytes = outputStream.toByteArray();
            return "data:image/png;base64," + Base64.getEncoder().encodeToString(imageBytes);
        } catch (WriterException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 生成二维码图片字节数组
     * @param content 二维码内容
     * @param width 宽度
     * @param height 高度
     * @return 图片字节数组
     */
    public static byte[] generateQrCodeBytes(String content, int width, int height) {
        try {
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            hints.put(EncodeHintType.MARGIN, 1);

            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);

            BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, DEFAULT_FORMAT, outputStream);

            return outputStream.toByteArray();
        } catch (WriterException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 生成唯一二维码标识
     * @return 唯一标识
     */
    public static String generateUniqueQrCode() {
        return "QR" + System.currentTimeMillis() + (int)(Math.random() * 10000);
    }

    /**
     * 生成唯一条码标识
     * @return 唯一标识
     */
    public static String generateUniqueBarcode() {
        return "BC" + System.currentTimeMillis() + (int)(Math.random() * 10000);
    }

    /**
     * 生成唯一包装编号
     * @return 包装编号
     */
    public static String generatePackageNo() {
        return "PKG" + System.currentTimeMillis();
    }

    /**
     * 生成唯一批次号
     * @return 批次号
     */
    public static String generateBatchNo() {
        return "BAT" + System.currentTimeMillis();
    }
}
