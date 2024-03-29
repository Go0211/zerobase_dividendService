package zerobase.dividend.service.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import zerobase.dividend.service.entity.CompanyEntity;
import zerobase.dividend.service.entity.DividendEntity;
import lombok.AllArgsConstructor;
import zerobase.dividend.service.model.Company;
import zerobase.dividend.service.model.ScrapedResult;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import zerobase.dividend.service.repository.CompanyRepository;
import zerobase.dividend.service.repository.DividendRepository;
import zerobase.dividend.service.scraper.Scrapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CompanyService {
    private final Scrapper scrapper;

    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;

    public Company save(String ticker) {
        boolean exists = this.companyRepository.existsByTicker(ticker);
        if (exists) {
            throw new RuntimeException("already exists ticker -> " + ticker);
        }
        return this.storeCompanyAndDividend(ticker);
    }

    public Page<CompanyEntity> getAllCompany(Pageable pageable) {
        return this.companyRepository.findAll(pageable);
    }

    public Company storeCompanyAndDividend(String ticker) {
        Company company = scrapper.scrapCompanyByTicker(ticker);
        if (ObjectUtils.isEmpty(company)){
            throw new RuntimeException("failed to scrap ticker -> " + ticker);
        }

        ScrapedResult scrap = scrapper.scrap(company);

        CompanyEntity save = companyRepository.save(new CompanyEntity(company));
        dividendRepository.saveAll(scrap.getDividendList().stream()
                .map(e -> new DividendEntity(save.getId(), e))
                .collect(Collectors.toList()));

        return company;
    }
}
