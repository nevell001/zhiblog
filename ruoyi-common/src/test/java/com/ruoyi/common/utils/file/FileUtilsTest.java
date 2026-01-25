package com.ruoyi.common.utils.file;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * FileUtils 文件处理工具类测试
 *
 * @author ruoyi
 */
@ExtendWith(MockitoExtension.class)
public class FileUtilsTest {

    @Mock
    private HttpServletRequest mockRequest;

    @Mock
    private HttpServletResponse mockResponse;

    @Test
    public void testIsValidFilename() {
        // 测试文件名验证
        assertTrue(FileUtils.isValidFilename("test.txt"));
        assertTrue(FileUtils.isValidFilename("测试文件.jpg"));
        assertTrue(FileUtils.isValidFilename("file-123.png"));
        assertTrue(FileUtils.isValidFilename("file_123.doc"));
        assertTrue(FileUtils.isValidFilename("file.123.pdf"));
        assertTrue(FileUtils.isValidFilename("file|txt")); // 竖线是合法的

        // 测试非法文件名
        assertFalse(FileUtils.isValidFilename("file/test.txt")); // 包含路径分隔符
        assertFalse(FileUtils.isValidFilename("file:txt")); // 包含冒号
        assertFalse(FileUtils.isValidFilename("file*txt")); // 包含星号
        assertFalse(FileUtils.isValidFilename("file?txt")); // 包含问号
        assertFalse(FileUtils.isValidFilename("file<txt>")); // 包含尖括号
        assertFalse(FileUtils.isValidFilename("")); // 空字符串
    }

    @Test
    public void testCheckAllowDownload() {
        // 测试允许下载的文件
        assertTrue(FileUtils.checkAllowDownload("test.jpg"));
        assertTrue(FileUtils.checkAllowDownload("test.png"));
        assertTrue(FileUtils.checkAllowDownload("test.gif"));
        assertTrue(FileUtils.checkAllowDownload("test.jpeg"));
        assertTrue(FileUtils.checkAllowDownload("test.doc"));
        assertTrue(FileUtils.checkAllowDownload("test.docx"));
        assertTrue(FileUtils.checkAllowDownload("test.xls"));
        assertTrue(FileUtils.checkAllowDownload("test.xlsx"));
        assertTrue(FileUtils.checkAllowDownload("test.txt"));
        assertTrue(FileUtils.checkAllowDownload("test.zip"));
        assertTrue(FileUtils.checkAllowDownload("test.rar"));

        // 测试不允许下载的文件
        assertFalse(FileUtils.checkAllowDownload("test.exe"));
        assertFalse(FileUtils.checkAllowDownload("test.bat"));
        assertFalse(FileUtils.checkAllowDownload("test.sh"));
        assertFalse(FileUtils.checkAllowDownload("test.php"));
        assertFalse(FileUtils.checkAllowDownload("test.jsp"));

        // 测试路径遍历攻击
        assertFalse(FileUtils.checkAllowDownload("../test.txt"));
        assertFalse(FileUtils.checkAllowDownload("../../test.txt"));
        assertFalse(FileUtils.checkAllowDownload("test/../secret.txt"));
    }

