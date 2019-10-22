package com.disp_mov.tacos.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.disp_mov.tacos.data.model.Orders;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<ArrayList<Orders.Order>> mList;

    public HomeViewModel() {
        mList = new MutableLiveData<>();
        mList.setValue(Orders.ITEMS);
    }

    public LiveData<ArrayList<Orders.Order>> getList() {
        return mList;
    }
}