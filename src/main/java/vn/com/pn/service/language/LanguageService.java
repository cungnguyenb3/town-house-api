package vn.com.pn.service.language;

import vn.com.pn.common.dto.LanguageDTO;
import vn.com.pn.common.dto.LanguageUpdateDTO;
import vn.com.pn.common.output.BaseOutput;

public interface LanguageService {
    BaseOutput getAll();
    BaseOutput insert(LanguageDTO languageDTO);
    BaseOutput update(LanguageUpdateDTO languageUpdateDTO);
}

