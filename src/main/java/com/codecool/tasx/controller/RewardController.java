package com.codecool.tasx.controller;

import com.codecool.tasx.controller.dto.reward.RewardResponseDto;
import com.codecool.tasx.exception.reward.RewardNotFoundException;
import com.codecool.tasx.service.company.reward.RewardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/companies/{companyId}/rewards")
public class RewardController {
    private final RewardService rewardService;

    @Autowired
    public RewardController(RewardService rewardService) {
        this.rewardService = rewardService;
    }

    @GetMapping
    public ResponseEntity<?> getAllRewards(@PathVariable Long companyId) {
        List<RewardResponseDto> rewards = rewardService.getAllRewards(companyId);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("data", rewards));
    }

    @GetMapping("/{rewardId}")
    public ResponseEntity<?> getRewardById(@PathVariable Long companyId, @PathVariable Long rewardId) {
        RewardResponseDto reward = rewardService.getRewardById(rewardId, companyId)
                .orElseThrow(() -> new RewardNotFoundException(rewardId));
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("data", reward));
    }


}
