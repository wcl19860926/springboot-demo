package com.study.export.csv.io;

import com.study.export.dto.FieldDto;
import org.supercsv.io.ICsvReader;

public interface ICsvBeanObjectReader extends ICsvReader {

    <T> T read(final Class<T> cls, FieldDto[] filedDtos) throws Exception;
}
