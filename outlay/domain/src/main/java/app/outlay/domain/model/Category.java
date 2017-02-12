package app.outlay.domain.model;

/**
 * Created by bmelnychuk on 10/24/16.
 */

public class Category {
    private String id;
    private String title;
    private String icon;
    private int order;
    private int color;

    public String getId() {
        return id;
    }

    public Category setId(String id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Category setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getIcon() {
        return icon;
    }

    public Category setIcon(String icon) {
        this.icon = icon;
        return this;
    }

    public int getOrder() {
        return order;
    }

    public Category setOrder(int order) {
        this.order = order;
        return this;
    }

    public int getColor() {
        return color;
    }

    public Category setColor(int color) {
        this.color = color;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Category category = (Category) o;

        return id != null ? id.equals(category.id) : category.id == null;

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
