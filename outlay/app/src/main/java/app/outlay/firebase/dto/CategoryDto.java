package app.outlay.firebase.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bmelnychuk on 10/26/16.
 */

public class CategoryDto {
    private String id;
    private String title;
    private String icon;
    private int order;
    private int color;
    private List<String> expenses;

    public CategoryDto() {
        expenses = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public List<String> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<String> expenses) {
        this.expenses = expenses;
    }

    public void addExpense(String id) {
        if (!expenses.contains(id)) {
            expenses.add(id);
        }
    }

    public void removeExpense(String id) {
        expenses.remove(id);
    }
}
