package com.example.monitoringsystem.service;

import com.example.monitoringsystem.entity.ExactValues;
import com.example.monitoringsystem.entity.TableChanges;
import com.example.monitoringsystem.model.FromAndToDates;
import com.example.monitoringsystem.model.ReportResponse;
import com.example.monitoringsystem.model.ValueWithEfficiency;
import com.example.monitoringsystem.repository.ExactValuesRepository;
import com.example.monitoringsystem.repository.TableChangesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final ExactColumnsService exactColumnsService;
    private final ExactValuesRepository exactValuesRepository;
    private final TableChangesRepository tableChangesRepository;
    public ReportResponse getReportData(String userId, LocalDate date,
                                String chosenDepartment, LocalDate from,
                                LocalDate to, String timeRange,
                                String monthName, int lastNDays,
                                Collection<? extends GrantedAuthority> role) {

        FromAndToDates dates =
                exactColumnsService.getFromAndToDates(date, from, to, timeRange, monthName, lastNDays);

        from = dates.from();
        to = dates.to();

        List<ValueWithEfficiency> valueWithEfficiencies = exactColumnsService.getPreviousDaysData
                            (userId, chosenDepartment, from, to, role);

        ExactValues exactValues =
                exactValuesRepository.findByDepartmentId(chosenDepartment);

        List<TableChanges> tableChangesList =
                tableChangesRepository
                        .findAllByDepartmentIdAndCreatedDateBetweenOrderByCreatedDateDesc
                                (chosenDepartment, from, to);


        return new ReportResponse(valueWithEfficiencies, exactValues, tableChangesList);
    }


}
