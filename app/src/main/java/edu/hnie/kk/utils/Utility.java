package edu.hnie.kk.utils;

import android.text.TextUtils;
import edu.hnie.kk.domain.City;
import edu.hnie.kk.domain.County;
import edu.hnie.kk.domain.Province;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Utility {

    /**
     * 解析和处理服务器返回的省级数据
     *
     * @param response 解析数据
     * @return
     */
    public static List<Province> handleProvinceResponse(String response) {
        List<Province> provinceList = new ArrayList<>();
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allProvince = new JSONArray(response);

                for (int i = 0; i < allProvince.length(); i++) {
                    JSONObject jsonObject = allProvince.getJSONObject(i);
                    Province province = new Province();
                    province.setProvinceName(jsonObject.getString("name"));
                    province.setProvinceCode(jsonObject.getInt("id"));
                    provinceList.add(province);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return provinceList;
    }

    /**
     * 解析和处理服务器返回的市级数据
     *
     * @param response   解析数据
     * @param provinceId 省id
     * @return
     */
    public static List<City> handleCityResponse(String response, int provinceId) {
        List<City> cityList = new ArrayList<>();
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allCity = new JSONArray(response);
                for (int i = 0; i < allCity.length(); i++) {
                    JSONObject jsonObject = allCity.getJSONObject(i);
                    City city = new City();
                    city.setCityName(jsonObject.getString("name"));
                    city.setCityCode(jsonObject.getInt("id"));
                    city.setProvinceId(provinceId);
                    cityList.add(city);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return cityList;
    }

    /**
     * 解析和处理服务器返回的县级数据
     *
     * @param response 解析数据
     * @param cityId   城市id
     * @return
     */
    public static List<County> handleCountyResponse(String response, int cityId) {
        List<County> countyList = new ArrayList<>();
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allCounty = new JSONArray(response);
                for (int i = 0; i < allCounty.length(); i++) {
                    JSONObject jsonObject = allCounty.getJSONObject(i);
                    County county = new County();
                    county.setCountyName(jsonObject.getString("name"));
                    county.setCityId(cityId);
                    countyList.add(county);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return countyList;
    }
}
