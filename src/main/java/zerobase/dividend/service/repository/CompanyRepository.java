package zerobase.dividend.service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import zerobase.dividend.service.entity.CompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<CompanyEntity, Long> {
    boolean existsByTicker(String ticker);

    Optional<CompanyEntity> findByName(String name);

    Page<CompanyEntity> findByNameStartingWithIgnoreCase(String keyword, Pageable limit);

    Optional<CompanyEntity> findByTicker(String ticker);
}
