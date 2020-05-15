package com.amundi.tech.onsite.rest;

import com.amundi.tech.onsite.db.UserRoleRepository;
import com.amundi.tech.onsite.db.WorkingDayRepository;
import com.amundi.tech.onsite.db.model.UserRole;
import com.amundi.tech.onsite.db.model.WorkingDay;
import com.amundi.tech.onsite.xlsx.XlsxClient;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/report")
@AllArgsConstructor
@Tag(name = "report", description = "manage reports")
public class ReportResource {

    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final WorkingDayRepository workingDayRepository;
    private final UserRoleRepository userRoleRepository;
    private final XlsxClient xlsxClient;

    @GetMapping(path = "/weekly", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity xlsxReport(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate) throws IOException {

        LocalDate reportDate = startDate != null ? startDate.with(DayOfWeek.MONDAY) : LocalDate.now().with(DayOfWeek.MONDAY);
        LocalDate st = reportDate.minusDays(1);
        LocalDate end = reportDate.plusDays(7);

        List<WorkingDay> days = workingDayRepository.findAllByDateAfterAndDateBefore(st, end);
        List<UserRole> roles = userRoleRepository.findAll();

        Workbook workbook = xlsxClient.createUserReport(days, roles, reportDate, 1);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        workbook.write(bos);
        bos.close();
        byte[] body = bos.toByteArray();

        return ResponseEntity.ok()
                .contentLength(body.length)
                .header("Content-Disposition", "attachment; filename=\"report-" + dtf.format(reportDate) + ".xlsx\"")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(body);
    }
}
