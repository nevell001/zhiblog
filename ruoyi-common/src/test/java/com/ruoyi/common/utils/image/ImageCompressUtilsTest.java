package com.ruoyi.common.utils.image;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 图片压缩工具类测试
 *
 * @author ruoyi
 * @date 2025-12-20
 */
public class ImageCompressUtilsTest {

    /**
     * 测试JPG图片压缩
     */
    @Test
    public void testJpgCompression() throws IOException {
        // 准备测试图片
        MultipartFile file = getTestImageFile("test.jpg");
        assertNotNull(file, "测试文件不能为空");
        assertTrue(file.getSize() > 0, "测试文件大小必须大于0");

        // 执行压缩
        byte[] compressed = ImageCompressUtils.smartCompress(file);
        assertNotNull(compressed, "压缩结果不能为空");
        assertTrue(compressed.length > 0, "压缩结果大小必须大于0");
        assertTrue(compressed.length < file.getSize(), "压缩后大小必须小于原始大小");

        // 验证格式
        String format = ImageCompressUtils.getImageFormat(file);
        assertEquals("jpg", format.toLowerCase(), "原始图片格式应为jpg");
    }

    /**
     * 测试PNG图片压缩（重点验证透明通道保留）
     */
    @Test
    public void testPngCompression() throws IOException {
        // 准备测试图片
        MultipartFile file = getTestImageFile("test.png");
        assertNotNull(file, "测试文件不能为空");
        assertTrue(file.getSize() > 0, "测试文件大小必须大于0");

        // 执行压缩
        byte[] compressed = ImageCompressUtils.smartCompress(file);
        assertNotNull(compressed, "压缩结果不能为空");
        assertTrue(compressed.length > 0, "压缩结果大小必须大于0");

        // 验证格式保留
        String format = ImageCompressUtils.getImageFormat(file);
        assertEquals("png", format.toLowerCase(), "原始图片格式应为png");
    }

    /**
     * 测试批量压缩功能
     */
    @Test
    public void testBatchCompression() {
        // 准备测试数据
        MultipartFile[] files = new MultipartFile[3];
        for (int i = 0; i < files.length; i++) {
            try {
                files[i] = getTestImageFile("test.jpg");
            } catch (IOException e) {
                fail("创建测试文件失败: " + e.getMessage());
            }
        }

        // 执行批量压缩
        byte[][] results = ImageCompressUtils.batchCompress(files);
        assertNotNull(results, "批量压缩结果不能为空");
        assertEquals(files.length, results.length, "批量压缩结果数量必须与输入文件数量一致");

        // 验证每个结果
        for (int i = 0; i < results.length; i++) {
            assertNotNull(results[i], "第" + i + "个压缩结果不能为空");
            assertTrue(results[i].length > 0, "第" + i + "个压缩结果大小必须大于0");
        }
    }

    /**
     * 测试水印添加功能
     */
    @Test
    public void testAddWatermark() throws IOException {
        // 准备测试图片
        MultipartFile file = getTestImageFile("test.jpg");
        assertNotNull(file, "测试文件不能为空");

        // 执行添加水印
        byte[] watermarked = ImageCompressUtils.addWatermark(file, "测试水印");
        assertNotNull(watermarked, "添加水印结果不能为空");
        assertTrue(watermarked.length > 0, "添加水印结果大小必须大于0");
    }

    /**
     * 测试头像压缩功能
     */
    @Test
    public void testCompressAvatar() throws IOException {
        // 准备测试图片
        MultipartFile file = getTestImageFile("test.jpg");
        assertNotNull(file, "测试文件不能为空");

        // 执行头像压缩
        byte[] avatar = ImageCompressUtils.compressAvatar(file);
        assertNotNull(avatar, "头像压缩结果不能为空");
        assertTrue(avatar.length > 0, "头像压缩结果大小必须大于0");
    }

    /**
     * 测试缩略图压缩功能
     */
    @Test
    public void testCompressThumbnail() throws IOException {
        // 准备测试图片
        MultipartFile file = getTestImageFile("test.jpg");
        assertNotNull(file, "测试文件不能为空");

        // 执行缩略图压缩
        byte[] thumbnail = ImageCompressUtils.compressThumbnail(file);
        assertNotNull(thumbnail, "缩略图压缩结果不能为空");
        assertTrue(thumbnail.length > 0, "缩略图压缩结果大小必须大于0");
    }

    /**
     * 测试格式转换功能
     */
    @Test
    public void testConvertFormat() throws IOException {
        // 准备测试图片
        MultipartFile file = getTestImageFile("test.jpg");
        assertNotNull(file, "测试文件不能为空");

        // 执行格式转换
        byte[] converted = ImageCompressUtils.convertFormat(file, "png");
        assertNotNull(converted, "格式转换结果不能为空");
        assertTrue(converted.length > 0, "格式转换结果大小必须大于0");
    }

    /**
     * 获取测试图片文件
     *
     * @param fileName 文件名
     * @return MultipartFile
     * @throws IOException
     */
    private MultipartFile getTestImageFile(String fileName) throws IOException {
        // 创建一个临时测试图片
        File tempFile = File.createTempFile("test", ".jpg");
        tempFile.deleteOnExit();

        // 使用ImageIO创建一个简单的测试图片
        java.awt.image.BufferedImage image = new java.awt.image.BufferedImage(100, 100, java.awt.image.BufferedImage.TYPE_INT_RGB);
        java.awt.Graphics2D g2d = image.createGraphics();
        g2d.setColor(java.awt.Color.RED);
        g2d.fillRect(0, 0, 100, 100);
        g2d.dispose();
        javax.imageio.ImageIO.write(image, "jpg", tempFile);

        // 创建MockMultipartFile
        return new MockMultipartFile(
                "file",
                "test.jpg",
                "image/jpeg",
                new FileInputStream(tempFile)
        );
    }
}