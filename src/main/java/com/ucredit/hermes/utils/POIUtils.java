/**
 * Copyright(c) 2011-2012 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.utils;

import java.text.NumberFormat;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellReference;

/**
 * @author ijay
 */
public class POIUtils {
    public static final String LINE_SEPARATOR = "line.separator";

    public static Row buildRowWithStringValues(Sheet sheet, int rowIndex,
            int startCol, short hAlign, short vAlign, String... values) {
        Row row = sheet.createRow(rowIndex);

        for (int i = startCol; i < values.length + startCol; i++) {
            POIUtils.createCell(row, i, hAlign, vAlign).setCellValue(
                values[i - startCol]);
        }

        return row;
    }

    public static Row buildRowWithValues(Sheet sheet, int rowIndex,
            int startCol, String... values) {
        return POIUtils.buildRowWithStringValues(sheet, rowIndex, startCol,
            CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_TOP, values);
    }

    public static Row buildRowWithStringValues(Sheet sheet, int rowIndex,
            int startCol, String... values) {
        return POIUtils.buildRowWithStringValues(sheet, rowIndex, startCol,
            CellStyle.ALIGN_RIGHT, CellStyle.VERTICAL_TOP, values);
    }

    public static void buildColumnWithValues(Sheet sheet, int colIndex,
            int startRow, short hAlign, short vAlign, String... values) {
        for (int i = startRow; i < startRow + values.length; i++) {
            Row r = sheet.createRow(i);
            POIUtils.createCell(r, colIndex, hAlign, vAlign).setCellValue(
                values[i - startRow]);
        }
    }

    public static void buildColumnWithValues(Sheet sheet, int colIndex,
            int startRow, String... values) {
        POIUtils.buildColumnWithValues(sheet, colIndex, startRow,
            CellStyle.ALIGN_RIGHT, CellStyle.VERTICAL_TOP, values);
    }

    public static Cell createCell(Row row, int index, short hAlign, short vAlign) {
        Cell cell = row.createCell(index);

        CellStyle style = row.getSheet().getWorkbook().createCellStyle();
        style.setAlignment(hAlign);
        style.setVerticalAlignment(vAlign);
        cell.setCellStyle(style);

        return cell;
    }

    public static Cell createCell(Row row, int index) {
        return POIUtils.createCell(row, index, CellStyle.ALIGN_RIGHT,
            CellStyle.VERTICAL_TOP);
    }

    /**
     * 构建“总计”单元格
     *
     * @param cell
     * @param size
     * @return
     */
    public static Cell sumCell(Cell cell, int size) {
        int rowIndex = cell.getRowIndex();
        int colIndex = cell.getColumnIndex();
        StringBuilder sumBuilder = new StringBuilder(5);
        sumBuilder
        .append("SUM(")
        .append(new CellReference(rowIndex + 1, colIndex).formatAsString())
        .append(':')
        .append(
            new CellReference(rowIndex + size, colIndex).formatAsString())
            .append(')');
        cell.setCellFormula(sumBuilder.toString());
        return cell;
    }

    /**
     * 格式化单元格格式为金额样式
     *
     * @param cell
     * @return
     */
    public static Cell formatCell4Amount(Cell cell) {
        Workbook workbook = cell.getSheet().getWorkbook();
        short amountFormat = workbook.createDataFormat().getFormat("#,##0.00");
        CellStyle cellStyle = cell.getCellStyle();
        cellStyle.setDataFormat(amountFormat);
        cell.setCellStyle(cellStyle);
        return cell;
    }

    public static String convertToCSV(Sheet sheet, int columns) {
        Iterator<Row> rows = sheet.iterator();
        StringBuilder sb = new StringBuilder();

        while (rows.hasNext()) {
            Row row = rows.next();

            for (int i = 0; i < columns; i++) {
                Cell cell = row.getCell(i);

                if (cell != null) {
                    switch (cell.getCellType()) {
                        case Cell.CELL_TYPE_STRING:
                            sb.append(cell.getStringCellValue());
                            break;
                        case Cell.CELL_TYPE_NUMERIC:
                            NumberFormat format = NumberFormat.getInstance();
                            format.setGroupingUsed(false);
                            sb.append(format.format(cell.getNumericCellValue()));
                            break;
                    }
                }

                sb.append(',');
            }
            sb.delete(sb.length() - 1, sb.length()).append("\r\n"); // 删掉最后一个逗号，加回车
        }

        return sb.toString();
    }
}
