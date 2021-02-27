package com.study.export.excel.callback;

import java.util.List;

public interface ImportPageCallBack<T> {


    int pageResult(List<T> objList);


}
