package com.codecool.tasx.controller.dto.requests;

import com.codecool.tasx.model.requests.RequestStatus;

public record CompanyJoinRequestUpdateDto(Long userId, Long companyId, RequestStatus status) {
}
