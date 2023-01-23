package com.seedotech.dddhexagonalarchitecture.infrastructure.adapters.input.graphql;

import com.seedotech.dddhexagonalarchitecture.application.ports.input.CreateOrderUseCase;
import com.seedotech.dddhexagonalarchitecture.application.ports.input.GetOrderUseCase;
import com.seedotech.dddhexagonalarchitecture.domain.model.Order;
import com.seedotech.dddhexagonalarchitecture.domain.model.Restaurant;
import com.seedotech.dddhexagonalarchitecture.infrastructure.adapters.input.rest.data.request.OrderCreateRequest;
import com.seedotech.dddhexagonalarchitecture.infrastructure.adapters.input.rest.mapper.OrderRestMapper;
import graphql.schema.DataFetchingEnvironment;
import jakarta.persistence.criteria.Fetch;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;


@Controller
@RequiredArgsConstructor
public class OrderGraphQLAdapter {
    private final CreateOrderUseCase createOrderUseCase;

    private final GetOrderUseCase getOrderUseCase;

    private final OrderRestMapper orderRestMapper;

    @MutationMapping
    public Order createOrder(@Argument OrderCreateRequest orderCreateRequest) {
        // Request to domain
        Order order = orderRestMapper.toOrder(orderCreateRequest);

        order = createOrderUseCase.createOrder(order);

        // Domain to response
        return order;
    }

    @QueryMapping
    public Order order(@Argument Long id, DataFetchingEnvironment environment) {
        Order order = getOrderUseCase.getOrderById(id);
        return order;
    }

//    @QueryMapping
//    public Order order(@Argument Long id, DataFetchingEnvironment environment) {
//        Specification<Order> spec = byId(id);
//        DataFetchingFieldSelectionSet selectionSet = environment.getSelectionSet();
//        if (selectionSet.contains("restaurants"))
//            spec = spec.and(fetchRestaurants());
////        if (selectionSet.contains("userProfile"))
////            spec = spec.and(fetchUserProfiles());
//        return repository.findOne(spec).orElseThrow();
//    }

    private Specification<Order> fetchRestaurants() {
        return (root, query, builder) -> {
            Fetch<Order, Restaurant> f = root.fetch("restaurants", JoinType.LEFT);
            Join<Order, Restaurant> join = (Join<Order, Restaurant>) f;
            return join.getOn();
        };
    }

//    private Specification<Order> fetchUserProfiles() {
//        return (root, query, builder) -> {
//            Fetch<Order, UserProfile> f = root.fetch("userProfiles", JoinType.LEFT);
//            Join<Order, UserProfile> join = (Join<Order, UserProfile>) f;
//            return join.getOn();
//        };
//    }

    private Specification<Order> byId(Long id) {
        return (root, query, builder) -> builder.equal(root.get("id"), id);
    }
}