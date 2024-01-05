package com.util.model;

import java.util.List;

public record DepartmentAndSubBranches(String departmentId, List<String> subBranchesId) {
}
