package com.foxconn.fii.common.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellAddress;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@UtilityClass
public class ExcelUtils {

    public String getStringValue(Cell cell) {
        if (cell == null) {
            return "";
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    return df.format(cell.getDateCellValue());
                } else {
                    double number = cell.getNumericCellValue();
                    BigDecimal bd = BigDecimal.valueOf(number);
                    return bd.toPlainString();
                }
            case FORMULA:
//                log.debug("### getStringValue error - formula {} - {}", cell.getAddress(), cell.getCellFormula());
                return "";
            case ERROR:
//                log.debug("### getStringValue error - error {} - {}", cell.getAddress(), FormulaError.forInt(cell.getErrorCellValue()).getString());
                return "";
            default:
//                log.info("### getStringValue error - default {} - {}", cell.getAddress(), cell.getCellType());
                return "";
        }
    }

    public Double getDoubleValue(Cell cell) {
        if (cell == null) {
            return 0D;
        }

        switch (cell.getCellType()) {
            case NUMERIC:
                return cell.getNumericCellValue();
            case STRING:
                try {
                    return Double.parseDouble(cell.getStringCellValue());
                } catch (Exception e) {
                    return 0D;
                }
            default:
                return 0D;
        }
    }

    public Integer getIntegerValue(Cell cell) {
        if (cell == null) {
            return 0;
        }

        switch (cell.getCellType()) {
            case NUMERIC:
                return (int)cell.getNumericCellValue();
            case STRING:
                try {
                    return Integer.parseInt(cell.getStringCellValue());
                } catch (Exception e) {
                    return 0;
                }
            default:
                return 0;
        }
    }

    public Double getDateExcelValue(Cell cell, String pattern) {
        if (cell == null) {
            return null;
        }

        SimpleDateFormat df = new SimpleDateFormat(pattern);

        switch (cell.getCellType()) {
            case NUMERIC:
                return cell.getNumericCellValue();
            case STRING:
                try {
                    return DateUtil.getExcelDate(df.parse(cell.getStringCellValue()));
                } catch (ParseException e) {
                    log.error("### getDateValue format error {}", cell.getStringCellValue());
                    return null;
                }
            default:
                return null;
        }
    }

    public Date getDateValue(Cell cell, String pattern) {
        if (cell == null) {
            return null;
        }

        SimpleDateFormat df = new SimpleDateFormat(pattern);

        switch (cell.getCellType()) {
            case NUMERIC:
                return DateUtil.getJavaDate(cell.getNumericCellValue());
            case STRING:
                try {
                    return df.parse(cell.getStringCellValue());
                } catch (ParseException e) {
                    log.error("### getDateValue format error {}", cell.getStringCellValue());
                    return null;
                }
            default:
                return null;
        }
    }

    public void copyRow(Sheet oldSheet, Sheet newSheet, int irStartOldSheet, int irStartNewSheet, int numberRow) {
        for (int i = 0; i < numberRow; i++) {
            for(Cell oldCell : oldSheet.getRow(irStartOldSheet + i)) {
                if (newSheet.getRow(irStartNewSheet + i) == null) {
                    newSheet.createRow(irStartNewSheet + i);
                }

                Cell newCell = newSheet.getRow(irStartNewSheet + i).createCell(oldCell.getColumnIndex());
                newCell.setCellStyle(oldCell.getCellStyle());
                switch (oldCell.getCellType()) {
                    case STRING:
                        newCell.setCellValue(oldCell.getRichStringCellValue());
                        break;
                    case NUMERIC:
                        newCell.setCellValue(oldCell.getNumericCellValue());
                        break;
                    case BOOLEAN:
                        newCell.setCellValue(oldCell.getBooleanCellValue());
                        break;
                    case FORMULA:
                        String oldFormula = oldCell.getCellFormula();
                        String[] cellAddresses = oldFormula.split("[+\\-\\*\\/]");
                        for (String cellAddress : cellAddresses) {
                            CellAddress oldCellAddress = new CellAddress(cellAddress);
                            CellAddress newCellAddress = new CellAddress(newCell.getRowIndex() + oldCellAddress.getRow() - oldCell.getRowIndex(), oldCellAddress.getColumn());
                            oldFormula = oldFormula.replace(cellAddress, newCellAddress.formatAsString());
                        }
                        newCell.setCellFormula(oldFormula);
                        break;
                    case BLANK:
                        newCell.setBlank();
                        break;
                    case ERROR:
                        newCell.setCellErrorValue(oldCell.getErrorCellValue());
                        break;
                }
            }
        }
    }
}
