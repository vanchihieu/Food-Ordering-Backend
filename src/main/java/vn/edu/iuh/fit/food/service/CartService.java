package vn.edu.iuh.fit.food.service;

import vn.edu.iuh.fit.food.exception.InvalidDataException;
import vn.edu.iuh.fit.food.model.Cart;
import vn.edu.iuh.fit.food.model.CartItem;
import vn.edu.iuh.fit.food.request.AddCartItemRequest;

public interface CartService {
    public CartItem addItemToCart(AddCartItemRequest req, String jwt) throws InvalidDataException;

    public CartItem updateCartItemQuantity(Long cartItemId, int quantity) throws InvalidDataException;

    public Cart removeItemFromCart(Long cartItemId, String jwt) throws InvalidDataException;

    public Long calculateCartTotals(Cart cart) throws InvalidDataException;

    public Cart findCartById(Long id) throws InvalidDataException;

    public Cart findCartByUserId(Long userId) throws InvalidDataException;

    public Cart clearCart(Long userId) throws InvalidDataException;
}
