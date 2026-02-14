package com.ems.employee_backend.service;

import com.ems.employee_backend.dto.PayrollRequest;
import com.ems.employee_backend.dto.PayrollResponse;
import com.ems.employee_backend.entity.Employee;
import com.ems.employee_backend.entity.Payroll;
import com.ems.employee_backend.repository.EmployeeRepository;
import com.ems.employee_backend.repository.PayrollRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PayrollService {

    private final PayrollRepository payrollRepository;
    private final EmployeeRepository employeeRepository;

    public PayrollResponse create(PayrollRequest request) {
        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        Payroll payroll = Payroll.builder()
                .employee(employee)
                .payPeriodStart(request.getPayPeriodStart())
                .payPeriodEnd(request.getPayPeriodEnd())
                .baseSalary(request.getBaseSalary())
                .overtimeHours(request.getOvertimeHours() != null ? request.getOvertimeHours() : 0.0)
                .overtimeRate(request.getOvertimeRate() != null ? request.getOvertimeRate() : 0.0)
                .overtimePay(request.getOvertimePay() != null ? request.getOvertimePay() : 0.0)
                .bonuses(request.getBonuses() != null ? request.getBonuses() : 0.0)
                .deductions(request.getDeductions() != null ? request.getDeductions() : 0.0)
                .taxes(request.getTaxes() != null ? request.getTaxes() : 0.0)
                .insurance(request.getInsurance() != null ? request.getInsurance() : 0.0)
                .retirement(request.getRetirement() != null ? request.getRetirement() : 0.0)
                .status("pending")
                .build();

        // Calculate gross and net pay
        Double grossPay = payroll.getBaseSalary() + payroll.getOvertimePay() + payroll.getBonuses();
        payroll.setGrossPay(grossPay);
        
        Double netPay = grossPay - payroll.getDeductions() - payroll.getTaxes() - 
                       payroll.getInsurance() - payroll.getRetirement();
        payroll.setNetPay(netPay);

        Payroll savedPayroll = payrollRepository.save(payroll);
        return convertToResponse(savedPayroll);
    }

    public List<PayrollResponse> getAll() {
        return payrollRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public PayrollResponse getById(Long id) {
        Payroll payroll = payrollRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payroll record not found"));
        return convertToResponse(payroll);
    }

    public List<PayrollResponse> getByPeriod(String period) {
        return payrollRepository.findByPayPeriod(period).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<PayrollResponse> getByEmployeeId(Long employeeId) {
        return payrollRepository.findByEmployeeId(employeeId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<PayrollResponse> getByStatus(String status) {
        return payrollRepository.findByStatus(status).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public PayrollResponse process(Long id) {
        Payroll payroll = payrollRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payroll record not found"));

        payroll.setStatus("processed");

        Payroll updatedPayroll = payrollRepository.save(payroll);
        return convertToResponse(updatedPayroll);
    }

    public PayrollResponse markAsPaid(Long id, LocalDate paymentDate) {
        Payroll payroll = payrollRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payroll record not found"));

        if (!"processed".equals(payroll.getStatus())) {
            throw new RuntimeException("Payroll must be processed before marking as paid");
        }

        payroll.setStatus("paid");
        payroll.setPaymentDate(paymentDate);

        Payroll updatedPayroll = payrollRepository.save(payroll);
        return convertToResponse(updatedPayroll);
    }

    public void delete(Long id) {
        if (!payrollRepository.existsById(id)) {
            throw new RuntimeException("Payroll record not found");
        }
        payrollRepository.deleteById(id);
    }

    private PayrollResponse convertToResponse(Payroll payroll) {
        PayrollResponse response = new PayrollResponse();
        response.setId(payroll.getId());
        response.setEmployeeId(payroll.getEmployee().getId());
        response.setEmployeeName(payroll.getEmployee().getFirstName() + " " + payroll.getEmployee().getLastName());
        response.setPayPeriodStart(payroll.getPayPeriodStart());
        response.setPayPeriodEnd(payroll.getPayPeriodEnd());
        response.setBaseSalary(payroll.getBaseSalary());
        response.setOvertimeHours(payroll.getOvertimeHours());
        response.setOvertimeRate(payroll.getOvertimeRate());
        response.setOvertimePay(payroll.getOvertimePay());
        response.setBonuses(payroll.getBonuses());
        response.setDeductions(payroll.getDeductions());
        response.setGrossPay(payroll.getGrossPay());
        response.setNetPay(payroll.getNetPay());
        response.setTaxes(payroll.getTaxes());
        response.setInsurance(payroll.getInsurance());
        response.setRetirement(payroll.getRetirement());
        response.setPaymentDate(payroll.getPaymentDate());
        response.setStatus(payroll.getStatus());
        response.setCreatedAt(payroll.getCreatedAt());
        response.setUpdatedAt(payroll.getUpdatedAt());
        return response;
    }
}
