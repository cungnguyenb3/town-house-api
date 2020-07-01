package vn.com.pn.screen.m012Language.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.pn.common.common.CommonFunction;
import vn.com.pn.common.common.ScreenMessageConstants;
import vn.com.pn.exception.ResourceInvalidInputException;
import vn.com.pn.screen.m012Language.dto.LanguageDTO;
import vn.com.pn.screen.m012Language.dto.LanguageUpdateDTO;
import vn.com.pn.common.output.BaseOutput;
import vn.com.pn.screen.m012Language.entity.Language;
import vn.com.pn.exception.ResourceNotFoundException;
import vn.com.pn.screen.m012Language.repository.LanguageRepository;
import vn.com.pn.screen.m002Host.service.HostServiceImpl;

import java.util.ArrayList;
import java.util.List;

@Service
public class LanguageServiceImpl implements LanguageService {
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
        logger.info("LanguageRepository.insert");
        try {
            Language language = new Language();
            if (languageDTO.getName() == null && languageDTO.getName().trim().length() == 0) {
                throw new ResourceInvalidInputException("Vui lòng nhập ngôn ngữ");
            }
            language.setName(languageDTO.getName());
            return CommonFunction.successOutput(languageRepository.save(language));
        } catch (Exception e) {
            logger.error(ScreenMessageConstants.FAILURE, e);
            throw new ResourceInvalidInputException(ScreenMessageConstants.INVALID_INPUT);
        }
    }

    @Override
    public BaseOutput update(LanguageUpdateDTO languageUpdateDTO) {
        logger.info("LanguageRepository.update");
        try {
            Language language = languageRepository.findById(
                    Long.parseLong(languageUpdateDTO.getId())).orElseThrow(
                    () -> new ResourceNotFoundException("Language", "id", languageUpdateDTO.getId()));
            if (languageUpdateDTO.getName() != null && languageUpdateDTO.getName() != "") {
                language.setName(languageUpdateDTO.getName());
            }
            return CommonFunction.successOutput(languageRepository.save(language));
        } catch (Exception e) {
            logger.error(ScreenMessageConstants.FAILURE, e);
            throw new ResourceInvalidInputException(ScreenMessageConstants.INVALID_INPUT);
        }
    }
}
