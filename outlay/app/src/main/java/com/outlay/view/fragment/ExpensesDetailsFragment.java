package com.outlay.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.github.johnkil.print.PrintView;
import com.outlay.App;
import com.outlay.R;
import com.outlay.dao.Category;
import com.outlay.dao.Expense;
import com.outlay.helper.TextWatcherAdapter;
import com.outlay.presenter.ExpensesDetailsPresenter;
import com.outlay.utils.DateUtils;
import com.outlay.utils.FormatUtils;
import com.outlay.utils.IconUtils;
import com.outlay.utils.ResourceUtils;
import com.outlay.view.autocomplete.CategoryAutoCompleteAdapter;
import com.outlay.view.dialog.DatePickerFragment;
import com.rengwuxian.materialedittext.MaterialAutoCompleteTextView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Bogdan Melnychuk on 1/20/16.
 */
public class ExpensesDetailsFragment extends BaseFragment {

    public static final String ARG_EXPENSE_ID = "_argExpenseId";
    public static final String ARG_DATE = "_argDate";

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.categoryTitle)
    MaterialAutoCompleteTextView categoryTitle;

    @Bind(R.id.categoryIcon)
    PrintView categoryIcon;

    @Bind(R.id.amount)
    EditText amount;

    @Bind(R.id.note)
    EditText note;

    @Bind(R.id.date)
    EditText dateEdit;

    @Bind(R.id.amountInputLayout)
    TextInputLayout amountInputLayout;

    @Inject
    ExpensesDetailsPresenter presenter;
    private CategoryAutoCompleteAdapter autoCompleteAdapter;
    private Expense expense;
    private Category selectedCategory;
    private Date defaultDate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getComponent(getActivity()).inject(this);
        presenter.attachView(this);
        defaultDate = new Date(getArguments().getLong(ARG_DATE, new Date().getTime()));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expense_details, null, false);
        ButterKnife.bind(this, view);
        enableToolbar(toolbar);
        setDisplayHomeAsUpEnabled(true);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_category_details, menu);
        MenuItem saveItem = menu.findItem(R.id.action_save);
        saveItem.setIcon(ResourceUtils.getMaterialToolbarIcon(getActivity(), R.string.ic_material_done));
        if (expense != null && expense.getId() == null) {
            MenuItem deleteItem = menu.findItem(R.id.action_delete);
            deleteItem.setVisible(false);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                if (validateInput()) {
                    presenter.updateExpense(getExpense());
                    getActivity().onBackPressed();
                }
                break;
            case R.id.action_delete:
                presenter.deleteExpense(getExpense());
                expense.setId(null);
                getActivity().onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        autoCompleteAdapter = new CategoryAutoCompleteAdapter();
        categoryTitle.setAdapter(autoCompleteAdapter);
        categoryTitle.setOnItemClickListener((parent, view1, position, id) -> {
            Category category = autoCompleteAdapter.getItem(position);
            selectedCategory = category;
            IconUtils.loadCategoryIcon(selectedCategory, categoryIcon);
        });
        presenter.loadCategories();
        dateEdit.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                showDatePickerDialog();
            }
        });
        dateEdit.setOnClickListener(v -> showDatePickerDialog());

        if (getArguments().containsKey(ARG_EXPENSE_ID)) {
            Long expenseId = getArguments().getLong(ARG_EXPENSE_ID);
            getActivity().setTitle(getString(R.string.caption_edit_expense));
            presenter.loadExpense(expenseId);
        } else {
            getActivity().setTitle(getString(R.string.caption_new_expense));
            displayExpense(new Expense());
        }
        amount.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                amountInputLayout.setErrorEnabled(false);
            }
        });
    }

    private void showDatePickerDialog() {
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.setOnDateSetListener((parent, year, monthOfYear, dayOfMonth) -> {
            Calendar c = Calendar.getInstance();
            c.set(year, monthOfYear, dayOfMonth);
            Date selected = c.getTime();
            setDateStr(selected);
            expense.setReportedAt(selected);
        });
        datePickerFragment.show(getChildFragmentManager(), "datePicker");
    }

    public void setCategories(List<Category> categories) {
        autoCompleteAdapter.setItems(categories);
    }

    public void displayExpense(Expense e) {
        this.expense = e;
        if (e.getId() != null) {
            selectedCategory = e.getCategory();
            IconUtils.loadCategoryIcon(e.getCategory(), categoryIcon);
            categoryTitle.setText(e.getCategory().getTitle());
            amount.setText(FormatUtils.formatAmount(e.getAmount()));
            note.setText(e.getNote());
            setDateStr(expense.getReportedAt());
        } else {
            this.expense.setReportedAt(defaultDate);
            setDateStr(expense.getReportedAt());
        }
    }

    public Expense getExpense() {
        if (selectedCategory != null) {
            expense.setCategory(selectedCategory);
        }
        if (!TextUtils.isEmpty(amount.getText().toString())) {
            expense.setAmount(Double.valueOf(amount.getText().toString()));
        }
        expense.setNote(note.getText().toString());
        return expense;
    }

    private void setDateStr(Date date) {
        dateEdit.setText(DateUtils.toLongString(date));
    }

    private boolean validateInput() {
        boolean result = true;
        if (selectedCategory == null) {
            categoryTitle.setError(getString(R.string.error_category_name_empty));
            categoryTitle.requestFocus();
            result = false;
        } else if (!selectedCategory.getTitle().equals(categoryTitle.getText().toString())) {
            categoryTitle.setError(getString(R.string.error_category_name_invalid));
            categoryTitle.requestFocus();
            result = false;
        } else if (TextUtils.isEmpty(amount.getText())) {
            //TODO validate number
            amountInputLayout.setError(getString(R.string.error_amount_invalid));
            amountInputLayout.requestFocus();
            result = false;
        }

        return result;
    }

    @Override
    public void onDestroy() {
        if (expense.getId() != null) {
            expense.refresh();
        }
        super.onDestroy();
    }
}
