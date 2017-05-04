package app.outlay.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import app.outlay.view.activity.base.ParentActivity;


public class SingleFragmentActivity extends ParentActivity {

    private static final String FRAGMENT_PARAM = "fragment";

    public static void start(Context context, Class<?> fragmentClass, Bundle bundle) {
        Intent intent = new Intent(context, SingleFragmentActivity.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        intent.putExtra("fragment", fragmentClass);
        context.startActivity(intent);
    }

    public static void start(Context context, Class<?> fragmentClass) {
        start(context, fragmentClass, null);
    }

    @Override
    protected void onCreate(Bundle bundle) {

        super.onCreate(bundle);
        setContentView(app.outlay.R.layout.activity_single_fragment);
        setTitle(null);

        Bundle b = getIntent().getExtras();
        Class<?> fragmentClass = (Class<?>) b.get(FRAGMENT_PARAM);
        if (bundle == null) {
            Fragment f = Fragment.instantiate(this, fragmentClass.getName());
            f.setArguments(b);
            getSupportFragmentManager().beginTransaction().replace(app.outlay.R.id.fragment, f, fragmentClass.getName()).commit();
        }
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}