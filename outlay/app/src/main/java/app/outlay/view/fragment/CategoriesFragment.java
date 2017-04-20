package app.outlay.view.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;

import app.outlay.utils.IconUtils;
import app.outlay.view.adapter.CategoriesDraggableGridAdapter;
import app.outlay.domain.model.Category;
import app.outlay.mvp.presenter.CategoriesPresenter;
import app.outlay.mvp.view.CategoriesView;
import app.outlay.utils.ResourceUtils;
import app.outlay.view.Navigator;
import app.outlay.view.fragment.base.BaseMvpFragment;
import app.outlay.view.helper.itemtouch.OnDragListener;
import app.outlay.view.helper.itemtouch.SimpleItemTouchHelperCallback;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Bogdan Melnychuk on 1/20/16.
 */
public class CategoriesFragment extends BaseMvpFragment<CategoriesView, CategoriesPresenter> implements OnDragListener, CategoriesView {

    @Bind(app.outlay.R.id.toolbar)
    Toolbar toolbar;

    @Bind(app.outlay.R.id.categoriesGrid)
    RecyclerView categoriesGrid;

    @Bind(app.outlay.R.id.noContent)
    View noContent;

    @Bind(app.outlay.R.id.noContentImage)
    ImageView noContentImage;

    @Bind(app.outlay.R.id.fab)
    FloatingActionButton fab;

    @Inject
    CategoriesPresenter presenter;

    private ItemTouchHelper mItemTouchHelper;
    private CategoriesDraggableGridAdapter adapter;

    @NonNull
    @Override
    public CategoriesPresenter createPresenter() {
        return presenter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getApp().getUserComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(app.outlay.R.layout.fragment_categories, null, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        setToolbar(toolbar);
        setDisplayHomeAsUpEnabled(true);
        getActivity().setTitle(getString(app.outlay.R.string.caption_categories));

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 4);
        categoriesGrid.setLayoutManager(gridLayoutManager);

        adapter = new CategoriesDraggableGridAdapter();
        adapter.setDragListener(this);
        adapter.setOnCategoryClickListener(c -> Navigator.goToCategoryDetails(getActivity(), c.getId()));
        categoriesGrid.setAdapter(adapter);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(categoriesGrid);

        fab.setImageDrawable(ResourceUtils.getMaterialToolbarIcon(getActivity(), app.outlay.R.string.ic_material_add));
        fab.setOnClickListener(v -> Navigator.goToCategoryDetails(getActivity(), null));

        Drawable noCategoryIcon = IconUtils.getIconMaterialIcon(
                getContext(),
                MaterialDesignIconic.Icon.gmi_label,
                ContextCompat.getColor(getActivity(), app.outlay.R.color.icon_inactive),
                app.outlay.R.dimen.icon_no_results,
                16
        );
        noContentImage.setImageDrawable(noCategoryIcon);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.getCategories();
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        analytics().trackCategoryDragEvent();
        mItemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public void onEndDrag(RecyclerView.ViewHolder viewHolder) {
        List<Category> items = adapter.getItems();
        for (int i = 0; i < items.size(); i++) {
            items.get(i).setOrder(i);
        }
        presenter.updateOrder(items);
    }

    @Override
    public void showCategories(List<Category> categoryList) {
        adapter.setItems(categoryList);
        noContent.setVisibility(categoryList.isEmpty() ? View.VISIBLE : View.GONE);
    }
}
