package com.codecool.tasx.model.company.reward;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RewardDao extends JpaRepository<Reward, Long> {
}
