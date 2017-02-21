package zzj.com.zhizuji.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import zzj.com.zhizuji.R;
import zzj.com.zhizuji.base.BaseFragment;

/**
 * Created by shawn on 2017-02-22.
 */

public class SocialFragment extends BaseFragment {

    private View mContentView;
    @BindView(R.id.list)
    RecyclerView mRecyclerView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = View.inflate(getActivity(), R.layout.fragment_social,null);
        ButterKnife.bind(this,mContentView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(new RecyclerView.Adapter<ViewHo>() {
            @Override
            public ViewHo onCreateViewHolder(ViewGroup parent, int viewType) {
                return new ViewHo(View.inflate(getActivity(),android.R.layout.simple_list_item_1,null));
            }

            @Override
            public void onBindViewHolder(ViewHo holder, int position) {
                holder.textView.setText("哈哈哈");
                holder.textView.setTextSize(40.0f);
            }


            @Override
            public int getItemCount() {
                return 30;
            }
        });
        return mContentView;
    }

    private class ViewHo extends RecyclerView.ViewHolder{

        private TextView textView;
        public ViewHo(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(android.R.id.text1);

        }
    }
}
