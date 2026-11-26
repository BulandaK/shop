package com.bulanda.shop.cart.domain.event;

import com.bulanda.shop.cart.domain.CartId;

public sealed interface CartEvent permits CartCreated, ItemAdded, ItemRemoved, CartCheckedOut {
    CartId cartId();
}
