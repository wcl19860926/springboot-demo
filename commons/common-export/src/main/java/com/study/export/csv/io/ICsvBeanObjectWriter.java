package com.study.export.csv.io;

import com.study.export.dto.FieldDto;
import org.supercsv.io.ICsvWriter;

import java.io.IOException;

public interface ICsvBeanObjectWriter extends ICsvWriter {

    void write(Object source, FieldDto[] head) throws IOException;
}
