package com.tien.ao.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.tien.ao.R;
import com.tien.ao.SendActivity;

public final class TemplateFragment extends Fragment {

	public static TemplateFragment newInstance(int[] resIds) {
		TemplateFragment fragment = new TemplateFragment();
		
		Bundle bundle = new Bundle();
		bundle.putIntArray("resIds", resIds);
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return inflater.inflate(R.layout.template_gridview_view, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		GridView mGridView = (GridView)getView().findViewById(R.id.gridview);
		
		final int[] resIds = getArguments().getIntArray("resIds");
		TemplateAdapter adapter = new TemplateAdapter(resIds);
		mGridView.setAdapter(adapter);
		
		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				SendActivity activity = (SendActivity)getActivity();
				activity.templateReselected(resIds[arg2]);
			}
		});
	}
	
	
	class TemplateAdapter extends BaseAdapter{
		
		private int[] resIds;
		
		public TemplateAdapter(int[] resIds){
			this.resIds = resIds;
		}

		@Override
		public int getCount() {
			return resIds.length;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = LayoutInflater.from(getActivity()).inflate(R.layout.template_gridview_item, null);
			
			ImageView templateIV = (ImageView)view.findViewById(R.id.template_iv);
			templateIV.setImageResource(resIds[position]);
			return view;
		}
		
	}
}