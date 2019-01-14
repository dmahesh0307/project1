package com.sb.atlas.excel;


import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.zkoss.zul.Filedownload;
 
public class BeanToExcel {
 
	
    protected List<?> dataList = null;
    protected List<ExcelColumns> excelColumns = null;
 protected String dataSheetName;
 private XSSFSheet sheet;
    private XSSFWorkbook workbook;
 public BeanToExcel()
 {
	 
 }
    
  public List<?> getDataList() {
        return dataList;
    }
 
   public void setDataList(List<?> dataList) {
       this.dataList = dataList;
   }
 
   public List<ExcelColumns> getExcelColumns() {
     return excelColumns;
     
    }
 
   public void setExcelColumns(List<ExcelColumns> excelColumns) {
        this.excelColumns = excelColumns;
   }
 
   public String getDataSheetName() {
      return dataSheetName;
   }
 
   public void setDataSheetName(String dataSheetName) {
        this.dataSheetName = dataSheetName;
 }
 
   public void exportToExcel() {
 
       try {
 
           // Blank workbook
           this.workbook = new XSSFWorkbook();
         // Create a blank sheet
         this.sheet = this.workbook.createSheet(this.dataSheetName);
 
         int numCols = this.excelColumns.size();
         int currentRow = 0;
 
         // Create the report header at row 0
            Row excelHeader = sheet.createRow(currentRow);
            Cell dataCell;
          // Loop over all the column beans and populate the report headers
           for (int i = 0; i < numCols; i++) {
              dataCell = excelHeader.createCell(i);
               dataCell.setCellStyle(getHeaderCellStyle(workbook));
                dataCell.setCellValue(excelColumns.get(i).getHeader());
                this.sheet.setColumnWidth(i, excelColumns.get(i).getWidth());
           }
 
           currentRow++;
           Row dataRow;
            for (int i = 0; i < this.dataList.size(); i++) {
             // create a row in the spreadsheet
              dataRow = sheet.createRow(currentRow++);
                // get the bean for the current row
             // get the bean for the current row
             Object bean = dataList.get(i);
              for (int y = 0; y < numCols; y++) {
                  Object value = PropertyUtils.getProperty(bean, excelColumns
                         .get(y).getMethod());
                   writeCell(dataRow, y, value, excelColumns.get(y).getType(),
                         excelColumns.get(y).getColor(), excelColumns.get(y)
                                 .getFont());
                }
           }
 
       } catch (IllegalAccessException e) {
            System.out.println(e);
      } catch (InvocationTargetException e) {
         System.out.println(e);
      } catch (NoSuchMethodException e) {
         System.out.println(e);
      }
 
       Calendar now = Calendar.getInstance();
       int day = now.get(Calendar.DAY_OF_MONTH);
       int hour = now.get(Calendar.HOUR_OF_DAY);
       int minute = now.get(Calendar.MINUTE);
       int second = now.get(Calendar.SECOND);
      String fileName = this.dataSheetName + "_" + day + "_" + hour + "_"
             + minute + "_" + second + "_";
 
      try {
           // Write the workbook in file system
            File temp = File.createTempFile(fileName, ".xlsx");
         FileOutputStream out = new FileOutputStream(temp);
          workbook.write(out);
            out.close();
            Filedownload.save(temp, null);
      } catch (Exception e) {
         System.out.println(e);
 
      }
   }
 
   private void writeCell(Row dataRow, int col, Object value,
          FormatType formatType, Short bgColor, XSSFFont font) {
 
      Cell dataCell;
      dataCell = dataRow.createCell(col);
     if (value == null) {
            return;
     }
       switch (formatType) {
       case TEXT:
          dataCell.setCellValue(value.toString());
            break;
      case DATE:
          dataCell.setCellStyle(getDateFormatStyle(this.workbook));
          dataCell.setCellValue((Date) value);
            break;
      case FLOAT:
         break;
      case INTEGER:
          dataCell.setCellStyle(getIntegerCellStyle(this.workbook));
          dataCell.setCellType(Cell.CELL_TYPE_NUMERIC);
          dataCell.setCellValue(((Number) value).intValue());
         break;
      case LONG:
          dataCell.setCellStyle(getIntegerCellStyle(this.workbook));
          dataCell.setCellType(Cell.CELL_TYPE_NUMERIC);
          dataCell.setCellValue(((Long) value).intValue());
         break;

      case MONEY:
    	  	dataCell.setCellStyle(getNumberCellStyle(this.workbook));
           dataCell.setCellType(Cell.CELL_TYPE_NUMERIC);
           dataCell.setCellValue(((BigDecimal) value).doubleValue());
          break;
      case PERCENTAGE:
            break;
      default:
            break;
      }
 
   }
 
   public static void createExcelHeader(XSSFWorkbook workbook,
         XSSFSheet excelSheet, String titles[]) {
 
        Row excelHeader = excelSheet.createRow(0);
      Cell dataCell;
      int count = 0;
      for (String caption : titles) {
         dataCell = excelHeader.createCell(count++);
         dataCell.setCellStyle(getHeaderCellStyle(workbook));
            dataCell.setCellValue(caption);
     }
   }
 
   public static XSSFCellStyle getHeaderCellStyle(XSSFWorkbook workbook) {
     XSSFCellStyle titleStyle = workbook.createCellStyle();
 
      titleStyle.setFillPattern(XSSFCellStyle.FINE_DOTS);
     titleStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
     titleStyle.setAlignment(XSSFCellStyle.ALIGN_LEFT);
      titleStyle.setBorderBottom(XSSFCellStyle.BORDER_MEDIUM);
        titleStyle.setBorderLeft(XSSFCellStyle.BORDER_MEDIUM);
      titleStyle.setBorderRight(XSSFCellStyle.BORDER_MEDIUM);
     titleStyle.setBorderTop(XSSFCellStyle.BORDER_MEDIUM);
 
       Font headerFont = workbook.createFont();
        headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
     headerFont.setFontHeightInPoints((short) 10);
       titleStyle.setFont(headerFont);
 
     return titleStyle;
  }
 
   private XSSFCellStyle getDateFormatStyle(XSSFWorkbook workbook) {
       XSSFCellStyle style = workbook.createCellStyle();
       XSSFDataFormat df = workbook.createDataFormat();
        style.setDataFormat(df.getFormat("mm/dd/yyyy"));
        return style;
   }
 
   private XSSFCellStyle getNumberCellStyle(XSSFWorkbook workbook) {
       XSSFCellStyle style = workbook.createCellStyle();
       XSSFDataFormat format = workbook.createDataFormat();
        style.setDataFormat(format.getFormat("0.00"));
      return style;
   }
 
   private XSSFCellStyle getIntegerCellStyle(XSSFWorkbook workbook) {
      XSSFCellStyle style = workbook.createCellStyle();
       XSSFDataFormat format = workbook.createDataFormat();
        style.setDataFormat(format.getFormat("#,##0"));
     return style;
   }
}
