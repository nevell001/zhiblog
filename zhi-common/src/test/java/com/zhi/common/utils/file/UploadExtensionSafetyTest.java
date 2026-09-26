package com.zhi.common.utils.file;

import com.zhi.common.exception.file.InvalidExtensionException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 上传后缀白名单安全守卫。
 *
 * <p>上传文件落在 static 资源目录并由 nginx 以同源方式托管，一旦放行 html/htm/svg
 * 等可执行后缀，任何已登录用户都能上传一个脚本页面再引导管理员访问，从而读取
 * 非 HttpOnly 的登录 token（存储型 XSS）。</p>
 */
class UploadExtensionSafetyTest {

    /** 会与同源页面共用 JS 上下文的后缀，一律不得进入白名单 */
    private static final List<String> SCRIPTABLE_EXTENSIONS = Arrays.asList(
        "html", "htm", "xhtml", "shtml", "svg", "svgz",
        "js", "mjs", "cjs", "jsp", "jspx", "php", "phtml", "asp", "aspx",
        "exe", "msi", "bat", "cmd", "sh", "jar", "war"
    );

    @Test
    @DisplayName("默认白名单不得包含可执行/可脚本后缀")
    void defaultWhitelistRejectsScriptableExtensions() {
        List<String> allowed = Arrays.asList(MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION);
        for (String extension : SCRIPTABLE_EXTENSIONS) {
            assertFalse(allowed.contains(extension),
                "默认上传白名单不允许放行 ." + extension + "（同源托管会变成存储型 XSS 载体）");
        }
    }

    @Test
    @DisplayName("默认白名单仍应放行博客常用类型")
    void defaultWhitelistKeepsCommonTypes() {
        List<String> allowed = Arrays.asList(MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION);
        for (String extension : Arrays.asList("jpg", "jpeg", "png", "gif", "pdf", "txt", "docx", "xlsx", "zip", "mp4")) {
            assertTrue(allowed.contains(extension), "默认白名单不应误删 ." + extension);
        }
    }

    @Test
    @DisplayName("上传 .html/.svg 必须被拒绝，.txt 应放行")
    void assertAllowedBlocksHtmlUpload() {
        for (String extension : Arrays.asList("html", "htm", "svg")) {
            MockMultipartFile file = new MockMultipartFile(
                "file", "evil." + extension, "text/html", "<script>alert(document.cookie)</script>".getBytes());
            assertThrows(InvalidExtensionException.class,
                () -> FileUploadUtils.assertAllowed(file, MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION),
                "." + extension + " 必须被上传校验拒绝");
        }

        MockMultipartFile text = new MockMultipartFile("file", "note.txt", "text/plain", "hi".getBytes());
        assertDoesNotThrow(() -> FileUploadUtils.assertAllowed(text, MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION));
    }
}
