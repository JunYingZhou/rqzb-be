package com.rqzb.renqing.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rqzb.common.ApiResponse;
import com.rqzb.common.PageQuery;
import com.rqzb.common.PageResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.lang.reflect.Method;
import java.util.List;

public abstract class BaseCrudController<T> {

    private final IService<T> service;

    protected BaseCrudController(IService<T> service) {
        this.service = service;
    }

    @GetMapping("/page")
    public ApiResponse<PageResult<T>> page(PageQuery query) {
        Page<T> page = service.page(new Page<>(query.getCurrent(), query.getSize()));
        return ApiResponse.ok(PageResult.of(page.getCurrent(), page.getSize(), page.getTotal(), page.getRecords()));
    }

    @GetMapping("/list")
    public ApiResponse<List<T>> list() {
        return ApiResponse.ok(service.list());
    }

    @GetMapping("/{id}")
    public ApiResponse<T> detail(@PathVariable Long id) {
        T entity = service.getById(id);
        if (entity == null) {
            return ApiResponse.fail(404, "data not found");
        }
        return ApiResponse.ok(entity);
    }

    @PostMapping
    public ApiResponse<T> create(@RequestBody T entity) {
        beforeCreate(entity);
        service.save(entity);
        return ApiResponse.ok(entity);
    }

    @PutMapping("/{id}")
    public ApiResponse<T> update(@PathVariable Long id, @RequestBody T entity) {
        setEntityId(entity, id);
        beforeUpdate(entity);
        boolean updated = service.updateById(entity);
        if (!updated) {
            return ApiResponse.fail(404, "data not found");
        }
        return ApiResponse.ok(entity);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        boolean removed = service.removeById(id);
        if (!removed) {
            return ApiResponse.fail(404, "data not found");
        }
        return ApiResponse.ok();
    }

    protected void beforeCreate(T entity) {
    }

    protected void beforeUpdate(T entity) {
    }

    private void setEntityId(T entity, Long id) {
        try {
            Method setter = entity.getClass().getMethod("setId", Long.class);
            setter.invoke(entity, id);
        } catch (ReflectiveOperationException ex) {
            throw new IllegalArgumentException("entity must provide setId(Long)");
        }
    }
}
