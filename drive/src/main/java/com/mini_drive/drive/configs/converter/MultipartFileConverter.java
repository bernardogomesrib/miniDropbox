package com.mini_drive.drive.configs.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.web.multipart.MultipartFile;

public class MultipartFileConverter implements Converter<String, MultipartFile> {
    @Override
    public MultipartFile convert(String source) {
        return null;
    }
}