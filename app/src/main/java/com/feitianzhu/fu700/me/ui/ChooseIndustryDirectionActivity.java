package com.feitianzhu.fu700.me.ui;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.feitianzhu.fu700.R;
import com.feitianzhu.fu700.me.adapter.ChooseIndustryDirectionAdapter;
import com.feitianzhu.fu700.me.base.BaseActivity;
import com.feitianzhu.fu700.me.navigationbar.DefaultNavigationBar;
import com.feitianzhu.fu700.model.IndustryOutModel;
import com.feitianzhu.fu700.utils.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Response;

import static com.feitianzhu.fu700.common.Constant.Common_HEADER;
import static com.feitianzhu.fu700.common.Constant.POST_INDUSTRY_INFO;

/**
 * Created by Vya on 2017/9/4 0004.
 * 选择行业方向界面
 */

public class ChooseIndustryDirectionActivity extends BaseActivity {
    @BindView(R.id.reycler_root)
    RecyclerView mRecyclerView;
    private String buttonText;
    private String textId;

    private List<IndustryOutModel> mDatas;
    private  ChooseIndustryDirectionAdapter adapter;
    private List<Button> mClick;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_choose_industry_direction;
    }


    @Override
    protected void initTitle() {
        defaultNavigationBar = new DefaultNavigationBar
                .Builder(ChooseIndustryDirectionActivity.this, (ViewGroup)findViewById(R.id.ll_Container))
                .setTitle("选择行业方向")
                .setStatusHeight(ChooseIndustryDirectionActivity.this)
                .setLeftIcon(R.drawable.iconfont_fanhuijiantou)
                .setRightText("完成", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       // Toast.makeText(ChooseIndustryDirectionActivity.this,"点击完成",Toast.LENGTH_SHORT).show();
                        Intent mIntent = new Intent();
                        mIntent.putExtra("industryTxt", buttonText);
                        mIntent.putExtra("industryId", textId);
                        setResult(1003, mIntent);
                        finish();
                    }
                })
                .builder();
        defaultNavigationBar.setImmersion(R.color.status_bar);
    }

    @Override
    protected void initView() {
        mDatas = new ArrayList<>();
        mClick = new ArrayList<>();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ChooseIndustryDirectionAdapter(mDatas,this);
        adapter.SetOnPassClickListner(new ChooseIndustryDirectionAdapter.OnPassClickListener() {
            @Override
            public void onPassClick(String id, View v) {
                Button bt = (Button) v;
                buttonText = bt.getText().toString();
                textId = id;
                if(mClick.size() <= 0){
                    bt.setTextColor(mContext.getResources().getColor(R.color.white));
                    bt.setBackgroundResource(R.drawable.grid_button_shape_click);
                    mClick.add(bt);
                }else if(mClick.size()>0){
                    Button temp = mClick.remove(0);
                    temp.setTextColor(mContext.getResources().getColor(R.color.txt_middle));
                    temp.setBackgroundResource(R.drawable.grid_button_shape_normal);

                    mClick.clear();
                    bt.setTextColor(mContext.getResources().getColor(R.color.white));
                    bt.setBackgroundResource(R.drawable.grid_button_shape_click);
                    mClick.add(bt);
                }
                //Toast.makeText(ChooseIndustryDirectionActivity.this,"--->"+((Button)v).getText()+"---"+position,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPassMoreButtonClick() {
                Toast.makeText(ChooseIndustryDirectionActivity.this,"点击更多按钮",Toast.LENGTH_SHORT).show();
            }
        });
        mRecyclerView.setAdapter(adapter);
        requestData();
    }

    private void requestData() {
        OkHttpUtils.post()//
                .url(Common_HEADER + POST_INDUSTRY_INFO)
                .build()
                .execute(new Callback< List<IndustryOutModel>>() {
                        @Override
                        public List<IndustryOutModel> parseNetworkResponse(String mData, Response response, int id) throws Exception {
                            Type type = new TypeToken<List<IndustryOutModel>>() {
                            }.getType();
                            List<IndustryOutModel> list = new Gson().fromJson(mData, type);
                            return list;
                        }

                        @Override
                        public void onError(Call call, Exception e, int id) {
                            ToastUtils.showShortToast(e.getMessage());
                        }

                    @Override
                    public void onResponse(List<IndustryOutModel> response, int id) {
                        mDatas.addAll(response);
                        adapter.notifyDataSetChanged();
                    }


                });
    }

    @Override
    protected void initData() {

    }


}
