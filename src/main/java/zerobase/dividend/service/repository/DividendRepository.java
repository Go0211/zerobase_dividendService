package zerobase.dividend.service.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import zerobase.dividend.service.entity.DividendEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
@Repository
public interface DividendRepository extends JpaRepository<DividendEntity, Long> {
    List<DividendEntity> findAllByCompanyId(Long companyId);

    @Transactional
    void deleteAllByCompanyId(Long id);

    boolean existsByCompanyIdAndDate(Long company, LocalDateTime date);
}
