package zerobase.dividend.service.scheduler;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import zerobase.dividend.service.entity.CompanyEntity;
import zerobase.dividend.service.entity.DividendEntity;
import zerobase.dividend.service.model.Company;
import zerobase.dividend.service.model.ScrapedResult;
import zerobase.dividend.service.repository.CompanyRepository;
import zerobase.dividend.service.repository.DividendRepository;
import zerobase.dividend.service.scraper.Scrapper;
import zerobase.dividend.service.type.CacheKey;

import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class ScraperScheduler {
    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;
    private final Scrapper yahooFinanceScraper;

    @CacheEvict(value = CacheKey.KEY_FINANCE, allEntries = true)
    @Scheduled(cron = "${scheduler.scrap.yahoo}")
    public void yahooFinanceScheduling() {
//        log.info("yahooFinanceScheduling is started");
        System.out.println("yahooFinanceScheduling is started");
        List<CompanyEntity> companyEntityList = companyRepository.findAll();

        for (var company : companyEntityList) {
            ScrapedResult scrap = yahooFinanceScraper.scrap(Company.builder()
                    .name(company.getName())
                    .ticker(company.getTicker())
                    .build());

            scrap.getDividendList().stream()
                    .map(e -> new DividendEntity(company.getId(), e))
                    .forEach(e -> {
                        boolean b = dividendRepository.existsByCompanyIdAndDate(e.getCompanyId(), e.getDate());
                        if (!b) {
                            dividendRepository.save(e);
                        }
                    });
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
