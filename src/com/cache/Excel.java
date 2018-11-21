package com.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.servlet.view.document.AbstractXlsView;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

public class Excel{
	
	public static Workbook createExcel(String fileType,List<Map<String,String>> head,List<Map<String,String>> data){
		
		Workbook wb = null;
		Sheet sh = null;
		long maxRow = 65535;
		
		if("xls".equals(fileType)) {
			//xls，HSSF，最大2^16=65535
			maxRow = 65535;
			//創建webbook文件
			 wb = new HSSFWorkbook();  	
		}else {
			//xlsx，XSSF，最大2^20=1048576
			maxRow = 1048576;
		}
		
		int sheetN = 0;
		int rowN = 0;
		int count = 0;
		
		while(count<data.size()) {
			//添加sheet
			sh =wb.createSheet("sheet"+sheetN++);
			
			Row row;
			Cell cell;
			CellStyle style = null;
			
			//表頭處理
			row = sh.createRow(rowN++);
			count++;
			for(int k = 0 ; k < head.size() ; k++){
				String title=head.get(k).get("title");
				
				cell = row.createCell(k);  
	            cell.setCellValue(title);
	            
	            if(style!= null)
	            	cell.setCellStyle(style); 
	            
	            //設定欄寬
	            if(head.get(k).get("size")!=null) {
	            	sh.setColumnWidth(k, 256*Integer.valueOf(head.get(k).get("size")));
	            }
			}
			
			
			//加入內容
			for(Map<String,String> content : data) {
				row = sh.createRow(rowN++);
				count++;
				for(int k = 0 ; k < head.size() ; k++){
					String col=head.get(k).get("col");
					String value = (String) content.get(col);
					cell = row.createCell(k);  
		            cell.setCellValue(value==null?"":value);
				}
				
				if(count==maxRow) break;
			}
		}
        return wb;  
	}
	public static Map<String,String> mapSetting(String title,String col){
		return mapSetting(title,col,null);
	}
	public static Map<String,String> mapSetting(String title,String col,String size){
		Map<String, String> m = new HashMap<String,String>();
		
		m.put("title", title);
		m.put("col", col);
		if(size!=null)
			m.put("size", size);
		return m;
	}

	static class xlsView extends AbstractXlsView {

		@Override
		protected void buildExcelDocument(Map<String, Object> model, Workbook wb, HttpServletRequest req, HttpServletResponse resp) throws Exception {
			
		}
		
	}
	
	static class xlsxView extends AbstractXlsxView {

		@Override
		protected void buildExcelDocument(Map<String, Object> model, Workbook wb, HttpServletRequest req, HttpServletResponse resp) throws Exception {

		}
		
	}
}
