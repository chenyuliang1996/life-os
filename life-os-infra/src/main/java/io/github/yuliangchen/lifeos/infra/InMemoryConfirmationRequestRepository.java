package io.github.yuliangchen.lifeos.infra;

import io.github.yuliangchen.lifeos.domain.model.ConfirmationRequest;
import io.github.yuliangchen.lifeos.domain.model.ConfirmationStatus;
import io.github.yuliangchen.lifeos.domain.repository.ConfirmationRequestRepository;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryConfirmationRequestRepository implements ConfirmationRequestRepository {

    private final ConcurrentHashMap<String, ConfirmationRequest> store = new ConcurrentHashMap<>();

    @Override
    public ConfirmationRequest save(ConfirmationRequest request) {
        store.put(request.id(), request);
        return request;
    }

    @Override
    public List<ConfirmationRequest> findAll() {
        return store.values().stream()
                .sorted(Comparator.comparing(ConfirmationRequest::createdAt).reversed())
                .toList();
    }

    @Override
    public List<ConfirmationRequest> findByStatus(ConfirmationStatus status) {
        return store.values().stream()
                .filter(request -> request.status() == status)
                .sorted(Comparator.comparing(ConfirmationRequest::createdAt).reversed())
                .toList();
    }

    @Override
    public Optional<ConfirmationRequest> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }
}
