package com.arma.inz.compcal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public abstract class CompliancePagination<Dao, Filter, MiniDTO> {

    private Class<Dao> dao;
    private EntityManager em;

    public CompliancePagination(EntityManager em, Class<Dao> dao) {
        this.em = em;
        this.dao = dao;
    }

    private CriteriaQuery<Dao> queryList(String sortBy, Filter filterDTO) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Dao> query = builder.createQuery(dao);
        Root<Dao> root = query.from(dao);
        query = query.select(root).distinct(true);
        List<Predicate> predicates = new ArrayList<Predicate>();
        whereQuery(predicates, filterDTO, builder, query, root);
        if (!(predicates != null && predicates.isEmpty())) {
            query.where(builder.and(predicates.toArray(new Predicate[predicates.size()])));
        }
        String[] sortt = sortBy.split(" ");
        query.orderBy(sortt[1].contains("asc") ? builder.asc(root.get(sortt[0])) : builder.desc(root.get(sortt[0])));
        return query;
    }

    private CriteriaQuery<Long> countQuery(Filter filterDTO) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<Dao> root = query.from(dao);
        query = query.select(builder.countDistinct(root)).distinct(true);
        List<Predicate> predicates = new ArrayList<Predicate>();
        whereQuery(predicates, filterDTO, builder, query, root);
        if (!(predicates != null && predicates.isEmpty())) {
            query.where(builder.and(predicates.toArray(new Predicate[predicates.size()])));
        }
        return query;
    }

    public abstract void whereQuery(List<Predicate> predicates, Filter filterDTO, CriteriaBuilder builder, CriteriaQuery<?> query, Root<Dao> root);

    public PageResultImpl<MiniDTO> paginationList(Integer page, Integer pageSize, String sortingBy, Filter filterDTO) {
        Pageable pageable = new PageRequest(page, pageSize);

        List<Dao> list = em.createQuery(queryList(sortingBy, filterDTO)).setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();
        Long total = em.createQuery(countQuery(filterDTO)).getResultList().get(0);

        List<MiniDTO> result = copyToMiniDTO(list);
        Page<MiniDTO> pages = new PageImpl<>(result, pageable, total);

        return new PageResultImpl<MiniDTO>(pages.getContent(), (int) pageable.getOffset(), (int) pages.getTotalElements());
    }

    public List<MiniDTO> noPaginationList(String sortingBy, Filter filterDTO) {

        List<Dao> list = em.createQuery(queryList(sortingBy, filterDTO)).getResultList();
        List<MiniDTO> result = copyToMiniDTO(list);

        return result;
    }
    public static <Dto,FilterDto> PageResultImpl<Dto> fakePaginationList(Integer page, Integer pageSize, String sortingBy, FilterDto filterDTO) {
        Pageable pageable = new PageRequest(page, pageSize);
        Page<Dto> pages = new PageImpl<>((new ArrayList<Dto>()), pageable, 0);
        return new PageResultImpl<Dto>(pages.getContent(), (int) pageable.getOffset(), (int) pages.getTotalElements());
    }

    public abstract List<MiniDTO> copyToMiniDTO(List<Dao> list);
}