package app.outlay.mvp.presenter;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import app.outlay.core.executor.DefaultSubscriber;
import app.outlay.domain.interactor.DeleteCategoryUseCase;
import app.outlay.domain.interactor.GetCategoryUseCase;
import app.outlay.domain.interactor.SaveCategoryUseCase;
import app.outlay.domain.model.Category;
import app.outlay.mvp.view.CategoryDetailsView;

import javax.inject.Inject;

/**
 * Created by Bogdan Melnychuk on 1/21/16.
 */
public class CategoryDetailsPresenter extends MvpBasePresenter<CategoryDetailsView> {
    private SaveCategoryUseCase updateCategoryUseCase;
    private DeleteCategoryUseCase deleteCategoryUseCase;
    private GetCategoryUseCase getCategoryUseCase;

    @Inject
    public CategoryDetailsPresenter(
            SaveCategoryUseCase updateCategoryUseCase,
            DeleteCategoryUseCase deleteCategoryUseCase,
            GetCategoryUseCase getCategoryUseCase
    ) {
        this.updateCategoryUseCase = updateCategoryUseCase;
        this.deleteCategoryUseCase = deleteCategoryUseCase;
        this.getCategoryUseCase = getCategoryUseCase;
    }

    public void getCategory(String id) {
        getCategoryUseCase.execute(id, new DefaultSubscriber<Category>() {
            @Override
            public void onNext(Category category) {
                if (getView()!=null){
                    getView().showCategory(category);
                }
            }
        });
    }

    public void updateCategory(Category category) {
        updateCategoryUseCase.execute(category, new DefaultSubscriber<Category>() {
            @Override
            public void onCompleted() {
                if (getView()!=null){
                    getView().finish();
                }
            }
        });

    }

    public void deleteCategory(Category category) {
        deleteCategoryUseCase.execute(category, new DefaultSubscriber<Category>() {
            @Override
            public void onCompleted() {
                if (getView()!=null){
                    getView().finish();
                }
            }
        });
    }
}
