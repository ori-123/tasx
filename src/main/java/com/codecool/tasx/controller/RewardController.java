package com.codecool.tasx.controller;

import com.codecool.tasx.controller.dto.company.CompanyResponsePrivateDTO;
import com.codecool.tasx.controller.dto.reward.RewardCreateRequestDto;
import com.codecool.tasx.controller.dto.reward.RewardResponseDto;
import com.codecool.tasx.controller.dto.reward.RewardUpdateRequestDto;
import com.codecool.tasx.controller.dto.user.UserResponsePublicDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/companies/{companyId}/rewards")
public class RewardController {
    @GetMapping("/")
    public ResponseEntity<?> getAllRewards(@PathVariable long companyId) {
        try {
            // TODO: impl
            List<RewardResponsePublicDto> rewards = List.of(
                    new RewardResponsePublicDto("Fake reward 1", "Public reward details"),
                    new RewardResponsePublicDto("Fake reward 2", "Public reward details")
            );

            return ResponseEntity.ok(Map.of("data", rewards));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to load rewards"));
        }
    }

    @GetMapping("/{rewardId}")
    public ResponseEntity<?> getRewardById(@PathVariable long companyId, @PathVariable long rewardId) {
        try {
            //TODO: impl
            RewardResponseDto reward = new RewardResponseDto(
                    1L,
                    "Fake reward 3",
                    "Reward details",
                    new CompanyResponsePrivateDTO(4L,
                            "Mock company name",
                            "Company description",
                            new UserResponsePublicDto(5L,
                                    "Mr. Foobar")),
                    Set.of() //empty set representing the set of users with this reward
            );

            return ResponseEntity.ok(Map.of("data", reward));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", "Failed to load reward with ID" + rewardId)
            );
        }
    }

    @PostMapping("/")
    public ResponseEntity<?> createReward(
            @PathVariable long companyId,
            @RequestBody RewardCreateRequestDto rewardDetails) {
        try {
            //TODO: impl
            RewardResponseDto rewardResponseDetails = new RewardResponseDto(
                    7, rewardDetails.name(), rewardDetails.description(),
                    new CompanyResponsePrivateDTO(companyId, "Mock company " + companyId,
                            "Company details to be seen only by the employees of the company",
                            new UserResponsePublicDto(1L, "Company Owner")),
                    Set.of() //empty set representing the set of users with this reward
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "message", "Reward created successfully",
                    "data", rewardResponseDetails));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                    Map.of("error", "Failed to create reward"));
        }
    }

    @PutMapping("/{rewardId}")
    public ResponseEntity<?> updateReward(@PathVariable long companyId, @PathVariable long rewardId,
                                          @RequestBody RewardUpdateRequestDto rewardDetails) {
        try {
            //TODO: impl
            RewardResponseDto rewardResponseDetails = new RewardResponseDto(
                    rewardId, rewardDetails.name(), rewardDetails.description(),
                    new CompanyResponsePrivateDTO(companyId, "Mock Company", "Company Details",
                            new UserResponsePublicDto(7L, "Company Owner")),
                    Set.of() //empty set representing the set of users with this reward
            );

            return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                    "message", "Reward with ID " + rewardId + " updated successfully",
                    "data", rewardResponseDetails));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                    Map.of("error", "Failed to update reward with ID " + rewardId));
        }
    }

    @DeleteMapping("/{rewardId}")
    public ResponseEntity<?> deleteReward(@PathVariable long companyId, @PathVariable long rewardId) {
        try {
            //TODO: impl
            return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                    "message",
                    "Reward with ID " + rewardId + " deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                    Map.of("error", "Failed to delete reward with ID " + rewardId));
        }
    }
}
