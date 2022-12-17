package com.example.tvdapp.confirmOrder;

import com.example.tvdapp.order.ProductOrderViewEntity;

public interface ConfirmProductViewHolderEvent {
    void productDidChange(ProductOrderViewEntity entity);
}
