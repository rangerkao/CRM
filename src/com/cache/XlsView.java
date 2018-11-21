package com.cache;

import java.io.OutputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.servlet.view.document.AbstractXlsView;

public class XlsView extends AbstractXlsView {

	
	
	@Override
	public void buildExcelDocument(Map<String, Object> map, Workbook wb, HttpServletRequest req, HttpServletResponse response) throws Exception {
		response.setContentType("application/vnd.ms-excel");         
        response.setHeader("Content-disposition", "attachment;filename="+map.get("name"));         
        OutputStream ouputStream = response.getOutputStream();         
        wb.write(ouputStream);         
        ouputStream.flush();         
        ouputStream.close(); 
	}

}