    @Test
    public void testSetFileDownloadHeader() throws Exception {
        // 测试 IE 浏览器
        when(mockRequest.getHeader("USER-AGENT")).thenReturn("Mozilla/5.0 (Windows NT 10.0; Trident/7.0; rv:11.0) like Gecko");
        String ieResult = FileUtils.setFileDownloadHeader(mockRequest, "测试文件.txt");
        assertNotNull(ieResult);
        assertTrue(ieResult.contains("%E6%B5%8B%E8%AF%95%E6%96%87%E4%BB%B6"));

        // 测试 Firefox 浏览器
        when(mockRequest.getHeader("USER-AGENT")).thenReturn("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:91.0) Gecko/20100101 Firefox/91.0");
        String firefoxResult = FileUtils.setFileDownloadHeader(mockRequest, "测试文件.txt");
        assertNotNull(firefoxResult);

        // 测试 Chrome 浏览器
        when(mockRequest.getHeader("USER-AGENT")).thenReturn("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
        String chromeResult = FileUtils.setFileDownloadHeader(mockRequest, "测试文件.txt");
        assertNotNull(chromeResult);
        assertTrue(chromeResult.contains("%E6%B5%8B%E8%AF%95%E6%96%87%E4%BB%B6"));

        // 测试其他浏览器
        when(mockRequest.getHeader("USER-AGENT")).thenReturn("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.1.1 Safari/605.1.15");
        String otherResult = FileUtils.setFileDownloadHeader(mockRequest, "测试文件.txt");
        assertNotNull(otherResult);
    }

    @Test
    public void testSetAttachmentResponseHeader() throws Exception {
        MockHttpServletResponse response = new MockHttpServletResponse();
        FileUtils.setAttachmentResponseHeader(response, "测试文件.txt");

        // 验证响应头设置
        String contentDisposition = response.getHeader("Content-disposition");
        assertNotNull(contentDisposition);
        assertTrue(contentDisposition.startsWith("attachment; filename="));
        assertTrue(contentDisposition.contains("filename*=utf-8''"));

        String downloadFilename = response.getHeader("download-filename");
        assertNotNull(downloadFilename);

        String exposeHeaders = response.getHeader("Access-Control-Expose-Headers");
        assertNotNull(exposeHeaders);
        assertTrue(exposeHeaders.contains("Content-Disposition"));
        assertTrue(exposeHeaders.contains("download-filename"));
    }

    @Test
    public void testPercentEncode() throws Exception {
        // 测试百分号编码
        String encoded = FileUtils.percentEncode("测试文件.txt");
        assertNotNull(encoded);
        assertTrue(encoded.contains("%"));
        assertFalse(encoded.contains("+")); // 空格应该被编码为 %20，而不是 +

        // 测试英文文件名
        String encoded2 = FileUtils.percentEncode("test file.txt");
        assertNotNull(encoded2);
        assertTrue(encoded2.contains("%20")); // 空格应该被编码为 %20

        // 测试特殊字符
        String encoded3 = FileUtils.percentEncode("file@#test.txt");
        assertNotNull(encoded3);
    }

    @Test
    public void testGetFileExtendName() {
        // 测试 GIF 文件
        byte[] gifBytes = {71, 73, 70, 56, 57, 97, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        assertEquals("gif", FileUtils.getFileExtendName(gifBytes));

        // 测试 JPG 文件
        byte[] jpgBytes = {0, 0, 0, 0, 0, 0, 74, 70, 73, 70, 0};
        assertEquals("jpg", FileUtils.getFileExtendName(jpgBytes));

        // 测试 BMP 文件
        byte[] bmpBytes = {66, 77, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        assertEquals("bmp", FileUtils.getFileExtendName(bmpBytes));

        // 测试 PNG 文件
        byte[] pngBytes = {0, 80, 78, 71, 13, 10, 26, 10, 0, 0, 0};
        assertEquals("png", FileUtils.getFileExtendName(pngBytes));

        // 测试默认返回 jpg
        byte[] unknownBytes = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        assertEquals("jpg", FileUtils.getFileExtendName(unknownBytes));
    }

    @Test
    public void testGetName() {
        // 测试获取文件名
        assertEquals("test.txt", FileUtils.getName("/path/to/test.txt"));
        assertEquals("test.txt", FileUtils.getName("C:\\path\\to\\test.txt"));
        assertEquals("test.txt", FileUtils.getName("test.txt"));
        assertEquals("测试文件.jpg", FileUtils.getName("/path/to/测试文件.jpg"));

        // 测试 null
        assertNull(FileUtils.getName(null));

        // 测试空字符串
        assertEquals("", FileUtils.getName(""));
    }

    @Test
    public void testGetNameNotSuffix() {
        // 测试获取不带后缀的文件名
        assertEquals("test", FileUtils.getNameNotSuffix("/path/to/test.txt"));
        assertEquals("test", FileUtils.getNameNotSuffix("C:\\path\\to\\test.doc"));
        assertEquals("test", FileUtils.getNameNotSuffix("test.pdf"));
        assertEquals("测试文件", FileUtils.getNameNotSuffix("/path/to/测试文件.jpg"));

        // 测试 null
        assertNull(FileUtils.getNameNotSuffix(null));

        // 测试空字符串
        assertEquals("", FileUtils.getNameNotSuffix(""));
    }

    @Test
    public void testStripPrefix() {
        // 测试移除路径前缀
        assertEquals("/upload/test.txt", FileUtils.stripPrefix("/profile/upload/test.txt"));
        assertEquals("//upload/test.txt", FileUtils.stripPrefix("/profile//upload/test.txt"));

        // 测试没有前缀的路径 - 返回空字符串
        assertEquals("", FileUtils.stripPrefix("test.txt"));

        // 测试 null - 返回 null
        assertNull(FileUtils.stripPrefix(null));
    }

    @Test
    public void testDeleteFile() throws IOException {
        // 创建临时文件
        File tempFile = File.createTempFile("test", ".txt");
        assertTrue(tempFile.exists());

        // 测试删除文件
        assertTrue(FileUtils.deleteFile(tempFile.getAbsolutePath()));
        assertFalse(tempFile.exists());

        // 测试删除不存在的文件
        assertFalse(FileUtils.deleteFile(tempFile.getAbsolutePath()));

        // 测试删除目录（应该返回 false）
        File tempDir = Files.createTempDirectory("test").toFile();
        assertTrue(tempDir.exists());
        assertFalse(FileUtils.deleteFile(tempDir.getAbsolutePath()));
        Files.delete(tempDir.toPath()); // 清理
    }

    @Test
    public void testWriteBytes() throws IOException {
        // 创建临时文件
        File tempFile = File.createTempFile("test", ".txt");
        tempFile.deleteOnExit();

        // 写入测试数据
        String testData = "Hello, World!";
        Files.write(tempFile.toPath(), testData.getBytes());

        // 测试读取文件
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        FileUtils.writeBytes(tempFile.getAbsolutePath(), outputStream);

        byte[] result = outputStream.toByteArray();
        assertEquals(testData, new String(result));

        // 测试文件不存在的情况
        assertThrows(FileNotFoundException.class, () -> {
            FileUtils.writeBytes("/non/existent/file.txt", outputStream);
        });
    }
}
