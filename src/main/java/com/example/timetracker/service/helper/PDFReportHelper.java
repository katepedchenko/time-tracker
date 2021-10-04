package com.example.timetracker.service.helper;

import com.example.timetracker.dto.ActivityReadDTO;
import com.example.timetracker.dto.SummaryDTO;
import com.example.timetracker.dto.WorkingDayReadDTO;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.stream.Stream;

@Component
public class PDFReportHelper {

    public byte[] createReport(SummaryDTO summaryDTO) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try {
            Document document = new Document();
            document.setPageSize(PageSize.A4.rotate());
            document.setMargins(30, 30, 30, 30);
            PdfWriter.getInstance(document, output);
            document.open();
            document.newPage();

            PdfPTable table = new PdfPTable(9);
            table.setWidthPercentage(100);
            table.setSpacingBefore(0f);
            table.setSpacingAfter(0f);

            float[] columnWidths = {70f, 200f, 200f, 50f, 50f, 50f, 55f, 50f, 50f};
            table.setWidths(columnWidths);
            table.setTotalWidth(775);
            table.setLockedWidth(true);

            addTableHeader(table);

            for (WorkingDayReadDTO entry : summaryDTO.getEntries()) {
                if (entry.getActivities().size() < 1) {
                    continue;
                }

                addSummaryRow(table, entry);

                addActivityRows(table, entry.getActivities());
            }

            document.add(table);
            document.close();
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
        return output.toByteArray();
    }

    private void addTableHeader(PdfPTable table) {
        Stream.of("Date", "Project", "Activity", "Tracked Hours", "Work Hours Norm",
                "Hours Delta", "Allowed Overtime Hours", "Allowed Pause Hours", "Status")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(1);
                    header.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    header.setPhrase(new Phrase(columnTitle));
                    table.addCell(header);
                });
    }

    private void addSummaryRow(PdfPTable table, WorkingDayReadDTO entry) {
        table.addCell(getSumCell(entry.getActivities().get(0).getDate().toString()));
        table.addCell(getSumCell(entry.getActivities().get(0).getDate().getDayOfWeek().toString()));
        table.addCell(getSumCell(""));
        table.addCell(getSumCell(""));
        table.addCell(getSumCell(entry.getWorkHoursNorm()));
        table.addCell(getSumCell(entry.getWorkHoursDelta()));
        table.addCell(getSumCell(entry.getAllowedOvertimeHours()));
        table.addCell(getSumCell(entry.getAllowedPausedHours()));
        table.addCell(getSumCell(""));
    }

    private void addActivityRows(PdfPTable table, List<ActivityReadDTO> activities) {
        for (ActivityReadDTO activity : activities) {
            table.addCell(getCell(activity.getDate().toString()));
            table.addCell(getCell(activity.getProject().getName()));
            table.addCell(getCell(activity.getDescription()));
            table.addCell(getCell(activity.getHours()));
            table.addCell(getCell(""));
            table.addCell(getCell(""));
            table.addCell(getCell(""));
            table.addCell(getCell(""));
            table.addCell(getCell(activity.getStatus().toString()));
        }
    }

    private PdfPCell getCell(String value) {
        Font font = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK);
        PdfPCell cell = new PdfPCell(new Paragraph(value, font));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setNoWrap(true);
        return cell;
    }

    private PdfPCell getCell(Integer value) {
        Font font = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK);

        PdfPCell cell = new PdfPCell(new Paragraph(value.toString(), font));
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        cell.setNoWrap(true);

        return cell;
    }

    private PdfPCell getSumCell(String value) {
        Font font = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.BLACK);
        PdfPCell cell = new PdfPCell(new Paragraph(value, font));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setNoWrap(true);
        return cell;
    }

    private PdfPCell getSumCell(Integer value) {
        Font font = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.BLACK);
        PdfPCell cell = new PdfPCell(new Paragraph(value.toString(), font));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setNoWrap(true);
        return cell;
    }
}
