package com.eneifour.fantry.dashboard.controller;

import com.eneifour.fantry.dashboard.dto.*;
import com.eneifour.fantry.dashboard.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/admin/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    //통합 대시보드
    @GetMapping("/integrated")
    public ResponseEntity<DashboardStats> getIntegratedDashboard() {
        return ResponseEntity.ok(dashboardService.getIntegratedDashboard());
    }

    //회원관리 대시보드
    @GetMapping("/members")
    public ResponseEntity<MemberDashboardStats> getMemberDashboardStats() {
        return ResponseEntity.ok(dashboardService.getMemberDashboardStats());
    }

    //재고관리대시보드
    @GetMapping("/orders")
    public ResponseEntity<OrdersStats> getOrdersDashboard() {
        return ResponseEntity.ok(dashboardService.getOrdersDashboard());
    }

    //판매관리 대시보드
    @GetMapping("/sales")
    public ResponseEntity<SalesStats> getSalesStats() {
        return ResponseEntity.ok(dashboardService.getSalesStats());
    }

    //주문관리 대시보드

    //입찰관리 대시보드
    @GetMapping("/bids")
    public ResponseEntity<BidStats> getBidStats() {
        return ResponseEntity.ok(dashboardService.getBidStats());
    }

    //검수관리 대시보드
    @GetMapping("/inspections")
    public ResponseEntity<InspectionStats> getInspectionStats() {
        return ResponseEntity.ok(dashboardService.getInspectionStats());
    }

    //카탈로그관리 대시보드
    @GetMapping("/catalogs-overview")
    public ResponseEntity<CatalogDashboardStats> getCatalogDashboardStats() {
        return ResponseEntity.ok(dashboardService.getCatalogDashboardStats());
    }

    // CS관리 대시보드
    @GetMapping("/cs")
    public ResponseEntity<CSDashboardStats> getCSDashboardStats() {
        return ResponseEntity.ok(dashboardService.getCSDashboardStats());
    }


    //재무/운영관리 대시보드
    @GetMapping("/finance-operations")
    public ResponseEntity<FinanceOperationsDashboardStats> getFinanceOperationsDashboardStats() {
        return ResponseEntity.ok(dashboardService.getFinanceOperationsDashboardStats());
    }
}