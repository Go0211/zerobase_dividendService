package zerobase.dividend.service.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import zerobase.dividend.service.entity.CompanyEntity;
import zerobase.dividend.service.entity.DividendEntity;
import zerobase.dividend.service.exception.NoCompanyException;
import zerobase.dividend.service.model.Company;
import zerobase.dividend.service.model.Dividend;
import zerobase.dividend.service.model.ScrapedResult;
import zerobase.dividend.service.repository.CompanyRepository;
import zerobase.dividend.service.repository.DividendRepository;
import zerobase.dividend.service.type.CacheKey;

import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@Service
@AllArgsConstructor
public class FinanceService {
    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;

    @Cacheable(key = "#companyName", value = CacheKey.KEY_FINANCE)
    public ScrapedResult getDividendByCompanyName(String companyName) {
        log.info("start2");
        CompanyEntity company = this.companyRepository.findByName(companyName)
                .orElseThrow(() -> new NoCompanyException());

        List<DividendEntity> dividendEntities = dividendRepository.findAllByCompanyId(company.getId());

        List<Dividend> dividendList = dividendEntities.stream().map(e -> Dividend.builder()
                .data(e.getDate()).dividend(e.getDividend()).build()).collect(Collectors.toList());

        return new ScrapedResult(Company.builder()
                .ticker(company.getTicker())
                .name(company.getName())
                .build(), dividendList);
    }
}
