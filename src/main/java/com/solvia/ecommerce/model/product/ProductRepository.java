package com.solvia.ecommerce.model.product;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Collection;
import java.util.Optional;

public interface ProductRepository extends CrudRepository<Product, Long>, PagingAndSortingRepository<Product, Long>,
        QuerydslPredicateExecutor<Product>, QuerydslBinderCustomizer<QProduct> {

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    @NonNull
    <S extends Product> S save(@NonNull S entity);

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    void delete(@NonNull Product entity);

    @Override
    default void customize(QuerydslBindings bindings, QProduct root) {
        bindings.bind(root.id).first(NumberExpression::eq);
        bindings.bind(String.class).all((StringPath path, Collection<? extends String> values) -> {
            BooleanBuilder predicate = new BooleanBuilder();
            values.forEach(value -> predicate.or(path.containsIgnoreCase(value)));
            return Optional.of(predicate);
        });
    }
}
