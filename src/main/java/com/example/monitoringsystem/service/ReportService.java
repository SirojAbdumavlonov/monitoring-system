package com.example.monitoringsystem.service;

import com.example.monitoringsystem.entity.Efficiency;
import com.example.monitoringsystem.entity.ExactColumns;
import com.example.monitoringsystem.entity.ExactValues;
import com.example.monitoringsystem.model.FromAndToDates;
import com.example.monitoringsystem.model.ReportResponse;
import com.example.monitoringsystem.model.ValueWithEfficiency;
import com.example.monitoringsystem.repository.DepartmentRepository;
import com.example.monitoringsystem.repository.EfficiencyRepository;
import com.example.monitoringsystem.repository.ExactValuesRepository;
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
    public Object getReportData(String userId, LocalDate date,
                                String chosenDepartment, LocalDate from,
                                LocalDate to, String timeRange,
                                String monthName, int lastNDays,
                                Collection<? extends GrantedAuthority> role,
                                String option) {

        List<ValueWithEfficiency> valueWithEfficiencies = exactColumnsService.getPreviousDaysData
                (userId, date, chosenDepartment,
                from, to, timeRange,
                monthName, lastNDays, role, option);

        ExactValues exactValues =
                exactValuesRepository.findByDepartmentId(chosenDepartment);


        return new ReportResponse(valueWithEfficiencies, exactValues);
    }
    public List<ValueWithEfficiency> mergeValueWithEfficiency
            (List<Efficiency> efficiencyList, List<ExactColumns> exactColumnsList){

        List<ValueWithEfficiency> valueWithEfficiencies = null;

        for(int i = 0; i < efficiencyList.size(); i++){
            valueWithEfficiencies.add(
                    new ValueWithEfficiency(exactColumnsList.get(i), efficiencyList.get(i))
            );
        }

        return valueWithEfficiencies;
    }

}
