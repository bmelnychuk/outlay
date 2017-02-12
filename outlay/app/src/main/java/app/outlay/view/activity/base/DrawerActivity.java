package app.outlay.view.activity.base;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import app.outlay.Constants;
import app.outlay.domain.model.User;
import app.outlay.view.activity.SingleFragmentActivity;
import app.outlay.view.alert.Alert;
import app.outlay.view.fragment.AboutFragment;
import app.outlay.view.fragment.AnalysisFragment;
import app.outlay.view.fragment.CategoriesFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bmelnychuk on 12/14/16.
 */

public abstract class DrawerActivity extends ParentActivity {
    private static final int ITEM_CATEGORIES = 1;
    private static final int ITEM_FEEDBACK = 2;
    private static final int ITEM_ABOUT = 3;
    private static final int ITEM_SING_OUT = 4;
    private static final int ITEM_CREATE_USER = 5;
    private static final int ITEM_ANALYSIS = 6;

    private Drawer mainDrawer;

    public void setupDrawer(User currentUser) {
        String email = TextUtils.isEmpty(currentUser.getEmail()) ? "Guest" : currentUser.getEmail();

        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(app.outlay.R.drawable.drawer_image)
                .withAlternativeProfileHeaderSwitching(false)
                .withSelectionListEnabledForSingleProfile(false)
                .withProfileImagesClickable(false)
                .withCloseDrawerOnProfileListClick(false)
                .addProfiles(
                        new ProfileDrawerItem().withEmail(email)
                )
                .build();

        List<IDrawerItem> items = new ArrayList<>();
        if (currentUser.isAnonymous()) {
            items.add(new PrimaryDrawerItem().withName(app.outlay.R.string.menu_item_create_user).withIcon(MaterialDesignIconic.Icon.gmi_account_add).withIdentifier(ITEM_CREATE_USER));
        }

        items.add(new PrimaryDrawerItem().withName(app.outlay.R.string.menu_item_analysis).withIcon(MaterialDesignIconic.Icon.gmi_trending_up).withIdentifier(ITEM_ANALYSIS));
        items.add(new PrimaryDrawerItem().withName(app.outlay.R.string.menu_item_categories).withIcon(MaterialDesignIconic.Icon.gmi_apps).withIdentifier(ITEM_CATEGORIES));
        items.add(new PrimaryDrawerItem().withName(app.outlay.R.string.menu_item_feedback).withIcon(MaterialDesignIconic.Icon.gmi_email).withIdentifier(ITEM_FEEDBACK));
        //items.add(new PrimaryDrawerItem().withName(R.string.menu_item_about).withIcon(MaterialDesignIconic.Icon.gmi_info).withIdentifier(ITEM_ABOUT));
        items.add(new PrimaryDrawerItem().withName(app.outlay.R.string.menu_item_signout).withIcon(MaterialDesignIconic.Icon.gmi_sign_in).withIdentifier(ITEM_SING_OUT));


        mainDrawer = new DrawerBuilder()
                .withFullscreen(true)
                .withActivity(this)
                .withAccountHeader(headerResult)
                .withSelectedItem(-1)
                .addDrawerItems(items.toArray(new IDrawerItem[items.size()]))
                .withOnDrawerItemClickListener((view, i, iDrawerItem) -> {
                    if (iDrawerItem != null) {
                        int id = (int) iDrawerItem.getIdentifier();
                        switch (id) {
                            case ITEM_CATEGORIES:
                                analytics().trackViewCategoriesList();
                                SingleFragmentActivity.start(this, CategoriesFragment.class);
                                break;
                            case ITEM_ANALYSIS:
                                analytics().trackAnalysisView();
                                SingleFragmentActivity.start(this, AnalysisFragment.class);
                                break;
                            case ITEM_FEEDBACK:
                                analytics().trackFeedbackClick();
                                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                                        "mailto", Constants.CONTACT_EMAIL, null));
                                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{Constants.CONTACT_EMAIL});
                                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Outlay Feedback");
                                try {
                                    startActivity(Intent.createChooser(emailIntent, getString(app.outlay.R.string.label_send_email)));
                                } catch (android.content.ActivityNotFoundException ex) {
                                    Alert.error(getRootView(), getString(app.outlay.R.string.error_no_email_clients));
                                }
                                break;
                            case ITEM_ABOUT:
                                SingleFragmentActivity.start(this, AboutFragment.class);
                                break;
                            case ITEM_SING_OUT:
                                analytics().trackSingOut();
                                signOut();
                                break;
                            case ITEM_CREATE_USER:
                                createUser();
                                break;
                        }

                        mainDrawer.setSelection(-1);
                        mainDrawer.closeDrawer();
                    }
                    return false;
                })
                .build();
    }

    protected abstract void signOut();

    protected abstract void createUser();

    @Override
    public void onBackPressed() {
        if (mainDrawer != null && mainDrawer.isDrawerOpen()) {
            mainDrawer.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    public Drawer getMainDrawer() {
        return mainDrawer;
    }
}
