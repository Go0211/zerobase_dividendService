package zerobase.dividend.service.service;

import org.apache.commons.collections4.Trie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import zerobase.dividend.service.entity.CompanyEntity;
import zerobase.dividend.service.entity.DividendEntity;
import lombok.AllArgsConstructor;
import zerobase.dividend.service.exception.NoCompanyException;
import zerobase.dividend.service.model.Company;
import zerobase.dividend.service.model.ScrapedResult;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import zerobase.dividend.service.repository.CompanyRepository;
import zerobase.dividend.service.repository.DividendRepository;
import zerobase.dividend.service.scraper.Scrapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CompanyService {
    private final Trie trie;
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

    public List<String> getCompanyNameByKeyword(String keyword) {
        Pageable limit = PageRequest.of(0, 10);
        Page<CompanyEntity> companyEntities = companyRepository.findByNameStartingWithIgnoreCase(keyword, limit);
        return companyEntities.stream()
                .map(e -> e.getName())
                .collect(Collectors.toList());
    }

    public void addAutocompleteKeyword(String keyword) {
        this.trie.put(keyword, null);
    }

    public List<String> autocomplete(String keyword) {
        return (List<String>) this.trie.prefixMap(keyword).keySet()
                .stream().collect(Collectors.toList());
    }

    public void deleteAutocompleteKeyword(String keyword) {
        this.trie.remove(keyword);
    }

    public String deleteCompany(String ticker) {
        CompanyEntity company = companyRepository.findByTicker(ticker)
                .orElseThrow(() -> new NoCompanyException());


        dividendRepository.deleteAllByCompanyId(company.getId());
        companyRepository.delete(company);

        deleteAutocompleteKeyword(company.getName());
        return company.getName();
    }
}
