package com.deer.wms.project.root.configurer;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.deer.wms.project.root.constant.Language;
import com.deer.wms.project.root.util.I18nTool;
import com.deer.wms.project.root.util.Np;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class I18nToolImpl implements I18nTool {

    private final HttpServletRequest request;

    @Override
    public Language getLanguage() {
        String languageCode = null;

        if (StrUtil.isBlank(languageCode)) {
            languageCode = Np.i(request).x(x -> x.getHeader("Content-Language")).o();
        }
        if (StrUtil.isBlank(languageCode)) {
            languageCode = Np.i(request).x(x -> x.getHeader("APP-LANGUAGE")).o();
        }

        return Language.getByCode(languageCode);
    }

}
