package utils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.*;
import java.util.*;
public class ExcelReader {
    public static List<Map<String, String>> read(String path, String sheetName) throws IOException {
        try (InputStream input = new FileInputStream(path); XSSFWorkbook workbook = new XSSFWorkbook(input)) {
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null || sheet.getRow(0) == null) throw new IllegalArgumentException("Missing sheet or header: " + sheetName);
            Row header = sheet.getRow(0);
            DataFormatter formatter = new DataFormatter(Locale.ROOT);
            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
            List<Map<String, String>> rows = new ArrayList<>();
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row current = sheet.getRow(i);
                if (current == null) continue;
                Map<String, String> values = new LinkedHashMap<>();
                for (int j = 0; j < header.getLastCellNum(); j++) {
                    String key = formatter.formatCellValue(header.getCell(j)).trim();
                    if (key.isEmpty() || values.containsKey(key)) throw new IllegalArgumentException("Empty or duplicate Excel header at column " + j);
                    values.put(key, formatter.formatCellValue(current.getCell(j), evaluator));
                }
                rows.add(values);
            }
            return rows;
        }
    }
    public static List<Map<String, String>> read(String sheetName) throws IOException { return read(ConfigReader.read("excelPath"), sheetName); }
    public static List<Map<String, String>> read() throws IOException { return read("Sheet1"); }
}