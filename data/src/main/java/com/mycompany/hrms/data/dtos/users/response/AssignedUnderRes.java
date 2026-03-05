package com.mycompany.hrms.data.dtos.users.response;

import java.util.List;

public class AssignedUnderRes {
    private List<OrgChartRes> chart;
    private List<OrgChartRes> assignedUnder;

    public List<OrgChartRes> getChart() {
        return chart;
    }

    public void setChart(List<OrgChartRes> chart) {
        this.chart = chart;
    }

    public List<OrgChartRes> getAssignedUnder() {
        return assignedUnder;
    }

    public void setAssignedUnder(List<OrgChartRes> assignedUnder) {
        this.assignedUnder = assignedUnder;
    }
}
