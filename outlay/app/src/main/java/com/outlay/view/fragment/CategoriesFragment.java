package com.outlay.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.outlay.App;
import com.outlay.R;
import com.outlay.view.adapter.CategoriesDraggableGridAdapter;
import com.outlay.domain.model.Category;
import com.outlay.mvp.presenter.CategoriesPresenter;
import com.outlay.mvp.view.CategoriesView;
import com.outlay.utils.ResourceUtils;
import com.outlay.view.Page;
import com.outlay.view.helper.itemtouch.OnDragListener;
import com.outlay.view.helper.itemtouch.SimpleItemTouchHelperCallback;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Bogdan Melnychuk on 1/20/16.
 */
public class CategoriesFragment extends BaseFragment implements OnDragListener, CategoriesView {

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.categoriesGrid)
    RecyclerView categoriesGrid;

    @Bind(R.id.fab)
    FloatingActionButton fab;

    @Inject
    CategoriesPresenter presenter;

    private ItemTouchHelper mItemTouchHelper;
    private CategoriesDraggableGridAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getComponent(getActivity()).inject(this);
        presenter.attachView(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categories, null, false);
        ButterKnife.bind(this, view);
        enableToolbar(toolbar);
        setDisplayHomeAsUpEnabled(true);
        getActivity().setTitle(getString(R.string.caption_categories));
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 4);
        categoriesGrid.setLayoutManager(gridLayoutManager);

        adapter = new CategoriesDraggableGridAdapter();
        adapter.setDragListener(this);
        adapter.setOnCategoryClickListener(c -> Page.goToCategoryDetails(getActivity(), c.getId()));
        categoriesGrid.setAdapter(adapter);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(categoriesGrid);

        fab.setImageDrawable(ResourceUtils.getMaterialToolbarIcon(getActivity(), R.string.ic_material_add));
        fab.setOnClickListener(v -> Page.goToCategoryDetails(getActivity(), null));
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.loadCategories();
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public void onEndDrag(RecyclerView.ViewHolder viewHolder) {
        List<Category> items = adapter.getItems();
        for (int i = 0; i < items.size(); i++) {
            items.get(i).setOrder(i);
        }
        presenter.updateCategories(items);
    }

    @Override
    public void showCategories(List<Category> categoryList) {
        adapter.setItems(categoryList);
    }
}
