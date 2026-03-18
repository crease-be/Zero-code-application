package com.xieyi.tool;

import org.springframework.stereotype.Component;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class FileGeneratorTool {

    // 建议使用绝对路径，确保文件夹能被精准创建
    private static final String BASE_PATH = "D:\\program\\java\\Zero_code_application\\generated_code";

    public void saveCodeToFile(String filename, String content) {
        try {
            Path dirPath = Paths.get(BASE_PATH);
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
                System.out.println("检测到目录不存在，已创建: " + BASE_PATH);
            }

            Path filePath = dirPath.resolve(filename);
            Files.writeString(filePath, content);

            System.out.println("【文件生成成功】路径: " + filePath.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("【文件生成失败】: " + e.getMessage());
        }
    }
}