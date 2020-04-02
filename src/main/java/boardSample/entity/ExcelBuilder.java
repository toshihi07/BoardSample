package boardSample.entity;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.web.servlet.view.document.AbstractXlsView;

public class ExcelBuilder extends  AbstractXlsView{
	
	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
        String fileName = new String("掲示板.xls".getBytes("MS932"), "ISO-8859-1");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        @SuppressWarnings("unchecked")
		List<Comment> comments = (List<Comment>) model.get("comments");
        String board_name = (String) model.get("board_name");
        Sheet sheet = workbook.createSheet(board_name);
	
        // create header
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("comment_id");
        row.createCell(1).setCellValue("title");
        row.createCell(2).setCellValue("text");
        row.createCell(3).setCellValue("board_id");
        row.createCell(4).setCellValue("user_id");
        row.createCell(5).setCellValue("image");
        row.createCell(6).setCellValue("CreatedAt");
        
        // create body
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (int i=0; i<comments.size(); i++) {
            Comment comment = comments.get(i);
            row = sheet.createRow(i+1);
            row.createCell(0).setCellValue(comment.getCommentId());
            row.createCell(1).setCellValue(comment.getTitle());
            row.createCell(2).setCellValue(comment.getText());
            row.createCell(3).setCellValue(comment.getBoard().getBoardId());
            row.createCell(4).setCellValue(comment.getUser().getUserId());
            row.createCell(5).setCellValue(comment.getImage());
            row.createCell(6).setCellValue(dateFormatter.format(comment.getCreated_at()));
        }
        
        // adjust column width
        for (int i=0; i<5; i++) {
            sheet.autoSizeColumn(i);
        }
        
		}
	}
