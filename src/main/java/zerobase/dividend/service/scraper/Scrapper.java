package zerobase.dividend.service.scraper;

import zerobase.dividend.service.model.Company;
import zerobase.dividend.service.model.ScrapedResult;

public interface Scrapper {
    Company scrapCompanyByTicker(String ticker);
    ScrapedResult scrap(Company company);
}
