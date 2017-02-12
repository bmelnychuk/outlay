package app.outlay.mvp.view;

import com.hannesdorfmann.mosby.mvp.MvpView;
import app.outlay.domain.model.Report;

/**
 * Created by bmelnychuk on 10/25/16.
 */

public interface StatisticView extends MvpView {
    void showReport(Report report);
}
