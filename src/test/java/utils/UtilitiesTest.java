package utils;

import org.junit.Test;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.io.*;
import java.util.*;
import static org.junit.Assert.*;

public class UtilitiesTest {
    @Test public void countriesSupportMoreThanOriginalFour() {
        assertEquals("DE", CountryUtils.toCountryCode(" germany "));
        assertEquals("US", CountryUtils.toCountryCode("United States"));
        assertEquals("RO", CountryUtils.toCountryCode("ro"));
    }
    @Test(expected = IllegalArgumentException.class) public void unknownCountryFails() { CountryUtils.toCountryCode("Atlantis"); }
    @Test(expected = IllegalArgumentException.class) public void missingConfigurationFails() {
        ConfigReader.read("missingTestProperty");
    }
    @Test public void systemPropertyOverridesConfiguration() {
        String key = "browser", old = System.getProperty(key);
        try { System.setProperty(key, "Firefox"); assertEquals("Firefox", ConfigReader.read(key)); }
        finally { if (old == null) System.clearProperty(key); else System.setProperty(key, old); }
    }
    @Test public void excelPreservesBlankCellsSparseRowsAndFormattedNumbers() throws Exception {
        Path path = Files.createTempFile("hrms-data-", ".xlsx");
        try {
            try (XSSFWorkbook workbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.createSheet("Data");
                Row header = sheet.createRow(0);
                header.createCell(0).setCellValue("id"); header.createCell(1).setCellValue("optional"); header.createCell(2).setCellValue("total");
                Row row = sheet.createRow(3);
                Cell id = row.createCell(0); id.setCellValue(167);
                CellStyle style = workbook.createCellStyle(); style.setDataFormat(workbook.createDataFormat().getFormat("000000")); id.setCellStyle(style);
                row.createCell(2).setCellFormula("2+3");
                try (OutputStream output = Files.newOutputStream(path)) { workbook.write(output); }
            }
            List<Map<String, String>> data = ExcelReader.read(path.toString(), "Data");
            assertEquals(1, data.size()); assertEquals("000167", data.get(0).get("id"));
            assertEquals("", data.get(0).get("optional")); assertEquals("5", data.get(0).get("total"));
        } finally { Files.deleteIfExists(path); }
    }
    @Test(expected = IOException.class) public void missingExcelFails() throws Exception {
        ExcelReader.read("nonexistent-test-data.xlsx", "Data");
    }
}
