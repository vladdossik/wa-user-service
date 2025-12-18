package org.wa.user.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.http.HttpStatus;
import org.wa.user.service.dto.ErrorResponse;
import java.time.OffsetDateTime;

@Mapper(componentModel = "spring", imports = OffsetDateTime.class)
public interface ErrorMapper {
    @Mapping(target = "message", source = "message")
    @Mapping(target = "status", expression = "java(status.value())")
    @Mapping(target = "timestamp", expression = "java(OffsetDateTime.now())")
    ErrorResponse toErrorResponse(String message, HttpStatus status);
}
