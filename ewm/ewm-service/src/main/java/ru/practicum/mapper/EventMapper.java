package ru.practicum.mapper;

import org.mapstruct.*;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.event.NewEventDto;
import ru.practicum.dto.event.UpdateEventAdminRequest;
import ru.practicum.dto.event.UpdateEventUserRequest;
import ru.practicum.model.Event;

/**
 * Маппер для преобразования Event entity в DTO и обратно
 */
@Mapper(componentModel = "spring", uses = {CategoryMapper.class, UserMapper.class, LocationMapper.class})
public interface EventMapper {

    /**
     * Преобразовать NewEventDto в Event entity
     * Поля initiator и category должны быть установлены отдельно в сервисе
     *
     * @param newEventDto NewEventDto
     * @return Event entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "initiator", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "publishedOn", ignore = true)
    @Mapping(target = "state", ignore = true)
    @Mapping(target = "confirmedRequests", ignore = true)
    Event toEntity(NewEventDto newEventDto);

    /**
     * Преобразовать Event entity в EventFullDto
     * Поле views должно быть установлено отдельно в сервисе
     *
     * @param event Event entity
     * @return EventFullDto
     */
    @Mapping(target = "views", ignore = true)
    @Mapping(target = "confirmedRequests", source = "confirmedRequests")
    EventFullDto toFullDto(Event event);

    /**
     * Преобразовать Event entity в EventShortDto
     * Поле views должно быть установлено отдельно в сервисе
     *
     * @param event Event entity
     * @return EventShortDto
     */
    @Mapping(target = "views", ignore = true)
    @Mapping(target = "confirmedRequests", source = "confirmedRequests")
    EventShortDto toShortDto(Event event);

    /**
     * Обновить Event entity из UpdateEventUserRequest
     * Обновляются только не-null поля
     *
     * @param updateRequest UpdateEventUserRequest
     * @param event Event entity для обновления
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "initiator", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "publishedOn", ignore = true)
    @Mapping(target = "state", ignore = true)
    @Mapping(target = "confirmedRequests", ignore = true)
    void updateEventFromUserRequest(UpdateEventUserRequest updateRequest, @MappingTarget Event event);

    /**
     * Обновить Event entity из UpdateEventAdminRequest
     * Обновляются только не-null поля
     *
     * @param updateRequest UpdateEventAdminRequest
     * @param event Event entity для обновления
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "initiator", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "publishedOn", ignore = true)
    @Mapping(target = "state", ignore = true)
    @Mapping(target = "confirmedRequests", ignore = true)
    void updateEventFromAdminRequest(UpdateEventAdminRequest updateRequest, @MappingTarget Event event);

}