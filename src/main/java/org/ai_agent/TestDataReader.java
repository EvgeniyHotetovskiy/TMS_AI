
package org.ai_agent;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.ai_agent.model.TestCase;
import org.ai_agent.model.TestSuite;
import org.ai_agent.utils.FilesUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class TestDataReader {

    private static final String TESTCASES_FILE = "generated/testcases.json";

    public static TestSuite readTestCases() {
        ObjectMapper mapper = new ObjectMapper();

        // Полезная настройка: если в JSON появятся лишние поля, код не упадет
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        File file = new File(TESTCASES_FILE);

        // Проверяем, существует ли файл перед чтением
        if (!file.exists()) {
            throw new RuntimeException("Test data file not found at: " + file.getAbsolutePath() +
                    ". Make sure the AI Pipeline has generated it!");
        }

        try {
            System.out.println("Reading test cases from: " + TESTCASES_FILE);
            return mapper.readValue(file, TestSuite.class);
        } catch (IOException e) {
            // Fallback: LLM иногда вставляет неэкранированные кавычки внутри строк.
            // Пробуем мягко починить JSON и распарсить повторно.
            try {
                String raw = Files.readString(Path.of(TESTCASES_FILE));
                String repaired = escapeInnerQuotes(raw);
                if (!raw.equals(repaired)) {
                    FilesUtil.write("generated/testcases_repaired.json", repaired);
                    System.out.println("Detected malformed quotes. Repaired JSON saved to: generated/testcases_repaired.json");
                }
                return mapper.readValue(repaired, TestSuite.class);
            } catch (Exception repairError) {
                throw new RuntimeException("Critical: Failed to parse JSON test cases. Check JSON format.", repairError);
            }
        }
    }

    private static String escapeInnerQuotes(String json) {
        StringBuilder out = new StringBuilder(json.length() + 64);
        boolean inString = false;
        boolean escaping = false;

        for (int i = 0; i < json.length(); i++) {
            char c = json.charAt(i);

            if (!inString) {
                if (c == '"') {
                    inString = true;
                }
                out.append(c);
                continue;
            }

            if (escaping) {
                out.append(c);
                escaping = false;
                continue;
            }

            if (c == '\\') {
                out.append(c);
                escaping = true;
                continue;
            }

            if (c == '"') {
                int j = i + 1;
                while (j < json.length() && Character.isWhitespace(json.charAt(j))) {
                    j++;
                }
                char next = j < json.length() ? json.charAt(j) : '\0';

                // If next token does not look like a JSON delimiter,
                // treat quote as inner text and escape it.
                if (next != ',' && next != ']' && next != '}' && next != ':') {
                    out.append("\\\"");
                } else {
                    out.append(c);
                    inString = false;
                }
                continue;
            }

            out.append(c);
        }
        return out.toString();
    }

    public static void generate() {
        TestSuite suite = readTestCases(); // Читаем свежий JSON
        if (suite == null) return;

        StringBuilder code = new StringBuilder();
        code.append("package com.example.aiagent;\n\n");
        code.append("import org.testng.annotations.Test;\n");
        code.append("import static io.restassured.RestAssured.*;\n\n");
        code.append("public class GeneratedRegistrationTest extends BaseApiTest {\n");

        for (TestCase tc : suite.testcases) {
            String methodName = "test_" + tc.id.replace("-", "_");
            code.append("\n    @Test(description = \"" + tc.title + "\")\n");
            code.append("    public void " + methodName + "() {\n");
            code.append("        System.out.println(\"Running: " + tc.title + "\");\n");
            code.append("        // Здесь логика вызова API на основе шагов: " + tc.steps + "\n");
            code.append("    }\n");
        }

        code.append("}\n");

        // Записываем сгенерированный код в папку с тестами
        FilesUtil.write("src/test/java/com/example/aiagent/GeneratedRegistrationTest.java", code.toString());
    }

}