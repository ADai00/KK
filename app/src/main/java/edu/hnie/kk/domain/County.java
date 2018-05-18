package edu.hnie.kk.domain;

import com.contrarywind.interfaces.IPickerViewData;

/**
 * 实体：区/县
 */
public class County implements IPickerViewData {
    private int id;
    private String countyName;
    private int cityId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    @Override
    public String getPickerViewText() {
        return getCountyName();
    }
}
