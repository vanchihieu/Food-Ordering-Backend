package vn.edu.iuh.fit.food.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.fit.food.exception.InvalidDataException;
import vn.edu.iuh.fit.food.model.Cart;
import vn.edu.iuh.fit.food.model.CartItem;
import vn.edu.iuh.fit.food.model.User;
import vn.edu.iuh.fit.food.request.AddCartItemRequest;
import vn.edu.iuh.fit.food.request.UpdateCartItemRequest;
import vn.edu.iuh.fit.food.service.CartService;
import vn.edu.iuh.fit.food.service.UserService;

@RestController
@RequestMapping("/api")
@Tag(name = "Cart", description = "The Cart API")
public class CartController {
    @Autowired
    private CartService cartService;
    @Autowired
    private UserService userService;

    @Operation(method = "Put", summary = "Add item to cart", description = "Add item to cart")
    @PutMapping("/cart/add")
    public ResponseEntity<CartItem> addItemToCart(@RequestBody AddCartItemRequest req,
                                                  @RequestHeader("Authorization") String jwt) throws InvalidDataException {
        CartItem cart = cartService.addItemToCart(req, jwt);
        return ResponseEntity.ok(cart);
    }

    @Operation(method = "Put", summary = "Update cart item quantity", description = "Update cart item quantity")
    @PutMapping("/cart-item/update")
    public ResponseEntity<CartItem> updateCartItemQuantity(
            @RequestBody UpdateCartItemRequest req,
            @RequestHeader("Authorization") String jwt) throws InvalidDataException {
        CartItem cart = cartService.updateCartItemQuantity(req.getCartItemId(), req.getQuantity());
        return ResponseEntity.ok(cart);
    }

    @Operation(method = "Delete", summary = "Remove item from cart", description = "Remove item from cart")
    @DeleteMapping("/cart-item/{id}/remove")
    public ResponseEntity<Cart> removeItemFromCart(@PathVariable Long id, @RequestHeader("Authorization") String jwt) throws InvalidDataException {
        Cart cart = cartService.removeItemFromCart(id, jwt);
        return ResponseEntity.ok(cart);
    }

    @Operation(method = "Get", summary = "Calculate cart totals", description = "Calculate cart totals")
    @GetMapping("/cart/total")
    public ResponseEntity<Double> calculateCartTotals(@RequestParam Long cartId, @RequestHeader("Authorization") String jwt) throws InvalidDataException {
        User user = userService.findUserProfileByJwt(jwt);

        Cart cart = cartService.findCartByUserId(user.getId());
        double total = cartService.calculateCartTotals(cart);
        return ResponseEntity.ok(total);
    }

    @Operation(method = "Get", summary = "Find user cart", description = "Find user cart")
    @GetMapping("/cart/")
    public ResponseEntity<Cart> findUserCart(@RequestHeader("Authorization") String jwt) throws InvalidDataException {
        User user = userService.findUserProfileByJwt(jwt);
        Cart cart = cartService.findCartByUserId(user.getId());
        return ResponseEntity.ok(cart);
    }

    @Operation(method = "Put", summary = "Clear cart", description = "Clear cart")
    @PutMapping("/cart/clear")
    public ResponseEntity<Cart> clearCart(@RequestHeader("Authorization") String jwt) throws InvalidDataException {
        User user = userService.findUserProfileByJwt(jwt);
        Cart cart = cartService.clearCart(user.getId());
        return ResponseEntity.ok(cart);
    }
}
