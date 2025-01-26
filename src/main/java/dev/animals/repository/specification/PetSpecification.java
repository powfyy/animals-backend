package dev.animals.repository.specification;

import dev.animals.entity.OrganizationEntity;
import dev.animals.entity.animal.AnimalEntity;
import dev.animals.enums.GenderType;
import dev.animals.enums.PetStatus;
import dev.animals.web.dto.FilterFields;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;


public class PetSpecification implements Specification<AnimalEntity> {

    private final FilterFields filterFields;

    public PetSpecification(FilterFields filterFields){
        this.filterFields=filterFields;
    }

    @Override
    public Predicate toPredicate(Root<AnimalEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();
        if (filterFields.getGender() != null) {
            predicates.add(criteriaBuilder.equal(root.get("gender"), GenderType.valueOf(filterFields.getGender())));
        }
        if (filterFields.getPetType() != null) {
            predicates.add(criteriaBuilder.equal(root.get("type"), filterFields.getPetType()));
        }
        if (filterFields.getName() != null) {
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + filterFields.getName().toLowerCase() + "%"));
        }
        if (filterFields.getNameOrganization() != null) {
          Join<AnimalEntity, OrganizationEntity> organizationJoin = root.join("organization", JoinType.INNER);
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(organizationJoin.get("nameOrganization")),
                    "%" + filterFields.getNameOrganization().toLowerCase() + "%"));
        }
        if (filterFields.getCity() != null) {
          Join<AnimalEntity, OrganizationEntity> organizationJoin = root.join("organization", JoinType.INNER);
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(organizationJoin.get("city")),
                    "%"+filterFields.getCity().toLowerCase()+"%"));
        }
        predicates.add(criteriaBuilder.equal(root.get("status"), PetStatus.valueOf("ACTIVE")));
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
