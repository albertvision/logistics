package bg.nbu.cscb532.logistics.service;

import bg.nbu.cscb532.logistics.data.dto.SaveCompanyInfoDto;
import bg.nbu.cscb532.logistics.data.entity.Company;

public interface CompanyService {
    public Company find();

    public Company save(SaveCompanyInfoDto saveCompanyInfoDto);
}
