package app.outlay.example;

import app.outlay.core.executor.DefaultSubscriber;
import app.outlay.domain.interactor.GetCategoriesUseCase;
import app.outlay.domain.interactor.UpdateCategoriesUseCase;
import app.outlay.domain.model.Category;
import app.outlay.mvp.presenter.CategoriesPresenter;
import app.outlay.mvp.view.CategoriesView;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Created by MR.ESSIG on 5/2/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class CategoriesPresenterTest {

    private CategoriesPresenter categoriesPresenter;
    private List<Category> categories;

    @Mock
    private GetCategoriesUseCase getCategoriesUseCaseMock;

    @Mock
    private UpdateCategoriesUseCase updateCategoriesUseCaseMock;

    @Mock
    private CategoriesView categoriesViewMock;

    @Captor
    private ArgumentCaptor<DefaultSubscriber<List<Category>>> CategoriesSubscriberCaptor;

    @Captor
    private ArgumentCaptor<DefaultSubscriber> SubscriberCaptor;


    @Before
    public void init(){
        categoriesPresenter = new CategoriesPresenter(getCategoriesUseCaseMock,updateCategoriesUseCaseMock);
        categoriesPresenter.attachView(categoriesViewMock);
        categories = new ArrayList<>();
    }

    @Test
    public void getCategories_UsesGetCategoriesUseCase(){
        categoriesPresenter.getCategories();

        verify(getCategoriesUseCaseMock).execute(any());
    }

    @Test
    public void getCategories_DefaultSubscriber_ShowsViewCategories_OnNext(){
        categoriesPresenter.getCategories();

        verify(getCategoriesUseCaseMock).execute(CategoriesSubscriberCaptor.capture());
        CategoriesSubscriberCaptor.getValue().onNext(categories);
        verify(categoriesViewMock).showCategories(categories);
    }

    @Test
    public void updateOrder_UsesUpdateCategoriesUseCase(){
        categoriesPresenter.updateOrder(categories);

        verify(updateCategoriesUseCaseMock).execute(eq(categories), any());
    }
}
