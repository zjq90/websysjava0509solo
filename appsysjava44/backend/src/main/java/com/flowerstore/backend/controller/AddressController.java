package com.flowerstore.backend.controller;

import com.flowerstore.backend.dto.Result;
import com.flowerstore.backend.entity.UserAddress;
import com.flowerstore.backend.service.AddressService;
import com.flowerstore.backend.util.JWTUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 收货地址控制器
 * 
 * @author FlowerStore Team
 * @version 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/api/address")
@Tag(name = "地址接口", description = "收货地址管理")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @Autowired
    private JWTUtil jwtUtil;

    /**
     * 获取地址列表
     */
    @GetMapping("/list")
    @Operation(summary = "获取地址列表", description = "获取当前用户的所有收货地址")
    public Result<List<UserAddress>> getAddressList(HttpServletRequest request) {
        Long userId = getUserIdFromToken(request);
        if (userId == null) {
            return Result.error("未登录或token已过期");
        }
        return addressService.getUserAddresses(userId);
    }

    /**
     * 获取地址详情
     */
    @GetMapping("/{addressId}")
    @Operation(summary = "获取地址详情", description = "获取单个地址的详细信息")
    public Result<UserAddress> getAddressDetail(@PathVariable Long addressId, HttpServletRequest request) {
        Long userId = getUserIdFromToken(request);
        if (userId == null) {
            return Result.error("未登录或token已过期");
        }
        return addressService.getAddressDetail(userId, addressId);
    }

    /**
     * 添加地址
     */
    @PostMapping("/add")
    @Operation(summary = "添加地址", description = "添加新的收货地址")
    public Result<UserAddress> addAddress(@RequestBody UserAddress address, HttpServletRequest request) {
        Long userId = getUserIdFromToken(request);
        if (userId == null) {
            return Result.error("未登录或token已过期");
        }
        return addressService.addAddress(userId, address);
    }

    /**
     * 更新地址
     */
    @PutMapping("/{addressId}")
    @Operation(summary = "更新地址", description = "更新收货地址信息")
    public Result<UserAddress> updateAddress(@PathVariable Long addressId, @RequestBody UserAddress address, HttpServletRequest request) {
        Long userId = getUserIdFromToken(request);
        if (userId == null) {
            return Result.error("未登录或token已过期");
        }
        return addressService.updateAddress(userId, addressId, address);
    }

    /**
     * 删除地址
     */
    @DeleteMapping("/{addressId}")
    @Operation(summary = "删除地址", description = "删除收货地址")
    public Result<String> deleteAddress(@PathVariable Long addressId, HttpServletRequest request) {
        Long userId = getUserIdFromToken(request);
        if (userId == null) {
            return Result.error("未登录或token已过期");
        }
        return addressService.deleteAddress(userId, addressId);
    }

    /**
     * 设置默认地址
     */
    @PostMapping("/default/{addressId}")
    @Operation(summary = "设置默认地址", description = "设置某个地址为默认地址")
    public Result<String> setDefaultAddress(@PathVariable Long addressId, HttpServletRequest request) {
        Long userId = getUserIdFromToken(request);
        if (userId == null) {
            return Result.error("未登录或token已过期");
        }
        return addressService.setDefaultAddress(userId, addressId);
    }

    /**
     * 从token中获取用户ID
     */
    private Long getUserIdFromToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        return jwtUtil.getUserIdFromToken(token);
    }
}
