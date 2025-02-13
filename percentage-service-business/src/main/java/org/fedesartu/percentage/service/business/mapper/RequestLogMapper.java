package org.fedesartu.percentage.service.business.mapper;

import org.fedesartu.percentage.service.business.dto.RequestLogDto;
import org.fedesartu.percentage.service.model.entity.postgres.RequestLog;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RequestLogMapper {

    RequestLogMapper INSTANCE = Mappers.getMapper(RequestLogMapper.class);

    RequestLog toEntity(RequestLogDto requestLogDto);

    RequestLogDto toDto(RequestLog requestLog);

}
