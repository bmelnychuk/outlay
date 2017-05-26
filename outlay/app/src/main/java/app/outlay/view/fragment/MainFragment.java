package app.outlay.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import app.outlay.R;
import app.outlay.core.utils.DateUtils;
import app.outlay.core.utils.NumberUtils;
import app.outlay.domain.model.Category;
import app.outlay.domain.model.Expense;
import app.outlay.domain.model.User;
import app.outlay.mvp.presenter.EnterExpensePresenter;
import app.outlay.mvp.view.EnterExpenseView;
import app.outlay.view.Navigator;
import app.outlay.view.activity.base.DrawerActivity;
import app.outlay.view.adapter.CategoriesGridAdapter;
import app.outlay.view.adapter.ExpenseAdapter;
import app.outlay.view.alert.Alert;
import app.outlay.view.dialog.DatePickerFragment;
import app.outlay.view.fragment.base.BaseMvpFragment;
import app.outlay.view.numpad.NumpadEditable;
import app.outlay.view.numpad.SimpleNumpadValidator;
import app.outlay.view.timeline.TimelineExpensesAdapter;
import butterknife.Bind;
import ca.barrenechea.widget.recyclerview.decoration.StickyHeaderDecoration;

public class MainFragment extends BaseMvpFragment<EnterExpenseView, EnterExpensePresenter>
        implements EnterExpenseView {
    @Bind(app.outlay.R.id.chartIcon)
    ImageView chartIcon;

    @Bind(app.outlay.R.id.drawerIcon)
    ImageView drawerIcon;

    @Bind(R.id.bottomSheet)
    View bottomSheet;

    @Bind(app.outlay.R.id.categoriesGrid)
    RecyclerView categoriesGrid;

    @Bind(R.id.timelineRecycler)
    RecyclerView timelineRecycler;

    @Bind(app.outlay.R.id.amountEditable)
    EditText amountText;

    @Bind(app.outlay.R.id.addCategory)
    Button addCategory;

    @Bind(app.outlay.R.id.dateLabel)
    TextView dateLabel;

    @Bind(R.id.expenseNote)
    EditText expenseNote;

    @Bind(R.id.bottomSheetToolbar)
    Toolbar bottomSheetToolbar;

    @Inject
    EnterExpensePresenter presenter;

    @Inject
    User currentUser;

    private BottomSheetBehavior bottomSheetBehavior;
    private StickyHeaderDecoration decor;
    private TimelineExpensesAdapter expensesAdapter;
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

    @Override
    public EnterExpensePresenter createPresenter() {
        return presenter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getApp().getUserComponent().inject(this);

        if(appPreferences().showWhatsNew()) {
            new MaterialDialog.Builder(getActivity())
                    .backgroundColor(getOutlayTheme().backgroundColor)
                    .negativeText(R.string.label_ok)
                    .title(R.string.label_whats_new)
                    .content(R.string.text_whats_new)
                    .show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.getCategories();
        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            presenter.loadTimeline();
        }
        cleanAmountInput();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(app.outlay.R.layout.fragment_main, null, false);
        return view;
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
                if (!TextUtils.isEmpty(expenseNote.getText())) {
                    e.setNote(expenseNote.getText().toString());
                }
                e.setAmount(new BigDecimal(amountText.getText().toString()));
                e.setReportedWhen(selectedDate);
                analytics().trackExpenseCreated(e);
                if(!TextUtils.isEmpty(e.getNote())) {
                    analytics().trackNoteEntered();
                }
                presenter.createExpense(e);
                cleanAmountInput();
            } else {
                validator.onInvalidInput(amountText.getText().toString());
            }
        });


        expensesAdapter = new TimelineExpensesAdapter();
        expensesAdapter.setOnExpenseClickListener(e -> Navigator.goToExpenseDetails(getActivity(), e, true));
        decor = new StickyHeaderDecoration(expensesAdapter);
        LinearLayoutManager stickyHeaderLayoutManager = new LinearLayoutManager(getActivity());
        timelineRecycler.setLayoutManager(stickyHeaderLayoutManager);
        timelineRecycler.setAdapter(expensesAdapter);
        timelineRecycler.addItemDecoration(decor);
    }

    private void initStaticContent() {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    analytics().trackViewTimeline();
                    presenter.loadTimeline();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                if (slideOffset == 0) {
                    bottomSheetToolbar.setVisibility(View.GONE);
                } else {
                    bottomSheetToolbar.setAlpha(slideOffset);
                    bottomSheetToolbar.setVisibility(View.VISIBLE);
                }
            }
        });

        bottomSheetToolbar.setTitle("Timeline");

        chartIcon.setImageDrawable(getResourceHelper().getCustomToolbarIcon(app.outlay.R.integer.ic_chart));
        drawerIcon.setImageDrawable(getResourceHelper().getMaterialToolbarIcon(app.outlay.R.string.ic_material_menu));

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
                dateLabel.setText(DateUtils.toShortString(selected));
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
    public void showTimeline(List<Expense> expenses) {
        expensesAdapter.setItems(expenses);
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
                    expenseNote.setText(expense.getNote());
                }
        );
    }

    public void inputError() {
        Animation shakeAnimation = AnimationUtils.loadAnimation(getActivity(), app.outlay.R.anim.shake);
        amountText.startAnimation(shakeAnimation);
    }

    private void cleanAmountInput() {
        amountText.setText("");
        expenseNote.setText("");
    }

    public boolean onBackPressed() {
        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            return false;
        }
        return true;
    }
}
