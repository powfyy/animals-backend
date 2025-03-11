package dev.animals.repository.specification;

import dev.animals.entity.OrganizationEntity;
import dev.animals.entity.animal.AnimalEntity;
import dev.animals.entity.animal.AnimalTypeEntity;
import dev.animals.enums.AnimalStatus;
import dev.animals.web.dto.AnimalFilterDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class PetSpecification implements Specification<AnimalEntity> {

  private final AnimalFilterDto filterFields;

  public PetSpecification(AnimalFilterDto filterFields) {
    this.filterFields = filterFields;
  }

  @Override
  public Predicate toPredicate(Root<AnimalEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
    List<Predicate> predicates = new ArrayList<>();
    if (Objects.nonNull(filterFields.getGender())) {
      predicates.add(criteriaBuilder.equal(root.get("gender"), filterFields.getGender()));
    }
    if (StringUtils.isNotBlank(filterFields.getType())) {
      Join<AnimalEntity, AnimalTypeEntity> typeJoin = root.join("type", JoinType.INNER);
      predicates.add(criteriaBuilder.like(criteriaBuilder.lower(typeJoin.get("name")), "%" + filterFields.getType().toLowerCase() + "%"));
    }
    if (StringUtils.isNotBlank(filterFields.getName())) {
      predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + filterFields.getName().toLowerCase() + "%"));
    }
    if (StringUtils.isNotBlank(filterFields.getOrganizationName())) {
      Join<AnimalEntity, OrganizationEntity> organizationJoin = root.join("organization", JoinType.INNER);
      predicates.add(criteriaBuilder.like(criteriaBuilder.lower(organizationJoin.get("name")),
        "%" + filterFields.getOrganizationName().toLowerCase() + "%"));
    }
    if (filterFields.getCity() != null) {
      Join<AnimalEntity, OrganizationEntity> organizationJoin = root.join("organization", JoinType.INNER);
      predicates.add(criteriaBuilder.like(criteriaBuilder.lower(organizationJoin.get("city")),
        "%" + filterFields.getCity().toLowerCase() + "%"));
    }
    predicates.add(criteriaBuilder.equal(root.get("status"), AnimalStatus.valueOf("ACTIVE")));
    return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
  }
}
