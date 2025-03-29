package Utility;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileInputStream;
import java.io.IOException;

public class ExcelUtils {
    private Workbook workbook;

    public ExcelUtils(String filePath, String sheetName) {
        try {
            FileInputStream fileInputStream = new FileInputStream(filePath);
            workbook = new XSSFWorkbook(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getRowCount() {
        return workbook.getSheetAt(0).getLastRowNum();
    }

    public int getColCount() {
        return workbook.getSheetAt(0).getRow(0).getLastCellNum();
    }

    public String getCellData(int row, int col) {
        return workbook.getSheetAt(0).getRow(row).getCell(col).getStringCellValue();
    }
}
