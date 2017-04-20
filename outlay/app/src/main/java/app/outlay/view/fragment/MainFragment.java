package app.outlay.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import app.outlay.core.utils.DateUtils;
import app.outlay.core.utils.NumberUtils;
import app.outlay.domain.model.Category;
import app.outlay.domain.model.Expense;
import app.outlay.domain.model.User;
import app.outlay.mvp.presenter.EnterExpensePresenter;
import app.outlay.mvp.view.EnterExpenseView;
import app.outlay.utils.ResourceUtils;
import app.outlay.view.Navigator;
import app.outlay.view.activity.base.DrawerActivity;
import app.outlay.view.adapter.CategoriesGridAdapter;
import app.outlay.view.alert.Alert;
import app.outlay.view.dialog.DatePickerFragment;
import app.outlay.view.fragment.base.BaseMvpFragment;
import app.outlay.view.numpad.NumpadEditable;
import app.outlay.view.numpad.SimpleNumpadValidator;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;

public class MainFragment extends BaseMvpFragment<EnterExpenseView, EnterExpensePresenter>
        implements EnterExpenseView {
    @Bind(app.outlay.R.id.chartIcon)
    ImageView chartIcon;

    @Bind(app.outlay.R.id.drawerIcon)
    ImageView drawerIcon;

    @Bind(app.outlay.R.id.categoriesGrid)
    RecyclerView categoriesGrid;

    @Bind(app.outlay.R.id.amountEditable)
    EditText amountText;

    @Bind(app.outlay.R.id.addCategory)
    Button addCategory;

    @Bind(app.outlay.R.id.dateLabel)
    TextView dateLabel;

    @Inject
    EnterExpensePresenter presenter;

    @Inject
    User currentUser;

    private CategoriesGridAdapter adapter;
    private Date selectedDate = new Date();

    private SimpleNumpadValidator validator = new SimpleNumpadValidator() {
        @Override
        public void onInvalidInput(String value) {
            super.onInvalidInput(value);
            inputError();
        }
    };

    private GridLayoutManager.SpanSizeLookup onSpanSizeLookup = new GridLayoutManager.SpanSizeLookup() {
        @Override
        public int getSpanSize(int position) {
            return position == 0 ? 4 : 1;
        }
    };

    @NonNull
    @Override
    public EnterExpensePresenter createPresenter() {
        return presenter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getApp().getUserComponent().inject(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.getCategories();
        cleanAmountInput();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(app.outlay.R.layout.fragment_main, null, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((DrawerActivity) getActivity()).setupDrawer(currentUser);

        initStaticContent();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 4);
        gridLayoutManager.setSpanSizeLookup(onSpanSizeLookup);
        categoriesGrid.setLayoutManager(gridLayoutManager);
        adapter = new CategoriesGridAdapter();
        categoriesGrid.setAdapter(adapter);

        adapter.attachNumpadEditable(new NumpadEditable() {
            @Override
            public String getText() {
                return amountText.getText().toString();
            }

            @Override
            public void setText(String text) {
                amountText.setText(text);
            }
        }, validator);
        adapter.setOnCategoryClickListener(category -> {
            if (validator.valid(amountText.getText().toString())) {

                Expense e = new Expense();
                e.setCategory(category);
                e.setAmount(new BigDecimal(amountText.getText().toString()));
                e.setReportedWhen(selectedDate);
                analytics().trackExpenseCreated(e);
                presenter.createExpense(e);
                cleanAmountInput();
            } else {
                validator.onInvalidInput(amountText.getText().toString());
            }
        });
    }

    private void initStaticContent() {
        chartIcon.setImageDrawable(ResourceUtils.getCustomToolbarIcon(getActivity(), app.outlay.R.integer.ic_chart));
        drawerIcon.setImageDrawable(ResourceUtils.getMaterialToolbarIcon(getActivity(), app.outlay.R.string.ic_material_menu));

        drawerIcon.setOnClickListener(v -> ((DrawerActivity) getActivity()).getMainDrawer().openDrawer());
        chartIcon.setOnClickListener(v -> {
            analytics().trackViewDailyExpenses();
            Navigator.goToReport(getActivity(), selectedDate);
        });

        dateLabel.setOnClickListener(v -> {
            DatePickerFragment datePickerFragment = new DatePickerFragment();
            datePickerFragment.setOnDateSetListener((parent, year, monthOfYear, dayOfMonth) -> {
                Calendar c = Calendar.getInstance();
                c.set(year, monthOfYear, dayOfMonth);
                Date selected = c.getTime();
                analytics().trackMainScreenDateChange(new Date(), selected);
                selectedDate = selected;
                dateLabel.setText(DateUtils.toLongString(selected));
            });
            datePickerFragment.show(getChildFragmentManager(), "datePicker");
        });

        addCategory.setOnClickListener(view -> Navigator.goToCategoriesList(getActivity()));
    }

    @Override
    public void showCategories(List<Category> categoryList) {
        adapter.setItems(categoryList);
        addCategory.setVisibility(categoryList.isEmpty() ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setAmount(BigDecimal amount) {
        amountText.setText(NumberUtils.formatAmount(amount));
    }

    @Override
    public void alertExpenseSuccess(Expense expense) {
        String message = getString(app.outlay.R.string.info_expense_created);
        message = String.format(message, expense.getAmount(), expense.getCategory().getTitle());
        Alert.info(getBaseActivity().getRootView(), message,
                v -> {
                    presenter.deleteExpense(expense);
                    amountText.setText(NumberUtils.formatAmount(expense.getAmount()));
                }
        );
    }

    public void inputError() {
        Animation shakeAnimation = AnimationUtils.loadAnimation(getActivity(), app.outlay.R.anim.shake);
        amountText.startAnimation(shakeAnimation);
    }

    private void cleanAmountInput() {
        amountText.setText("");
    }
}
