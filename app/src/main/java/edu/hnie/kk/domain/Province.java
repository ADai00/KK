package edu.hnie.kk.domain;

import com.contrarywind.interfaces.IPickerViewData;

/**
 * 实体：省
 */
public class Province implements IPickerViewData {
    private int id;
    private String provinceName;
    private int provinceCode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public int getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }

    @Override
    public String getPickerViewText() {
        return getProvinceName();
    }
}
