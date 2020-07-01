package vn.com.pn.screen.m012Language.service;

import vn.com.pn.screen.m012Language.dto.LanguageDTO;
import vn.com.pn.screen.m012Language.dto.LanguageUpdateDTO;
import vn.com.pn.common.output.BaseOutput;

public interface LanguageService {
    BaseOutput getAll();

    BaseOutput insert(LanguageDTO languageDTO);

    BaseOutput update(LanguageUpdateDTO languageUpdateDTO);
}

