package com.xiawei.testruoyi.learn.base.util;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.xiawei.testruoyi.learn.base.enums.ImgTypeEnum;
import com.xiawei.testruoyi.learn.base.exceptions.SystemException;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class ImgUtil {


    /**
     * 获取Base64编码图片的类型
     * <p>
     * 该方法支持两种Base64格式：
     * 1. Data URI格式（data:image/png;base64,xxx）：直接从MIME类型提取
     * 2. 纯Base64编码：解码后通过文件头特征识别图片类型
     * </p>
     *
     * @param base64Img Base64编码的图片字符串，支持Data URI格式或纯Base64格式
     * @return 图片类型枚举，识别失败时返回 {@link ImgTypeEnum#UNKNOWN}
     */
    public static ImgTypeEnum getBase64ImgType(String base64Img) {
        // 参数校验：空值直接返回未知类型
        if (ObjectUtil.isEmpty(base64Img)) {
            return ImgTypeEnum.UNKNOWN;
        }

        // 如果是Data URI格式（data:image/png;base64,xxx），直接提取MIME类型
        if (StrUtil.startWithIgnoreCase(base64Img, "data:")) {
            String mime = StrUtil.subBetween(base64Img, "data:", ";");
            return ImgTypeEnum.getByMime(mime);
        }

        // 纯Base64格式：解码后通过文件头特征识别
        byte[] bytes = Base64.decode(base64Img);
        try (InputStream imgInputStream = IoUtil.toStream(bytes)) {
            String ext = FileTypeUtil.getType(imgInputStream);
            return ImgTypeEnum.getByExt(ext);
        } catch (IOException e) {
            log.error("获取图片类型失败", e);
            return ImgTypeEnum.UNKNOWN;
        }
    }


    /**
     * 从Base64编码字符串中解析并获取图片对象
     * <p>
     * 该方法支持两种Base64格式：
     * 1. Data URI格式（data:image/png;base64,xxx）：自动剥离前缀
     * 2. 纯Base64编码：直接解码
     * </p>
     *
     * @param base64Img Base64编码的图片字符串，支持Data URI格式或纯Base64格式
     * @return 解析后的BufferedImage图片对象
     * @throws IOException     当图片解析失败时抛出（如：格式不支持、数据损坏等）
     * @throws SystemException 当Base64图片参数为空时抛出
     */
    public static BufferedImage getImageFromBase64Url(String base64Img) throws IOException {
        // 参数校验：Base64图片不能为空
        Assert.notEmpty(base64Img, () -> new SystemException("Base64图片不能为空"));

        // 提取纯Base64数据（去除Data URI前缀）
        String pureBase64 = base64Img;
        if (StrUtil.startWithIgnoreCase(base64Img, "data:")) {
            pureBase64 = StrUtil.subAfter(base64Img, "base64,", false);
        }

        // 解码Base64为字节数组
        byte[] imageBytes = Base64.decode(pureBase64);

        // 将字节数组转换为BufferedImage对象
        try (ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes)) {
            return ImageIO.read(bis);
        }
    }


    /**
     * 根据图片URL获取Base64编码字符串
     * <p>
     * 该方法会下载指定URL的图片，并转换为Base64编码字符串。
     * 支持http/https协议的图片URL。
     * </p>
     *
     * @param imgUrl 图片URL地址
     * @return Base64编码的图片字符串（Data URI格式，包含data:image/xxx;base64,前缀）
     * @throws SystemException 当URL为空、下载失败或图片格式不支持时抛出
     */
    public static String getBase64FromUrl(String imgUrl) {
        // 参数校验
        Assert.notEmpty(imgUrl, () -> new SystemException("图片URL不能为空"));
        try {
            // 下载图片文件
            byte[] imageBytes = HttpUtil.downloadBytes(imgUrl);
            Assert.isTrue(ObjectUtil.isNotNull(imageBytes) && imageBytes.length > 0, () -> new SystemException("下载图片失败，返回数据为空"));

            // 识别图片类型
            ImgTypeEnum imgType;
            try (InputStream imgInputStream = IoUtil.toStream(imageBytes)) {
                String ext = FileTypeUtil.getType(imgInputStream);
                imgType = ImgTypeEnum.getByExt(ext);
            }

            // 如果无法识别图片类型，默认使用PNG
            if (imgType == ImgTypeEnum.UNKNOWN) {
                imgType = ImgTypeEnum.PNG;
                log.warn("无法识别图片类型，URL：{}，默认使用PNG格式", imgUrl);
            }

            // 将字节数组转换为Base64编码
            String base64Data = Base64.encode(imageBytes);

            // 拼接Data URI前缀
            return StrUtil.format("data:{};base64,{}", imgType.getMime(), base64Data);
        } catch (Exception e) {
            log.error("从URL获取Base64图片失败，URL：{}", imgUrl, e);
            throw new SystemException(StrUtil.format("获取图片失败：{}", e.getMessage()));
        }
    }


    public static BufferedImage cropImage(BufferedImage image, int x, int y, int width, int height) {
        log.info("imageWith={}, imageHeight={}", image.getWidth(), image.getHeight());
        // 参数校验
        Assert.notNull(image, () -> new SystemException("图片不能为空"));
        Assert.isTrue(x >= 0 && y >= 0 && width > 0 && height > 0, () -> new SystemException("参数错误：x,y,width,height不能小于0"));
        Assert.isTrue(x + width <= image.getWidth() && y + height <= image.getHeight(), () -> new SystemException("参数错误：截取区域超出图片范围"));

        // 校验截取区域是否超出图片范围
        int imgWidth = image.getWidth();
        int imgHeight = image.getHeight();
        Assert.isTrue(x + width <= imgWidth && y + height <= imgHeight,
                () -> new SystemException(StrUtil.format("截取区域超出图片范围，图片尺寸：{}x{}，截取区域：({},{}) - {}x{}", imgWidth, imgHeight, x, y, width, height)));
        // 截取图片区域并返回
        return image.getSubimage(x, y, width, height);
    }


    /**
     * 截取Base64图片的部分区域
     * <p>
     * 该方法支持两种Base64格式：
     * 1. Data URI格式（data:image/png;base64,xxx）：自动识别MIME类型
     * 2. 纯Base64编码：通过文件头识别图片类型
     * </p>
     *
     * @param base64Img Base64编码的图片字符串
     * @param x         截取区域左上角的x坐标（相对于原图）
     * @param y         截取区域左上角的y坐标（相对于原图）
     * @param width     截取区域的宽度
     * @param height    截取区域的高度
     * @return 截取后的图片Base64字符串（Data URI格式，包含data:image/xxx;base64,前缀）
     * @throws SystemException 当图片解析失败或截取区域超出图片范围时抛出
     */
    public static String cropBase64Image(String base64Img, int x, int y, int width, int height) throws IOException {

        // 参数校验
        Assert.notEmpty(base64Img, () -> new SystemException("Base64图片不能为空"));
        Assert.isTrue(x >= 0 && y >= 0, () -> new SystemException("截取坐标不能为负数"));
        Assert.isTrue(width > 0 && height > 0, () -> new SystemException("截取区域宽高必须大于0"));

        BufferedImage originalImage = getImageFromBase64Url(base64Img);
        BufferedImage croppedImage = cropImage(originalImage, x, y, width, height);

        // 获取原图片格式，默认使用PNG
        ImgTypeEnum imgType = getBase64ImgType(base64Img);
        if (imgType == ImgTypeEnum.UNKNOWN) {
            imgType = ImgTypeEnum.PNG;
            log.warn("无法识别图片类型，默认使用PNG格式");
        }
        String formatName = imgType.getExt();

        return img2Base64(croppedImage, formatName, imgType);
    }


    /**
     * 将BufferedImage图片对象转换为Base64编码字符串
     * <p>
     * 该方法会将图片对象编码为指定格式（如PNG、JPEG等），并转换为Base64字符串。
     * 返回的字符串为Data URI格式，包含MIME类型前缀，可直接用于HTML或Web传输。
     * </p>
     *
     * @param croppedImage 需要转换的图片对象
     * @param formatName   图片编码格式（如：png、jpg、gif等）
     * @param imgType      图片类型枚举，用于获取对应的MIME类型
     * @return Base64编码的图片字符串（Data URI格式，如：data:image/png;base64,iVBORw0KG...）
     * @throws IOException 当图片编码写入失败时抛出
     */
    private static String img2Base64(BufferedImage croppedImage, String formatName, ImgTypeEnum imgType) throws IOException {
        // 将图片对象编码为字节数组
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            // 将BufferedImage按指定格式写入输出流
            ImageIO.write(croppedImage, formatName, bos);

            // 获取编码后的字节数组
            byte[] croppedBytes = bos.toByteArray();

            // 将字节数组转换为Base64编码字符串
            String base64Data = Base64.encode(croppedBytes);

            // 拼接Data URI前缀（格式：data:image/xxx;base64,xxx）
            return StrUtil.format("data:{};base64,{}", imgType.getMime(), base64Data);
        }
    }
}
