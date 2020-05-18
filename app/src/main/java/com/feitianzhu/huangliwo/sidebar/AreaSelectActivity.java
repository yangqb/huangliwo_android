package com.feitianzhu.huangliwo.sidebar;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.base.activity.BaseActivity;
import com.feitianzhu.huangliwo.model.AreaPhoneBean;
import com.feitianzhu.huangliwo.utils.PinyinUtils;
import com.feitianzhu.huangliwo.utils.ReadAssetsJsonUtil;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * package name: com.feitianzhu.huangliwo.sidebar
 * user: yangqinbo
 * date: 2020/4/13
 * time: 16:23
 * email: 694125155@qq.com
 */
public class AreaSelectActivity extends BaseActivity {
    private Context mContext = this;
    private AreaCodeListAdapter mAdapter;
    private ArrayList<AreaPhoneBean> mBeans = new ArrayList<>();

    /** 记录当前是否是search图标 */
    private boolean IS_SEARCH_ICON = true;
    @BindView(R.id.img_search)
    ImageView imgSearch;
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.txt_tittle)
    TextView txtTittle;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.lv_area)
    ListView lvArea;
    @BindView(R.id.sb_index)
    DLSideBar sbIndex;

    @Override
    protected int getLayoutId() {
        return R.layout.act_area_select;
    }

    @Override
    protected void initView() {
        // 配置列表适配器
        lvArea.setVerticalScrollBarEnabled(false);
        lvArea.setFastScrollEnabled(false);
        mAdapter = new AreaCodeListAdapter(mContext, mBeans);
        lvArea.setAdapter(mAdapter);
        lvArea.setOnItemClickListener(mItemClickListener);
        // 配置右侧index
        sbIndex.setOnTouchingLetterChangedListener(mSBTouchListener);
        // 配置搜索
        etSearch.addTextChangedListener(mTextWatcher);
    }

    @OnClick({R.id.img_search, R.id.img_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_search:
                if (IS_SEARCH_ICON){
                    IS_SEARCH_ICON = false;
                    changeMode();
                } else {
                    etSearch.setText("");
                }
                break;
            case R.id.img_back:
                if (IS_SEARCH_ICON){
                    finish();
                } else {
                    IS_SEARCH_ICON = true;
                    changeMode();
                }
                break;
        }
    }

    @Override
    protected void initData() {
        mBeans.clear();
        JSONArray array = ReadAssetsJsonUtil.getJSONArray(getResources().getString(R.string.area_select_json_name), mContext);
        if (null == array) array = new JSONArray();
        for (int i = 0; i < array.length(); i++) {
            AreaPhoneBean bean = new AreaPhoneBean();
            bean.name = array.optJSONObject(i).optString("zh");
            bean.fisrtSpell = PinyinUtils.getFirstSpell(bean.name.substring(0,1));
            bean.name_py = PinyinUtils.getPinYin(bean.name);
            bean.code = array.optJSONObject(i).optString("code");
            bean.locale = array.optJSONObject(i).optString("locale");
            bean.en_name = array.optJSONObject(i).optString("en");
            mBeans.add(bean);
        }
        // 根据拼音为数组进行排序
        Collections.sort(mBeans, new AreaPhoneBean.ComparatorPY());
        // 数据更新
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 右侧index选项监听
     */
    private DLSideBar.OnTouchingLetterChangedListener mSBTouchListener = new DLSideBar.OnTouchingLetterChangedListener() {
        @Override
        public void onTouchingLetterChanged(String str) {
            //触摸字母列时,将品牌列表更新到首字母出现的位置
            if (mBeans.size()>0){
                for (int i=0;i<mBeans.size();i++){
                    if (mBeans.get(i).fisrtSpell.compareToIgnoreCase(str) == 0) {
                        lvArea.setSelection(i);
                        break;
                    }
                }
            }
        }
    };

    /**
     * 搜索监听
     */
    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(!"".equals(s.toString().trim())) {
                //根据编辑框值过滤联系人并更新联系列表
                filterContacts(s.toString().trim());
                sbIndex.setVisibility(View.GONE);
            } else {
                sbIndex.setVisibility(View.VISIBLE);
                mAdapter.updateListView(mBeans);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    /**
     * 选项点击事件
     */
    private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ArrayList<AreaPhoneBean> bs = mAdapter.getList();
            AreaPhoneBean bean =bs.get(position);
           /* Intent data = new Intent();
            data.putExtra("string", bean.name + "（+" + bean.code + "）");
            setResult(RESULT_OK, data);*/
            finish();
        }
    };

    /**
     * 比对筛选
     * @param filterStr
     */
    private void filterContacts(String filterStr){
        ArrayList<AreaPhoneBean> filters = new ArrayList<>();
        //遍历数组,筛选出包含关键字的item
        for (int i = 0; i < mBeans.size(); i++) {
            //过滤的条件
            if (isStrInString(mBeans.get(i).name_py,filterStr)
                    ||mBeans.get(i).name.contains(filterStr)
                    ||isStrInString(mBeans.get(i).fisrtSpell,filterStr)){
                //将筛选出来的item重新添加到数组中
                AreaPhoneBean filter = mBeans.get(i);
                filters.add(filter);
            }
        }

        //将列表更新为过滤的联系人
        mAdapter.updateListView(filters);
    }

    /**
     * 判断字符串是否包含
     * @param bigStr
     * @param smallStr
     * @return
     */
    public boolean isStrInString(String bigStr,String smallStr){
        return bigStr.toUpperCase().contains(smallStr.toUpperCase());
    }

    /**
     * 修改当前显示
     */
    private void changeMode(){
        if (IS_SEARCH_ICON){
            imgSearch.setImageResource(R.mipmap.a01_03sousuo);
            imgSearch.setVisibility(View.VISIBLE);
            etSearch.setText("");
            etSearch.setVisibility(View.GONE);
            txtTittle.setVisibility(View.VISIBLE);
        } else {
            imgSearch.setImageResource(R.mipmap.g07_03shanchu);
            imgSearch.setVisibility(View.INVISIBLE);
            etSearch.setVisibility(View.VISIBLE);
            etSearch.setFocusable(true);
            txtTittle.setVisibility(View.GONE);
        }
    }
}
