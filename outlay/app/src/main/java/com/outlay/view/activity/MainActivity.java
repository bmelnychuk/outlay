package com.outlay.view.activity;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.outlay.R;
import com.outlay.view.Navigator;
import com.outlay.view.activity.base.DrawerActivity;
import com.outlay.view.fragment.MainFragment;

public class MainActivity extends DrawerActivity {

    private MainFragment mainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_fragment);

        Bundle b = getIntent().getExtras();
        mainFragment = new MainFragment();
        mainFragment.setArguments(b);
        addFragment(R.id.fragment, mainFragment);
    }

//    public void updateDrawerData(DateSummary summary) {
//    headerView = inflater.inflate(R.layout.layout_drawer_header, null, false);
//        if (headerView != null) {
//            TextView dateAmount = (TextView) headerView.findViewById(R.id.dateAmount);
//            TextView weekAmount = (TextView) headerView.findViewById(R.id.weekAmount);
//            TextView monthAmount = (TextView) headerView.findViewById(R.id.monthAmount);
//
//            dateAmount.setText(NumberUtils.formatAmount(summary.getDayAmount()));
//            weekAmount.setText(NumberUtils.formatAmount(summary.getWeekAmount()));
//            monthAmount.setText(NumberUtils.formatAmount(summary.getMonthAmount()));
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

//    @Override
//    public void onSignOut() {
//        ((App) getApplication()).releaseUserComponent();
//        Intent intent = new Intent(this, LoginActivity.class);
//        startActivity(intent);
//        finish();
//    }
//
//    @Override
//    public void onSignIn() {
//        ((App) getApplication()).releaseUserComponent();
//        Intent intent = new Intent(this, LoginActivity.class);
//        startActivity(intent);
//        finish();
//    }

    @Override
    protected void signOut() {
        getApp().releaseUserComponent();
        FirebaseAuth.getInstance().signOut();
        Navigator.goToLoginScreen(this);
        finish();
    }

    @Override
    protected void createUser() {
        Navigator.goToSyncGuestActivity(this);
    }
}
