package com.example.timetracker.service.helper;

import com.example.timetracker.dto.ActivityReadDTO;
import com.example.timetracker.dto.SummaryDTO;
import com.example.timetracker.dto.WorkingDayReadDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;

@Component
public class ExcelReportHelper {

    private static final int CHAR_LENGTH = 256;

    public byte[] createReport(SummaryDTO summaryDTO) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try {
            XSSFWorkbook workbook = new XSSFWorkbook();

            Sheet sheet = workbook.createSheet("Report");
            setColumnsWidth(sheet);

            DataFormat format = workbook.createDataFormat();
            CellStyle stringStyle = workbook.createCellStyle();
            stringStyle.setWrapText(true);

            CellStyle numberStyle = workbook.createCellStyle();
            numberStyle.setDataFormat(format.getFormat("##"));
            numberStyle.setAlignment(HorizontalAlignment.CENTER);

            CellStyle dateStyle = workbook.createCellStyle();
            dateStyle.setDataFormat(format.getFormat("dd.mm.yyyy"));

            addTableHeader(workbook, sheet);

            int rowNumber = 1;
            for (WorkingDayReadDTO entry : summaryDTO.getEntries()) {
                Row row = sheet.createRow(rowNumber++);

                if (entry.getActivities().size() < 1) {
                    continue;
                }

                addDateCell(dateStyle, row, 0, entry.getActivities().get(0).getDate());
                addStringCell(stringStyle, row, 1, entry.getActivities().get(0).getDate().getDayOfWeek().toString());
                addNumberCell(numberStyle, row, 4, entry.getWorkHoursNorm());
                addNumberCell(numberStyle, row, 5, entry.getWorkHoursDelta());
                addNumberCell(numberStyle, row, 6, entry.getAllowedOvertimeHours());
                addNumberCell(numberStyle, row, 7, entry.getAllowedPausedHours());

                row = sheet.createRow(rowNumber++);

                for (ActivityReadDTO activity : entry.getActivities()) {
                    addDateCell(dateStyle, row, 0, activity.getDate());
                    addStringCell(stringStyle, row, 1, activity.getProject().getName());
                    addStringCell(stringStyle, row, 2, activity.getDescription());
                    addNumberCell(numberStyle, row, 3, activity.getHours());
                    addStringCell(stringStyle, row, 8, activity.getStatus().toString());

                    row = sheet.createRow(rowNumber++);
                }
            }

            workbook.write(output);
            workbook.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return output.toByteArray();
    }

    private void addNumberCell(CellStyle style, Row row, int columnNumber, int value) {
        Cell cell = row.createCell(columnNumber);
        cell.setCellValue(value);
        cell.setCellStyle(style);
    }

    private void addStringCell(CellStyle style, Row row, int columnNumber, String value) {
        Cell cell = row.createCell(columnNumber);
        cell.setCellValue(value);
        cell.setCellStyle(style);
    }

    private void addDateCell(CellStyle style, Row row, int columnNumber, LocalDate value) {
        Cell cell = row.createCell(columnNumber);
        cell.setCellValue(value);
        cell.setCellStyle(style);
    }

    private void addTableHeader(XSSFWorkbook workbook, Sheet sheet) {
        Row header = sheet.createRow(0);

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setWrapText(true);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        XSSFFont font = workbook.createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 12);
        font.setBold(true);
        headerStyle.setFont(font);

        Cell headerCell = header.createCell(0);
        headerCell.setCellValue("Date");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(1);
        headerCell.setCellValue("Project");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(2);
        headerCell.setCellValue("Activity");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(3);
        headerCell.setCellValue("Tracked Hours");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(4);
        headerCell.setCellValue("Work Hours Norm");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(5);
        headerCell.setCellValue("Hours Delta");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(6);
        headerCell.setCellValue("Allowed Overtime Hours");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(7);
        headerCell.setCellValue("Allowed Pause Hours");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(8);
        headerCell.setCellValue("Status");
        headerCell.setCellStyle(headerStyle);
    }

    private void setColumnsWidth(Sheet sheet) {
        sheet.setColumnWidth(0, 15 * CHAR_LENGTH);
        sheet.setColumnWidth(1, 50 * CHAR_LENGTH);
        sheet.setColumnWidth(2, 50 * CHAR_LENGTH);
        sheet.setColumnWidth(3, 10 * CHAR_LENGTH);
        sheet.setColumnWidth(4, 10 * CHAR_LENGTH);
        sheet.setColumnWidth(5, 10 * CHAR_LENGTH);
        sheet.setColumnWidth(6, 15 * CHAR_LENGTH);
        sheet.setColumnWidth(7, 15 * CHAR_LENGTH);
        sheet.setColumnWidth(8, 15 * CHAR_LENGTH);
    }
}