package app.outlay.mvp.presenter;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import app.outlay.core.executor.DefaultSubscriber;
import app.outlay.domain.interactor.GetCategoriesUseCase;
import app.outlay.domain.interactor.UpdateCategoriesUseCase;
import app.outlay.domain.model.Category;
import app.outlay.mvp.view.CategoriesView;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Bogdan Melnychuk on 1/21/16.
 */
public class CategoriesPresenter extends MvpBasePresenter<CategoriesView> {
    private GetCategoriesUseCase getCategoriesUseCase;
    private UpdateCategoriesUseCase updateCategoriesUseCase;

    @Inject
    public CategoriesPresenter(
            GetCategoriesUseCase getCategoriesUseCase,
            UpdateCategoriesUseCase updateCategoriesUseCase
    ) {
        this.getCategoriesUseCase = getCategoriesUseCase;
        this.updateCategoriesUseCase = updateCategoriesUseCase;
    }

    public void getCategories() {
        getCategoriesUseCase.execute(new DefaultSubscriber<List<Category>>() {
            @Override
            public void onNext(List<Category> categories) {
                if (getView()!=null){
                    getView().showCategories(categories);
                }
            }
        });
    }

    public void updateOrder(List<Category> categories) {
        updateCategoriesUseCase.execute(categories, new DefaultSubscriber());
    }
}
