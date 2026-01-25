package com.ruoyi.common.utils.file;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

/**
 * FileTypeUtils 文件类型工具类测试
 *
 * @author ruoyi
 */
public class FileTypeUtilsTest {

    @Test
    public void testGetFileTypeWithFile() {
        // 测试从 File 对象获取文件类型
        File txtFile = new File("test.txt");
        assertEquals("txt", FileTypeUtils.getFileType(txtFile));

        File jpgFile = new File("test.jpg");
        assertEquals("jpg", FileTypeUtils.getFileType(jpgFile));

        File pngFile = new File("test.PNG");
        assertEquals("png", FileTypeUtils.getFileType(pngFile));

        File docFile = new File("test.DOC");
        assertEquals("doc", FileTypeUtils.getFileType(docFile));

        // 测试 null
        assertEquals("", FileTypeUtils.getFileType((File) null));
    }

    @Test
    public void testGetFileTypeWithString() {
        // 测试从字符串获取文件类型
        assertEquals("txt", FileTypeUtils.getFileType("test.txt"));
        assertEquals("jpg", FileTypeUtils.getFileType("test.jpg"));
        assertEquals("png", FileTypeUtils.getFileType("test.PNG"));
        assertEquals("doc", FileTypeUtils.getFileType("test.DOC"));
        assertEquals("pdf", FileTypeUtils.getFileType("test.PDF"));

        // 测试没有后缀的文件名
        assertEquals("", FileTypeUtils.getFileType("test"));
        assertEquals("", FileTypeUtils.getFileType("test."));

        // 测试多个点
        assertEquals("txt", FileTypeUtils.getFileType("test.file.txt"));

        // 测试空字符串
        assertEquals("", FileTypeUtils.getFileType(""));
    }

    @Test
    public void testGetFileExtendName() {
        // 测试 GIF 文件
        byte[] gifBytes = {71, 73, 70, 56, 57, 97, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        assertEquals("GIF", FileTypeUtils.getFileExtendName(gifBytes));

        // 测试 JPG 文件
        byte[] jpgBytes = {0, 0, 0, 0, 0, 0, 74, 70, 73, 70, 0};
        assertEquals("JPG", FileTypeUtils.getFileExtendName(jpgBytes));

        // 测试 BMP 文件
        byte[] bmpBytes = {66, 77, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        assertEquals("BMP", FileTypeUtils.getFileExtendName(bmpBytes));

        // 测试 PNG 文件
        byte[] pngBytes = {0, 80, 78, 71, 13, 10, 26, 10, 0, 0, 0};
        assertEquals("PNG", FileTypeUtils.getFileExtendName(pngBytes));

        // 测试默认返回 JPG
        byte[] unknownBytes = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        assertEquals("JPG", FileTypeUtils.getFileExtendName(unknownBytes));
    }

    @Test
    public void testGetFileTypeCaseInsensitive() {
        // 测试大小写不敏感
        assertEquals("txt", FileTypeUtils.getFileType("test.TXT"));
        assertEquals("txt", FileTypeUtils.getFileType("test.TxT"));
        assertEquals("txt", FileTypeUtils.getFileType("test.tXt"));

        assertEquals("jpg", FileTypeUtils.getFileType("test.JPG"));
        assertEquals("jpg", FileTypeUtils.getFileType("test.JpG"));

        assertEquals("pdf", FileTypeUtils.getFileType("test.PDF"));
    }

    @Test
    public void testGetFileTypeWithSpecialCharacters() {
        // 测试特殊字符文件名
        assertEquals("txt", FileTypeUtils.getFileType("测试文件.txt"));
        assertEquals("pdf", FileTypeUtils.getFileType("文档-2024.pdf"));
        assertEquals("docx", FileTypeUtils.getFileType("report_2024.docx"));
        assertEquals("xlsx", FileTypeUtils.getFileType("data.final.xlsx"));
    }
}