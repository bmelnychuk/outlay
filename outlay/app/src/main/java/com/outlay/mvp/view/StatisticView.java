package com.outlay.mvp.view;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.outlay.domain.model.Report;

import java.util.List;

/**
 * Created by bmelnychuk on 10/25/16.
 */

public interface StatisticView extends MvpView {
    void showReport(Report report);
}
