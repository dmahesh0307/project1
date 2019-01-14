package com.sb.atlas.excel;

import org.apache.poi.xssf.usermodel.XSSFFont;
 
public class ExcelColumns {
 
	private String m_method;
    private String m_header;
    private FormatType m_type;
	private XSSFFont m_font;
    private Short m_color;
	private int m_width;
 
    public ExcelColumns(String method, String header, FormatType type,
          XSSFFont font, Short color, int width) {
			this.m_method = method;
			this.m_header = header;
			this.m_type = type;
			this.m_font = font;
			this.m_color = color;
			this.m_width = width;
   }
 
   public ExcelColumns(String method, String header, FormatType type,
          XSSFFont font) {
        this(method, header, type, font, null, 3000);
   }
 
   public ExcelColumns(String method, String header, FormatType type,
          Short color) {
      this(method, header, type, null, color, 3000);
  }
 
   public ExcelColumns(String method, String header, FormatType type) {
        this(method, header, type, null, null, 3000);
   }
 
   public ExcelColumns(String method, String header, FormatType type, int width) {
     this(method, header, type, null, null, width);
  }
 
   public String getMethod() {
     return m_method;
    }
 
   public void setMethod(String method) {
      this.m_method = method;
 }
 
   public String getHeader() {
     return m_header;
    }
 
   public void setHeader(String header) {
      this.m_header = header;
 }
 
   public FormatType getType() {
       return m_type;
  }
 
   public void setType(FormatType type) {
      this.m_type = type;
 }
 
   public XSSFFont getFont() {
     return m_font;
  }
 
   public void setFont(XSSFFont m_font) {
      this.m_font = m_font;
   }
 
   public Short getColor() {
       return m_color;
 }
 
  public void setColor(Short m_color) {
       this.m_color = m_color;
 }
 
   public int getWidth() {
     if (this.m_width == 0)
          return 3000;
        else
            return m_width;
 }
 
   public void setWidth(int m_width) {
     this.m_width = m_width;
 }
 
}