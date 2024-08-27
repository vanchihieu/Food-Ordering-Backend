package vn.edu.iuh.fit.food.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.food.exception.InvalidDataException;
import vn.edu.iuh.fit.food.model.Cart;
import vn.edu.iuh.fit.food.model.CartItem;
import vn.edu.iuh.fit.food.model.Food;
import vn.edu.iuh.fit.food.model.User;
import vn.edu.iuh.fit.food.repository.CartItemRepository;
import vn.edu.iuh.fit.food.repository.CartRepository;
import vn.edu.iuh.fit.food.repository.FoodRepository;
import vn.edu.iuh.fit.food.request.AddCartItemRequest;
import vn.edu.iuh.fit.food.service.CartService;
import vn.edu.iuh.fit.food.service.UserService;

import java.util.Optional;

@Service
public class CartServiceImplementation implements CartService {
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private FoodRepository menuItemRepository;

    @Override
    public CartItem addItemToCart(AddCartItemRequest req, String jwt) throws InvalidDataException {
        User user = userService.findUserProfileByJwt(jwt);

        // Tìm món ăn theo id
        Optional<Food> menuItem = menuItemRepository.findById(req.getMenuItemId());
        if (menuItem.isEmpty()) {
            throw new InvalidDataException("Menu Item not exist with id " + req.getMenuItemId());
        }

        // Tìm giỏ hàng của người dùng
        Cart cart = findCartByUserId(user.getId());

        // Kiểm tra xem món ăn đã có trong giỏ hàng chưa nếu có thì tăng số lượng
        for (CartItem cartItem : cart.getItems()) {
            if (cartItem.getFood().equals(menuItem.get())) {
                int newQuantity = cartItem.getQuantity() + req.getQuantity();
                return updateCartItemQuantity(cartItem.getId(), newQuantity);
            }
        }

        // Nếu món ăn chưa có trong giỏ hàng thì thêm vào giỏ hàng
        CartItem newCartItem = new CartItem();
        newCartItem.setFood(menuItem.get());
        newCartItem.setQuantity(req.getQuantity());
        newCartItem.setCart(cart);
        newCartItem.setIngredients(req.getIngredients());
        newCartItem.setTotalPrice(req.getQuantity() * menuItem.get().getPrice());

        // Lưu cartItem vào database
        CartItem savedItem = cartItemRepository.save(newCartItem);
        // Thêm cartItem vào giỏ hàng
        cart.getItems().add(savedItem);
        // Lưu giỏ hàng vào database
        cartRepository.save(cart);

        return savedItem;
    }

    @Override
    public CartItem updateCartItemQuantity(Long cartItemId, int quantity) throws InvalidDataException {
        // Tìm cartItem theo id
        Optional<CartItem> cartItem = cartItemRepository.findById(cartItemId);
        if (cartItem.isEmpty()) {
            throw new InvalidDataException("cart item not exist with id " + cartItemId);
        }
        // Cập nhật số lượng và tổng giá
        cartItem.get().setQuantity(quantity);
        cartItem.get().setTotalPrice((cartItem.get().getFood().getPrice() * quantity));
        return cartItemRepository.save(cartItem.get());
    }

    @Override
    public Cart removeItemFromCart(Long cartItemId, String jwt) throws InvalidDataException {
        User user = userService.findUserProfileByJwt(jwt);

        // Tìm giỏ hàng của người dùng
        Cart cart = findCartByUserId(user.getId());

        // Tìm cartItem theo id
        Optional<CartItem> cartItem = cartItemRepository.findById(cartItemId);
        if (cartItem.isEmpty()) {
            throw new InvalidDataException("cart item not exist with id " + cartItemId);
        }

        // Xóa cartItem khỏi giỏ hàng
        cart.getItems().remove(cartItem.get());

        // Lưu giỏ hàng vào database
        return cartRepository.save(cart);
    }

    @Override
    public Long calculateCartTotals(Cart cart) throws InvalidDataException {
        Long total = 0L;
        for (CartItem cartItem : cart.getItems()) {
            total += cartItem.getFood().getPrice() * cartItem.getQuantity();
        }
        return total;
    }

    @Override
    public Cart findCartById(Long id) throws InvalidDataException {
        Optional<Cart> cart = cartRepository.findById(id);
        if (cart.isPresent()) {
            return cart.get();
        }
        throw new InvalidDataException("Cart not found with the id " + id);
    }

    @Override
    public Cart findCartByUserId(Long userId) throws InvalidDataException {
        Optional<Cart> opt = cartRepository.findByCustomer_Id(userId);
        if (opt.isPresent()) {
            return opt.get();
        }
        throw new InvalidDataException("cart not found");
    }

    @Override
    public Cart clearCart(Long userId) throws InvalidDataException {
        Cart cart = findCartByUserId(userId);
        cart.getItems().clear();
        return cartRepository.save(cart);
    }
}
