package com.outlay.view.activity.base;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.Toolbar;

import com.google.firebase.auth.FirebaseUser;
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.outlay.Constants;
import com.outlay.R;
import com.outlay.domain.model.User;
import com.outlay.view.activity.SingleFragmentActivity;
import com.outlay.view.alert.Alert;
import com.outlay.view.fragment.AboutFragment;
import com.outlay.view.fragment.CategoriesFragment;

/**
 * Created by bmelnychuk on 12/14/16.
 */

public abstract class DrawerActivity extends ParentActivity {
    private static final int ITEM_CATEGORIES = 1;
    private static final int ITEM_FEEDBACK = 2;
    private static final int ITEM_ABOUT = 3;
    private static final int ITEM_LOGOUT = 4;

    private Drawer mainDrawer;

    public void setupDrawer(User currentUser) {
        String email = currentUser.getEmail();

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
                .withName(currentUser.isAnonymous() ? R.string.menu_item_signin : R.string.menu_item_signout)
                .withIcon(currentUser.isAnonymous() ? MaterialDesignIconic.Icon.gmi_account : MaterialDesignIconic.Icon.gmi_square_right);

        mainDrawer = new DrawerBuilder()
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
                                SingleFragmentActivity.start(this, CategoriesFragment.class);
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
                                SingleFragmentActivity.start(this, AboutFragment.class);
                                break;
                            case ITEM_LOGOUT:
                                if (currentUser.isAnonymous()) {
                                    createUser();
                                } else {
                                    signOut();
                                }
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
