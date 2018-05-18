package edu.hnie.kk;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectChangeListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import edu.hnie.kk.domain.City;
import edu.hnie.kk.domain.County;
import edu.hnie.kk.domain.Province;
import edu.hnie.kk.utils.HttpUtils;
import edu.hnie.kk.utils.Utility;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SelectHometownActivity extends BaseActivity {
    private TextView commonBack;
    private TextView commonBackTitle;

    private LinearLayout provinceCityLayout;
    private TextView provinceCityTextView;

    private List<Province> provinceList = new ArrayList<>();
    private List<List<City>> cityList = new ArrayList<>();
    private List<List<List<County>>> countyList = new ArrayList<>();
    private OptionsPickerView pvOptions;
    private int provinceOptions = 0;
    private int cityOptions = 0;
    private int countyOptions = 0;

    private List<City> result = new ArrayList<>();
    private List<List<County>> tempList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_hometown);
        //初始化title
        commonBack = findViewById(R.id.common_back);
        commonBackTitle = findViewById(R.id.common_back_title);
        commonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionStart(SelectHometownActivity.this, EditInfoActivity.class);
                finish();
            }
        });
        commonBackTitle.setText("家乡");

        //初始化content
        provinceCityLayout = findViewById(R.id.province_city_layout);
        provinceCityTextView = findViewById(R.id.province_city_text_view);

        getOptionData();
        provinceCityLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initOptionPicker();
                pvOptions.show();
            }
        });

    }


    private void initOptionPicker() {//条件选择器初始化

        /**
         * 注意 ：如果是三级联动的数据(省市区等)，请参照 JsonDataActivity 类里面的写法。
         */

        pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                StringBuilder builder = new StringBuilder();
                //返回的分别是三个级别的选中位置
                if (provinceOptions == 0 || provinceOptions == 1 || provinceOptions == 2 || provinceOptions == 3 || provinceOptions == 4 || provinceOptions == 5) {
                    if (cityOptions == 0 && countyOptions == 0) {
                        builder.append(provinceList.get(options1).getPickerViewText());
                    } else if (cityOptions == 0 && countyOptions != 0) {
                        builder.append(provinceList.get(options1).getPickerViewText())
                                .append("-")
                                .append(countyList.get(options1).get(options2).get(options3).getPickerViewText());
                    }
                } else {
                    if ((cityOptions == 0 && countyOptions == 0) || (cityOptions != 0 && countyOptions == 0)) {
                        builder.append(provinceList.get(options1).getPickerViewText())
                                .append("-")
                                .append(countyList.get(options1).get(options2).get(options3).getPickerViewText());
                    } else if ((cityOptions == 0 && countyOptions != 0) || (cityOptions != 0 && countyOptions != 0)) {
                        builder.append(provinceList.get(options1).getPickerViewText())
                                .append("-")
                                .append(cityList.get(options1).get(options2).getPickerViewText())
                                .append("-")
                                .append(countyList.get(options1).get(options2).get(options3).getPickerViewText());
                    }
                }

                provinceCityTextView.setText(builder.toString());
            }
        })
                .setTitleText("城市选择")
                .setContentTextSize(20)//设置滚轮文字大小
                .setDividerColor(Color.LTGRAY)//设置分割线的颜色
                .setSelectOptions(provinceOptions, cityOptions, countyOptions)
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setBackgroundId(0x00000000) //设置外部遮罩颜色
                .setOptionsSelectChangeListener(new OnOptionsSelectChangeListener() {
                    @Override
                    public void onOptionsSelectChanged(int options1, int options2, int options3) {
                        provinceOptions = options1;
                        cityOptions = options2;
                        countyOptions = options3;
                    }
                })
                .build();

//        pvOptions.setPicker(provinceList);//一级选择器
//        pvOptions.setPicker(provinceList, cityList);//二级选择器
        pvOptions.setPicker(provinceList, cityList, countyList);//三级选择器
    }


    private void getOptionData() {

        /**
         * 注意：如果是添加JavaBean实体数据，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
                queryProvinces();
            }
        }).start();


        /*--------数据源添加完毕---------*/
    }

    private void queryProvinces() {
        String address = "http://guolin.tech/api/china";
        queryProvinceFromServer(address);
    }

    private void queryProvinceFromServer(String address) {
        HttpUtils.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                provinceList = Utility.handleProvinceResponse(responseText);
                for (Province province : provinceList) {
                    result.clear();
                    tempList.clear();
                    queryCities(province);
                }

            }
        });
    }

    private void queryCities(Province province) {
        int provinceCode = province.getProvinceCode();
        String address = "http://guolin.tech/api/china/" + provinceCode;
        queryCityFromServer(address, province);
    }

    private void queryCityFromServer(String address, final Province province) {
        HttpUtils.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                result = Utility.handleCityResponse(responseText, province.getId());
                if (result != null && result.size() > 0) {
                    cityList.add(result);
                    for (City city : result) {
                        queryCounties(province, city, tempList);
                    }
                    countyList.add(tempList);
                }
            }
        });
    }

    private void queryCounties(Province province, City city, List<List<County>> list) {
        int provinceCode = province.getProvinceCode();
        int cityCode = city.getCityCode();
        String address = "http://guolin.tech/api/china/" + provinceCode + "/" + cityCode;
        queryCountyFromServer(address, city, list);

    }

    private void queryCountyFromServer(String address, final City city, final List<List<County>> tempList) {

        HttpUtils.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(final Call call, Response response) throws IOException {
                String responseText = response.body().string();
                List<County> countyResult = Utility.handleCountyResponse(responseText, city.getId());
                if (result != null && result.size() > 0) {
                    tempList.add(countyResult);
                }
            }
        });
    }
}
