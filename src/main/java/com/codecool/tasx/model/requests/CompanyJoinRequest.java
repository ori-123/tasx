package com.codecool.tasx.model.requests;

import com.codecool.tasx.model.company.Company;
import com.codecool.tasx.model.user.User;
import jakarta.persistence.*;

@Entity
@Table(name = "company_join_request")
public class CompanyJoinRequest {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "company_id")
  private Company company;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @Enumerated(EnumType.STRING)
  private RequestStatus status;
}
