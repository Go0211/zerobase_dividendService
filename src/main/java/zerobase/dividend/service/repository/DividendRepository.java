package zerobase.dividend.service.repository;

import zerobase.dividend.service.entity.DividendEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DividendRepository extends JpaRepository<DividendEntity, Long> {
}
