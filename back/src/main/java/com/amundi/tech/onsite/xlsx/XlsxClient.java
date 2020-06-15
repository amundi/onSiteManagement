package com.amundi.tech.onsite.xlsx;

import com.amundi.tech.onsite.db.model.UserRole;
import com.amundi.tech.onsite.db.model.WorkingDay;
import com.amundi.tech.onsite.model.Parking;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Component
public class XlsxClient {

    private DateTimeFormatter dtf = DateTimeFormatter.ISO_DATE;
    private static final int colsPerDay = 24;

    public XSSFWorkbook createUserReport(List<WorkingDay> days, List<UserRole> roles, LocalDate reportDate, int nbWeek) {
        XSSFWorkbook workbook = new XSSFWorkbook();

        // Create styles -----------------------------
        IndexedColorMap colorMap = workbook.getStylesSource().getIndexedColors();
        XSSFColor title = new XSSFColor(new java.awt.Color(5, 71, 88), colorMap);
        XSSFColor primary = new XSSFColor(new java.awt.Color(16, 117, 144), colorMap);
        XSSFColor secondary = new XSSFColor(new java.awt.Color(0, 182, 232), colorMap);
        XSSFColor tertiary = new XSSFColor(new java.awt.Color(150, 232, 255), colorMap);
        XSSFColor total = new XSSFColor(new java.awt.Color(219, 184, 90), colorMap);
        XSSFColor white = new XSSFColor(new java.awt.Color(255, 255, 255), colorMap);

        XSSFFont titleFont = workbook.createFont();
        titleFont.setColor(IndexedColors.WHITE.getIndex());
        titleFont.setFontHeightInPoints((short) 16);
        XSSFCellStyle titleStyle = workbook.createCellStyle();
        titleStyle.setFillForegroundColor(title);
        titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        titleStyle.setFont(titleFont);

        XSSFFont primaryFont = workbook.createFont();
        primaryFont.setFontHeightInPoints((short) 14);
        primaryFont.setBold(true);
        primaryFont.setColor(IndexedColors.WHITE.getIndex());
        XSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(primary);
        headerStyle.setBorderRight(BorderStyle.THIN);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setFont(primaryFont);

        XSSFFont secondaryFont = workbook.createFont();
        secondaryFont.setFontHeightInPoints((short) 12);
        XSSFCellStyle secondHeaderStyle = workbook.createCellStyle();
        secondHeaderStyle.setFillForegroundColor(secondary);
        secondHeaderStyle.setBorderRight(BorderStyle.THIN);
        secondHeaderStyle.setBorderBottom(BorderStyle.THIN);
        secondHeaderStyle.setBorderLeft(BorderStyle.THIN);
        secondHeaderStyle.setBorderTop(BorderStyle.THIN);
        secondHeaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        secondHeaderStyle.setFont(secondaryFont);

        XSSFFont rotatedFont = workbook.createFont();
        rotatedFont.setFontHeightInPoints((short) 12);

        XSSFCellStyle headerRotatedStyle = workbook.createCellStyle();
        headerRotatedStyle.setFillForegroundColor(tertiary);
        headerRotatedStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerRotatedStyle.setRotation((short) 330);
        headerRotatedStyle.setFont(rotatedFont);

        XSSFCellStyle totalRotatedStyle = workbook.createCellStyle();
        totalRotatedStyle.setFillForegroundColor(total);
        totalRotatedStyle.setBorderRight(BorderStyle.THIN);
        totalRotatedStyle.setBorderLeft(BorderStyle.THIN);
        totalRotatedStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        totalRotatedStyle.setRotation((short) 330);
        totalRotatedStyle.setFont(rotatedFont);

        // Create header -----------------------------
        XSSFSheet sheet = workbook.createSheet("report");
        XSSFRow firstHeaderRow = sheet.createRow(0);
        XSSFRow secondHeaderRow = sheet.createRow(1);
        XSSFRow thirdHeaderRow = sheet.createRow(2);

        XSSFCell cell = firstHeaderRow.createCell(0);
        cell.setCellStyle(titleStyle);
        cell.setCellValue("POST Covid");
        sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 4));

        cell = thirdHeaderRow.createCell(0);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("User");

        cell = thirdHeaderRow.createCell(1);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Given name");

        cell = thirdHeaderRow.createCell(2);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Last name");

        cell = thirdHeaderRow.createCell(3);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Department");

        cell = thirdHeaderRow.createCell(4);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Role");

        int colIndex = 5;
        LocalDate date = reportDate;
        for (int i = 0; i < nbWeek * 7; i++) {
            cell = firstHeaderRow.createCell(colIndex);
            cell.setCellStyle(headerStyle);
            cell.setCellValue(dtf.format(date));

            for (int j = 0; j < colsPerDay; j++) {
                cell = firstHeaderRow.createCell(colIndex + j);
                // First header row
                cell.setCellStyle(headerStyle);
                if (j == 0) {
                    cell.setCellValue(dtf.format(date));
                }

                // Second header row
                cell = secondHeaderRow.createCell(colIndex + j);
                cell.setCellStyle(secondHeaderStyle);
                if (j == 0) {
                    cell.setCellValue("Arrival");
                } else if (j == 8) {
                    cell.setCellValue("Lunch");
                } else if (j == 13) {
                    cell.setCellValue("Departure");
                } else if (j == 18) {
                    cell.setCellValue("Parking");
                } else if (j == 21) {
                    cell.setCellValue("Total");
                }

                // Third header row
                cell = thirdHeaderRow.createCell(colIndex + j);
                cell.setCellStyle(headerRotatedStyle);
                if (j == 0) {
                    cell.setCellValue("7:00");
                } else if (j == 1) {
                    cell.setCellValue("7:30");
                } else if (j == 2) {
                    cell.setCellValue("8:00");
                } else if (j == 3) {
                    cell.setCellValue("8:30");
                } else if (j == 4) {
                    cell.setCellValue("9:00");
                } else if (j == 5) {
                    cell.setCellValue("9:30");
                } else if (j == 6) {
                    cell.setCellValue("10:00");
                } else if (j == 7) {
                    cell.setCellValue("10:30");
                } else if (j == 8) {
                    cell.setCellValue("11:30");
                } else if (j == 9) {
                    cell.setCellValue("12:00");
                } else if (j == 10) {
                    cell.setCellValue("12:30");
                } else if (j == 11) {
                    cell.setCellValue("13:00");
                } else if (j == 12) {
                    cell.setCellValue("13:30");
                } else if (j == 13) {
                    cell.setCellValue("17:00");
                } else if (j == 14) {
                    cell.setCellValue("17:30");
                } else if (j == 15) {
                    cell.setCellValue("18:00");
                } else if (j == 16) {
                    cell.setCellValue("18:30");
                } else if (j == 17) {
                    cell.setCellValue("19:00");
                } else if (j == 18) {
                    cell.setCellValue("car");
                } else if (j == 19) {
                    cell.setCellValue("moto");
                } else if (j == 20) {
                    cell.setCellValue("bike");
                } else if (j == 21) {
                    cell.setCellValue("rest.");
                    cell.setCellStyle(totalRotatedStyle);

                } else if (j == 22) {
                    cell.setCellValue("park.");
                    cell.setCellStyle(totalRotatedStyle);

                } else {
                    cell.setCellValue("site");
                    cell.setCellStyle(totalRotatedStyle);
                    sheet.setColumnWidth(colIndex + j, 4000);
                }

            }
            sheet.groupColumn(colIndex, colIndex + 22);
            sheet.setColumnGroupCollapsed(colIndex, true);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, colIndex, colIndex + 23));
            sheet.addMergedRegion(new CellRangeAddress(1, 1, colIndex, colIndex + 7));
            sheet.addMergedRegion(new CellRangeAddress(1, 1, colIndex + 8, colIndex + 12));
            sheet.addMergedRegion(new CellRangeAddress(1, 1, colIndex + 13, colIndex + 17));
            sheet.addMergedRegion(new CellRangeAddress(1, 1, colIndex + 18, colIndex + 20));
            sheet.addMergedRegion(new CellRangeAddress(1, 1, colIndex + 21, colIndex + 23));
            date = date.plusDays(1);
            colIndex += colsPerDay;
        }

        int rowIndex = 3;
        for (UserRole role : roles) {
            XSSFRow row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(role.getUser());
            row.createCell(1).setCellValue(role.getGivenName() != null ? role.getGivenName() : "");
            row.createCell(2).setCellValue(role.getLastName() != null ? role.getLastName() : "");
            row.createCell(3).setCellValue(role.getDepartment() != null ? role.getDepartment() : "");
            row.createCell(4).setCellValue(role.getRole());
            populateRow(role.getUser(), row, days, reportDate);
        }
        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);
        sheet.autoSizeColumn(3);
        sheet.autoSizeColumn(4);
        sheet.autoSizeColumn(5);
        sheet.createFreezePane(5, 3);

        return workbook;
    }

    private void populateRow(String user, XSSFRow row, List<WorkingDay> days, LocalDate reportDate) {
        days.stream()
                .filter(workingDay -> user.equals(workingDay.getUser()))
                .forEach(workingDay -> {
                    int nbDays = (int) ChronoUnit.DAYS.between(reportDate, workingDay.getDate());

                    // Arrival
                    if (workingDay.getArrival() == 700) {
                        XSSFCell cell = row.createCell(6 + nbDays * colsPerDay);
                        cell.setCellValue("X");
                    } else if (workingDay.getArrival() == 730) {
                        XSSFCell cell = row.createCell(7 + nbDays * colsPerDay);
                        cell.setCellValue("X");
                    } else if (workingDay.getArrival() == 800) {
                        XSSFCell cell = row.createCell(8 + nbDays * colsPerDay);
                        cell.setCellValue("X");
                    } else if (workingDay.getArrival() == 830) {
                        XSSFCell cell = row.createCell(9 + nbDays * colsPerDay);
                        cell.setCellValue("X");
                    } else if (workingDay.getArrival() == 900) {
                        XSSFCell cell = row.createCell(10 + nbDays * colsPerDay);
                        cell.setCellValue("X");
                    } else if (workingDay.getArrival() == 930) {
                        XSSFCell cell = row.createCell(11 + nbDays * colsPerDay);
                        cell.setCellValue("X");
                    } else if (workingDay.getArrival() == 1000) {
                        XSSFCell cell = row.createCell(12 + nbDays * colsPerDay);
                        cell.setCellValue("X");
                    }

                    // Lunchs
                    if (workingDay.getLunch() == 1130) {
                        XSSFCell cell = row.createCell(13 + nbDays * colsPerDay);
                        cell.setCellValue("X");
                    }
                    if (workingDay.getLunch() == 1200) {
                        XSSFCell cell = row.createCell(14 + nbDays * colsPerDay);
                        cell.setCellValue("X");
                    }
                    if (workingDay.getLunch() == 1230) {
                        XSSFCell cell = row.createCell(15 + nbDays * colsPerDay);
                        cell.setCellValue("X");
                    }
                    if (workingDay.getLunch() == 1300) {
                        XSSFCell cell = row.createCell(16 + nbDays * colsPerDay);
                        cell.setCellValue("X");
                    }
                    if (workingDay.getLunch() == 1330) {
                        XSSFCell cell = row.createCell(17 + nbDays * colsPerDay);
                        cell.setCellValue("X");
                    }

                    // Departures
                    if (workingDay.getDeparture() == 1700) {
                        XSSFCell cell = row.createCell(18 + nbDays * colsPerDay);
                        cell.setCellValue("X");
                    }
                    if (workingDay.getDeparture() == 1730) {
                        XSSFCell cell = row.createCell(19 + nbDays * colsPerDay);
                        cell.setCellValue("X");
                    }
                    if (workingDay.getDeparture() == 1800) {
                        XSSFCell cell = row.createCell(20 + nbDays * colsPerDay);
                        cell.setCellValue("X");
                    }
                    if (workingDay.getDeparture() == 1830) {
                        XSSFCell cell = row.createCell(21 + nbDays * colsPerDay);
                        cell.setCellValue("X");
                    }
                    if (workingDay.getDeparture() == 1900) {
                        XSSFCell cell = row.createCell(22 + nbDays * colsPerDay);
                        cell.setCellValue("X");
                    }

                    if (Parking.CAR.equals(workingDay.getParkingType())) {
                        XSSFCell cell = row.createCell(23 + nbDays * colsPerDay);
                        cell.setCellValue("X");
                    }
                    if (Parking.MOTO.equals(workingDay.getParkingType())) {
                        XSSFCell cell = row.createCell(24 + nbDays * colsPerDay);
                        cell.setCellValue("X");
                    }
                    if (Parking.BIKE.equals(workingDay.getParkingType())) {
                        XSSFCell cell = row.createCell(25 + nbDays * colsPerDay);
                        cell.setCellValue("X");
                    }
                    if (workingDay.getRestaurant() != null) {
                        XSSFCell cell = row.createCell(26 + nbDays * colsPerDay);
                        cell.setCellValue(workingDay.getRestaurant().getName());
                    }
                    if (workingDay.getParking() != null) {
                        XSSFCell cell = row.createCell(27 + nbDays * colsPerDay);
                        cell.setCellValue(workingDay.getParking().getName());
                    }
                    if (workingDay.getSite() != null) {
                        XSSFCell cell = row.createCell(28 + nbDays * colsPerDay);
                        if(workingDay.isOffSite()) {
                            cell.setCellValue("offsite");
                        } else {
                            cell.setCellValue(workingDay.getSite().getName());
                        }
                    }
                });
    }
}
