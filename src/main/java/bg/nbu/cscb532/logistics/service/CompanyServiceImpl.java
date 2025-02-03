package bg.nbu.cscb532.logistics.service;

import bg.nbu.cscb532.logistics.data.dto.SaveCompanyInfoDto;
import bg.nbu.cscb532.logistics.data.entity.Company;
import bg.nbu.cscb532.logistics.data.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {
    private final CompanyRepository companyRepository;

    @Override
    public Company find() {
        return companyRepository.findAll().getLast();
    }

    @Override
    public Company save(SaveCompanyInfoDto saveCompanyInfoDto) {
        Company company = find();
        company.setName(saveCompanyInfoDto.getCompanyName());
        company.setPhoneNumber(saveCompanyInfoDto.getPhoneNumber());

        return companyRepository.save(company);
    }

    @EventListener
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(!companyRepository.existsById(1L)) {
            Company company = Company.builder()
                .name("Acme")
                .phoneNumber("+359 888 888 888")
                .build();

            companyRepository.save(company);
        }
    }
}
