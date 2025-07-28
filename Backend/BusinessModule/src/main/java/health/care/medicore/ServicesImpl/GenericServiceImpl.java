package health.care.medicore.ServicesImpl;

import health.care.medicore.RequestDTO.PageableRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class GenericServiceImpl<E> {
    private final Class<E> entityClass;

    public GenericServiceImpl(Class<E> entityClass) {
        this.entityClass = entityClass;
    }

    public final <T> T convertEntityToDto(E entity, Class<T> dtoClass) {
        try {
            T dto = dtoClass.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(entity, dto);
            return dto;
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert entity to DTO", e);
        }
    }

    public final E convertDtoToEntity(Object dto) {
        try {
            E entity = entityClass.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(dto, entity);
            return entity;
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert DTO to entity", e);
        }
    }

    public Pageable getPageable(PageableRequest pageableRequest) {
        Sort.Direction sortDirection = pageableRequest.getOrder() == 1 ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(sortDirection, pageableRequest.getSort());
        return PageRequest.of(pageableRequest.getPage(), pageableRequest.getSize(), sort);
    }
}
