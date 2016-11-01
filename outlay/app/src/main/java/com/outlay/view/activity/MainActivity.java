package com.outlay.view.activity;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.outlay.App;
import com.outlay.Constants;
import com.outlay.R;
import com.outlay.domain.model.DateSummary;
import com.outlay.mvp.presenter.MainViewPresenter;
import com.outlay.mvp.view.MainView;
import com.outlay.view.alert.Alert;
import com.outlay.view.fragment.AboutFragment;
import com.outlay.view.fragment.CategoriesFragment;
import com.outlay.view.fragment.MainFragment;

import javax.inject.Inject;

public class MainActivity extends BaseActivity implements MainView {
    private static final int ITEM_FEEDBACK = 2;
    private static final int ITEM_CATEGORIES = 1;
    private static final int ITEM_ABOUT = 3;
    private static final int ITEM_LOGOUT = 4;

    private Drawer mainDrawer;

    @Inject
    MainViewPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getUserComponent(this).inject(this);
        presenter.attachView(this);

        setContentView(R.layout.activity_single_fragment);
        setTitle(null);

        Bundle b = getIntent().getExtras();
        Fragment f = Fragment.instantiate(this, MainFragment.class.getName());
        f.setArguments(b);
        getFragmentManager().beginTransaction().replace(R.id.fragment, f, MainFragment.class.getName()).commit();
    }

    public void setupDrawer(Toolbar toolbar) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        String email = "";
        if (firebaseUser != null) {
            email = firebaseUser.getEmail();
        }

        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.drawer_image)
                .withAlternativeProfileHeaderSwitching(false)
                .withSelectionListEnabledForSingleProfile(false)
                .withProfileImagesClickable(false)
                .withCloseDrawerOnProfileListClick(false)
                .addProfiles(
                        new ProfileDrawerItem().withEmail(email)
                )
                .build();

        PrimaryDrawerItem inOutItem = new PrimaryDrawerItem()
                .withIdentifier(ITEM_LOGOUT)
                .withName(firebaseUser != null ? R.string.menu_item_signout : R.string.menu_item_signin)
                .withIcon(firebaseUser != null ? MaterialDesignIconic.Icon.gmi_square_right : MaterialDesignIconic.Icon.gmi_account);

        mainDrawer = new DrawerBuilder()
                .withToolbar(toolbar)
                .withFullscreen(true)
                .withActivity(this)
                .withAccountHeader(headerResult)
                .withSelectedItem(-1)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.menu_item_categories).withIcon(MaterialDesignIconic.Icon.gmi_apps).withIdentifier(ITEM_CATEGORIES),
                        new PrimaryDrawerItem().withName(R.string.menu_item_feedback).withIcon(MaterialDesignIconic.Icon.gmi_email).withIdentifier(ITEM_FEEDBACK),
                        new PrimaryDrawerItem().withName(R.string.menu_item_about).withIcon(MaterialDesignIconic.Icon.gmi_info).withIdentifier(ITEM_ABOUT),
                        inOutItem
                )
                .withOnDrawerItemClickListener((view, i, iDrawerItem) -> {
                    if (iDrawerItem != null) {
                        int id = (int) iDrawerItem.getIdentifier();
                        switch (id) {
                            case ITEM_CATEGORIES:
                                SingleFragmentActivity.start(MainActivity.this, CategoriesFragment.class);
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
                                SingleFragmentActivity.start(MainActivity.this, AboutFragment.class);
                                break;
                            case ITEM_LOGOUT:
                                if (firebaseUser != null) {
                                    presenter.signOut();
                                } else {
                                    presenter.signIn();
                                }
                        }

                        mainDrawer.setSelection(-1);
                        mainDrawer.closeDrawer();
                    }
                    return false;
                })
                .build();

        Drawer rightDrawer = new DrawerBuilder()
                .withDrawerGravity(GravityCompat.END)
                .withActivity(this)
                .withSelectedItem(-1)
                .build();
    }

    @Override
    public void onBackPressed() {
        if (mainDrawer != null && mainDrawer.isDrawerOpen()) {
            mainDrawer.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

//    public void updateDrawerData(DateSummary summary) {
//    headerView = inflater.inflate(R.layout.layout_drawer_header, null, false);
//        if (headerView != null) {
//            TextView dateAmount = (TextView) headerView.findViewById(R.id.dateAmount);
//            TextView weekAmount = (TextView) headerView.findViewById(R.id.weekAmount);
//            TextView monthAmount = (TextView) headerView.findViewById(R.id.monthAmount);
//
//            dateAmount.setText(FormatUtils.formatAmount(summary.getDayAmount()));
//            weekAmount.setText(FormatUtils.formatAmount(summary.getWeekAmount()));
//            monthAmount.setText(FormatUtils.formatAmount(summary.getMonthAmount()));
//
//            LinearLayout categoriesContainer = (LinearLayout) headerView.findViewById(R.id.mostSpentCategories);
//            categoriesContainer.removeAllViews();
//            for (Category c : summary.getCategories()) {
//                categoriesContainer.addView(createCategoryView(c));
//            }
//        }
//    }

//    private View createCategoryView(Category category) {
//        View categoryView = inflater.inflate(R.layout.item_category, null, false);
//
//        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
//                ViewGroup.LayoutParams.WRAP_CONTENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT,
//                1);
//        categoryView.setLayoutParams(layoutParams);
//
//        PrintView icon = (PrintView) categoryView.findViewById(R.id.categoryIcon);
//        TextView title = (TextView) categoryView.findViewById(R.id.categoryTitle);
//        title.setText(category.getTitle());
//        icon.setIconSizeRes(R.dimen.category_icon);
//        IconUtils.loadCategoryIcon(category, icon);
//        return categoryView;
//    }

    public void updateDrawerData(DateSummary summary) {
    }

    @Override
    public void onSignOut() {
        ((App) getApplication()).releaseUserComponent();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onSignIn() {
        ((App) getApplication()).releaseUserComponent();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
