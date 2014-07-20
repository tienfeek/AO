package com.tien.ao.fragment;



import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tien.ao.R;
import com.tien.ao.widget.viewpagerindicator.IconPagerAdapter;

public class TemplateFragmentAdapter extends FragmentPagerAdapter implements IconPagerAdapter {
    protected static final String[] CONTENT = new String[] { "This", "Is", "A", "Test", };
    protected static final int[] ICONS = new int[] {
            R.drawable.ic_action_new_secret,
            R.drawable.ic_action_new_secret,
            R.drawable.ic_action_new_secret,
            R.drawable.ic_action_new_secret
    };

    private int mCount = 2;

    public TemplateFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
    	if(position == 0){
    		int[] resIds = new int[]{R.drawable.s_1,R.drawable.s_2,R.drawable.s_3,R.drawable.s_4,R.drawable.s_5,R.drawable.s_6,R.drawable.s_7,R.drawable.s_8,R.drawable.s_9,
    				R.drawable.s_10,R.drawable.s_11,R.drawable.s_12,R.drawable.s_13,R.drawable.s_14,R.drawable.s_15};
    		return TemplateFragment.newInstance(resIds);
    	}else{
    		int[] resIds = new int[]{R.drawable.s_16,R.drawable.s_17,R.drawable.s_18,R.drawable.s_19,R.drawable.s_20,R.drawable.s_21,R.drawable.s_22,R.drawable.s_23,R.drawable.s_24,
    				R.drawable.s_25,R.drawable.s_26,R.drawable.s_27,R.drawable.s_28,R.drawable.s_29,R.drawable.s_30};
    		return TemplateFragment.newInstance(resIds);
    	}
    }

    @Override
    public int getCount() {
        return mCount;
    }

    @Override
    public CharSequence getPageTitle(int position) {
      return TemplateFragmentAdapter.CONTENT[position % CONTENT.length];
    }

    @Override
    public int getIconResId(int index) {
      return ICONS[index % ICONS.length];
    }

    public void setCount(int count) {
        if (count > 0 && count <= 10) {
            mCount = count;
            notifyDataSetChanged();
        }
    }
}