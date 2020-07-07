package cn.mtjsoft.www.gridviewpager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ListAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public ListAdapter(@Nullable List<String> data) {
        super(R.layout.item_text, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, String s) {
        baseViewHolder.setText(R.id.tv_name, s);
    }
}