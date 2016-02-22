package com.outlay.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.johnkil.print.PrintView;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.outlay.Constants;
import com.outlay.R;
import com.outlay.dao.Category;
import com.outlay.model.Summary;
import com.outlay.utils.IconUtils;
import com.outlay.view.alert.Alert;
import com.outlay.view.fragment.AboutFragment;
import com.outlay.view.fragment.CategoriesFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Bogdan Melnychuk on 1/15/16.
 */
public class BaseActivity extends AppCompatActivity {

    private static final int ITEM_FEEDBACK = 2;
    private static final int ITEM_CATEGORIES = 1;
    private static final int ITEM_ABOUT = 3;

    private Drawer drawer;

    @Nullable
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    private View headerView;
    private LayoutInflater inflater;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        if (hasDrawer()) {
            setupDrawer(this.toolbar);
        }
        inflater = LayoutInflater.from(this);
    }

    public void setupDrawer(Toolbar toolbar) {
        LayoutInflater inflater = LayoutInflater.from(this);
        headerView = inflater.inflate(R.layout.layout_drawer_header, null, false);

        drawer = new DrawerBuilder()
                .withHeader(headerView)
                .withToolbar(toolbar)
                .withFullscreen(true)
                .withActivity(this)
                .withSelectedItem(-1)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.menu_item_categories).withIcon(GoogleMaterial.Icon.gmd_apps).withIdentifier(ITEM_CATEGORIES),
                        new PrimaryDrawerItem().withName(R.string.menu_item_feedback).withIcon(GoogleMaterial.Icon.gmd_mail).withIdentifier(ITEM_FEEDBACK),
                        new PrimaryDrawerItem().withName(R.string.menu_item_about).withIcon(GoogleMaterial.Icon.gmd_info).withIdentifier(ITEM_ABOUT)
                )
                .withOnDrawerItemClickListener((view, i, iDrawerItem) -> {
                    if (iDrawerItem != null) {
                        int id = iDrawerItem.getIdentifier();
                        switch (id) {
                            case ITEM_CATEGORIES:
                                SingleFragmentActivity.start(BaseActivity.this, CategoriesFragment.class);
                                break;
                            case ITEM_FEEDBACK:
                                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                                        "mailto", Constants.CONTACT_EMAIL, null));
                                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{Constants.CONTACT_EMAIL});
                                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Outlay Feedback");
                                try {
                                    startActivity(Intent.createChooser(emailIntent, getString(R.string.label_send_email)));
                                } catch (android.content.ActivityNotFoundException ex) {
                                    Alert.error(getRootView(), getString(R.string.error_no_email_clients));
                                }
                                break;
                            case ITEM_ABOUT:
                                SingleFragmentActivity.start(BaseActivity.this, AboutFragment.class);
                                break;
                        }
                        drawer.setSelection(-1);
                        drawer.closeDrawer();
                    }
                    return false;
                })
                .build();
    }

    @Override
    public void onBackPressed() {
        if (drawer != null && drawer.isDrawerOpen()) {
            drawer.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    public void updateDrawerData(Summary summary) {
        if (headerView != null) {
            TextView dateAmount = (TextView) headerView.findViewById(R.id.dateAmount);
            TextView weekAmount = (TextView) headerView.findViewById(R.id.weekAmount);
            TextView monthAmount = (TextView) headerView.findViewById(R.id.monthAmount);

            dateAmount.setText(String.valueOf(summary.getDayAmount()));
            weekAmount.setText(String.valueOf(summary.getWeekAmount()));
            monthAmount.setText(String.valueOf(summary.getMonthAmount()));

            LinearLayout categoriesContainer = (LinearLayout) headerView.findViewById(R.id.mostSpentCategories);
            categoriesContainer.removeAllViews();
            for (Category c : summary.getCategories()) {
                categoriesContainer.addView(createCategoryView(c));
            }
        }
    }

    public boolean hasDrawer() {
        return true;
    }

    public Drawer getDrawer() {
        return drawer;
    }

    private View createCategoryView(Category category) {
        View categoryView = inflater.inflate(R.layout.item_category, null, false);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                1);
        categoryView.setLayoutParams(layoutParams);

        PrintView icon = (PrintView) categoryView.findViewById(R.id.categoryIcon);
        TextView title = (TextView) categoryView.findViewById(R.id.categoryTitle);
        title.setText(category.getTitle());
        icon.setIconSizeRes(R.dimen.category_icon);
        IconUtils.loadCategoryIcon(category, icon);
        return categoryView;
    }

    public View getRootView() {
        return findViewById(android.R.id.content);
    }
}
