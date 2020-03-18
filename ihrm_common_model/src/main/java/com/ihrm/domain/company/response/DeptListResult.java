package com.ihrm.domain.company.response;

import com.ihrm.domain.company.Company;
import com.ihrm.domain.company.Department;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class DeptListResult {
    private String companyId;
    private String companyName;
    private String companyManager;
    private List<Department> deps;

    public DeptListResult(Company company, List<Department> deps) {
        this.companyId = company.getId();
        this.companyManager=company.getLegalRepresentative();//公司联系人
        this.companyName=company.getName();
        this.deps = deps;
    }
}
