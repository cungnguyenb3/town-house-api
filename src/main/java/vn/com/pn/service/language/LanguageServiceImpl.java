package vn.com.pn.service.language;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vn.com.pn.common.common.CommonFunction;
import vn.com.pn.common.common.ScreenMessageConstants;
import vn.com.pn.common.dto.HostCategoryDTO;
import vn.com.pn.common.dto.HostCategoryUpdateDTO;
import vn.com.pn.common.dto.LanguageDTO;
import vn.com.pn.common.output.BaseOutput;
import vn.com.pn.domain.HostCategory;
import vn.com.pn.domain.Language;
import vn.com.pn.exception.ResourceNotFoundException;
import vn.com.pn.repository.hostcategory.HostCategoryRepository;
import vn.com.pn.repository.language.LanguageRepository;
import vn.com.pn.service.host.HostServiceImpl;

import java.util.ArrayList;
import java.util.List;

@Service
public class LanguageServiceImpl implements LanguageService{
    private static Log logger = LogFactory.getLog(HostServiceImpl.class);

    @Autowired
    private LanguageRepository languageRepository;

    @Override
    public BaseOutput getAll() {
        logger.info("LanguageServiceImpl.getAll");
        List<Object> listLanguages = new ArrayList<Object>(languageRepository.findAll());
        return CommonFunction.successOutput(listLanguages);
    }

    @Override
    public BaseOutput insert(LanguageDTO languageDTO) {
        logger.info("ForgotPasswordCodeServiceImpl.insert");
        try {
            Language language = new Language();
            if (languageDTO.getName() != null && languageDTO.getName() != ""){
                language.setName(languageDTO.getName());
            }
            return CommonFunction.successOutput(languageRepository.save(language));
        } catch (Exception e) {
            logger.error(ScreenMessageConstants.FAILURE, e);
            return CommonFunction.failureOutput();
        }
    }
}
